package org.example.minesweeper;

import javafx.scene.control.Button;

public class Tile {
    private int nearby = 0;
    private boolean mine = false;
    private boolean flagged = false;
    private boolean visible = false;

    public Button btn = new Button(" ");

    public Tile() {}

    // Getters
    public int getNearby() {
        return nearby;
    }

    public boolean isMine() {
        return mine;
    }

    public boolean isFlagged() {
        return flagged;
    }

    public boolean isVisible() {
        return visible;
    }

    // Setters
    public void setNearby(int nearby) {
        this.nearby = nearby;
        this.updateText();
    }

    public void setMine(boolean mine) {
        this.mine = mine;
        this.updateText();
    }

    public void setFlagged(boolean flagged) {
        this.flagged = flagged;
        this.updateText();
    }

    public void setVisible(boolean visible) {
        this.visible = visible;
        this.updateText();
    }

    /**
     * Update the button's text.
     */
    private void updateText() {
        if (visible) {
            btn.setText(toString());
            return;
        }

        btn.setText(" ");
    }

    /**
     * Tile in string format.
     *
     * @return string - F if flag, X if mine, or # of mines nearby.
     */
    @Override
    public String toString() {
        if (flagged) return "F";
        if (mine) return "X";

        return Integer.toString(nearby);
    }
}
