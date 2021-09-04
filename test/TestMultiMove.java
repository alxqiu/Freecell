import cs3500.freecell.model.PileType;
import cs3500.freecell.model.hw02.Card;
import cs3500.freecell.model.hw02.CardType;
import cs3500.freecell.model.hw04.MultiFreecellModel;

import org.junit.Before;
import org.junit.Test;

import java.util.Random;
import static org.junit.Assert.assertEquals;

/**
 * Testing multi-move functionality unique to MultiFreecellModel.
 * Also tests constructor exception.
 */
public class TestMultiMove {
  MultiFreecellModel newMulti;
  MultiFreecellModel freshSeededMulti;

  @Before
  public void init() {
    newMulti = new MultiFreecellModel(new Random(71));
    newMulti.startGame(newMulti.getDeck(), 6, 4, true);
    freshSeededMulti = new MultiFreecellModel(new Random(2719));
  }

  // constructor exception for MultiFreecellModel is already tested in AbstractModelExceptionsTest

  // test moving from an empty pile....
  @Test(expected = IllegalArgumentException.class)
  public void testMoveFromEmptyCascade() {
    newMulti.startGame(newMulti.getDeck(), 52, 2, true);

    // 52 cascade piles, so if we move from one cascade pile, that cascade pile will be empty...
    newMulti.move(PileType.CASCADE, 0, 0, PileType.OPEN, 0);
    assertEquals(newMulti.getOpenCardAt(0), new Card(5, CardType.CLUB));
    assertEquals(newMulti.getNumCardsInCascadePile(0), 0);
    newMulti.move(PileType.CASCADE, 0, 0, PileType.OPEN, 1);
  }

  // test moving when not started yet
  @Test(expected = IllegalStateException.class)
  public void testMultiMoveNotStarted() {
    freshSeededMulti.move(PileType.CASCADE, 3, 12, PileType.OPEN, 0);
  }

  // test illegal multi moves: not enough free slots
  @Test(expected = IllegalArgumentException.class)
  public void testInsufficientOpens() {
    freshSeededMulti.startGame(freshSeededMulti.getDeck(), 7, 1, true);
    freshSeededMulti.move(PileType.CASCADE, 6, 6, PileType.OPEN, 1);
    freshSeededMulti.move(PileType.CASCADE, 5, 5, PileType.CASCADE, 3);
    // Then attempt to move that build to C7 as the top card should be 6♠: attempting a
    // three card move when theres only one open space left...
    freshSeededMulti.move(PileType.CASCADE, 3, 6, PileType.CASCADE, 6);
  }

  // test illegal multi moves: in descending order but not alternating colors
  @Test(expected = IllegalArgumentException.class)
  public void testDescendingButNotAltColor() {
    freshSeededMulti.startGame(freshSeededMulti.getDeck(), 8, 6, true);
    // move 8♦ from C5 to O1
    freshSeededMulti.move(PileType.CASCADE, 4, 5, PileType.OPEN, 0);
    assertEquals(freshSeededMulti.getOpenCardAt(0), new Card(8, CardType.DIAMOND));
    // move  5♦ from C6 to O2
    freshSeededMulti.move(PileType.CASCADE, 5, 5, PileType.OPEN, 1);
    assertEquals(freshSeededMulti.getOpenCardAt(1), new Card(5, CardType.DIAMOND));
    // attempt to move 9♠, 8 to C6 which has 10♠ on top...
    freshSeededMulti.move(PileType.CASCADE, 4, 3, PileType.CASCADE, 5);
  }

  // test illegal multi moves: in alternating color but not descending order
  @Test(expected = IllegalArgumentException.class)
  public void testAltColorButNotDescending() {
    freshSeededMulti.startGame(freshSeededMulti.getDeck(), 8, 6, true);
    // C4: 6♥, Q♥, Q♠, K♦, 8♠, J♦, 3♠, last 6 cards are alternating colors, but not descending order
    // moved 4♦ from C3 to O1 so K♠ is on top of C3
    freshSeededMulti.move(PileType.CASCADE, 2, 6, PileType.OPEN, 0);
    assertEquals(freshSeededMulti.getCascadeCardAt(2, 5), new Card(13, CardType.SPADE));
    freshSeededMulti.move(PileType.CASCADE, 3, 4, PileType.CASCADE, 2);
  }

  // illegal move: valid build but to open pile
  @Test(expected = IllegalArgumentException.class)
  public void testMoveBuildToOpen() {
    freshSeededMulti.startGame(freshSeededMulti.getDeck(), 7, 4, true);
    freshSeededMulti.move(PileType.CASCADE, 6, 6, PileType.OPEN, 1);
    freshSeededMulti.move(PileType.CASCADE, 5, 5, PileType.CASCADE, 3);
    // Then attempt to move that build to C7 as the top card should be 6♠:
    // attempting a three card move when theres only one open space left...
    freshSeededMulti.move(PileType.CASCADE, 3, 6, PileType.OPEN, 3);
  }


  // test multi-move ability for two cards
  @Test
  public void noOpenCascades() {
    // move 8♣, 6♠ from C6 to O3 and O4
    newMulti.move(PileType.CASCADE, 5, 7, PileType.OPEN, 2);
    newMulti.move(PileType.CASCADE, 5, 6, PileType.OPEN, 3);
    // attempt to move Q♠, J♦ to from C5 to C6
    assertEquals(newMulti.getNumCardsInCascadePile(4), 8);
    newMulti.move(PileType.CASCADE, 4, 6, PileType.CASCADE, 5);
    // assert that last 3 cards in C6 are K♦, Q♠, J♦
    assertEquals(newMulti.getCascadeCardAt(5, 5), new Card(13, CardType.DIAMOND));
    assertEquals(newMulti.getCascadeCardAt(5, 6), new Card(12, CardType.SPADE));
    assertEquals(newMulti.getCascadeCardAt(5, 7), new Card(11, CardType.DIAMOND));
    // assert that C5 now has two less cards...
    assertEquals(newMulti.getNumCardsInCascadePile(4), 6);
  }

  // test move 3 cards with one of each type of free pile
  @Test
  public void moveThreeCardsOneOfEach() {
    freshSeededMulti.startGame(freshSeededMulti.getDeck(), 7, 9, true);
    // move everything from C5 and 6♦ from C7 to O2 through O9 so that C5 and O1 are free piles,
    // and move 4♠, 3♥ from C6 to C4, so that a build is formed at the end of C4 with 5♦, 4♠, 3♥.
    for (int i = 6; i >= 0; i--) {
      freshSeededMulti.move(PileType.CASCADE, 4, i, PileType.OPEN, i + 2);
    }
    assertEquals(0, freshSeededMulti.getNumCardsInCascadePile(4));
    freshSeededMulti.move(PileType.CASCADE, 6, 6, PileType.OPEN, 1);
    freshSeededMulti.move(PileType.CASCADE, 5, 5, PileType.CASCADE, 3);

    // Then attempt to move that build to C7 as the top card should be 6♠
    freshSeededMulti.move(PileType.CASCADE, 3, 6, PileType.CASCADE, 6);

    // last three cards of C7 should be 6♠, 5♦, 4♠, 3♥
    assertEquals(new Card(6, CardType.SPADE), freshSeededMulti.getCascadeCardAt(6, 5));
    assertEquals(new Card(5, CardType.DIAMOND), freshSeededMulti.getCascadeCardAt(6, 6));
    assertEquals(new Card(4, CardType.SPADE), freshSeededMulti.getCascadeCardAt(6, 7));
    assertEquals(new Card(3, CardType.HEART), freshSeededMulti.getCascadeCardAt(6, 8));
  }

  // testing that one of the first if conditions in move() allows the super.move() call to be
  // made before calling .getNumbCardsInCascadePile(pileNumber). We pass in a pileNumber
  // that will throw an exception if getNumCardsInCascadePile() is called with it, and want to see
  // if the condition !(source == PileType.CASCADE && destination == PileType.CASCADE) will pass
  // before that happens. As of 6/3 8:17 PT so far it works!
  @Test
  public void testSuperCallOutOfBoundsCascadeNumber() {
    // start it with 4 cascade piles, so a pileNumber of 5 throw exception if
    // .getNumbCardsInCascadePile(pileNumber) is called.
    newMulti.startGame(newMulti.getDeck(), 4, 6, true);
    // move J♠ from C1 to O1
    newMulti.move(PileType.CASCADE, 0, 12, PileType.OPEN, 0);
    // move 6♥ from C1 to O6
    newMulti.move(PileType.CASCADE, 0, 11, PileType.OPEN, 5);

    // try to access with pileNumber 5, with source type being Open
    // attempt to move 6♥ from O6 to C4
    newMulti.move(PileType.OPEN, 5, 0, PileType.CASCADE, 3);
    assertEquals(newMulti.getCascadeCardAt(3, 12), new Card(7, CardType.SPADE));
    assertEquals(newMulti.getCascadeCardAt(3, 13), new Card(6, CardType.HEART));
  }
}
