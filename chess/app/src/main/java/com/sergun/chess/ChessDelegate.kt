package com.sergun.chess

interface ChessDelegate {
    fun pieceAt(col: Int, row: Int) : ChessPiece?
}