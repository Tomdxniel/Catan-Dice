package comp1140.ass2.gui;

import comp1140.ass2.Board;
import comp1140.ass2.Piece;
import javafx.application.Application;
import javafx.scene.Group;
import javafx.scene.Scene;
import javafx.stage.Stage;


public class Game extends Application {
    // FIXME Task 11: implement a playable game
    private final Group root = new Group();
    private static final int WINDOW_WIDTH = 1200;
    private static final int WINDOW_HEIGHT = 700;

    Board board = new Board(WINDOW_HEIGHT,WINDOW_WIDTH);

    void displayState(String boardState) {
        newBoard();
        board.loadBoard(boardState);
        for(Piece p : board.settlements)
        {
            p.updatePiece();
        }
        for(Piece p : board.knights)
        {
            p.updatePiece();
        }
        for(Piece p : board.castles)
        {
            p.updatePiece();
        }
        board.roadsMap.forEach((key,value)-> value.updatePiece());
    }

    private void newBoard(){
        //Hex Tiles
        root.getChildren().remove(board.hexPlate);
        root.getChildren().remove(board.settlementLayer);
        root.getChildren().remove(board.castleLayer);
        root.getChildren().remove(board.knightLayer);
        root.getChildren().remove(board.roadLayer);

        board = new Board(WINDOW_HEIGHT,WINDOW_WIDTH);

        root.getChildren().add(board.hexPlate);
        root.getChildren().add(board.settlementLayer);
        root.getChildren().add(board.castleLayer);
        root.getChildren().add(board.knightLayer);
        root.getChildren().add(board.roadLayer);
    }
    @Override
    public void start(Stage stage) throws Exception {

        stage.setTitle("Catan Dice Game XXL");

        Scene scene = new Scene(this.root, WINDOW_WIDTH, WINDOW_HEIGHT);


        //Hex Tiles
        root.getChildren().add(board.hexPlate);
        root.getChildren().add(board.settlementLayer);
        root.getChildren().add(board.castleLayer);
        root.getChildren().add(board.knightLayer);
        root.getChildren().add(board.roadLayer);


        stage.setScene(scene);
        stage.show();


    }
}
