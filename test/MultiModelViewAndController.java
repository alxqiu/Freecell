import cs3500.freecell.controller.FreecellController;
import cs3500.freecell.controller.SimpleFreecellController;
import cs3500.freecell.model.FreecellModel;
import cs3500.freecell.model.PileType;
import cs3500.freecell.model.hw02.ICard;
import cs3500.freecell.model.hw04.MultiFreecellModel;
import cs3500.freecell.view.FreecellTextView;
import cs3500.freecell.view.FreecellView;
import org.junit.Before;
import org.junit.Test;

import static org.junit.Assert.assertEquals;

import java.io.IOException;
import java.io.StringReader;
import java.util.Random;

/**
 * Tests if the view and controller can execute and show the multi-card moves. Single card
 * moves done through controller are tested in AbstractControllerGameplay.
 */
public class MultiModelViewAndController {
  private FreecellModel<ICard> newMultiModel;
  StringBuilder outputText;
  private FreecellView newView;

  @Before
  public void init() {
    outputText = new StringBuilder();
    newMultiModel = new MultiFreecellModel(new Random(772));
    newView = new FreecellTextView(newMultiModel, outputText);
  }

  // test some stuff with rendering multi card moves...
  // testing three card move
  @Test
  public void testingViewAfterThreeMoves() throws IOException {
    // moves: C3 8 C7 C6 7 O1 C6 6 O2 C7 7 C6 C3 7 O3 C6 5 C3
    newMultiModel.startGame(newMultiModel.getDeck(), 7, 6, true);
    newMultiModel.move(PileType.CASCADE, 2, 7, PileType.CASCADE, 6);
    newMultiModel.move(PileType.CASCADE, 5, 6, PileType.OPEN, 0);
    newMultiModel.move(PileType.CASCADE, 5, 5, PileType.OPEN, 1);
    newMultiModel.move(PileType.CASCADE, 6, 6, PileType.CASCADE, 5);
    newMultiModel.move(PileType.CASCADE, 2, 6, PileType.OPEN, 2);
    newMultiModel.move(PileType.CASCADE, 5, 4, PileType.CASCADE, 2);
    assertEquals(newView.toString(),
            "F1:\nF2:\nF3:\nF4:\n"
                    + "O1: A♠\n"
                    + "O2: Q♠\n"
                    + "O3: 4♠\n"
                    + "O4:\n"
                    + "O5:\n"
                    + "O6:\n"
                    + "C1: J♠, K♦, 10♦, Q♦, A♦, 10♥, 2♣, 8♣\n"
                    + "C2: 3♣, 3♠, 7♦, Q♣, 4♣, Q♥, J♣, 3♥\n"
                    + "C3: 7♥, 2♥, 10♣, 4♥, 8♥, 9♠, 8♦, 7♣, 6♥\n"
                    + "C4: 9♣, J♥, 9♥, 2♠, 6♠, A♣, 4♦\n"
                    + "C5: 5♥, 5♣, J♦, 8♠, 9♦, A♥, K♥\n"
                    + "C6: 10♠, 7♠, 2♦, 5♦\n"
                    + "C7: K♣, K♠, 5♠, 6♣, 3♦, 6♦");
  }


  // testing could not start for controller
  // test could not start game: attempting to play game with invalid args
  @Test
  public void couldNotStart() {
    FreecellController newController = new SimpleFreecellController(newMultiModel,
            new StringReader("q"), outputText);
    newController.playGame(newMultiModel.getDeck(), 3, 2, true);
    assertEquals(outputText.toString(), "Could not start game.");
  }

  /*
  F1:
  F2:
  F3:
  F4:
  O1:
  O2:
  O3:
  O4:
  O5:
  O6:
  C1: J♠, K♦, 10♦, Q♦, A♦, 10♥, 2♣, 8♣
  C2: 3♣, 3♠, 7♦, Q♣, 4♣, Q♥, J♣, 3♥
  C3: 7♥, 2♥, 10♣, 4♥, 8♥, 9♠, 4♠, 6♥
  C4: 9♣, J♥, 9♥, 2♠, 6♠, A♣, 4♦
  C5: 5♥, 5♣, J♦, 8♠, 9♦, A♥, K♥
  C6: 10♠, 7♠, 2♦, 5♦, 8♦, Q♠, A♠
  C7: K♣, K♠, 5♠, 6♣, 3♦, 6♦, 7♣
   */
  @Test
  public void testThreeCardMove() {
    // move 6♥ from C3 to C7, then move A♠ from C6 to O1, then move Q♠ from C6 to O2,
    // move 7♣, 6♥ from C7 to C6, move 4♠ from C3 to O3, then move 8♦ 7♣, 6♥ to C3
    FreecellController newController = new SimpleFreecellController(newMultiModel,
            new StringReader("C3 8 C7 C6 7 O1 C6 6 O2 C7 7 C6 C3 7 O3 C6 5 C3 q"), outputText);
    newController.playGame(newMultiModel.getDeck(), 7, 6, true);

    // assert the state of the game right before the three card move with C6 5 C3
    assertEquals(outputText.toString().substring(1377, 1647),
            "F1:\nF2:\nF3:\nF4:\n"
                    + "O1: A♠\n"
                    + "O2: Q♠\n"
                    + "O3: 4♠\n"
                    + "O4:\n"
                    + "O5:\n"
                    + "O6:\n"
                    + "C1: J♠, K♦, 10♦, Q♦, A♦, 10♥, 2♣, 8♣\n"
                    + "C2: 3♣, 3♠, 7♦, Q♣, 4♣, Q♥, J♣, 3♥\n"
                    + "C3: 7♥, 2♥, 10♣, 4♥, 8♥, 9♠\n"
                    + "C4: 9♣, J♥, 9♥, 2♠, 6♠, A♣, 4♦\n"
                    + "C5: 5♥, 5♣, J♦, 8♠, 9♦, A♥, K♥\n"
                    + "C6: 10♠, 7♠, 2♦, 5♦, 8♦, 7♣, 6♥\n"
                    + "C7: K♣, K♠, 5♠, 6♣, 3♦, 6♦\n");
    // assert the final state of the game: after 8♦, 7♣, 6♥ have been moved to C3
    assertEquals(outputText.toString().substring(1647, 1940),
            "F1:\nF2:\nF3:\nF4:\n"
                    + "O1: A♠\n"
                    + "O2: Q♠\n"
                    + "O3: 4♠\n"
                    + "O4:\n"
                    + "O5:\n"
                    + "O6:\n"
                    + "C1: J♠, K♦, 10♦, Q♦, A♦, 10♥, 2♣, 8♣\n"
                    + "C2: 3♣, 3♠, 7♦, Q♣, 4♣, Q♥, J♣, 3♥\n"
                    + "C3: 7♥, 2♥, 10♣, 4♥, 8♥, 9♠, 8♦, 7♣, 6♥\n"
                    + "C4: 9♣, J♥, 9♥, 2♠, 6♠, A♣, 4♦\n"
                    + "C5: 5♥, 5♣, J♦, 8♠, 9♦, A♥, K♥\n"
                    + "C6: 10♠, 7♠, 2♦, 5♦\n"
                    + "C7: K♣, K♠, 5♠, 6♣, 3♦, 6♦\n"
                    + "Game quit prematurely.\n");
  }

  // show an invalid move message from multi-move's exception being thrown
  @Test
  public void testBadMultiMoveMessage() {
    // move 6♥ from C3 to C7, then attempt to move 7♣, 6♥ on top of 8♣ at C1
    FreecellController newController = new SimpleFreecellController(newMultiModel,
            new StringReader("C3 8 C7 C7 7 C1 q"), outputText);
    newController.playGame(newMultiModel.getDeck(), 7, 6, true);
    assertEquals(outputText.toString().substring(290, 634),
            "F1:\nF2:\nF3:\nF4:\n"
                    + "O1:\n"
                    + "O2:\n"
                    + "O3:\n"
                    + "O4:\n"
                    + "O5:\n"
                    + "O6:\n"
                    + "C1: J♠, K♦, 10♦, Q♦, A♦, 10♥, 2♣, 8♣\n"
                    + "C2: 3♣, 3♠, 7♦, Q♣, 4♣, Q♥, J♣, 3♥\n"
                    + "C3: 7♥, 2♥, 10♣, 4♥, 8♥, 9♠, 4♠\n"
                    + "C4: 9♣, J♥, 9♥, 2♠, 6♠, A♣, 4♦\n"
                    + "C5: 5♥, 5♣, J♦, 8♠, 9♦, A♥, K♥\n"
                    + "C6: 10♠, 7♠, 2♦, 5♦, 8♦, Q♠, A♠\n"
                    + "C7: K♣, K♠, 5♠, 6♣, 3♦, 6♦, 7♣, 6♥\n"
                    + "invalid move: cannot form build with destination\n"
                    + "Game quit prematurely.");
  }
}
