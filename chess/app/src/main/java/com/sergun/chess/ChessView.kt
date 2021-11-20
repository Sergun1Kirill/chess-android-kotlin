package com.sergun.chess

import android.annotation.SuppressLint
import android.content.Context
import android.graphics.*
import android.util.AttributeSet
import android.view.View


class ChessView(context: Context?, attrs: AttributeSet?) : View(context, attrs) {
    private final val originX = 20f
    private final val originY = 200f
    private final val cellSide = 130f

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

    init {
        loadBitmaps()
    }
    // Рисование фигуры
    override fun onDraw(canvas: Canvas?) {
        drawChessBoard(canvas)

        val whiteQueenBitmap = bitMaps[R.drawable.queen_black]!!
        canvas?.drawBitmap(whiteQueenBitmap, null, Rect(0, 0, 600, 600), paint)

    }
    private fun loadBitmaps() {
      imgResIds.forEach {
          bitMaps[it] = BitmapFactory.decodeResource(resources, it)
      }
    }

    //Отрисовка доски
    private fun drawChessBoard(canvas: Canvas?) {
        for(i in 0..7) {
            for (j in 0..7) {
                paint.color = if((i + j) % 2 == 1) Color.DKGRAY else Color.LTGRAY
                canvas?.drawRect(
                    originX + i * cellSide,
                    originY + j * cellSide,
                    originX + (i + 1) * cellSide,
                    originY +  (j + 1) * cellSide, paint
                )
            }
        }

    }
}
