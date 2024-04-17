package org.example.minesweeper;

import javafx.application.Application;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.input.MouseButton;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.HBox;
import javafx.stage.Stage;

public class Game extends Application {
    // Game state
    private Board board;
    private final int size = 3; // TODO: Custom board size
    private final int mines = 1; // TODO: Custom mine amount
    private boolean playing;
    private boolean firstClick;

    // UI state
    private Button start;
    private Label flags;
    private Label status;
    private BorderPane root;
    private GridPane tiles;

    // Game management
    private void startGame() {
        board = new Board(size, mines);
        tiles = new GridPane();

        start.setText("Restart");
        flags.setText("0");
        status.setText("");
        firstClick = true;

        tiles.setMinSize(400, 200);
        tiles.setPadding(new Insets(10, 10, 10, 10));

        // Add actions to buttons
        for (int y = 0; y < size; y++) {
            for (int x = 0; x < size; x++) {
                Button btn = board.getButton(x, y);

                final int by = y;
                final int bx = x;

                btn.setOnMouseClicked(e -> {
                    boolean flagged = e.getButton() == MouseButton.SECONDARY;

                    if (flagged) {
                        board.flagBoard(bx, by);
                    } else {
                        if (firstClick) {
                            board.populateBoard(bx, by);
                            board.clickBoard(bx, by);
                            board.printBoard(true);

                            firstClick = false;
                        } else if (board.clickBoard(bx, by)) {
                            board.revealAll();
                            status.setText("You lose!");
                        }
                    }

                    flags.setText(Integer.toString(board.flagCount));

                    if (board.checkWin() && !firstClick) {
                        board.revealAll();
                        status.setText("You win!");
                    }
                });

                tiles.add(btn, bx, by);
            }
        }

        tiles.setAlignment(Pos.CENTER);
        root.setCenter(tiles);
    }

    private void stopGame() {
        start.setText("Start");
        flags.setText("0");
        status.setText("");

        // Remove the board
        for (int y = 0; y < size; y++) {
            for (int x = 0; x < size; x++) {
                tiles.getChildren().remove(board.getButton(x, y));
            }
        }
    }


    @Override
    public void start(Stage primaryStage) {
        HBox control = new HBox(8);

        // Create elements
        root = new BorderPane();
        start = new Button("Start");
        status = new Label("");
        flags = new Label("0");

        start.setOnAction(e -> {
            playing = !playing;

            if (playing) startGame();
            else stopGame();
        });

        control.getChildren().addAll(start, flags, status);
        root.setTop(control);

        // Create scene
        Scene scene = new Scene(root, 400, 400);

        primaryStage.setScene(scene);
        primaryStage.setTitle("Minesweeper");
        primaryStage.setResizable(false);
        primaryStage.show();
    }

    public static void main(String[] args) {
        launch(args);
    }
}
