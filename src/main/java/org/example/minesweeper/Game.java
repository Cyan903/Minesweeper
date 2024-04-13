package org.example.minesweeper;

public class Game {
    public static void main(String[] args) {
        Board board = new Board(10, 10);

        board.initBoard(4, 4);
        board.printBoard(true);

        System.out.println();
        board.printBoard(false);
    }
}
