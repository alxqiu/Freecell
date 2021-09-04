import cs3500.freecell.model.FreecellModel;
import cs3500.freecell.model.hw02.ICard;
import cs3500.freecell.model.hw02.SimpleFreecellModel;
import cs3500.freecell.model.hw04.MultiFreecellModel;
import org.junit.Before;
import org.junit.Test;

/**
 * Abstract version of TestGetterExceptions applied to both SimpleFreecellModel and
 * MultiFreecellModel. Checking to see if MultiFreecellModel will throw the same
 * exceptions as SimpleFreecellModel will.
 */
public abstract class AbstractModelGetterExceptionsTest {
  private FreecellModel<ICard> freecellModel1;
  private FreecellModel<ICard> unstartedGame;

  protected abstract FreecellModel<ICard> makeFreecellModel1();

  protected abstract FreecellModel<ICard> makeUnstartedGame();

  @Before
  public void init() {
    freecellModel1 = makeFreecellModel1();
    unstartedGame = makeUnstartedGame();
  }

  // testing IllegalStateExceptions with unstarted game thrown from getters:
  // NOTE: the getNumCascadePiles and getNumOpenPiles don't throw exceptions and will return -1
  // when called on unstarted game so they are tested in TestGettersWork.

  @Test(expected = IllegalStateException.class)
  public void unstartedGetFoundationCards() {
    unstartedGame.getNumCardsInFoundationPile(2);
  }

  @Test(expected = IllegalStateException.class)
  public void unstartedGetCascadeCards() {
    unstartedGame.getNumCardsInCascadePile(3);
  }

  @Test(expected = IllegalStateException.class)
  public void unstartedGetOpenCards() {
    unstartedGame.getNumCardsInOpenPile(3);
  }

  @Test(expected = IllegalStateException.class)
  public void unstartedGetFoundationCardsAt() {
    unstartedGame.getFoundationCardAt(3, 3);
  }

  @Test(expected = IllegalStateException.class)
  public void unstartedGetCascadeCardsAt() {
    unstartedGame.getCascadeCardAt(3, 3);
  }

  @Test(expected = IllegalStateException.class)
  public void unstartedGetOpenCardsAt() {
    unstartedGame.getOpenCardAt(3);
  }

  /*
    Testing illegalArgumentException for getters when giving invalid indices
    for getNumCards methods. These are the getNumCardsinXPile(...) methods.
   */
  @Test(expected = IllegalArgumentException.class)
  public void getNumCardsAtOpenTooHigh() {
    freecellModel1.getNumCardsInOpenPile(2);
  }

  @Test(expected = IllegalArgumentException.class)
  public void getNumCardsAtOpenTooLow() {
    freecellModel1.getNumCardsInOpenPile(-1);
  }

  @Test(expected = IllegalArgumentException.class)
  public void getNumCardsAtCascadeTooHigh() {
    freecellModel1.getNumCardsInCascadePile(5);
  }

  @Test(expected = IllegalArgumentException.class)
  public void getNumCardsAtCascadeTooLow() {
    freecellModel1.getNumCardsInCascadePile(-1);
  }

  @Test(expected = IllegalArgumentException.class)
  public void getNumCardsAtFoundationTooHigh() {
    freecellModel1.getNumCardsInFoundationPile(4);
  }

  @Test(expected = IllegalArgumentException.class)
  public void getNumCardsAtFoundationTooLow() {
    freecellModel1.getNumCardsInFoundationPile(-1);
  }

  /*
   testing IllegalArgumentException for getters when trying to access out of bounds for pileIndex
   and cardIndex. These are the getXCardAt(...) methods

   */

  // Open methods: pileIndex invalid
  @Test(expected = IllegalArgumentException.class)
  public void pileIdxTooLargeO() {
    freecellModel1.getOpenCardAt(3);
  }

  @Test(expected = IllegalArgumentException.class)
  public void pileIdxTooSmallO() {
    freecellModel1.getOpenCardAt(-1);
  }

  // note: attempting to retrieve from empty open space returns null, DOES NOT
  // throw exception!

  // Cascade methods: pileIndex invalid
  @Test(expected = IllegalArgumentException.class)
  public void pileIdxTooLargeC() {
    freecellModel1.getCascadeCardAt(5, 0);
  }

  @Test(expected = IllegalArgumentException.class)
  public void pileIdxTooSmallC() {
    freecellModel1.getCascadeCardAt(-2, 0);
  }

  // Cascade methods: cardIndex invalid
  // C4 has 10 cards, so a cardIndex of 10 is just over the lowest valid cardIndex of 9
  @Test(expected = IllegalArgumentException.class)
  public void cardIdxTooLargeC() {
    freecellModel1.getCascadeCardAt(3, 10);
  }

  @Test(expected = IllegalArgumentException.class)
  public void cardIdxTooSmallC() {
    freecellModel1.getCascadeCardAt(1, -1);
  }

  // Foundation methods: pileIndex invalid
  @Test(expected = IllegalArgumentException.class)
  public void pileIdxTooLargeF() {
    freecellModel1.getFoundationCardAt(4, 0);
  }

  @Test(expected = IllegalArgumentException.class)
  public void pileIdxTooSmallF() {
    freecellModel1.getFoundationCardAt(-1, 0);
  }

  // Foundation methods: cardIndex invalid
  @Test(expected = IllegalArgumentException.class)
  public void cardIdxTooLargeF() {
    freecellModel1.getFoundationCardAt(2, 3);
  }

  @Test(expected = IllegalArgumentException.class)
  public void cardIdxTooSmallF() {
    freecellModel1.getFoundationCardAt(0, -3);
  }

  /**
   * Subclass of abstract test class for SimpleFreecellModel.
   */
  public static class TestSimpleFreecellModel extends AbstractModelGetterExceptionsTest {

    @Override
    protected FreecellModel<ICard> makeFreecellModel1() {
      FreecellModel<ICard> freecellModel1 = new SimpleFreecellModel();
      freecellModel1.startGame(freecellModel1.getDeck(),5, 2, false);
      return freecellModel1;
    }

    @Override
    protected FreecellModel<ICard> makeUnstartedGame() {
      return new SimpleFreecellModel();
    }
  }

  /**
   * Subclass of abstract test class for MultiFreecellModel.
   */
  public static class TestMultiFreecellModel extends AbstractModelGetterExceptionsTest {

    @Override
    protected FreecellModel<ICard> makeFreecellModel1() {
      FreecellModel<ICard> freecellModel1 = new MultiFreecellModel();
      freecellModel1.startGame(freecellModel1.getDeck(),5, 2, false);
      return freecellModel1;
    }

    @Override
    protected FreecellModel<ICard> makeUnstartedGame() {
      return new MultiFreecellModel();
    }
  }
}
