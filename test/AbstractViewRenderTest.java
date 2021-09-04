import cs3500.freecell.model.FreecellModel;
import cs3500.freecell.model.PileType;
import cs3500.freecell.model.hw02.ICard;
import cs3500.freecell.model.hw02.SimpleFreecellModel;
import cs3500.freecell.model.hw04.MultiFreecellModel;
import cs3500.freecell.view.FreecellTextView;
import org.junit.Before;
import org.junit.Test;

import java.io.IOException;
import java.util.Random;

import static org.junit.Assert.assertEquals;

/**
 * Abstract class for testing view object render methods and constructor exceptions
 * with both SimpleFreecellModel and MultiFreecellModel.
 */
public abstract class AbstractViewRenderTest {
  private FreecellModel<ICard> gameOne;
  private FreecellTextView freshBoard;
  private Appendable freshBoardHolder;

  protected abstract FreecellModel<ICard> constructGameOne();

  @Before
  public void init() {
    gameOne = constructGameOne();
    freshBoardHolder = new StringBuilder();
    freshBoard = new FreecellTextView(gameOne, freshBoardHolder);
  }

  // test exceptions for null args instead of model
  @Test(expected = IllegalArgumentException.class)
  public void givenNullModel() {
    new FreecellTextView(null);
  }

  // given null instead of an appendable
  @Test(expected = IllegalArgumentException.class)
  public void givenNullOutput() {
    new FreecellTextView(this.gameOne, null);
  }

  // testing that renderBoard() and renderMessage can throw IOExceptions that
  // result from IOExceptions of the Appendable

  // testing a case calling append to a Appendable that will throw an exception if we try to
  // append any String longer than empty String
  @Test(expected = IOException.class)
  public void noAppendingBoard() throws IOException {
    this.gameOne.startGame(this.gameOne.getDeck(), 4, 1, true);
    // calling renderBoard with this startGame will append a String of length 244 lmao
    Appendable noSpaceToAppendHere = new MockAppendable(1);
    FreecellTextView viewToFail = new FreecellTextView(this.gameOne, noSpaceToAppendHere);
    viewToFail.renderBoard();
  }

  // testing that renderMessage will throw, even if it worked right before it, trying to
  // simulate the unpredictable nature of IOException.
  @Test(expected = IOException.class)
  public void noAppendingMessageTwice() throws IOException {
    // just enough space to handle the first message, but not the second
    Appendable noSpaceToAppendHere = new MockAppendable(15);
    FreecellTextView viewToFail = new FreecellTextView(this.gameOne, noSpaceToAppendHere);
    viewToFail.renderMessage("testing once\n");
    // this assertion still is meaningful here, as the assertEquals will cause ComparisonFailure
    // if not correct.
    assertEquals(noSpaceToAppendHere.toString(), "testing once\n");
    // this is the message that causes the IOException throw
    viewToFail.renderMessage("another message is not acceptable!");
  }

  // testing the renderBoard() of a freshly created board, and of an unstarted game.
  @Test
  public void testRenderFreshBoard() throws IOException {
    // checking that renderBoard() will produce empty String before game starts
    this.freshBoard.renderBoard();
    assertEquals(this.freshBoardHolder.toString(), "");

    // no need to refresh the StringBuilder() as the only thing appended would be empty
    gameOne.startGame(gameOne.getDeck(), 4, 3, true);
    freshBoard.renderBoard();
    // testing StringBuilder objects using .toString() per piazza post @905
    assertEquals(freshBoardHolder.toString(),
            "F1:\n"
                    + "F2:\n"
                    + "F3:\n"
                    + "F4:\n"
                    + "O1:\n"
                    + "O2:\n"
                    + "O3:\n"
                    + "C1: 5♣, 10♦, K♥, 2♣, 8♥, K♠, 8♠, 5♦, K♦, J♣, 4♠, 10♣, 6♦\n"
                    + "C2: 10♥, J♥, 6♥, 6♣, 7♣, 5♥, Q♦, 4♣, 3♠, 4♦, 4♥, 6♠, 9♦\n"
                    + "C3: Q♠, A♦, 9♠, 9♥, 3♦, 10♠, A♠, 2♥, 7♦, A♣, J♠, 8♣, 3♣\n"
                    + "C4: 8♦, Q♣, 2♠, J♦, A♥, 5♠, Q♥, 2♦, 3♥, 9♣, 7♠, K♣, 7♥\n");
  }

  // testing the use of renderBoard with a game where some moves have been made
  @Test
  public void testRenderSomeBoardMoves() throws IOException {
    gameOne.startGame(gameOne.getDeck(), 7, 4, true);
    /*
      Current board state after starting....
      F1:
      F2:
      F3:
      F4:
      O1:
      O2:
      O3:
      O4:
      C1: 5♣, Q♣, 9♥, 5♥, 5♦, 3♥, J♠, 9♦
      C2: 10♥, K♥, J♦, 10♠, 4♣, J♣, 7♠, 3♣
      C3: Q♠, 6♥, 8♥, 5♠, 2♥, 4♦, 10♣, 7♥
      C4: 8♦, 9♠, 7♣, 8♠, 2♦, A♣, 6♠
      C5: 10♦, 2♠, 3♦, Q♦, K♦, 9♣, 8♣
      C6: J♥, 2♣, A♥, A♠, 3♠, 4♠, K♣
      C7: A♦, 6♣, K♠, Q♥, 7♦, 4♥, 6♦
     */
    // moving 7♥ from C3 to C5
    gameOne.move(PileType.CASCADE, 2, 7, PileType.CASCADE, 4);
    // moving 6♠ from C4 to C5
    gameOne.move(PileType.CASCADE, 3, 6, PileType.CASCADE, 4);
    // moving A♣ from C4 to F2
    gameOne.move(PileType.CASCADE, 3, 5, PileType.FOUNDATION, 1);
    freshBoard.renderBoard();
    assertEquals(freshBoardHolder.toString(),
            "F1:\n"
                    + "F2: A♣\n"
                    + "F3:\n"
                    + "F4:\n"
                    + "O1:\n"
                    + "O2:\n"
                    + "O3:\n"
                    + "O4:\n"
                    + "C1: 5♣, Q♣, 9♥, 5♥, 5♦, 3♥, J♠, 9♦\n"
                    + "C2: 10♥, K♥, J♦, 10♠, 4♣, J♣, 7♠, 3♣\n"
                    + "C3: Q♠, 6♥, 8♥, 5♠, 2♥, 4♦, 10♣\n"
                    + "C4: 8♦, 9♠, 7♣, 8♠, 2♦\n"
                    + "C5: 10♦, 2♠, 3♦, Q♦, K♦, 9♣, 8♣, 7♥, 6♠\n"
                    + "C6: J♥, 2♣, A♥, A♠, 3♠, 4♠, K♣\n"
                    + "C7: A♦, 6♣, K♠, Q♥, 7♦, 4♥, 6♦\n");
  }

  // testing renderMessage and its accumulation on the Appendable object
  @Test
  public void testMessageAccumulation() throws IOException {
    // tests that renderMessage doesn't print a newline when rendering empty String
    freshBoard.renderMessage("");
    assertEquals(freshBoardHolder.toString(), "");
    freshBoard.renderMessage("This will be the first actual line of text\n");
    assertEquals(freshBoardHolder.toString(), "This will be the first actual line of text\n");
    freshBoard.renderMessage("The second line is here!\n");
    assertEquals(freshBoardHolder.toString(), "This will be the first actual line of text\n"
            + "The second line is here!\n");
  }

  // testing that nothing will happen when attempting to render a null message
  @Test
  public void testDoNothingRenderNull() throws IOException {
    freshBoard.renderMessage(null);
    assertEquals(freshBoardHolder.toString(), "");
  }
  
  /**
   * Subclass of abstract test class for SimpleFreecellModel.
   */
  public static class TestSimpleFreecellModel extends AbstractViewRenderTest {

    @Override
    protected FreecellModel<ICard> constructGameOne() {
      return new SimpleFreecellModel(new Random(62));
    }
  }

  /**
   * Subclass of abstract test class for MultiFreecellModel.
   */
  public static class TestMultiFreecellModel extends AbstractViewRenderTest {

    @Override
    protected FreecellModel<ICard> constructGameOne() {
      return new MultiFreecellModel(new Random(62));
    }
  }
}
