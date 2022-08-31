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
import javafx.stage.Stage;

public class Hex extends Polygon {
    //Hex coordinate for neighbours & storage
    //q
    //r
    //Hex coordinate for boardString
    int hexIndex;
    //Hex coordinate for drawing hex
    //Layoutx
    //Layouty
    private HexType type;

    //Offset to get from one hex to the next
    private static double qLengthX;
    private static double rLengthX;
    private static double rLengthY;
    //Calculate where the middle hex is going to be and then put it in the middle of the board
    private static double startX;
    private static double startY;
    //FIXME why does .getPoints().add() require Double (capital D)?
    private static Double[] points = new Double[12];
    //FIXME to reduce calculation time points are only calculated once and reused, is this a problem

    public Hex(int q, int r, HexType type,int size) {
        this.type = type;


        for(int i = 0; i < 6; i++)
        {
            //Add x coord
            //FIXME what is a better way than just type casting?
            points[i*2] = Math.cos(Math.PI * ((double)1/6 + (double)i/3))*size;
            //Add y coord
            points[i*2 + 1] = Math.sin(Math.PI * ((double)1/6 + (double)i/3))*size;
        }

        this.getPoints().addAll(points);
        this.setLayoutX(qLengthX * q + rLengthX * r + startX);
        this.setLayoutY(rLengthY * r + startY);
        this.setFill(Board.hexColor);


    }
    //FIXME setup hex will need to be called before initialising a hex, is this okay?
    public static void setUpHex(int size,double boardHeight,double boardWidth)
    {
        //To get to the next q hex add cos(30)*2*size to x coordinate
        qLengthX = Math.cos((double) 1/6 * Math.PI) * 2 * size;
        //To get to the next r hex add cos(30) * size to x coordinate and (sin(-30)*size-size to y
        rLengthX = Math.cos((double)1/6 * Math.PI) * size;
        rLengthY = Math.sin((double)1/6*Math.PI) * size + size;
        //Set start point of hex placement as top left

        startX = boardWidth/2;
        startY = boardHeight/2;

    }







}

