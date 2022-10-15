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
import javafx.scene.text.Font;
import javafx.stage.Stage;
import javafx.scene.text.Text;

// Game.java originally created Patrik Haslum
// Edited by Sam Liersch u7448311
public class Game extends Application {
    // FIXME Task 11: implement a playable game
    private final static Group root = new Group();
    private final static Group controls = new Group();
    private static final int WINDOW_WIDTH = 1200;
    private static final int WINDOW_HEIGHT = 700;
    private TextField boardTextField;
    private final static Group winLayer = new Group();
    private final static Text winText = new Text();
    public static Board board = new Board(WINDOW_HEIGHT,WINDOW_WIDTH);

    private static final Action reRollBlank = new Action();
    static void updateState() {

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

    private void makeControlButtons() {
        Button reRollButton = new Button("ReRoll");
        Button endTurnButton = new Button("End Turn");
        reRollButton.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent actionEvent) {
                reRoll();
            }
        });
        endTurnButton.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent actionEvent) {
                endTurn();
            }
        });
        
        
        
        
        HBox hb = new HBox();
        hb.getChildren().addAll(reRollButton,endTurnButton);
        hb.setSpacing(10);
        hb.setLayoutX(board.BOARD_WIDTH/10 * 7.5);
        hb.setLayoutY(board.BOARD_HEIGHT/10 * 9);
        controls.getChildren().add(hb);
    }

    private void makeReplayGameButton(){
        Button replayGame = new Button("Replay");
        replayGame.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent actionEvent) {
                newGame();
            }
        });
        replayGame.setLayoutX(board.BOARD_WIDTH/2 );
        replayGame.setLayoutY(board.BOARD_HEIGHT/10 * 5.5 );
        winLayer.getChildren().add(replayGame);

    }
    private void makeLoadGameButton() {
        Label boardLabel = new Label("Board State:");
        boardTextField = new TextField();
        boardTextField.setPrefWidth(500);
        Button button = new Button("Load");
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
        root.getChildren().remove(controls);
        root.getChildren().remove(winLayer);

        Game.board = new Board(WINDOW_HEIGHT,WINDOW_WIDTH);


        root.getChildren().add(board.hexPlate);
        root.getChildren().add(board.settlementLayer);
        root.getChildren().add(board.castleLayer);
        root.getChildren().add(board.knightLayer);
        root.getChildren().add(board.roadLayer);
        root.getChildren().add(board.turnLayer);
        root.getChildren().add(controls);
    }
    private void newGame()
    {
        newBoard();
        Game.board.loadBoard("W00WXW00X00");
        Game.updateState();
    }
    @Override
    public void start(Stage stage) throws Exception {
        //Create reRoll action to be used
        CatanDiceExtra.loadAction("keep", reRollBlank);

        //FIXME align button on centre
        //Create winlayer
        winText.setLayoutX(board.BOARD_WIDTH/10 * 4.75);
        winText.setLayoutY(board.BOARD_HEIGHT/2);
        winText.setFont(Font.font(30));
        winLayer.getChildren().add(winText);
        makeReplayGameButton();


        stage.setTitle("Catan Dice Game XXL");
        Scene scene = new Scene(root, WINDOW_WIDTH, WINDOW_HEIGHT);

        newGame();

        makeControlButtons();
        makeLoadGameButton();

        stage.setScene(scene);
        stage.show();


    }

    public static void applyGameAction(Action action)
    {
        System.out.println(action);
        System.out.println(CatanDiceExtra.isActionValid(board,action));
        if(CatanDiceExtra.isActionValid(board,action))
        {
            CatanDiceExtra.applyAction(board,action);
            //Player wins if score > 9
            if(board.playerTurn.score > 9)
            {
                playerWin(board.playerTurn,board);
            }

            if(board.setupPhase)
            {
                if((board.playerTurn.playerIndex + 1)%board.players.length == 0)
                {
                    board.numDice = 3;
                    board.rollsDone = 0;
                    board.setupPhase = false;
                    board.resources = new int[] {0,0,0,0,0,0};
                    applyGameAction(reRollBlank);
                }

                board.playerTurn = board.players[(board.playerTurn.playerIndex + 1)%board.players.length];

            }

            for(ResourcePiece r : board.resourceDisplay)
            {
                r.clicked = false;
            }
            Game.updateState();
        }

        System.out.println(board);


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
            applyGameAction(action);

        }
    }
    public static void playerWin(Player player, Board board)
    {
        root.getChildren().remove(board.hexPlate);
        root.getChildren().remove(board.settlementLayer);
        root.getChildren().remove(board.castleLayer);
        root.getChildren().remove(board.knightLayer);
        root.getChildren().remove(board.roadLayer);
        root.getChildren().remove(board.turnLayer);
        root.getChildren().remove(controls);
        winText.setText(player.name + " Wins");
        root.getChildren().add(winLayer);

    }


    public void endTurn()
    {
        if(!board.setupPhase)
        {
            if(board.numDice < 6)
            {
                board.numDice ++ ;
            }
            board.rollsDone = 0;
            board.playerTurn = board.players[(board.playerTurn.playerIndex + 1)%board.players.length];
            board.resources = new int[] {0,0,0,0,0,0};
            applyGameAction(reRollBlank);
            updateState();
        }

    }
}
