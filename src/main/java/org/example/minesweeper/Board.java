package org.example.minesweeper;

import javafx.scene.control.Button;

import java.util.ArrayList;
import java.util.Random;

public class Board {
    private final ArrayList<ArrayList<Tile>> BOARD = new ArrayList<>();
    private final int size;
    private final int mineCount;
    public int flagCount;

    public Board(int size, int mineCount) {
        this.size = size;
        this.mineCount = mineCount;

        this.initBoard();
    }

    /**
     * Create the game board with empty tiles.
     */
    private void initBoard() {
        BOARD.clear();

        // Create board with size
        for (int iy = 0; iy < size; iy++) {
            ArrayList<Tile> row = new ArrayList<>();

            for (int ix = 0; ix < size; ix++) {
                row.add(new Tile());
            }

            BOARD.add(row);
        }
    }

    /**
     * Find nearby mines and update the tile at X and Y.
     *
     * @param x - X position on the board.
     * @param y - Y position on the board.
     */
    private void setNearby(int x, int y) {
        int startx = Math.max(x - 1, 0), endx = Math.min(x + 1, BOARD.size() - 1);
        int starty = Math.max(y - 1, 0), endy = Math.min(y + 1, BOARD.size() - 1);
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
     * Check if tile at X and Y is an empty tile that needs to be revealed.
     *
     * @param x - The X position on the board.
     * @param y - The Y position on the board.
     * @return continue - Should the loop continue.
     */
    private boolean checkEmpty(int x, int y) {
        Tile t = BOARD.get(y).get(x);

        if (t.isMine() || t.isVisible()) return false;
        if (t.isFlagged()) flagCount--;

        t.setVisible(true);
        t.setFlagged(false);

        return t.getNearby() == 0;
    }

    /**
     * Recursively reveal tiles if they are empty tiles (tiles with no nearby mines).
     *
     * @param x - The start X position.
     * @param y - The start Y position.
     */
    private void revealEmpty(int x, int y) {
        int top = Math.max(y - 1, 0), bottom = Math.min(y + 1, BOARD.size() - 1);
        int left = Math.max(x - 1, 0), right = Math.min(x + 1, BOARD.size() - 1);

        if (checkEmpty(x, top)) revealEmpty(x, top);
        if (checkEmpty(x, bottom)) revealEmpty(x, bottom);

        if (checkEmpty(left, y)) revealEmpty(left, y);
        if (checkEmpty(right, y)) revealEmpty(right, y);
    }

    /**
     * Create the board and populate the mines.
     *
     * @param x - Ensure tile X is safe.
     * @param y - Ensure tile Y is safe.
     */
    public void populateBoard(int x, int y) {
        Random rand = new Random();

        // Populate mines, ensure X and Y are safe
        for (int i = 0; i < mineCount; i++) {
            int ry = rand.nextInt(BOARD.size());
            int rx = rand.nextInt(BOARD.size());

            if (BOARD.get(ry).get(rx).isMine() || (rx == x && ry == y)) {
                i--;
                continue;
            }

            BOARD.get(ry).get(rx).setMine(true);
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
     * Update tiles when board is clicked.
     *
     * @param x - The X position on the board.
     * @param y - The Y position on the board.
     * @return mine - Was the clicked tile a mine?
     */
    public boolean clickBoard(int x, int y) {
        Tile t = BOARD.get(y).get(x);

        // If visible or flagged, don't let the user click
        if (t.isVisible() || t.isFlagged()) {
            return false;
        }

        t.setVisible(true);

        if (t.getNearby() == 0 && !t.isMine()) {
            revealEmpty(x, y);
        }

        return t.isMine();
    }

    /**
     * Flag tile at X and Y.
     *
     * @param x - The X position on the board.
     * @param y - The Y position on the board.
     */
    public void flagBoard(int x, int y) {
        Tile t = BOARD.get(y).get(x);

        // If visible or missing flags, don't let the user flag.
        if (t.isVisible() || (!t.isFlagged() && flagCount >= mineCount)) return;

        t.setFlagged(!t.isFlagged());
        flagCount += t.isFlagged() ? 1 : -1;
    }

    /**
     * Check if game has been won.
     *
     * @return win - Have all mines been flagged?
     */
    public boolean checkWin() {
        for (int y = 0; y < BOARD.size(); y++) {
            for (int x = 0; x < BOARD.size(); x++) {
                Tile t = BOARD.get(y).get(x);

                if (t.isMine() && !t.isFlagged()) {
                    return false;
                }
            }
        }

        return true;
    }

    /**
     * Reveal all elements on board.
     */
    public void revealAll() {
        for (int y = 0; y < BOARD.size(); y++) {
            for (int x = 0; x < BOARD.size(); x++) {
                Tile t = BOARD.get(y).get(x);

                if (t.isFlagged() && !t.isMine()) {
                    flagCount--;
                    t.setFlagged(false);
                }

                t.setVisible(true);
            }
        }
    }

    /**
     * Get the button element from the tile at X and Y.
     *
     * @param x - X position on the board.
     * @param y - Y position on the board.
     * @return btn - The tile's button.
     */
    public Button getButton(int x, int y) {
        return BOARD.get(y).get(x).btn;
    }

    /**
     * Print the board. For debugging purposes.
     *
     * @param show - Ignore tile visibility, show everything.
     */
    public void printBoard(boolean show) {
        for (ArrayList<Tile> row : BOARD) {
            for (Tile t : row) {
                if (show || t.isVisible()) {
                    System.out.print(t.toString() + ", ");
                    continue;
                }

                System.out.print("?, ");
            }

            System.out.println();
        }
    }
}
