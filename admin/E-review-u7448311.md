## Code Review

Reviewed by: Sam Liersch, u7448311

Reviewing code written by: Tom Daniel u7490675

Component: isActionValid

### Comments 
Coding Style:
The lack of comments and reliance on else-if statements makes the code extremely hard to read and understand

In this section of code especially there is a bunch of if,else and else-if statements in a stack with no comments given describing what is happening
https://gitlab.cecs.anu.edu.au/-/ide/project/u7448311/comp1140-ass2/edit/main/-/src/comp1140/ass2/CatanDiceExtra.java#L392


Bugs:
1)To check if its a road that appears on the board this code is used.
`if (cor2 - cor1 <= 6 && cor2 - cor1 > 2 )`
https://gitlab.cecs.anu.edu.au/u7448311/comp1140-ass2/-/blob/main/src/comp1140/ass2/CatanDiceExtra.java#L365
However this does not work as with boardState "W00WXW00X00" acitons "buildR4750","buildR4751","buildR4752" are valid even though they do not appear on the board.

2)There is no code to ensure roads are at least 5 units apart from eachother in setup

Test cases to show this is a boardState of "X00WR0004XW00X00" with action buildR0105" should evaluate to false as the roads only have 1 space inbetween however it evaluates as true.

3)xString contains the score 
`String xString = boardState.substring(boardState.indexOf("X")+1);`
https://gitlab.cecs.anu.edu.au/-/ide/project/u7448311/comp1140-ass2/edit/main/-/src/comp1140/ass2/CatanDiceExtra.java#L357
This causes problems with checks involving xString.contains()

4)Knights,settlements,cities with no neighbours can be built
Test case boardState "W33gowWXW00X00" action "buildK01" should evaluate to false as no neighbour to the knight is present

5)Knights,settlements,cities can be built ontop of eachother
Test case boardState "W33gowWJ04XW00X00" action "buildK04" should evaluate to false as knight is built ontop of another knight

6)Knights / Cities can be built everywhere
Test case boardState "W33ggoooWXW00X00" action "buildT11" should evaluate to false as 11 is a position that a city should not be able to be placed

7)Cities can be placed not on settlements
Test case boardState "W33ggoooWXW00X00" action "buildT01" should evaluate to false as 01 does not contain a settlement
