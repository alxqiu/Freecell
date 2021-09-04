import cs3500.freecell.model.hw02.Card;
import cs3500.freecell.model.hw02.CardType;
import org.junit.Before;
import org.junit.Test;
import static org.junit.Assert.assertEquals;

/**
 * Test class for Card methods.
 */
public class CardTests {
  private Card aceOfClubs;
  private Card tenOfDiamonds;
  private Card sevenOfHearts;
  private Card queenOfSpades;

  // initializing four cards to test with
  @Before
  public void init() {
    aceOfClubs = new Card(1, CardType.CLUB);
    tenOfDiamonds = new Card(10, CardType.DIAMOND);
    sevenOfHearts = new Card(7, CardType.HEART);
    queenOfSpades = new Card(12, CardType.SPADE);
  }

  // exception from giving null to constructor
  @Test(expected = IllegalArgumentException.class)
  public void constructWithNull() {
    new Card(3, null);
  }

  // exception from giving a value too low
  @Test(expected = IllegalArgumentException.class)
  public void constructWithTooLow() {
    new Card(0, CardType.DIAMOND);
  }

  // exception from giving value too high
  @Test(expected = IllegalArgumentException.class)
  public void constructWithTooHigh() {
    new Card(20, CardType.SPADE);
  }

  // testing observers

  // testing getValue()
  @Test
  public void getValueTest() {
    assertEquals(aceOfClubs.getValue(), 1);
    assertEquals(tenOfDiamonds.getValue(), 10);
    assertEquals(sevenOfHearts.getValue(), 7);
    assertEquals(queenOfSpades.getValue(), 12);
  }

  // testing getSuit()
  @Test
  public void getSuitTest() {
    assertEquals(aceOfClubs.getSuit(), CardType.CLUB);
    assertEquals(tenOfDiamonds.getSuit(), CardType.DIAMOND);
    assertEquals(sevenOfHearts.getSuit(), CardType.HEART);
    assertEquals(queenOfSpades.getSuit(), CardType.SPADE);
  }

  // testing sameColor()
  @Test
  public void sameColorTest() {
    assertEquals(aceOfClubs.sameColor(sevenOfHearts), false);
    assertEquals(tenOfDiamonds.sameColor(sevenOfHearts), true);
    assertEquals(sevenOfHearts.sameColor(queenOfSpades), false);
    assertEquals(queenOfSpades.sameColor(aceOfClubs), true);
  }

  // exception from sameColor
  @Test(expected = IllegalArgumentException.class)
  public void colorOfNull() {
    aceOfClubs.sameColor(null);
  }

  // remember to test stuff overridden from object...

  // testing equals() override
  @Test
  public void equalsTest() {
    // constrast .equals() with == to show logical equality...
    assertEquals(aceOfClubs == new Card(1, CardType.CLUB), false);
    assertEquals(aceOfClubs.equals(new Card(1, CardType.CLUB)), true);
    assertEquals(aceOfClubs.equals(new Card(2, CardType.CLUB)), false);
    assertEquals(queenOfSpades.equals(new Card(12, CardType.SPADE)), true);
    assertEquals(tenOfDiamonds.equals(new Card(10, CardType.SPADE)), false);

    // check for functionality other objects
    // NOTE: I did test .equals() with null, and it works fine, but the style checker
    // gets mad as I am "comparing null with .equals() instead of ==".
    assertEquals(tenOfDiamonds.equals("tenOfDiamonds"), false);
  }

  // testing hashCode() override
  @Test
  public void hashCodeTest() {
    // test that hashCode() returns the same thing as another Card if the two have logical equality
    assertEquals(aceOfClubs.hashCode(), 992);
    assertEquals(aceOfClubs.hashCode(), new Card(1, CardType.CLUB).hashCode());
    assertEquals(queenOfSpades.hashCode(), 1336);
    assertEquals(new Card(11, CardType.SPADE).hashCode(), 1305);
    assertEquals(queenOfSpades.hashCode() != new Card(11, CardType.SPADE).hashCode(), true);
  }

  // testing toString() override
  @Test
  public void toStringTest() {
    assertEquals(aceOfClubs.toString(), "A♣");
    assertEquals(tenOfDiamonds.toString(), "10♦");
    assertEquals(sevenOfHearts.toString(), "7♥");
    assertEquals(queenOfSpades.toString(), "Q♠");
  }
}
