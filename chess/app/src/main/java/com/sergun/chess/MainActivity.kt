package com.sergun.chess

import android.os.Build
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.widget.Button
import android.widget.Toast
import java.io.PrintWriter
import java.net.ConnectException
import java.net.ServerSocket
import java.net.Socket
import java.net.SocketException
import java.util.*
import java.util.concurrent.Executors

const val TAG = "MainActivity"

class MainActivity : AppCompatActivity(), ChessDelegate {
    private val socketHost = "192.168.100.244"
    //127.0.0.1 // 192.168.100.244
    private val socketPort: Int = 50000
    private val socketGuestPort: Int = 50001 //emu
    private lateinit var chessView: ChessView
    private lateinit var resetButton: Button
    private lateinit var listenButton: Button
    private lateinit var connectButton: Button
    private var printWriter: PrintWriter? = null
    private var serverSocket: ServerSocket? = null
    private val isEmulator = Build.FINGERPRINT.contains("generic")

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        Log.d(TAG,"$isEmulator" )

        chessView = findViewById<ChessView>(R.id.chess_view)
        resetButton = findViewById<Button>(R.id.reset_button)
        listenButton = findViewById<Button>(R.id.listen_button)
        connectButton = findViewById<Button>(R.id.connect_button)
        chessView.chessDelegate = this

        resetButton.setOnClickListener{
            ChessGame.reset()
            chessView.invalidate()
            serverSocket?.close()
            listenButton.isEnabled = true

        }

        listenButton.setOnClickListener{
            listenButton.isEnabled = false
            val port = if(isEmulator) socketPort else socketGuestPort
            Toast.makeText(this, "Создание сервера", Toast.LENGTH_SHORT).show()
            Log.d(TAG, "Socket server listening on port ...")
            Executors.newSingleThreadExecutor().execute{
                ServerSocket(port).let { srvSkt ->
                    serverSocket = srvSkt
                    try{
                        val socket = srvSkt.accept()
                        receiveMove(socket)
                    }
                    catch (e: SocketException){
                    }

                }
            }
        }


        connectButton.setOnClickListener{
            Toast.makeText(this, "Подключение", Toast.LENGTH_SHORT).show()
            Log.d(TAG, "Socket client connecting to addr:port...")
            Executors.newSingleThreadExecutor().execute{
                   try {
                       val socket = Socket(socketHost, socketPort) //  IP сервера
                       receiveMove(socket)
                   }
                   catch (e: ConnectException){
                       Toast.makeText(this, "Подключение провалилось", Toast.LENGTH_SHORT).show()
                   }

            }
        }

    }

    private fun receiveMove(socket: Socket){
        val scanner = Scanner(socket.getInputStream())
        printWriter = PrintWriter(socket.getOutputStream(), true)
        while(scanner.hasNextLine())
        {
            val move : List<Int> = scanner.nextLine().split(",").map { it.toInt() }
            runOnUiThread{
                ChessGame.movePiece(Square(move[0], move[1]), Square(move[2], move[3]))


                chessView.invalidate()
            }
        }
    }

    override fun pieceAt(square: Square): ChessPiece? = ChessGame.pieceAt(square)


    override fun movePiece(from: Square, to:Square) {

        ChessGame.movePiece(from, to)

        chessView.invalidate()
        printWriter?.let{
            val moveStr = "${from.col},${from.row},${to.col},${to.row}"
            Executors.newSingleThreadExecutor().execute {
                it.println(moveStr)
            }
        }
    }
}