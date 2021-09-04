import cs3500.freecell.model.FreecellModel;
import cs3500.freecell.model.PileType;
import cs3500.freecell.model.hw02.Card;
import cs3500.freecell.model.hw02.CardType;
import cs3500.freecell.model.hw02.ICard;
import cs3500.freecell.model.hw02.SimpleFreecellModel;
import cs3500.freecell.model.hw04.MultiFreecellModel;
import org.junit.Before;
import org.junit.Test;

import java.util.Random;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;

/**
 * Abstract class that tests the getters of both implementations of FreecellModel,
 * inherited from FreecellModelState. Abstract version of TestGettersWork, that ensures
 * that MultiFreecellModel can do everything that SimpleFreecellModel can.
 */
public abstract class AbstractModelGettersTest {
  private FreecellModel<ICard> freecellModel1;
  private FreecellModel<ICard> unstartedGame;
  private FreecellModel<ICard> gameWithShuffle;

  protected abstract FreecellModel<ICard> constructFreecellModel1();

  protected abstract FreecellModel<ICard> constructUnstarted();

  protected abstract FreecellModel<ICard> constructGameWithShuffle();

  @Before
  public void init() {
    freecellModel1 = constructFreecellModel1();
    unstartedGame = constructUnstarted();
    gameWithShuffle = constructGameWithShuffle();
  }

  // testing that getters for Cascade and Open piles return -1 when
  // the game hasn't started
  @Test
  public void testUnstartedMethods() {
    assertEquals(unstartedGame.getNumCascadePiles(), -1);
    assertEquals(unstartedGame.getNumOpenPiles(), -1);
  }

  // attempting to get a Card from an empty Open slot, will return null
  @Test
  public void cardNotHereO() {
    assertNull(freecellModel1.getOpenCardAt(0));
  }

  // testing all Cascade/Open/Foundation getters work normally
  @Test
  public void testNumPiles() {
    assertEquals(freecellModel1.getNumCascadePiles(), 5);
    assertEquals(freecellModel1.getNumOpenPiles(), 2);
    assertEquals(gameWithShuffle.getNumCascadePiles(), 4);
    assertEquals(gameWithShuffle.getNumOpenPiles(), 3);
  }

  // testing that all getNumCardsinXPile methods work correctly with moves
  @Test
  public void testGetNumCards() {
    // should have 11 cards C1, as 52 distributed across 5 cascade piles means
    // the first two get 11 while C3 through C5 get 10
    assertEquals(freecellModel1.getNumCardsInCascadePile(0), 11);
    assertEquals(freecellModel1.getNumCardsInCascadePile(2), 10);

    // before moving J♣ from C2 to O1 pile:
    assertEquals(gameWithShuffle.getNumCardsInCascadePile(1), 13);
    assertEquals(gameWithShuffle.getNumCardsInOpenPile(0), 0);
    gameWithShuffle.move(PileType.CASCADE, 1, 12, PileType.OPEN, 0);
    assertEquals(gameWithShuffle.getNumCardsInCascadePile(1), 12);
    assertEquals(gameWithShuffle.getNumCardsInOpenPile(0), 1);

    // moving 9♦ from C2 to O2, then moving A♠ from C2 to F1:
    assertEquals(gameWithShuffle.getNumCardsInFoundationPile(0), 0);
    gameWithShuffle.move(PileType.CASCADE, 1, 11, PileType.OPEN, 1);
    gameWithShuffle.move(PileType.CASCADE, 1, 10, PileType.FOUNDATION, 0);
    assertEquals(gameWithShuffle.getNumCardsInFoundationPile(0), 1);
  }

  /*
  for reference this is what gameWithShuffle is after init()
  F1:
  F2:
  F3:
  F4:
  O1:
  O2:
  O3:
  C1: 7♣, 10♦, 4♠, 3♦, 5♥, Q♠, 10♣, 2♣, 6♥, 6♣, 3♣, 9♥, 5♣
  C2: 7♠, 2♦, 6♠, J♥, 7♦, A♥, 8♣, K♦, K♠, 5♦, A♠, 9♦, J♣
  C3: A♦, 9♣, 4♦, 5♠, A♣, 8♥, J♠, Q♣, 3♠, 3♥, 8♠, 7♥, 8♦
  C4: K♣, 4♣, K♥, 4♥, J♦, 2♥, 2♠, 10♠, 9♠, Q♥, 10♥, Q♦, 6♦

   */
  // testing all Card getters in each pile works correctly
  @Test
  public void testGetCardAt() {
    // moving J♣ from C2 to O1 pile, moving 9♦ from C2 to O2, then moving A♠ from C2 to F1
    gameWithShuffle.move(PileType.CASCADE, 1, 12, PileType.OPEN, 0);
    gameWithShuffle.move(PileType.CASCADE, 1, 11, PileType.OPEN, 1);
    gameWithShuffle.move(PileType.CASCADE, 1, 10, PileType.FOUNDATION, 0);

    assertEquals(gameWithShuffle.getCascadeCardAt(3, 12), new Card(6, CardType.DIAMOND));
    assertEquals(gameWithShuffle.getCascadeCardAt(1, 9), new Card(5, CardType.DIAMOND));
    assertEquals(gameWithShuffle.getOpenCardAt(0), new Card(11, CardType.CLUB));
    assertEquals(gameWithShuffle.getOpenCardAt(1), new Card(9, CardType.DIAMOND));
    assertEquals(gameWithShuffle.getFoundationCardAt(0, 0), new Card(1, CardType.SPADE));
  }
  
  /**
   * Subclass of abstract test class for SimpleFreecellModel.
   */
  public static class TestSimpleFreecellModel extends AbstractModelGettersTest {

    @Override
    protected FreecellModel<ICard> constructFreecellModel1() {
      FreecellModel<ICard> freecellModel1 = new SimpleFreecellModel();
      freecellModel1.startGame(freecellModel1.getDeck(),5, 2, false);
      return freecellModel1;
    }

    @Override
    protected FreecellModel<ICard> constructUnstarted() {
      return new SimpleFreecellModel();
    }

    @Override
    protected FreecellModel<ICard> constructGameWithShuffle() {
      FreecellModel<ICard> gameWithShuffle = new SimpleFreecellModel(new Random(3));
      gameWithShuffle.startGame(gameWithShuffle.getDeck(), 4, 3, true);
      return gameWithShuffle;
    }
  }

  /**
   * Subclass of abstract test class for MultiFreecellModel.
   */
  public static class TestMultiFreecellModel extends AbstractModelGettersTest {

    @Override
    protected FreecellModel<ICard> constructFreecellModel1() {
      FreecellModel<ICard> freecellModel1 = new MultiFreecellModel();
      freecellModel1.startGame(freecellModel1.getDeck(),5, 2, false);
      return freecellModel1;
    }

    @Override
    protected FreecellModel<ICard> constructUnstarted() {
      return new MultiFreecellModel();
    }

    @Override
    protected FreecellModel<ICard> constructGameWithShuffle() {
      FreecellModel<ICard> gameWithShuffle = new MultiFreecellModel(new Random(3));
      gameWithShuffle.startGame(gameWithShuffle.getDeck(), 4, 3, true);
      return gameWithShuffle;
    }
  }
}

