package comp1140.ass2;

import javafx.scene.paint.Color;
import javafx.scene.shape.Polygon;

public class ResourcePiece extends Polygon {
    public int type;
    public boolean clicked;
    public Polygon outline;
    public ResourcePiece(int type, double x, double y)
    {
        this.type = type;
        this.getPoints().addAll(-10.0,10.0,10.0,10.0,10.0,-10.0,-10.0,-10.0);
        this.setLayoutX(x);
        this.setLayoutY(y);
        this.setFill(null);


        this.outline = new Polygon();
        this.outline.getPoints().addAll(-12.0,12.0,12.0,12.0,12.0,-12.0,-12.0,-12.0);
        this.outline.setLayoutX(x);
        this.outline.setLayoutY(y);
        this.outline.setFill(null);

        this.setOnMousePressed((event) -> {
            this.clicked = !this.clicked;
            if(clicked && this.type != -1)
            {
                outline.setFill(Color.RED);
            }
            else {
                outline.setFill(null);
            }
        });
    }
    public void updateColor()
    {
        this.setFill(switch (type)
                {
                    case 0 -> Color.BROWN;
                    case 1 -> Color.YELLOW;
                    case 2 -> Color.DARKGREEN;
                    case 3 -> Color.GOLD;
                    case 4 -> Color.SILVER;
                    case 5 -> Color.GREENYELLOW;
                    default -> null;
                }
        );
        //Remove highlight square main square is null
        if(this.getFill() == null)
        {
            this.outline.setFill(null);
        }
    }
}
