package org.example.minesweeper;

import java.util.ArrayList;
import java.util.Random;

public class Board {
    private final ArrayList<ArrayList<Tile>> BOARD = new ArrayList<>();
    private int size = 5;
    private int mineCount = 10;

    public Board() {};
    public Board(int size, int mineCount) {
        this.size = size;
        this.mineCount = mineCount;
    }

    /**
     * Find nearby mines and update the tile at X and Y.
     *
     * @param x - X position on the board.
     * @param y - Y position on the board.
     */
    private void setNearby(int x, int y) {
        int startx = Math.max(x - 1, 0), endx = Math.min(x + 1, BOARD.size()-1);
        int starty = Math.max(y - 1, 0), endy = Math.min(y + 1, BOARD.size()-1);
        int nearby = 0;

        for (int iy = starty; iy <= endy; iy++) {
            for (int ix = startx; ix <= endx; ix++) {
                if (BOARD.get(iy).get(ix).isMine()) {
                    nearby++;
                }
            }
        }

        BOARD.get(y).get(x).setNearby(nearby);
    }

    /**
     * Create the board and populate the mines.
     *
     * @param x - Ensure tile X is safe.
     * @param y - Ensure tile Y is safe.
     */
    public void initBoard(int x, int y) {
        Random rand = new Random();
        BOARD.clear();

        // Create board with size
        for (int iy = 0; iy < size; iy++) {
            ArrayList<Tile> row = new ArrayList<>();

            for (int ix = 0; ix < size; ix++) {
                row.add(new Tile());
            }

            BOARD.add(row);
        }

        // Populate mines, ensure X and Y are safe
        for (int i = 0; i < mineCount; i++) {
            int ry = rand.nextInt(BOARD.size());
            int rx = rand.nextInt(BOARD.size());

            if (BOARD.get(rx).get(ry).isMine()|| (rx == x && ry == y)) {
                i--;
                continue;
            }

            BOARD.get(rx).get(ry).setMine(true);
        }

        // Update all tiles on board
        for (int sy = 0; sy < BOARD.size(); sy++) {
            for (int sx = 0; sx < BOARD.size(); sx++) {
                if (!BOARD.get(sy).get(sx).isMine()) {
                    setNearby(sx, sy);
                }
            }
        }
    }

    /**
     * Print the board. For debugging purposes.
     *
     * @param show - Ignore tile visibility, show everything.
     */
    public void printBoard(boolean show) {
        for (ArrayList<Tile> row : BOARD) {
            for (Tile t : row) {
                if (show) {
                    System.out.print(t.toString() + ", ");
                    continue;
                }

                if (t.isVisible()) {
                    System.out.print(t.toString() + ", ");
                } else {
                    System.out.print("?, ");
                }
            }

            System.out.println();
        }
    }
}
