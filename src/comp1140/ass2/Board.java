package comp1140.ass2;

import javafx.application.Application;

import javafx.application.Application;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.scene.Group;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.layout.HBox;
import javafx.scene.shape.Polygon;
import  javafx.scene.paint.Color;
import javafx.stage.Stage;

public class Board {

    public Hex hexes[][] = new Hex[5][5];
    public Group hexPlate = new Group();
    //FIXME are constants all caps
    public static final int hexSize = 75;
    public static final Color hexColor = Color.LIGHTGRAY;



    public void createHexes()
    {
        Hex outLineHex;
        for(int r = -2; r <= 2; r++)
        {
            for(int q = -2; q <=2; q++)
            {
                if(Math.abs(r+q) < 3)
                {
                   //FIXME im not storing the objects anywhere for the large hexes, is this bad practice?
                    outLineHex = new Hex(q,r,null,hexSize);
                    outLineHex.setFill(Color.BLACK);
                    hexPlate.getChildren().add(outLineHex);
                    //Big hexes
                    hexes[r+2][q+2] = new Hex(q,r,null,hexSize-3);
                    hexPlate.getChildren().add(hexes[r+2][q+2]);

                }
            }

        }
    }

}
