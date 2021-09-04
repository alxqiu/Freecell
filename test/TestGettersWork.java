import cs3500.freecell.model.PileType;
import cs3500.freecell.model.hw02.Card;
import cs3500.freecell.model.hw02.CardType;
import cs3500.freecell.model.hw02.SimpleFreecellModel;
import org.junit.Before;
import org.junit.Test;

import java.util.Random;

import static org.junit.Assert.assertEquals;

/**
 *  Test class for the getters in SimpleFreecell models, specifically the methods from
 *  FreecellModelState. Exceptions for these methods are tested in TestGetterExceptions.
 */
public class TestGettersWork {
  private SimpleFreecellModel simpleFreecs1;
  private SimpleFreecellModel unstartedGame;
  private SimpleFreecellModel gameWithShuffle;

  // initializing fields
  @Before
  public void init() {

    simpleFreecs1 = new SimpleFreecellModel();
    simpleFreecs1.startGame(simpleFreecs1.getDeck(),5, 2, false);
    // try starting with a shuffled deck....
    unstartedGame = new SimpleFreecellModel();

    gameWithShuffle = new SimpleFreecellModel(new Random(3));
    gameWithShuffle.startGame(gameWithShuffle.getDeck(), 4, 3, true);
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
    assertEquals(simpleFreecs1.getOpenCardAt(0), null);
  }

  // testing all Cascade/Open/Foundation getters work normally
  @Test
  public void testNumPiles() {
    assertEquals(simpleFreecs1.getNumCascadePiles(), 5);
    assertEquals(simpleFreecs1.getNumOpenPiles(), 2);
    assertEquals(gameWithShuffle.getNumCascadePiles(), 4);
    assertEquals(gameWithShuffle.getNumOpenPiles(), 3);
  }

  // testing that all getNumCardsinXPile methods work correctly with moves
  @Test
  public void testGetNumCards() {
    // should have 11 cards C1, as 52 distributed across 5 cascade piles means
    // the first two get 11 while C3 through C5 get 10
    assertEquals(simpleFreecs1.getNumCardsInCascadePile(0), 11);
    assertEquals(simpleFreecs1.getNumCardsInCascadePile(2), 10);

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
}
