package comp1140.ass2.gui;

import comp1140.ass2.*;
import javafx.application.Application;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.scene.Group;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.layout.HBox;
import javafx.stage.Stage;

// Game.java originally created Patrik Haslum
// Edited by Sam Liersch u7448311
public class Game extends Application {
    // FIXME Task 11: implement a playable game
    private final Group root = new Group();
    private final Group controls = new Group();
    private static final int WINDOW_WIDTH = 1200;
    private static final int WINDOW_HEIGHT = 700;
    private TextField boardTextField;

    public static Board board = new Board(WINDOW_HEIGHT,WINDOW_WIDTH);

    void updateState() {

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
        board.updateTurnInfo();
    }

    private void makeActionButton() {
        Button button = new Button("ReRoll");
        button.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent actionEvent) {
                reRoll();
            }
        });
        button.setLayoutX(board.BOARD_WIDTH/10 * 8);
        button.setLayoutY(board.BOARD_HEIGHT/10 * 9);
        controls.getChildren().add(button);
    }

    private void makeControls() {
        Label boardLabel = new Label("Board State:");
        boardTextField = new TextField();
        boardTextField.setPrefWidth(500);
        Button button = new Button("Show");
        button.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent e) {
                newBoard();
                board.loadBoard(boardTextField.getText());
                updateState();
            }
        });
        HBox hb = new HBox();
        hb.getChildren().addAll(boardLabel, boardTextField, button);
        hb.setSpacing(10);
        controls.getChildren().add(hb);
    }

    private void newBoard(){
        //Hex Tiles
        root.getChildren().remove(board.hexPlate);
        root.getChildren().remove(board.settlementLayer);
        root.getChildren().remove(board.castleLayer);
        root.getChildren().remove(board.knightLayer);
        root.getChildren().remove(board.roadLayer);
        root.getChildren().remove(board.turnLayer);

        board = new Board(WINDOW_HEIGHT,WINDOW_WIDTH);

        root.getChildren().add(board.hexPlate);
        root.getChildren().add(board.settlementLayer);
        root.getChildren().add(board.castleLayer);
        root.getChildren().add(board.knightLayer);
        root.getChildren().add(board.roadLayer);
        root.getChildren().add(board.turnLayer);
    }
    @Override
    public void start(Stage stage) throws Exception {

        stage.setTitle("Catan Dice Game XXL");
        Scene scene = new Scene(this.root, WINDOW_WIDTH, WINDOW_HEIGHT);

        root.getChildren().add(controls);
        newBoard();


        makeActionButton();
        makeControls();

        stage.setScene(scene);
        stage.show();


    }

    public static void applyGameAction(Action action)
    {
        CatanDiceExtra.applyAction(board,action);
    }
    public void reRoll()
    {
        //FIXME move loadBoard to be a constructor and remove
        if(board.resources == null)
        {
            return;
        }



        if(board.rollsDone < 3)
        {
            Action action = new Action();
            int[] keepResources = {0,0,0,0,0,0};
            for(ResourcePiece r : board.resourceDisplay)
            {
                if(r.clicked)
                {
                    keepResources[r.type] ++;
                }
            }
            //Roll Phase
            action.type = ActionType.KEEP;
            //Invert resources so highlighed resources are rerolled
            for(int i = 0; i < 6; i++)
            {
                keepResources[i] = board.resources[i] - keepResources[i];
            }
            action.resourceArray = keepResources;
            CatanDiceExtra.applyAction(board,action);
        }


    }
}
