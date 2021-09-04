import cs3500.freecell.model.hw02.SimpleFreecellModel;
import org.junit.Before;
import org.junit.Test;

/**
 * Test class for FreecellModelState getter method exceptions, as implemented
 * by SimpleFreecellModel.
 */
public class TestGetterExceptions {
  private SimpleFreecellModel simpleFreecs1;
  private SimpleFreecellModel unstartedGame;

  @Before
  public void init() {
    simpleFreecs1 = new SimpleFreecellModel();
    simpleFreecs1.startGame(simpleFreecs1.getDeck(),5, 2, false);
    unstartedGame = new SimpleFreecellModel();
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
    simpleFreecs1.getNumCardsInOpenPile(2);
  }

  @Test(expected = IllegalArgumentException.class)
  public void getNumCardsAtOpenTooLow() {
    simpleFreecs1.getNumCardsInOpenPile(-1);
  }

  @Test(expected = IllegalArgumentException.class)
  public void getNumCardsAtCascadeTooHigh() {
    simpleFreecs1.getNumCardsInCascadePile(5);
  }

  @Test(expected = IllegalArgumentException.class)
  public void getNumCardsAtCascadeTooLow() {
    simpleFreecs1.getNumCardsInCascadePile(-1);
  }

  @Test(expected = IllegalArgumentException.class)
  public void getNumCardsAtFoundationTooHigh() {
    simpleFreecs1.getNumCardsInFoundationPile(4);
  }

  @Test(expected = IllegalArgumentException.class)
  public void getNumCardsAtFoundationTooLow() {
    simpleFreecs1.getNumCardsInFoundationPile(-1);
  }

  /*
   testing IllegalArgumentException for getters when trying to access out of bounds for pileIndex
   and cardIndex. These are the getXCardAt(...) methods

   */

  // Open methods: pileIndex invalid
  @Test(expected = IllegalArgumentException.class)
  public void pileIdxTooLargeO() {
    simpleFreecs1.getOpenCardAt(3);
  }

  @Test(expected = IllegalArgumentException.class)
  public void pileIdxTooSmallO() {
    simpleFreecs1.getOpenCardAt(-1);
  }

  // note: attempting to retrieve from empty open space returns null, DOES NOT
  // throw exception!

  // Cascade methods: pileIndex invalid
  @Test(expected = IllegalArgumentException.class)
  public void pileIdxTooLargeC() {
    simpleFreecs1.getCascadeCardAt(5, 0);
  }

  @Test(expected = IllegalArgumentException.class)
  public void pileIdxTooSmallC() {
    simpleFreecs1.getCascadeCardAt(-2, 0);
  }

  // Cascade methods: cardIndex invalid
  // C4 has 10 cards, so a cardIndex of 10 is just over the lowest valid cardIndex of 9
  @Test(expected = IllegalArgumentException.class)
  public void cardIdxTooLargeC() {
    simpleFreecs1.getCascadeCardAt(3, 10);
  }

  @Test(expected = IllegalArgumentException.class)
  public void cardIdxTooSmallC() {
    simpleFreecs1.getCascadeCardAt(1, -1);
  }

  // Foundation methods: pileIndex invalid
  @Test(expected = IllegalArgumentException.class)
  public void pileIdxTooLargeF() {
    simpleFreecs1.getFoundationCardAt(4, 0);
  }

  @Test(expected = IllegalArgumentException.class)
  public void pileIdxTooSmallF() {
    simpleFreecs1.getFoundationCardAt(-1, 0);
  }

  // Foundation methods: cardIndex invalid
  @Test(expected = IllegalArgumentException.class)
  public void cardIdxTooLargeF() {
    simpleFreecs1.getFoundationCardAt(2, 3);
  }

  @Test(expected = IllegalArgumentException.class)
  public void cardIdxTooSmallF() {
    simpleFreecs1.getFoundationCardAt(0, -3);
  }
}
