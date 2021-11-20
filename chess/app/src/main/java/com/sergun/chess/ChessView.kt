package com.sergun.chess

import android.content.Context
import android.graphics.*
import android.util.AttributeSet
import android.util.Log
import android.view.MotionEvent

import android.view.View
import kotlin.math.min


class ChessView(context: Context?, attrs: AttributeSet?) : View(context, attrs) {
    private val scaleFactor = 1.0f
    private var originX = 20f
    private var originY = 200f
    private var cellSide = 130f
    private val lightColor = Color.parseColor("#EEEEEE")
    private val darkColor = Color.parseColor("#BBBBBB")

    private val imgResIds = setOf(
        R.drawable.bishop_black,
        R.drawable.bishop_white,
        R.drawable.king_black,
        R.drawable.king_white,
        R.drawable.queen_black,
        R.drawable.queen_white,
        R.drawable.rook_black,
        R.drawable.rook_white,
        R.drawable.knight_black,
        R.drawable.knight_white,
        R.drawable.pawn_black,
        R.drawable.pawn_white,
    )
    private val bitMaps = mutableMapOf<Int, Bitmap>()
    private val paint = Paint()
    private var movingPieceBitmap: Bitmap? = null
    private var movingPiece: ChessPiece? = null
    private var fromCol: Int = -1
    private var fromRow: Int = -1
    private var movingPieceX: Float = -1f
    private var movingPieceY: Float = -1f

    var chessDelegate: ChessDelegate? = null

    init {
        loadBitmaps()
    }
    // Рисование фигуры
    override fun onDraw(canvas: Canvas?) {
        canvas ?: return

        val chessBoarSide = min(width, height) * scaleFactor
        cellSide = chessBoarSide / 8f
        originX = (width - chessBoarSide) / 2f
        originY = (height - chessBoarSide) / 2f

        drawChessBoard(canvas)
        drawPieces(canvas)

      }

    override fun onTouchEvent(event: MotionEvent?): Boolean {
        event ?: return false
        when (event.action){
            MotionEvent.ACTION_DOWN -> {
                fromCol = ((event.x - originX) / cellSide).toInt()
                fromRow = 7 - ((event.y - originY) / cellSide).toInt()

                chessDelegate?.pieceAt(fromCol, fromRow)?.let {
                    movingPiece = it
                    movingPieceBitmap = bitMaps[it.resID]
                }
            }
            MotionEvent.ACTION_MOVE -> {
                movingPieceX = event.x
                movingPieceY = event.y
                invalidate()
            }
            MotionEvent.ACTION_UP -> {
                val col = ((event.x - originX) / cellSide).toInt()
                val row = 7 - ((event.y - originY) / cellSide).toInt()
                chessDelegate?.movePiece(fromCol, fromRow, col, row)
                movingPieceBitmap = null
                movingPiece = null

            }
        }
        return true
    }
    private fun drawPieces(canvas: Canvas) {
        for(row in 0..7){
            for(col in 0..7) {

                chessDelegate?.pieceAt(col, row)?.let {
                    if(it != movingPiece) {
                        drawPieceAt(canvas, col, row, it.resID)
                    }
                }

            }
        }

        movingPieceBitmap?.let{
            canvas.drawBitmap(it, null, RectF(movingPieceX - cellSide / 2, movingPieceY - cellSide / 2, movingPieceX + cellSide / 2,movingPieceY + cellSide / 2), paint)
            }
        }

    private fun drawPieceAt(canvas: Canvas,col: Int, row: Int, resID: Int){
        val bitmap = bitMaps[resID]!!
        canvas.drawBitmap(bitmap, null, RectF(originX + col * cellSide , originY + (7 - row) * cellSide, originX + (col + 1) * cellSide ,originY + ((7 - row) + 1) * cellSide ), paint)
    }

    private fun loadBitmaps() {
      imgResIds.forEach {
          bitMaps[it] = BitmapFactory.decodeResource(resources, it)
      }
    }

    //Отрисовка доски
    private fun drawChessBoard(canvas: Canvas) {
        for(row in 0..7) {
            for (col in 0..7) {
                drawSquareAt(canvas, row, col, (row + col) % 2 == 1)
            }
        }
    }
    private fun drawSquareAt(canvas: Canvas, col: Int, row: Int, isDark: Boolean){
        paint.color = if(isDark) darkColor else lightColor
        canvas.drawRect(
            originX + col * cellSide,
            originY + row * cellSide,
            originX + (col + 1) * cellSide,
            originY +  (row + 1) * cellSide, paint
        )
    }
}
