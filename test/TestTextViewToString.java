import cs3500.freecell.model.PileType;
import cs3500.freecell.model.hw02.SimpleFreecellModel;
import cs3500.freecell.view.FreecellTextView;
import org.junit.Before;
import org.junit.Test;

import java.util.Random;

import static org.junit.Assert.assertEquals;

/**
 * Test class for the toString() method implemented by FreecellTextView.
 */
public class TestTextViewToString {
  private SimpleFreecellModel simpleFreecs1;
  private SimpleFreecellModel unstartedGame;
  private SimpleFreecellModel shuffledGame;

  @Before
  public void init() {
    simpleFreecs1 = new SimpleFreecellModel();
    simpleFreecs1.startGame(simpleFreecs1.getDeck(), 5, 2, false);
    unstartedGame = new SimpleFreecellModel();

    shuffledGame = new SimpleFreecellModel(new Random(123));
    shuffledGame.startGame(shuffledGame.getDeck(), 8, 4, true);
  }

  // testing toString() of an unstarted game
  @Test
  public void unStartedTest() {
    assertEquals(new FreecellTextView(unstartedGame).toString(), "");
  }


  // testing toString() after unstartedGame has thrown an exception when
  // attempting to start
  @Test
  public void postExceptionTest() {
    try {
      unstartedGame.startGame(unstartedGame.getDeck(), 3, 0, false);
    } catch (IllegalArgumentException ia) {
      assertEquals(new FreecellTextView(unstartedGame).toString(), "");
    }
  }

  // testing an freshly started game with an unshuffled and shuffled deck
  @Test
  public void freshlyStarted() {
    assertEquals(new FreecellTextView(simpleFreecs1).toString(),
            "F1:\nF2:\nF3:\nF4:\nO1:\nO2:\n"
            + "C1: A♣, 6♣, J♣, 3♦, 8♦, K♦, 5♥, 10♥, 2♠, 7♠, Q♠\n"
            + "C2: 2♣, 7♣, Q♣, 4♦, 9♦, A♥, 6♥, J♥, 3♠, 8♠, K♠\n"
            + "C3: 3♣, 8♣, K♣, 5♦, 10♦, 2♥, 7♥, Q♥, 4♠, 9♠\n"
            + "C4: 4♣, 9♣, A♦, 6♦, J♦, 3♥, 8♥, K♥, 5♠, 10♠\n"
            + "C5: 5♣, 10♣, 2♦, 7♦, Q♦, 4♥, 9♥, A♠, 6♠, J♠");
  }

  // testing toString() after a move has been made in the game
  @Test
  public void afterOneMove() {
    // move K♠ from C2 to O2
    simpleFreecs1.move(PileType.CASCADE, 1, 10,
            PileType.OPEN, 1);
    assertEquals(new FreecellTextView(simpleFreecs1).toString(),
            "F1:\nF2:\nF3:\nF4:\nO1:\nO2: K♠\n"
                    + "C1: A♣, 6♣, J♣, 3♦, 8♦, K♦, 5♥, 10♥, 2♠, 7♠, Q♠\n"
                    + "C2: 2♣, 7♣, Q♣, 4♦, 9♦, A♥, 6♥, J♥, 3♠, 8♠\n"
                    + "C3: 3♣, 8♣, K♣, 5♦, 10♦, 2♥, 7♥, Q♥, 4♠, 9♠\n"
                    + "C4: 4♣, 9♣, A♦, 6♦, J♦, 3♥, 8♥, K♥, 5♠, 10♠\n"
                    + "C5: 5♣, 10♣, 2♦, 7♦, Q♦, 4♥, 9♥, A♠, 6♠, J♠");
  }

  // testing toString() after multiple moves in the game
  @Test
  public void afterMultMoves() {
    // no moves
    assertEquals(new FreecellTextView(shuffledGame).toString(),
            "F1:\n"
                    + "F2:\n"
                    + "F3:\n"
                    + "F4:\n"
                    + "O1:\n"
                    + "O2:\n"
                    + "O3:\n"
                    + "O4:\n"
                    + "C1: 10♥, 6♠, Q♦, J♥, K♠, K♥, 9♦\n"
                    + "C2: Q♥, 4♣, 9♥, 8♦, 7♠, J♦, A♥\n"
                    + "C3: 6♥, 8♥, Q♠, 5♦, 8♠, 3♣, 9♠\n"
                    + "C4: 6♣, K♣, J♣, 2♦, 9♣, 3♠, 5♥\n"
                    + "C5: 4♦, 3♥, K♦, 5♣, J♠, 10♦\n"
                    + "C6: 5♠, A♦, 3♦, 7♣, Q♣, 6♦\n"
                    + "C7: 7♦, 10♠, 8♣, A♠, 2♠, A♣\n"
                    + "C8: 4♠, 10♣, 2♣, 4♥, 7♥, 2♥");

    // move A♥ from C2 to F1
    shuffledGame.move(PileType.CASCADE, 1, 6, PileType.FOUNDATION, 0);
    // move 2♥ from C8 to F1
    shuffledGame.move(PileType.CASCADE, 7, 5, PileType.FOUNDATION, 0);
    // move A♣ from C7 to F2
    shuffledGame.move(PileType.CASCADE, 6, 5, PileType.FOUNDATION, 1);
    assertEquals(new FreecellTextView(shuffledGame).toString(),
            "F1: A♥, 2♥\n"
                    + "F2: A♣\n"
                    + "F3:\n"
                    + "F4:\n"
                    + "O1:\n"
                    + "O2:\n"
                    + "O3:\n"
                    + "O4:\n"
                    + "C1: 10♥, 6♠, Q♦, J♥, K♠, K♥, 9♦\n"
                    + "C2: Q♥, 4♣, 9♥, 8♦, 7♠, J♦\n"
                    + "C3: 6♥, 8♥, Q♠, 5♦, 8♠, 3♣, 9♠\n"
                    + "C4: 6♣, K♣, J♣, 2♦, 9♣, 3♠, 5♥\n"
                    + "C5: 4♦, 3♥, K♦, 5♣, J♠, 10♦\n"
                    + "C6: 5♠, A♦, 3♦, 7♣, Q♣, 6♦\n"
                    + "C7: 7♦, 10♠, 8♣, A♠, 2♠\n"
                    + "C8: 4♠, 10♣, 2♣, 4♥, 7♥");

    // move 2♠ from C7 to O3
    shuffledGame.move(PileType.CASCADE, 6, 4, PileType.OPEN, 2);
    // move A♠ from C7 to F4
    shuffledGame.move(PileType.CASCADE, 6, 3, PileType.FOUNDATION, 3);
    // move 2♠ from O3 to F4
    shuffledGame.move(PileType.OPEN, 2, 0, PileType.FOUNDATION, 3);
    // move 7♥ from C8 to C7
    shuffledGame.move(PileType.CASCADE, 7, 4, PileType.CASCADE, 6);
    assertEquals(new FreecellTextView(shuffledGame).toString(),
            "F1: A♥, 2♥\n"
                    + "F2: A♣\n"
                    + "F3:\n"
                    + "F4: A♠, 2♠\n"
                    + "O1:\n"
                    + "O2:\n"
                    + "O3:\n"
                    + "O4:\n"
                    + "C1: 10♥, 6♠, Q♦, J♥, K♠, K♥, 9♦\n"
                    + "C2: Q♥, 4♣, 9♥, 8♦, 7♠, J♦\n"
                    + "C3: 6♥, 8♥, Q♠, 5♦, 8♠, 3♣, 9♠\n"
                    + "C4: 6♣, K♣, J♣, 2♦, 9♣, 3♠, 5♥\n"
                    + "C5: 4♦, 3♥, K♦, 5♣, J♠, 10♦\n"
                    + "C6: 5♠, A♦, 3♦, 7♣, Q♣, 6♦\n"
                    + "C7: 7♦, 10♠, 8♣, 7♥\n"
                    + "C8: 4♠, 10♣, 2♣, 4♥");
  }
}
