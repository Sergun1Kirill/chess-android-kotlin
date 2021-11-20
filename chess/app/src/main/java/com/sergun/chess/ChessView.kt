package com.sergun.chess

import android.content.Context
import android.graphics.*
import android.util.AttributeSet
import android.util.Log
import android.view.View
import java.lang.Float.min
import kotlin.math.min


class ChessView(context: Context?, attrs: AttributeSet?) : View(context, attrs) {
    private final val scaleFactor = .9f
    private final var originX = 20f
    private final var originY = 200f
    private final var cellSide = 130f
    private final val lightColor = Color.parseColor("#EEEEEE")
    private final val darkColor = Color.parseColor("#BBBBBB")

    private final val imgResIds = setOf(
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
    private final val bitMaps = mutableMapOf<Int, Bitmap>()
    private final val paint = Paint()

    var chessDelegate: ChessDelegate? = null

    init {
        loadBitmaps()
    }
    // Рисование фигуры
    override fun onDraw(canvas: Canvas?) {
        canvas ?: return
  
        val chessBoarSide = min(canvas.width, canvas.height) * scaleFactor
        cellSide = chessBoarSide / 8f
        originX = (canvas.width - chessBoarSide) / 2f
        originY = (canvas.height - chessBoarSide) / 2f

        drawChessBoard(canvas)
        drawPieces(canvas)

      }
    private fun drawPieces(canvas: Canvas) {
        for(row in 0..7){
            for(col in 0..7){
                chessDelegate?.pieceAt(col, row)?.let { drawPieceAt(canvas, col, row, it.resID) }
            }
        }
        }

    private fun drawPieceAt(canvas: Canvas,col: Int, row: Int, resID: Int){
        val bitmap = bitMaps[resID]!!
        canvas.drawBitmap(bitmap, null, RectF(originX + col * cellSide , originY + (7 - row) * cellSide, (col + 1) * cellSide + cellSide/2,originY + ((7 - row) + 1) * cellSide ), paint)
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
