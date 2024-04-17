package org.example.minesweeper;

import javafx.geometry.Insets;
import javafx.scene.control.Button;

import javafx.scene.layout.*;
import javafx.scene.paint.*;
import javafx.scene.text.*;

public class Tile {
    private int nearby = 0;
    private boolean mine = false;
    private boolean flagged = false;
    private boolean visible = false;

    public Button btn = new Button("0");

    public Tile() {
        btn.setMinHeight(25);
        btn.setMinWidth(25);

        this.updateText();
        this.updateStyles();
    }

    // Getters
    public int getNearby() { return nearby; }
    public boolean isMine() { return mine; }
    public boolean isFlagged() { return flagged; }
    public boolean isVisible() { return visible; }

    // Setters
    public void setNearby(int nearby) { this.nearby = nearby; }
    public void setMine(boolean mine) { this.mine = mine; }
    public void setFlagged(boolean flagged) {
        this.flagged = flagged;

        this.updateText();
        this.updateStyles();
    }

    public void setVisible(boolean visible) {
        this.visible = visible;

        this.updateText();
        this.updateStyles();
    }

    /**
     * Update the button's text.
     */
    private void updateText() {
        if (visible || flagged) {
            btn.setText(toString());
            return;
        }

        btn.setText("0");
    }

    /**
     * Update the button's styles.
     */
    private void updateStyles() {
        final Color BG = Color.rgb(220, 220, 220);
        final String[] COLORS = {
                "#0102f3",
                "#017e00",
                "#fd0100",
                "#010280",
                "#830103",
                "#00807f",
                "#000000",
                "#808080"
        };

        btn.setFont(Font.font("Arial", FontWeight.BOLD, FontPosture.REGULAR, 16));
        btn.setTextFill(Color.TRANSPARENT);

        if (flagged) {
            btn.setTextFill(Color.PINK);
            return;
        }

        if (visible) {
            btn.setBackground(new Background(new BackgroundFill(BG, CornerRadii.EMPTY, Insets.EMPTY)));

            if (mine) {
                btn.setTextFill(Color.BLACK);
            } else if (nearby < COLORS.length && nearby > 0) {
                btn.setTextFill(Paint.valueOf(COLORS[nearby-1]));
            }
        }
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
