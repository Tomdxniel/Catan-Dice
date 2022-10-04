## Code Review

Reviewed by: <Thomas Daniel>, <u7490675>

Reviewing code written by: <Eliz So> <u7489812>

Component: <isActionWellFormed>

### Comments

<Some good features of this piece of code are the structuring as even without 
looking at any comments or prior documentation, the reader can see clearly 
what each section is doing. The code is documented well, some extra comments
around the for loops could help an inexperienced reader. The structure of this 
implementation and the implementation itself do not rely on any other classes 
and uses the board string. This is fine however it may have been easier to utilize
the “action” class.  The variables have been named so that they describe their 
contents or purpose, and as none of them are too long there is no need for 
typical java conventions such as camel case. Overall, the code is concise and
effective, additionally as far as I can see there are no bugs in the code, 
and it should perform as intended. >