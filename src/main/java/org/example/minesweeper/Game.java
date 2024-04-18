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
    private VBox customs;
    private ComboBox<String> diff;
    private GridPane tiles;
    private Button start;
    private Label flags;
    private Label status;

    /**
     * Read and validate custom mine count and field size.
     *
     * @param text - The text to parse.
     * @param type - What type of difficulty? True = size.
     */
    private void readDifficulty(String text, boolean type) {
        final int MAX_SIZE = 50;
        final int MIN_SIZE = 4;

        int val = -1;

        try {
            val = Integer.parseInt(text);
        } catch (NumberFormatException ignored) {}

        if (type) size = val;
        else mines = val;

        if (size <= MIN_SIZE || size >= MAX_SIZE || mines <= MIN_SIZE || (mines > (size * size) - 1)) {
            flags.setText("Invalid custom values!");
            start.setDisable(true);
            return;
        }

        flags.setText("0/?");
        start.setDisable(false);
    }

    /**
     * Set the tile size and mine count based on the difficulty.
     */
    private void setDifficulty() {
        if (customs.getChildren().size() >= 2) {
            customs.getChildren().remove(1);
        }

        start.setDisable(false);
        mines = 0;
        size = 0;

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
                size = 15;
                mines = 40;
                break;

            case "Custom":
                HBox container = new HBox(8);
                container.setPadding(new Insets(10, 10, 4, 4));

                TextField sizeField = new TextField();
                TextField mineField = new TextField();

                sizeField.setPromptText("Minefield Size");
                mineField.setPromptText("Number of Mines");
                start.setDisable(true);

                sizeField.textProperty().addListener((observable, old, n) -> readDifficulty(n, true));
                mineField.textProperty().addListener((observable, old, n) -> readDifficulty(n, false));

                container.getChildren().addAll(sizeField, mineField);
                customs.getChildren().add(container);

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
        customs = new VBox();

        diff = new ComboBox<>(FXCollections.observableArrayList("Easy", "Medium", "Hard", "Custom"));
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
        customs.getChildren().addAll(control);
        root.setTop(customs);

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
