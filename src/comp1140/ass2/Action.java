package comp1140.ass2;

import comp1140.ass2.ActionType;
import comp1140.ass2.Piece;

public class Action {
    public int[] resourceArray;
    public int pieceIndex;
    public PieceType pieceType;
    public ActionType type;

    @Override
    public String toString()
    {
        StringBuilder output = new StringBuilder();
        switch (type)
        {
            case KEEP ->
            {
                output.append("keep");
                for(int i = 0; i < 6; i++)
                {
                    for(int y = 0; y < this.resourceArray[i]; y++)
                    {
                        output.append(Board.resourceArray.charAt(i));
                    }
                }
            }
            //FIXME is there a way to do case A or B?
            case BUILD -> {
                output.append("build");
                output.append(pieceType.toChar());
                switch (pieceType)
                {
                    case ROAD -> output.append(String.format("%4d",this.pieceIndex).replace(' ','0'));
                    case CASTLE -> output.append(pieceIndex);
                    default -> output.append(String.format("%2d",this.pieceIndex).replace(' ','0'));
                }
            }
            case TRADE ->
            {
                output.append("trade");
                for(int i = 0; i < 6; i++)
                {
                    if(Board.resourceArray.charAt(i) == 'm')
                    {
                        continue;
                    }
                    for(int y = 0; y < this.resourceArray[i]; y++)
                    {
                        output.append(Board.resourceArray.charAt(i));
                    }
                }
            }
            case SWAP ->
            {
                output.append("swap");
                for(int i = 0; i < 6; i++)
                {
                     if(this.resourceArray[i] < 0)
                     {
                         output.append(Board.resourceArray.charAt(i));
                     }
                }
                for(int i = 0; i < 6; i++)
                {
                    if(this.resourceArray[i] > 0)
                    {
                        output.append(Board.resourceArray.charAt(i));
                    }
                }
            }
        }
        return output.toString();
    }
}
