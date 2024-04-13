package org.example.minesweeper;

import javafx.application.Application;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.HBox;
import javafx.stage.Stage;

public class Game extends Application {
    // Game state
    private Board board;
    private final int size = 10; // TODO: Custom board size
    private final int mines = 10; // TODO: Custom mine amount
    private boolean playing;
    private boolean firstClick;

    // UI state
    private Button start;
    private BorderPane root;
    private GridPane tiles;

    private void createControl() {
        HBox control = new HBox(8);
        Label lmines = new Label("Mines: " + mines);
        Label lsize = new Label("Size: " + size);

        start = new Button("Start");

        start.setOnAction(e -> {
            playing = !playing;

            if (playing) startGame();
            else stopGame();
        });

        control.getChildren().addAll(lmines, lsize, start);
        root.setTop(control);
    }

    // Game management
    private void startGame() {
        board = new Board(size, mines);
        tiles = new GridPane();

        start.setText("Give up!");
        firstClick = true;

        tiles.setMinSize(400, 200);
        tiles.setPadding(new Insets(10, 10, 10, 10));

        // Add actions to buttons
        for (int y = 0; y < size; y++) {
            for (int x = 0; x < size; x++) {
                Button btn = board.getButton(x, y);

                final int by = y;
                final int bx = x;

                btn.setOnAction(e -> {
                    if (firstClick) {
                        board.populateBoard(bx, by);
                        board.revealEmpty(bx, by);

                        firstClick = false;
                        return;
                    }

                    // TODO: Flag
                    board.clickBoard(bx, by, false);
                });

                tiles.add(btn, bx, by);
            }
        }

        tiles.setAlignment(Pos.CENTER);
        root.setCenter(tiles);
    }

    private void stopGame() {
        start.setText("Start");
        firstClick = false;

        for (int y = 0; y < size; y++) {
            for (int x = 0; x < size; x++) {
                tiles.getChildren().remove(board.getButton(x, y));
            }
        }
    }

    @Override
    public void start(Stage primaryStage) {
        root = new BorderPane();

        createControl();

        // Create scene
        Scene scene = new Scene(root, 400, 400);

        primaryStage.setScene(scene);
        primaryStage.setTitle("Minesweeper");
        primaryStage.show();
    }

    public static void main(String[] args) {
        launch(args);
    }
}
