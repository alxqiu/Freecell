import cs3500.freecell.model.hw02.ICard;
import cs3500.freecell.model.hw02.Pile;
import cs3500.freecell.model.hw02.Card;
import cs3500.freecell.model.hw02.CardType;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Random;
import org.junit.Test;
import org.junit.Before;
import static org.junit.Assert.assertEquals;

/**
 * Test class for Pile methods.
 */
public class PileTests {
  private Pile sorted;
  private Pile unsorted;
  private Pile empty;

  @Before
  public void init() {
    ArrayList<Card> sortedToAdd = new ArrayList<Card>();
    for (int i = 0; i < 10; i++) {
      sortedToAdd.add(new Card(i + 1, CardType.DIAMOND));
    }
    sorted = pileFromList(sortedToAdd);

    ArrayList<Card> unsortedToAdd = new ArrayList<Card>();
    for (int i = 0; i < 9; i++) {
      unsortedToAdd.add(new Card(i + 4, CardType.SPADE));
    }
    Collections.shuffle(unsortedToAdd, new Random(147));

    // value of unsorted: 6♠, 8♠, 9♠, 7♠, Q♠, 4♠, 5♠, 10♠, J♠
    unsorted = pileFromList(unsortedToAdd);

    empty = new Pile();
  }

  // helper that builds a pile from a list of Cards
  private static Pile pileFromList(ArrayList<Card> from) {
    Pile result = new Pile();
    for (Card c : from) {
      result.addCard(c);
    }
    return result;
  }

  // testing addCard
  @Test
  public void addCardTest() {
    // check number of cards before and after....
    assertEquals(sorted.cardCount(), 10);
    sorted.addCard(new Card(8, CardType.SPADE));
    assertEquals(sorted.cardCount(), 11);
    ICard lastOfSorted = sorted.getCard(10);
    assertEquals(lastOfSorted, new Card(8, CardType.SPADE));


    assertEquals(empty.cardCount(), 0);
    empty.addCard(new Card(2, CardType.HEART));
    assertEquals(empty.cardCount(), 1);
    empty.addCard(new Card(11, CardType.DIAMOND));
    assertEquals(empty.cardCount(), 2);
    assertEquals(empty.getCard(0), new Card(2, CardType.HEART));
    assertEquals(empty.getCard(1), new Card(11, CardType.DIAMOND));
  }

  // testing addCard exceptions
  @Test(expected = IllegalArgumentException.class)
  public void noAddNull() {
    empty.addCard(null);
  }

  // testing removeAt()
  @Test
  public void removeAtTest() {
    // check number of cards before and after....
    assertEquals(sorted.cardCount(), 10);
    sorted.removeAt(9);
    assertEquals(sorted.cardCount(), 9);
    ICard lastOfSorted = sorted.getCard(8);
    assertEquals(lastOfSorted, new Card(9, CardType.DIAMOND));

    // check that nothing happens when removing from empty
    assertEquals(empty.cardCount(), 0);
    empty.removeAt(0);
    assertEquals(empty.cardCount(), 0);

    // new card added and removed
    empty.addCard(new Card(1, CardType.CLUB));
    assertEquals(empty.cardCount(), 1);
    empty.removeAt(0);
    assertEquals(empty.cardCount(), 0);
  }


  // testing getCard
  @Test
  public void getCardTest() {
    assertEquals(sorted.getCard(0), new Card(1, CardType.DIAMOND));
    assertEquals(sorted.getCard(9), new Card(10, CardType.DIAMOND));
    assertEquals(unsorted.getCard(5), new Card(4, CardType.SPADE));
  }

  // testing getCard exceptions
  @Test(expected = IllegalArgumentException.class)
  public void noGetEmpty() {
    empty.getCard(0);
  }

  // reaching too high for an index
  @Test(expected = IllegalArgumentException.class)
  public void cardIdxTooHigh() {
    unsorted.getCard(15);
  }

  // reaching too low for an index
  @Test(expected = IllegalArgumentException.class)
  public void cardIdxTooLow() {
    unsorted.getCard(-2);
  }

  // testing cardCount
  @Test
  public void cardCountTest() {
    assertEquals(empty.cardCount(), 0);
    assertEquals(sorted.cardCount(), 10);
    assertEquals(unsorted.cardCount(), 9);

    // testing to see how cardCount()'s return changes
    // as we remove or add cards has already been done
    // as I have used cardCount() in the test methods for
    // removeTop() and addCard().
  }
}
