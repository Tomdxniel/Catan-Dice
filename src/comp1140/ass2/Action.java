package comp1140.ass2;





// Action.java created by Sam Liersch u7448311
public class Action {
    public int[] resourceArray;
    public int pieceIndex;
    public PieceType pieceType;
    public ActionType type;

    public HexType requiredType;



    //loadActionValid created by Sam Liersch u7448311
    //Loads action into action class
    public boolean  loadAction(String actionString)
    {
        //Required resources for building
        int[][] reqResources = {
                {0,-1,0,0,-1,-1},//Knight
                {-1,0,-1,0,0,0},//Road
                {-1,-1,-1,0,0,-1},//Settlement
                {0,-2,0,0,-3,0}};//City


        //Check if action has type
        if(actionString.length() < 4) return false;
        String type;
        String actionSubject;
        //Swap is the only 4 letter action all other actions are 5 letters
        if(actionString.charAt(3) == 'p')
        {
            actionSubject = actionString.substring(4);
            type = actionString.substring(0,4);
        }
        else
        {
            actionSubject = actionString.substring(5);
            type = actionString.substring(0,5);
        }
        ActionType actionType = ActionType.fromString(type);
        if(actionType == null) return false;
        int pos1;
        int pos2;
        char lastChar =' ';
        int[] resourceArray = new int[] {0,0,0,0,0,0};
        switch (actionType)
        {
            case KEEP -> {
                //Check if correct length
                if (actionSubject.length() > 6) return false;
                this.type = ActionType.KEEP;
                if(actionSubject.length() > 0)
                {
                    lastChar = actionSubject.charAt(0);
                }
                for(char c : actionSubject.toCharArray())
                {
                    //If resources are not in order
                    if((int) lastChar > (int) c)
                    {
                        return false;
                    }
                    //if resources is invalid
                    if(Board.resourceArray.indexOf(c) < 0)
                    {
                        return false;
                    }
                    resourceArray[Board.resourceArray.indexOf(c)] ++;
                    lastChar = c;
                }
                this.resourceArray = resourceArray;
            }
            case BUILD -> {

                this.type = ActionType.BUILD;
                switch (actionSubject.charAt(0))
                {
                    case 'R' -> {
                        //Road
                        this.pieceType = PieceType.ROAD;
                        if(actionSubject.length()!= 5)
                        {
                            return false;
                        }
                        pos1 = Integer.parseInt(actionSubject.substring(1,3));
                        pos2 = Integer.parseInt(actionSubject.substring(3,5));
                        //Check if road is in correct format and within the index bounds
                        if(!(pos1 < pos2 && pos1 >=0 && pos2 < 54))
                        {
                            return false;
                        }
                        this.pieceIndex = Integer.parseInt(actionSubject.substring(1,5));
                        this.resourceArray = reqResources[1];
                    }
                    case  'C' -> {
                        //Castle
                        this.pieceType = PieceType.CASTLE;
                        if(actionSubject.length() != 2) return false;
                        pos1 = Integer.parseInt(actionSubject.substring(1,2));
                        if(!(pos1 >= 0 && pos1 < 4)) return false;
                        this.pieceIndex = pos1;
                        //Potential bug, ReqResources for castle can change depending on available resources
                        //And so resource req is calculated when action is applied
                        this.resourceArray = resourceArray;
                    }
                    case 'S' -> {
                        //Settlement
                        this.pieceType = PieceType.SETTLEMENT;
                        if(actionSubject.length() != 3) return false;
                        pos1 = Integer.parseInt(actionSubject.substring(1,3));
                        if(!(pos1 >= 0 && pos1 < 54)) return false;
                        this.pieceIndex = pos1;
                        this.resourceArray = reqResources[2];
                    }
                    case 'T' -> {
                        //City
                        this.pieceType = PieceType.CITY;
                        if(actionSubject.length() != 3) return false;
                        pos1 = Integer.parseInt(actionSubject.substring(1,3));
                        if(!(pos1 >= 0 && pos1 < 54)) return false;
                        this.pieceIndex = pos1;
                        this.resourceArray = reqResources[3];
                    }
                    case 'K' -> {
                        //Knight
                        this.pieceType = PieceType.KNIGHT;
                        if(actionSubject.length() != 3) return false;
                        pos1 = Integer.parseInt(actionSubject.substring(1,3));
                        if(!(pos1 >= 0 && pos1 < 20)) return false;
                        this.pieceIndex = pos1;
                        this.resourceArray = reqResources[0];
                    }
                    default ->
                    {
                        return false;
                    }
                }
            }
            case TRADE -> {
                this.type = ActionType.TRADE;
                lastChar = actionSubject.charAt(0);
                //Check resources are in order
                for(char r : actionSubject.toCharArray())
                {
                    if((int) lastChar > r) return false;
                    //Cannot trade money for money
                    if(r == 'm') return false;
                    //Ensure resource is of the valid type
                    if(Board.resourceArray.indexOf(r) == -1) return false;
                    resourceArray[Board.resourceArray.indexOf(r)]++;
                    resourceArray[3] -= 2;

                }
                this.resourceArray = resourceArray;
            }
            case SWAP -> {

                this.type = ActionType.SWAP;

                //Can only swap 1 resource for 1 resource
                if (actionSubject.length() != 2) return false;
                //Can't Swap 1 resource for the same resource
                if(actionSubject.charAt(0) == actionSubject.charAt(1)) return false;
                //Check if it's a valid resource
                if(Board.resourceArray.indexOf(actionSubject.charAt(0)) == -1) return false;
                if(Board.resourceArray.indexOf(actionSubject.charAt(1)) == -1) return false;

                this.requiredType = HexType.fromChar(actionSubject.charAt(1));
                resourceArray[Board.resourceArray.indexOf(actionSubject.charAt(0))] --;
                resourceArray[Board.resourceArray.indexOf(actionSubject.charAt(1))] ++;
                this.resourceArray = resourceArray;
            }
        }
        return true;
    }
    @Override
    public String toString()
    {
        StringBuilder output = new StringBuilder();
        //string output is dependent on the type of Action
        switch (type)
        {
            case KEEP ->
            {
                output.append("keep");
                for(int i = 0; i < 6; i++)
                {
                    output.append(Board.resourceArray.substring(i,i+1).repeat(this.resourceArray[i]));
                }
            }
            case BUILD -> {
                output.append("build");
                output.append(pieceType.toChar());
                switch (pieceType)
                {
                    //String has to be of a certain length EG road is R followed 4 numbers
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
                    output.append(Board.resourceArray.substring(i,i+1).repeat(this.resourceArray[i]));

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
