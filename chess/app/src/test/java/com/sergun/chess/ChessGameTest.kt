package com.sergun.chess

import org.junit.Test
import org.junit.Assert.*
class ChessGameTest{
    @Test
    fun clear(){
        assertNotNull(ChessGame.pieceAt(Square(0,0)))
        ChessGame.clear()
        assertNull(ChessGame.pieceAt(Square(0,0)))
    }
    @Test
    fun toString_isCorrect(){
        println(ChessGame.pgnBoard())
        assertTrue((ChessGame.toString().contains(("0 r n b q k b n r"))))
    }

    @Test
    fun movePiece_isCorrect() {
        assertNull(ChessGame.pieceAt(Square(0, 2)))
        ChessGame.movePiece(Square(0, 1),Square(0, 2))
        assertNotNull(ChessGame.pieceAt(Square(0, 2)))
    }

    @Test
    fun reset_isCorrect() {
        assertNull(ChessGame.pieceAt(Square(0, 2)))
        ChessGame.movePiece(Square(0, 1),Square(0, 2))
        assertNotNull(ChessGame.pieceAt(Square(0, 2)))
        ChessGame.reset()
        assertNull(ChessGame.pieceAt(Square(0, 2)))
    }

    @Test
    fun pieceAt_isCorrect() {
        assertNotNull(ChessGame.pieceAt(Square(0, 0)))
        assertEquals(Player.WHITE, ChessGame.pieceAt(Square(0, 0))?.player)

    }

}