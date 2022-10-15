package comp1140.ass2;

import comp1140.ass2.gui.Game;
import javafx.scene.shape.Polygon;


// Hex.java created by Sam Liersch u7448311
public class Hex extends Polygon {
    //Hex coordinate for neighbours & storage
    public int q;
    public int r;
    //Hex coordinate for drawing hex
    //LayoutX
    //LayoutY
    public HexType type;
    public int index;
    public Piece[] settlement = new Piece[6];
    public Piece[] roads = new Piece[6];


    //Offset to get from one hex to the next
    private static double qLengthX;
    private static double rLengthX;
    private static double rLengthY;
    //Calculate where the middle hex is going to be and then put it in the middle of the board
    private static double startX;
    private static double startY;
    //FIXME why does .getPoints().add() require Double (capital D)?
    //Points is used to first store all the points of the

    public Hex(int q, int r, HexType type,double size) {
        this.type = type;
        this.q = q;
        this.r = r;

        this.getPoints().addAll(generatePoints(size));
        this.setLayoutX(qLengthX * q + rLengthX * r + startX);
        this.setLayoutY(rLengthY * r + startY);
        if(type != null)
        {


            this.setFill(HexType.toColor(type));
            this.setOnMouseClicked((event) -> {
                Action action = new Action();
                StringBuilder actionString = new StringBuilder();
                actionString.append("trade");
                actionString.append(this.type.toString());
                if(this.type != HexType.WILD)
                {
                    //FIXME create a proper error message;
                    if(!action.loadAction(actionString.toString()))
                    {
                        throw new RuntimeException();
                    }
                    Game.applyGameAction(action);
                }
            });
        }
    }

    public static Double[] generatePoints(double size)
    {
        Double[] points = new Double[12];
        for(int i = 0; i < 6; i++)
        {
            //Ensure the points start at the top vertex and go anticlockwise
            //Add x co-ord
            //FIXME what is a better way than just type casting?
            points[i*2] = Math.cos(Math.PI * ((double)1/2 + -(double)i/3))*size;
            //Add y co-ord
            points[i*2 + 1] = Math.sin(-Math.PI * ((double)1/2 +(double)i/3))*size;
        }
        return points;
    }
    //FIXME setup hex will need to be called before initialising a hex, is this okay?
    public static void setUpHex(double size,double boardHeight,double boardWidth)
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

