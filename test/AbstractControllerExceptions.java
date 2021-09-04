import cs3500.freecell.controller.SimpleFreecellController;
import cs3500.freecell.model.FreecellModel;
import cs3500.freecell.model.hw02.Card;
import cs3500.freecell.model.hw02.CardType;
import cs3500.freecell.model.hw02.ICard;
import cs3500.freecell.model.hw02.SimpleFreecellModel;
import cs3500.freecell.model.hw04.MultiFreecellModel;
import org.junit.Before;
import org.junit.Test;

import java.io.InputStreamReader;
import java.io.StringReader;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;

import static org.junit.Assert.assertEquals;

/**
 * Abstract class for testing controller exceptions for both types of FreecellModel.
 *
 * <p>Ensures that MultiFreecellModel can behave the same way for controller tests as
 * SimpleFreecellModel.</p>
 */
public abstract class AbstractControllerExceptions {
  private FreecellModel<ICard> newModel;
  private FreecellModel<ICard> easyModel;

  protected abstract FreecellModel<ICard> makeNewModel();

  protected abstract FreecellModel<ICard> makeEasyModel();

  @Before
  public void init() {
    newModel = makeNewModel();
    easyModel = makeEasyModel();
  }

  // testing that creating with null model will throw exception
  @Test(expected = IllegalArgumentException.class)
  public void testNullModel() {
    new SimpleFreecellController(null, new InputStreamReader(System.in), System.out);
  }

  // testing that creating with null readable will throw exception
  @Test(expected = IllegalArgumentException.class)
  public void testNullReadable() {
    new SimpleFreecellController(newModel, null, System.out);
  }

  // testing that creating with null appendable will throw exception
  @Test(expected = IllegalArgumentException.class)
  public void testNullAppendable() {
    new SimpleFreecellController(newModel, new InputStreamReader(System.in), null);
  }

  /*
    Testing playGame exceptions here....
   */
  // testing illegalargumentexception when given null
  @Test(expected = IllegalArgumentException.class)
  public void testNullDeck() {
    SimpleFreecellController newController = new SimpleFreecellController(newModel,
            new StringReader(""),
            new StringBuilder());
    newController.playGame(null, 4, 3, false);
  }

  // testing that an IllegalStateException can be thrown when trying to transmit
  // "Could not start game." when the model complains about starting with bad arguments
  @Test(expected = IllegalStateException.class)
  public void testCouldNotStartBadTransmission() {
    // creating with a MockAppendable with a char limit of 3, so it will throw an
    // IOException when newController is trying to handle the IllegalArgumentException
    // from the model
    SimpleFreecellController newController = new SimpleFreecellController(newModel,
            new StringReader(""),
            new MockAppendable(3));
    // numCascades can't be 3 under game rules, but only the model knows that....
    newController.playGame(newModel.getDeck(), 3, 3, false);
  }

  // the same code in the try block as above, just checking that we got the right
  // exception...
  @Test
  public void testCouldntStartMessage() {
    try {
      SimpleFreecellController newController = new SimpleFreecellController(newModel,
              new StringReader(""),
              new MockAppendable(3));
      // numCascades can't be 3 under game rules, but only the model knows that....
      newController.playGame(newModel.getDeck(), 3, 3, false);
    } catch (IllegalStateException ise) {
      assertEquals(ise.getMessage(), "attempted to render \"could "
              + "not start\" message but couldn't");
    }
  }

  // with legal arguments that don't cause model to throw an exception with startGame,
  // testing that illegalStateException will be thrown in the opening transmission
  // of board and message to the appendable
  @Test(expected = IllegalStateException.class)
  public void testFailTransmitOpeningMessage() {
    SimpleFreecellController newController = new SimpleFreecellController(newModel,
            new StringReader(""),
            new MockAppendable(3));
    // legal arguments here....
    newController.playGame(newModel.getDeck(), 5, 3, false);
  }

  // testing that the opening message transmission failure is at
  // the right exception. Again, the code in the try block is the
  // same as the test for the existence of this exception.
  @Test
  public void testTransmitOpeningMessage() {
    try {
      SimpleFreecellController newController = new SimpleFreecellController(newModel,
              new StringReader(""),
              new MockAppendable(3));
      // legal arguments here....
      newController.playGame(newModel.getDeck(), 5, 3, false);
    } catch (IllegalStateException ise) {
      assertEquals(ise.getMessage(), "attempted to render board "
              + "and opening message but couldn't");
    }
  }

  /*
    Testing all IOExceptions originally thrown from moveHandler
   */

  // when readable doesn't have any input...
  @Test(expected = IllegalStateException.class)
  public void testNoMoreReadable() {
    // this time controller is given with a legal appendable to transmit to,
    // but is given a StringReader with no input to give to moveHandler's while loop
    SimpleFreecellController newController = new SimpleFreecellController(newModel,
            new StringReader(""),
            new StringBuilder());
    // legal arguments here....
    newController.playGame(newModel.getDeck(), 5, 3, false);
  }

  // testing the IllegalStateException has the right message: note this is the only
  // IllegalStateException uniquely thrown in moveHandler, the following tests test
  // that moveHandler's IOException causes an IllegalStateException in playGame...
  @Test
  public void testNoMoreReadableMessage() {
    try {
      SimpleFreecellController newController = new SimpleFreecellController(newModel,
              new StringReader(""),
              new StringBuilder());
      newController.playGame(newModel.getDeck(), 5, 3, false);
    } catch (IllegalStateException ise) {
      assertEquals(ise.getMessage(), "ran out of input");
    }
  }

  // testing a failure to render quit message
  @Test(expected = IllegalStateException.class)
  public void testFailToTransmitQuit() {
    // MockAppendable will fail to transmit when attempting to transmit quit message
    SimpleFreecellController newController = new SimpleFreecellController(newModel,
            new StringReader("Q f"),
            new MockAppendable(3));
    // legal arguments here....
    newController.playGame(newModel.getDeck(), 5, 3, false);
  }

  // testing a failure to render "Try again: input valid ..." message
  @Test(expected = IllegalStateException.class)
  public void testFailTransmitInvalidIn() {
    SimpleFreecellController newController = new SimpleFreecellController(newModel,
            new StringReader("ggs"),
            new MockAppendable(3));
    // legal arguments here....
    newController.playGame(newModel.getDeck(), 5, 3, false);
  }

  // testing failure to transmit game over message
  @Test(expected = IllegalStateException.class)
  public void testFailTransmitGameOver() {
    // winGameString() will provide the right input to win the game
    SimpleFreecellController newController = new SimpleFreecellController(easyModel,
            new StringReader(winGameString()),
            new StringBuilder());
    newController.playGame(easiestDeckToWin(), 4, 1, false);
  }

  // construct a list of cards from 13 to 1 in each suit, which can be easily winnable
  // if you don't shuffle
  private static List<ICard> easiestDeckToWin() {
    ArrayList<ICard> toBuild = new ArrayList<>();
    for (int i = 13; i > 0; i--) {
      for (CardType c : CardType.values()) {
        toBuild.add(new Card(i, c));
      }
    }
    return toBuild;
  }

  // method to give the input to win the easiest game to win in freecell history
  /*
    For reference, easiestDeckToWin() is started in init() to look like this:
    C1: K♣, Q♣, J♣, 10♣, 9♣, 8♣, 7♣, 6♣, 5♣, 4♣, 3♣, 2♣, A♣
    C2: K♦, Q♦, J♦, 10♦, 9♦, 8♦, 7♦, 6♦, 5♦, 4♦, 3♦, 2♦, A♦
    C3: K♥, Q♥, J♥, 10♥, 9♥, 8♥, 7♥, 6♥, 5♥, 4♥, 3♥, 2♥, A♥
    C4: K♠, Q♠, J♠, 10♠, 9♠, 8♠, 7♠, 6♠, 5♠, 4♠, 3♠, 2♠, A♠
   */
  private static String winGameString() {
    String result = "";
    // iterate through each Cascade pile, moving each card at a time
    for (int i = 1; i <= 4; i++) {
      for (int j = 13; j > 0; j--) {
        result += "C" + i + " " + j + " F" + i;
      }
    }
    return result;
  }

  /**
   * Subclass of abstract test class for SimpleFreecellModel.
   */
  public static class TestSimpleFreecellModel extends AbstractControllerExceptions {

    @Override
    protected FreecellModel<ICard> makeNewModel() {
      return new SimpleFreecellModel(new Random(700));
    }

    @Override
    protected FreecellModel<ICard> makeEasyModel() {
      return new SimpleFreecellModel();
    }
  }

  /**
   * Subclass of abstract test class for MultiFreecellModel.
   */
  public static class TestMultiFreecellModel extends AbstractControllerExceptions {

    @Override
    protected FreecellModel<ICard> makeNewModel() {
      return new MultiFreecellModel(new Random(700));
    }

    @Override
    protected FreecellModel<ICard> makeEasyModel() {
      return new MultiFreecellModel();
    }
  }
}
