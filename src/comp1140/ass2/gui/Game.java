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
// Completed by Sam Liersch u7448311
public class Game extends Application {
    private final static Group root = new Group();
    private final static Group controls = new Group();
    private final static Group menuLayer = new Group();
    private static final double WINDOW_WIDTH = 1200;
    private static final double WINDOW_HEIGHT = 700;
    private TextField boardTextField;
    public static String errorMessage = "";
    private final static Group winLayer = new Group();
    private final static Text winText = new Text();
    public static Board board = new Board(WINDOW_HEIGHT,WINDOW_WIDTH);
    public static boolean playAi = false;

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
    static void numPlayersButton() {
        Label loadPlayer = new Label("Enter Num Players");
        Button select = new Button("Select");
        Button aiButton = new Button("Vs AI");
        TextField enterPlayerCount = new TextField();
        enterPlayerCount.setPrefWidth(100);
        enterPlayerCount.setPromptText("1-4 Players");
        playAi = false;
        select.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent actionEvent) {
                try
                {
                    int playerCount =Integer.parseInt(enterPlayerCount.getText());
                    if(playerCount > 4 || playerCount < 1)
                    {
                        errorMessage = "Invalid Player Count";
                    }
                    else
                    {
                        newGame(playerCount);
                    }

                } catch (NumberFormatException e)
                {
                    errorMessage = "Invalid Input";
                }


            }
        });
        aiButton.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent actionEvent) {
                playAi = true;
                newGame(2);
            }
        });
        HBox hb = new HBox();
        hb.getChildren().addAll(loadPlayer,enterPlayerCount,select,aiButton);
        hb.setSpacing(10);
        menuLayer.getChildren().add(hb);

        //Create Text on how to play



        Text build = new Text("-To build a Piece ensure all rolls are used and click on desired Piece");
        Text reroll = new Text("-To Re-roll your resources select all resources that require re-rolling and click the reroll button");
        Text trade = new Text("-To trade 2 gold for 1 resource ensure all rolls are used and click on desired resource type Hex");
        Text swap = new Text("-To use the swap action select only one resource and click knight on a hex of desires output resource.");

        build.setX(0);
        build.setY(WINDOW_HEIGHT/10);
        build.setFont(Font.font(20));

        reroll.setX(0);
        reroll.setY(WINDOW_HEIGHT/10 * 2);
        reroll.setFont(Font.font(20));

        trade.setX(0);
        trade.setY(WINDOW_HEIGHT/10 * 3);
        trade.setFont(Font.font(20));

        swap.setX(0);
        swap.setY(WINDOW_HEIGHT/10 * 4);
        swap.setFont(Font.font(20));

        menuLayer.getChildren().addAll(build,reroll,trade,swap);











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

                root.getChildren().remove(winLayer);
                root.getChildren().add(menuLayer);
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
                int count = 0;
                for(int i = 0; i < Board.playerIDArray.length(); i++)
                {
                    if(boardTextField.getText().contains(Board.playerIDArray.substring(i,i+1)))
                    {
                        count++;
                    }
                }
                newBoard(count);
                board.loadBoard(boardTextField.getText());
                updateState();
            }
        });
        HBox hb = new HBox();
        hb.getChildren().addAll(boardLabel, boardTextField, button);
        hb.setSpacing(10);
        controls.getChildren().add(hb);
    }

    private static void newBoard(int playerCount){


        //Hex Tiles
        root.getChildren().remove(board.hexPlate);
        root.getChildren().remove(board.settlementLayer);
        root.getChildren().remove(board.castleLayer);
        root.getChildren().remove(board.knightLayer);
        root.getChildren().remove(board.roadLayer);
        root.getChildren().remove(board.turnLayer);
        root.getChildren().remove(controls);
        root.getChildren().remove(winLayer);
        root.getChildren().remove(menuLayer);

        Game.board = new Board(WINDOW_HEIGHT,WINDOW_WIDTH);
        Game.board.playerCount = playerCount;

        root.getChildren().add(board.hexPlate);
        root.getChildren().add(board.settlementLayer);
        root.getChildren().add(board.castleLayer);
        root.getChildren().add(board.knightLayer);
        root.getChildren().add(board.roadLayer);
        root.getChildren().add(board.turnLayer);
        root.getChildren().add(controls);
    }
    private static void newGame(int playerCount)
    {
        newBoard(playerCount);
        StringBuilder startString = new StringBuilder();
        startString.append("W00");
        startString.append(Board.playerIDArray, 0, board.playerCount);
        for(int i = 0; i < board.playerCount; i++)
        {
            startString.append(Board.playerIDArray.charAt(i));
            startString.append("00");
        }
        Game.board.loadBoard(startString.toString());
        Game.updateState();
    }
    @Override
    public void start(Stage stage) throws Exception {
        //Create reRoll action to be used
        reRollBlank.loadAction("keep");

        //FIXME align button on centre
        //Create win layer
        winText.setLayoutX(board.BOARD_WIDTH/10 * 4.75);
        winText.setLayoutY(board.BOARD_HEIGHT/2);
        winText.setFont(Font.font(30));
        winLayer.getChildren().add(winText);



        stage.setTitle("Catan Dice Game XXL");
        Scene scene = new Scene(root, WINDOW_WIDTH, WINDOW_HEIGHT);

        root.getChildren().add(menuLayer);

        makeReplayGameButton();
        numPlayersButton();
        makeControlButtons();
        makeLoadGameButton();

        stage.setScene(scene);
        stage.show();


    }

    public static void applyGameAction(Action action)
    {
        Game.errorMessage = "";

        if(board.resources == null)
        {
            return;
        }
        System.out.println("Action: " + action);
        System.out.println("IsActionValid: " + CatanDiceExtra.isActionValid(board,action));
        System.out.println("Board String: " + board);
        if(CatanDiceExtra.isActionValid(board,action))
        {
            CatanDiceExtra.applyAction(board,action);
            //Player wins if score > 9
            if(board.playerTurn.score > 9)
            {
                playerWin(board.playerTurn);
            }

            if(board.setupPhase)
            {
                //Set setup phase to phase as false as soon as first round is completed
                if((board.playerTurn.playerIndex + 1)%board.players.length == 0)
                {
                    board.numDice = 3;
                    board.rollsDone = 0;
                    board.setupPhase = false;
                    board.resources = new int[] {0,0,0,0,0,0};
                    applyGameAction(reRollBlank);
                }
                board.playerTurn = board.players[(board.playerTurn.playerIndex + 1)%board.players.length];

                //If still in setup phase check player can place road otherwise reset
                if(board.setupPhase)
                {
                    boolean flag = false;
                    Action buildRoad;
                    int roadIndex;
                    for(int i = 0; i < Board.coastRoads.size(); i ++)
                    {
                        roadIndex = Math.min(Board.coastRoads.get(i % 30),Board.coastRoads.get((i + 1) % 30)) * 100;
                        roadIndex += Math.max(Board.coastRoads.get(i % 30),Board.coastRoads.get((i + 1) % 30));

                        buildRoad = new Action();
                        if(!buildRoad.loadAction("buildR" + String.format("%4d", roadIndex).replace(' ', '0')))
                        {
                            return;
                        }
                        if(CatanDiceExtra.isActionValid(board,buildRoad))
                        {
                            flag = true;
                            break;
                        }

                    }
                    if(!flag)
                    {
                        Game.newGame(board.playerCount);
                        return;
                    }
                }
                if(playAi && board.playerTurn.playerIndex == 1)
                {
                    aiMove();
                }
            }

            for(ResourcePiece r : board.resourceDisplay)
            {
                r.clicked = false;
            }
            Game.updateState();
        }
        board.errorText.setText(Game.errorMessage);
    }



    public void reRoll()
    {
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
            //Invert resources so highlighted resources are rerolled
            for(int i = 0; i < 6; i++)
            {
                keepResources[i] = board.resources[i] - keepResources[i];
            }
            action.resourceArray = keepResources;
            applyGameAction(action);

        }
    }
    public static void playerWin(Player player)
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
        //If ai is active and turn = player 2, it's the ai's turn
        if(playAi && board.playerTurn.playerIndex == 1)
        {
            aiMove();
        }

    }

    public static void aiMove()
    {
        Action action;
        for(String s : CatanDiceExtra.generateAction(board))
        {
            action = new Action();
            action.loadAction(s);
            applyGameAction(action);
        }
    }
}
