import cs3500.freecell.model.PileType;
import cs3500.freecell.model.hw02.Card;
import cs3500.freecell.model.hw02.CardType;
import cs3500.freecell.model.hw02.ICard;
import cs3500.freecell.model.hw02.SimpleFreecellModel;
import org.junit.Before;
import org.junit.Test;

import java.util.ArrayList;
import java.util.Random;

/**
 * Test class containing code that tests the throwing of exceptions for the
 * move and startGame methods from FreecellModel as implemented by SimpleFreecellModel.
 *
 * <p> Also tests constructor exception. </p>
 *
 * <p> Those two methods are the only two public methods that throw exceptions. </p>
 */
public class TestSFMExceptions {
  private SimpleFreecellModel unstartedGame;
  private SimpleFreecellModel startedGame;

  @Before
  public void init() {
    unstartedGame = new SimpleFreecellModel();
    startedGame = new SimpleFreecellModel(new Random(21));
    startedGame.startGame(startedGame.getDeck(), 6, 3, true);
  }

  // test constructor exception
  @Test(expected = IllegalArgumentException.class)
  public void createWithNull() {
    new SimpleFreecellModel(null);
  }

  /*
    testing exceptions for startGame
   */

  // given null
  @Test(expected = IllegalArgumentException.class)
  public void startWithNull() {
    unstartedGame.startGame(null, 4, 1,false);
  }

  // numCascadePiles is too low
  @Test(expected = IllegalArgumentException.class)
  public void cascadeTooLow() {
    unstartedGame.startGame(unstartedGame.getDeck(), 2, 1,false);
  }

  // numOpenPiles is too low
  @Test(expected = IllegalArgumentException.class)
  public void openTooLow() {
    unstartedGame.startGame(unstartedGame.getDeck(), 4, 0,false);
  }

  // given invalid deck: not 52 cards
  @Test(expected = IllegalArgumentException.class)
  public void notValidLength() {
    ArrayList<ICard> invalidDeck = new ArrayList<ICard>();
    invalidDeck.add(new Card(5, CardType.HEART));
    unstartedGame.startGame(invalidDeck, 4, 3,false);
  }

  // given invalid deck: 52 cards but has a single duplicate
  @Test(expected = IllegalArgumentException.class)
  public void duplicate() {
    ArrayList<ICard> invalidDeck = new ArrayList<ICard>(unstartedGame.getDeck());
    // first Card from .getDeck() is A♣, so adding the duplicate won't accidentally
    // replace that card.
    invalidDeck.remove(0);
    invalidDeck.add(new Card(5, CardType.HEART));
    unstartedGame.startGame(invalidDeck, 4, 3,false);
  }

  /*
    Testing exceptions thrown from calling move with bad arguments/state
   */
  
  // test moves before game starts
  @Test(expected = IllegalStateException.class)
  public void noPregamingDude() {
    unstartedGame.move(PileType.CASCADE, 3, 0, PileType.OPEN, 0);
  }

  // given null in either source or destination
  @Test(expected = IllegalArgumentException.class)
  public void nullSource() {
    startedGame.move(null, 3, 0, PileType.OPEN, 0);
  }

  @Test(expected = IllegalArgumentException.class)
  public void nullDestination() {
    startedGame.move(PileType.CASCADE, 3, 0, null, 0);
  }

  // given foundation as source
  @Test(expected = IllegalArgumentException.class)
  public void foundationSource() {
    // move J♥ to O1 from C1 and move A♠ to F1 from C1
    startedGame.move(PileType.CASCADE, 0, 8, PileType.OPEN, 0);
    startedGame.move(PileType.CASCADE, 0, 7, PileType.FOUNDATION, 0);

    // now attempt to move A♠ from F1 to O2
    startedGame.move(PileType.FOUNDATION, 0, 0, PileType.OPEN, 1);
  }

  /*
    Testing exceptions thrown from calling an "illegal" move per the rules of the game
    This is startedGame when started:
    F1:
    F2:
    F3:
    F4:
    O1:
    O2:
    O3:
    C1: 7♦, 8♥, 6♥, 7♠, Q♥, 8♠, 3♥, A♠, J♥
    C2: 2♠, J♣, A♣, Q♠, 10♥, 4♥, 3♣, 2♣, 8♦
    C3: K♥, 9♠, Q♦, 2♦, K♦, 10♣, A♦, 5♦, 6♦
    C4: 7♥, 5♥, J♦, 6♣, 2♥, 10♠, 5♠, 6♠, 10♦
    C5: 4♠, 8♣, A♥, 3♦, 7♣, 9♦, K♠, J♠
    C6: 9♥, 3♠, 4♣, K♣, 4♦, 5♣, Q♣, 9♣
   */
  // invalid pileNumber
  @Test(expected = IllegalArgumentException.class)
  public void highPileNumber() {
    // pile index is one above allowed: valid pile indexes are 0 to 5
    startedGame.move(PileType.CASCADE, 6, 0, PileType.OPEN, 0);
  }

  @Test(expected = IllegalArgumentException.class)
  public void lowPileNumber() {
    // pile index is below allowed: valid pile indexes are 0 to 5
    startedGame.move(PileType.CASCADE, -1, 0, PileType.OPEN, 0);
  }

  // invalid cardIndex
  @Test(expected = IllegalArgumentException.class)
  public void highCardIndex() {
    // card index is higher than max card index 8
    startedGame.move(PileType.CASCADE, 3, 10, PileType.OPEN, 0);
  }

  @Test(expected = IllegalArgumentException.class)
  public void lowCardIndex() {
    // card index is lower than 0
    startedGame.move(PileType.CASCADE, 3, -1, PileType.OPEN, 0);
  }

  @Test(expected = IllegalArgumentException.class)
  public void notLastCardIndex() {
    // card index is not the last card index
    startedGame.move(PileType.CASCADE, 3, 4, PileType.OPEN, 0);
  }

  // moving from empty Pile
  @Test(expected = IllegalArgumentException.class)
  public void fromEmptyPile() {
    startedGame.move(PileType.OPEN, 0, 0, PileType.OPEN, 1);
  }

  // illegal move to Cascade pile: not less than last card by 1
  // C3 6♦ to C6: color not matching but value is not less than top of C6 by 1
  @Test(expected = IllegalArgumentException.class)
  public void toNotLessByOneCascade() {
    startedGame.move(PileType.CASCADE, 2, 8, PileType.CASCADE, 5);
  }

  // illegal move to Cascade pile: matching color
  // move 9♣ from C6 to O1, then attempt to move J♠ from C5 to C6. The last card
  // of C6 is Q♣, exactly one more than J♠, but of the same color.
  @Test(expected = IllegalArgumentException.class)
  public void toMatchingColorCascade() {
    startedGame.move(PileType.CASCADE, 5, 7, PileType.OPEN, 0);
    startedGame.move(PileType.CASCADE, 4, 7, PileType.CASCADE, 5);
  }

  // illegal move to Foundation pile: non-Ace
  // attempt to move 8♦ from C2 to F1
  @Test(expected = IllegalArgumentException.class)
  public void moveNonAceFoundation() {
    startedGame.move(PileType.CASCADE, 1, 8, PileType.FOUNDATION, 0);
  }

  // illegal move to Foundation pile: not greater than last card by 1
  // same suit, but not greater than last card by 1....
  // move J♥ from C1 to O1, then moving A♠ from C1 to F1, then attempting to move J♠ from C5 to F1
  @Test(expected = IllegalArgumentException.class)
  public void moveNonLessByOneFoundation() {
    startedGame.move(PileType.CASCADE, 0, 8, PileType.OPEN, 0);
    startedGame.move(PileType.CASCADE, 0, 7, PileType.FOUNDATION, 0);
    startedGame.move(PileType.CASCADE, 4, 7, PileType.FOUNDATION, 0);
  }

  // illegal move to Foundation pile: not same suit
  // greater than last card by 1, but not same suit....
  // move J♥ from C1 to O1, then moving A♠ from C1 to F1, then moving 8♦ from C2 to O1=2,
  // then attempting to move 2♣ from C2 to F1
  @Test(expected = IllegalArgumentException.class)
  public void moveNotSameSuitFoundation() {
    // move J♥ from C1 to O1
    startedGame.move(PileType.CASCADE, 0, 8, PileType.OPEN, 0);
    // move A♠ from C1 to F1
    startedGame.move(PileType.CASCADE, 0, 7, PileType.FOUNDATION, 0);
    // move 8♦ from C2 to O1
    startedGame.move(PileType.CASCADE, 1, 8, PileType.OPEN, 1);
    // attempt to move 2♣ from C2 to F1 on top of A♠
    startedGame.move(PileType.CASCADE, 1, 7, PileType.FOUNDATION, 0);
  }

  // illegal move to Open pile: already occupied
  @Test(expected = IllegalArgumentException.class)
  public void toOccupiedOpenPile() {
    // moving J♥ from C1 to O1
    startedGame.move(PileType.CASCADE, 0, 8, PileType.OPEN, 0);

    // attempting to move 8♦ from C2 to O1
    startedGame.move(PileType.CASCADE, 1, 8, PileType.OPEN, 0);
  }
}
