# Freecell
 
=========How to Play=========

Run on FreecellMain.java. The game should play through the console, so
if you have the jar you can also play through running on the command line. 

Example:
	C:\user\location_of_jar> java -jar FreecellCombined.jar

	NOTE: Recommend running on an IDE that has support for the card symbols (♥, ♦, ♣, ♠), 
	as regular windows cmd may not be able to display those symbols. 

You can move any card to an open pile, and move a card from the open pile at any time.

Each foundation pile needs to start with an ace, each cards needs to be followed by a consecutive card of
the same suit. 

Each cascade pile can have a card moved on top of it provided that card is one less than the card on top of the 
pile and of a different color.  

You finish the game when all cards are sorted, in ascending order and of the same suit, in each foundation pile. 

=========How to Make A Move==========

With whitespace separating each component, each move needs a source pile and number, a card number, and a 
destination pile and number, in that exact order. Each individual move can be separated by whitespace. 

Multiple card moves are also possible, from a valid build on a Cascade pile to another cascade pile, 
provided the cards within and on each end of the build follow the rules of moving cards. 

F1:
F2: A♦, 2♦
F3:
F4:
O1:
O2:
O3:
O4: 
C1: Q♦, 3♣, 3♥, J♣, 5♦, 3♦, 8♥
C2: 8♣, 4♥, 7♥, 2♥, 5♣, 6♣, 5♥
C3: 7♦, 9♥, K♥, 6♦, 9♦, K♠, K♣
C4: J♦, 10♦, 2♠, 3♠, 9♣, 5♠, 2♣
C5: 8♦, J♠, 7♠, A♠, Q♠
C6: 7♣, A♥, Q♣, A♣, 8♠, J♥
C7: 6♥, 9♠, 10♠, 6♠, 4♠, 10♣
C8: 10♥, 4♣, Q♥, K♦, 4♦

Examples of valid moves on the game state below:
	> C6 6 C5 // move a J♥ on top of Q♠ at C5, making a valid two card build
	> C5 5 C3 // move Q♠, J♥ on top of K♣ at C3, valid three card build
	> C5 4 F1 // move A♠ to F1, starting a new foundation pile

Final Game State after those moves:

F1: A♠
F2: A♦, 2♦
F3:
F4:
O1:
O2:
O3:
O4: 
C1: Q♦, 3♣, 3♥, J♣, 5♦, 3♦, 8♥
C2: 8♣, 4♥, 7♥, 2♥, 5♣, 6♣, 5♥
C3: 7♦, 9♥, K♥, 6♦, 9♦, K♠, K♣, Q♠, J♥
C4: J♦, 10♦, 2♠, 3♠, 9♣, 5♠, 2♣
C5: 8♦, J♠, 7♠
C6: 7♣, A♥, Q♣, A♣, 8♠, 
C7: 6♥, 9♠, 10♠, 6♠, 4♠, 10♣
C8: 10♥, 4♣, Q♥, K♦, 4♦


