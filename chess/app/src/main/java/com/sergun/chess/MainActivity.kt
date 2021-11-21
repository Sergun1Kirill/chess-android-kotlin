package com.sergun.chess

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.widget.Button
import java.io.PrintWriter
import java.net.ServerSocket
import java.net.Socket
import java.util.*
import java.util.concurrent.Executors

const val TAG = "MainActivity"

class MainActivity : AppCompatActivity(), ChessDelegate {
    private val PORT: Int = 50000
    private var chessModel = ChessModel()
    private lateinit var chessView: ChessView
    private lateinit var printWriter: PrintWriter

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        chessView = findViewById<ChessView>(R.id.chess_view)
        chessView.chessDelegate = this

        findViewById<Button>(R.id.reset_button).setOnClickListener{
            chessModel.reset()
            chessView.invalidate()

        }
        /*

        */
        findViewById<Button>(R.id.listen_button).setOnClickListener{
            Log.d(TAG, "Socket server listening on port ...")
            Executors.newSingleThreadExecutor().execute{
                val serverSocket = ServerSocket(PORT)
                val socket = serverSocket.accept()
                receiveMove(socket)
            }
        }


        findViewById<Button>(R.id.connect_button).setOnClickListener{
            Log.d(TAG, "Socket client connecting to addr:port...")
            Executors.newSingleThreadExecutor().execute{
                val socket = Socket("172.16.3.126", PORT) //  IP сервера
                receiveMove(socket)
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
                movePiece(move[0], move[1], move[2], move[3])
            }
        }
    }

    override fun pieceAt(col: Int, row: Int): ChessPiece? {
        return chessModel.pieceAt(col, row)
    }

    override fun movePiece(fromCol: Int, fromRow: Int, toCol: Int, toRow: Int) {
        chessModel.movePiece(fromCol, fromRow, toCol, toRow)
        chessView.invalidate()
        val moveStr = "$fromCol,$fromRow,$toCol,$toRow"
        Executors.newSingleThreadExecutor().execute {
        printWriter.println(moveStr)
    }
    }
}