package comp1140.ass2;

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

public class TestClass {


    public static void main(String[] args) {
        Board board = new Board(0,0);

        Action action = new Action();

        System.out.println(board.loadBoard("X63llllllWR0307XC0R4650W00X02"));
        System.out.println(CatanDiceExtra.loadAction("buildC1",action));
        System.out.println(CatanDiceExtra.isActionValid(board, action));
    }
}
