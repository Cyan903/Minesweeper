package org.example.minesweeper;

public class Tile {
    private int nearby = 0;
    private boolean mine = false;
    private boolean flagged = false;
    private boolean visible = false;

    public Tile() {}

    // Getters
    public int getNearby() { return nearby; }
    public boolean isMine() { return mine; }
    public boolean isFlagged() { return flagged; }
    public boolean isVisible() { return visible; }

    // Setters
    public void setNearby(int nearby) { this.nearby = nearby; }
    public void setMine(boolean mine) { this.mine = mine; }
    public void setFlagged(boolean flagged) { this.flagged = flagged; }
    public void setVisible(boolean visible) { this.visible = visible; }

    /**
     * Tile in string format. For debugging purposes.
     *
     * @return string - X if mine, or # of mines nearby.
     */
    @Override
    public String toString() {
        if (mine) {
            return "X";
        }

        return Integer.toString(nearby);
    }
}
