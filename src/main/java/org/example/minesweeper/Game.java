package org.example.minesweeper;

import javafx.application.Application;
import javafx.collections.FXCollections;
import javafx.scene.input.MouseButton;
import javafx.scene.Scene;
import javafx.stage.Stage;

import javafx.scene.control.*;
import javafx.scene.layout.*;
import javafx.scene.text.*;
import javafx.geometry.*;

public class Game extends Application {
    private Board board;
    private int size;
    private int mines;
    private boolean playing;
    private boolean firstClick;

    // UI state
    private BorderPane root;
    private ComboBox<String> diff;
    private GridPane tiles;
    private Button start;
    private Label flags;
    private Label status;

    /**
     * Set the tile size and mine count based on the difficulty.
     */
    private void setDifficulty() {
        // TODO: Custom difficulty
        // TODO: Set tile size
        //  - btn.setMinHeight(25);
        //  - btn.setMinWidth(25);

        switch (diff.getValue()) {
            case "Easy":
                size = 10;
                mines = 8;
                break;

            case "Medium":
                size = 10;
                mines = 15;
                break;

            case "Hard":
                size = 20;
                mines = 25;
                break;
        }
    }

    /**
     * Reset all elements and start the game.
     */
    private void startGame() {
        StackPane main = new StackPane();

        board = new Board(size, mines);
        tiles = new GridPane();

        start.setText("Stop");
        flags.setText("0/" + mines);
        status.setText("");

        diff.setDisable(true);
        tiles.setOpacity(1);

        firstClick = true;

        tiles.setMinSize(400, 200);
        tiles.setPadding(new Insets(10, 10, 10, 10));
        tiles.setHgap(2);
        tiles.setVgap(2);

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
                            tiles.setOpacity(0.3);
                            status.setText("You lose!");
                        }
                    }

                    flags.setText(board.flagCount + "/" + mines);

                    if (board.checkWin() && !firstClick) {
                        board.revealAll();
                        tiles.setOpacity(0.3);
                        status.setText("You win!");
                    }
                });

                tiles.add(btn, bx, by);
            }
        }

        main.getChildren().addAll(tiles, status);

        tiles.setAlignment(Pos.CENTER);
        root.setCenter(main);
    }

    /**
     * Stop the game.
     */
    private void stopGame() {
        start.setText("Start");
        flags.setText("0/?");
        status.setText("");

        diff.setDisable(false);

        // Remove the board
        for (int y = 0; y < size; y++) {
            for (int x = 0; x < size; x++) {
                tiles.getChildren().remove(board.getButton(x, y));
            }
        }
    }

    /**
     * Main entrypoint. Initialize the elements and create the game.
     *
     * @param primaryStage - The primary stage.
     */
    @Override
    public void start(Stage primaryStage) {
        HBox control = new HBox(8);
        Region right = new Region();

        HBox.setHgrow(right, Priority.ALWAYS);
        control.setPadding(new Insets(10, 10, 4, 4));

        // Create elements
        root = new BorderPane();
        diff = new ComboBox<>(FXCollections.observableArrayList("Easy", "Medium", "Hard"));

        start = new Button("Start");
        status = new Label("");
        flags = new Label("0/?");

        status.setFont(Font.font("Arial", FontWeight.BOLD, FontPosture.ITALIC, 20));
        flags.setFont(Font.font("Arial", FontWeight.BOLD, FontPosture.REGULAR, 16));
        diff.setValue("Easy");
        setDifficulty();

        // Events
        diff.setOnAction(e -> setDifficulty());
        start.setOnAction(e -> {
            playing = !playing;

            if (playing) startGame();
            else stopGame();
        });

        // Add elements
        control.getChildren().addAll(diff, start, right, flags);
        root.setTop(control);

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
