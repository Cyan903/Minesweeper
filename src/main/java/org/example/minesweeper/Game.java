package org.example.minesweeper;

import java.util.Scanner;

public class Game {
    public static void main(String[] args) {
        Scanner scan = new Scanner(System.in);
        Board board = new Board(10, 10);

        board.initBoard(4, 4);
        board.printBoard(true);

        for (int i = 0; i < 10; i++) {
            System.out.print("Enter X: ");
            int x = scan.nextInt();

            System.out.print("Enter Y: ");
            int y = scan.nextInt();

            board.revealEmpty(x, y);
            board.printBoard(false);
        }

        scan.close();
    }
}
