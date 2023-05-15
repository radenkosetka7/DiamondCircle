# DiamondCircle, Faculty of Electrical Engineering, 2022

Application specification

A simple desktop application tha represents Diamond Circle game. 
The game takes place on a matrix of minimum dimensions 7x7, a
maximum 10x10. The game is played by a maximum of 4 players, and a minimum of 2. Dimensions of the die and number
of the player are set before starting the application itself. It is necessary to validate the user
input.
Each player has a name (which must be unique) and owns four pieces of the same color. Each of
the figure is characterized by color and manner of movement. There are three types of figures: regular figure, floating figure and
super fast figure. Each of the figures can be red, green, blue or yellow. Ordinary
a figure crosses a given number of squares, as does a floating figure. A super fast figure crosses over twice as much
given number of fields. A normal and a super fast figure can fall into the hole, while the floating one remains
to float above the hole. At the beginning of the game, each player is given four randomly selected figures
same color.
In addition to the figures used by the players, there is also one "ghost" figure - it starts its movement
when the first player i moves along the path "in the background" placing bonus fields - diamonds, on
path. Sets the random number of diamonds in the range from 2 to the dimension of the die, to random
positions. Set up is done every 5 seconds and lasts until the end of the game. When a figure comes across
on the diamond, she "picks up" it and in the continuation of the game, by moving, the number of squares she crosses
is increased by the number of diamonds she has picked up.
The order of players is determined
in a random way and players play a move one after the other. A move is considered a movement
figures for a certain number of fields from position to position. When moving, take care that if
if the square to which it should be moved is already occupied, the figure is placed on the next free square.
Moving from field to field should take one second. The mode of movement is determined by
based on a randomly selected card from a deck of 52 cards. There is a regular ticket and a special ticket.
A regular card consists of an image and the number of squares that the figure crosses. Special ticket on you
it only has a picture. When a special card is drawn, holes are created in n places on the path.
The holes are black. After drawing, the card is returned to the deck. Under the pull out
considers plotting the map on the GUI. If a figure is on the hole, but it is not in question
a floating figure, it collapses. When a piece falls, if the player has more pieces, he moves
with a new one from the beginning. The game ends when all players run out of pieces - ie. each of the figures
of players reached the goal (field 25 in the example) or they all failed. Remember for each figure
information about the fields crossed, as well as the time of movement. When displaying a figure on the field
it is necessary that both the color and the type of figure are visible in a certain way. The game can stop and
start again. The game is played automatically. At the end of the game, the results are saved in text format
files named GAME_current_time.txt
