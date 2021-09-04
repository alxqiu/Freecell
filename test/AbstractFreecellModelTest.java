import cs3500.freecell.model.FreecellModel;
import cs3500.freecell.model.PileType;
import cs3500.freecell.model.hw02.Card;
import cs3500.freecell.model.hw02.CardType;
import cs3500.freecell.model.hw02.ICard;
import cs3500.freecell.model.hw02.SimpleFreecellModel;
import cs3500.freecell.model.hw04.MultiFreecellModel;
import org.junit.Before;
import org.junit.Test;

import static org.junit.Assert.assertEquals;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

/**
 * Abstract test class that tests both SimpleFreecellModel and MultiFreecellModel.
 *
 * <p>Essentially is SimpleFreecellTests but abstract and applied to both Multi and Single move
 * models, just ensuring that MultiFreecellModel can do everything that SimpleFreecellModel can.</p>
 */
public abstract class AbstractFreecellModelTest {

  private FreecellModel<ICard> freecellModel1;
  private FreecellModel<ICard> unstartedGame;
  private FreecellModel<ICard> easiestGame;
  private FreecellModel<ICard> midGame;

  protected abstract FreecellModel<ICard> constructModel1();

  protected abstract FreecellModel<ICard> constructUnstartedGame();

  protected abstract FreecellModel<ICard> constructEasiestGame();

  protected abstract FreecellModel<ICard> constructMidGame();

  @Before
  public void init() {
    freecellModel1 = constructModel1();
    unstartedGame = constructUnstartedGame();
    easiestGame = constructEasiestGame();
    midGame = constructMidGame();
  }

  // construct a list of cards from 13 to 1 in each suit, which can be easily winnable
  // if you don't shuffle
  private static List<ICard> easiestDeckToWin() {
    ArrayList<ICard> toBuild = new ArrayList<ICard>();
    for (int i = 13; i > 0; i--) {
      for (CardType c : CardType.values()) {
        toBuild.add(new Card(i, c));
      }
    }
    return toBuild;
  }

  // method to win the easiest game to win in freecell history
  /*
    For reference, easiestDeckToWin() is started in init() to look like this:
    C1: K♣, Q♣, J♣, 10♣, 9♣, 8♣, 7♣, 6♣, 5♣, 4♣, 3♣, 2♣, A♣
    C2: K♦, Q♦, J♦, 10♦, 9♦, 8♦, 7♦, 6♦, 5♦, 4♦, 3♦, 2♦, A♦
    C3: K♥, Q♥, J♥, 10♥, 9♥, 8♥, 7♥, 6♥, 5♥, 4♥, 3♥, 2♥, A♥
    C4: K♠, Q♠, J♠, 10♠, 9♠, 8♠, 7♠, 6♠, 5♠, 4♠, 3♠, 2♠, A♠
   */
  private void winGame() {
    // iterate through each Cascade pile, moving each card at a time
    for (int i = 0; i < 4; i++) {
      for (int j = 12; j >= 0; j--) {
        easiestGame.move(PileType.CASCADE, i, j, PileType.FOUNDATION, i);
      }
    }
  }

  // testing if getDeck() will produce a deck that has 52 cards
  // and is exactly the same as expected (produced manually here)
  @Test
  public void testGetDeck() {
    // this implementation has a nested for loop but is still O(1)
    ArrayList<ICard> deckCopy = new ArrayList<ICard>();

    // iterate over the different card suits, creating Cards values 1 through 13 each time
    for (CardType c : CardType.values()) {
      for (int i = 1; i <= 13; i++) {
        deckCopy.add(new Card(i, c));
      }
    }
    assertEquals(freecellModel1.getDeck(), deckCopy);
    assertEquals(freecellModel1.getDeck().size(), 52);
  }

  // test that calling startGame after calling startGame restarts the game with fresh piles
  // testing traits, as well as the Cards of a select pile to ensure that nothing is left behind
  // when calling .startGame
  @Test
  public void testRefresh() {
    // traits before "refresh"
    assertEquals(freecellModel1.getNumCardsInCascadePile(0), 11);
    assertEquals(freecellModel1.getNumOpenPiles(), 2);
    // String representation of all Cards in C2 before refresh
    String c2PreRefresh = "";
    for (int i = 0; i < freecellModel1.getNumCardsInCascadePile(1); i++) {
      c2PreRefresh += freecellModel1.getCascadeCardAt(1, i).toString() + " ";
    }
    assertEquals(c2PreRefresh, "2♣ 7♣ Q♣ 4♦ 9♦ A♥ 6♥ J♥ 3♠ 8♠ K♠ ");

    // restarting with.startGame()
    freecellModel1.startGame(freecellModel1.getDeck(), 7, 5, true);
    // testing the same traits post refresh
    assertEquals(freecellModel1.getNumCardsInCascadePile(0), 8);
    assertEquals(freecellModel1.getNumOpenPiles(), 5);
    String c2PostRefresh = "";
    for (int i = 0; i < freecellModel1.getNumCardsInCascadePile(1); i++) {
      c2PostRefresh += freecellModel1.getCascadeCardAt(1, i).toString() + " ";
    }
    assertEquals(c2PostRefresh, "A♥ 6♠ 9♥ 3♦ 7♦ 4♥ 8♠ K♥ ");
  }

  // test that shuffling with startGame doesn't change the original deck given...
  @Test
  public void testShuffling() {
    // test that this deck object isn't going to be changed after we shuffle it...
    ArrayList<ICard> easyDeckCopy = new ArrayList<ICard>(easiestDeckToWin());
    unstartedGame.startGame(easyDeckCopy, 5, 3, true);
    assertEquals(easyDeckCopy, easiestDeckToWin());

    /*
      we can trust that each cascade pile in easiestGame is going to be dealt
      such that each cascade pile goes from K, Q, J, to 1....so lets check to make sure
      shuffling changes that
     */

    // this is checking one specific card without shuffling....
    easiestGame.startGame(easiestDeckToWin(), 4, 2, false);
    ICard firstCardAtC1 = easiestGame.getCascadeCardAt(0, 0);
    ICard shallowFirstCardCopy = new Card(firstCardAtC1.getValue(), firstCardAtC1.getSuit());
    assertEquals(firstCardAtC1, new Card(13, CardType.CLUB));

    // turning shuffling on....NOTE that easiestGame is constructed with a seeded random object
    // so we won't have the one in 52 chance that the firstCardAtC1 stays the same...
    easiestGame.startGame(easiestDeckToWin(), 4, 2, true);
    ICard newfirstCardAtC1 = easiestGame.getCascadeCardAt(0, 0);
    // the value of this card is A♠
    assertEquals(newfirstCardAtC1, new Card(1, CardType.SPADE));
    assertEquals(newfirstCardAtC1.equals(shallowFirstCardCopy), false);
  }

  // testing that gameOver returns true when over and returns false when not over
  @Test
  public void testGameOver() {
    assertEquals(unstartedGame.isGameOver(), false);
    assertEquals(freecellModel1.isGameOver(), false);
    assertEquals(easiestGame.isGameOver(), false);
    // call winGame() to win easiestGame
    winGame();
    assertEquals(easiestGame.isGameOver(), true);
  }

  // test some valid moves....

  /*
   initial state of midGame after startGame in init....
    F1:
    F2:
    F3:
    F4:
    O1:
    O2:
    O3:
    O4:
    C1: 8♥, J♠, 5♣, J♥, 9♠, A♠, A♣, Q♠, 4♦
    C2: 10♥, 7♦, 4♣, 7♣, 6♦, 3♠, 2♣, 8♠, 4♥
    C3: A♦, 2♥, 9♦, 8♦, 5♠, 5♥, 10♣, 9♥, Q♦
    C4: 6♣, K♠, 9♣, 6♠, 4♠, Q♥, 10♦, Q♣, 5♦
    C5: K♦, 3♥, A♥, 10♠, K♥, 2♠, 6♥, J♦
    C6: 3♣, K♣, J♣, 3♦, 2♦, 8♣, 7♠, 7♥
   */


  // move 4♥ from C2 to O1, then move 7♥ from C6 to C2, then move
  // J♦ from C5 to O2, then move 6♥ from C5 to C6.
  @Test
  public void testOpenAndCascadeMoves() {
    assertEquals(midGame.getNumCardsInCascadePile(1), 9);
    assertEquals(midGame.getNumCardsInCascadePile(4), 8);
    assertEquals(midGame.getNumCardsInCascadePile(5), 8);
    assertEquals(midGame.getNumCardsInOpenPile(0), 0);
    assertEquals(midGame.getNumCardsInOpenPile(1), 0);
    midGame.move(PileType.CASCADE, 1, 8, PileType.OPEN, 0);
    midGame.move(PileType.CASCADE, 5, 7, PileType.CASCADE, 1);
    midGame.move(PileType.CASCADE, 4, 7, PileType.OPEN, 1);
    midGame.move(PileType.CASCADE, 4, 6, PileType.CASCADE, 5);
    // verify that C2 and C6 have the number of cards replaced due to the moves
    // while C5 has two less cards, and O1 and O2 now have cards
    assertEquals(midGame.getNumCardsInCascadePile(1), 9);
    assertEquals(midGame.getNumCardsInCascadePile(4), 6);
    assertEquals(midGame.getNumCardsInCascadePile(5), 8);
    assertEquals(midGame.getNumCardsInOpenPile(0), 1);
    assertEquals(midGame.getNumCardsInOpenPile(1), 1);
  }

  // testing as many foundation moves we can make here...
  @Test
  public void testFoundationMoves() {
    assertEquals(easiestGame.getNumCardsInFoundationPile(0), 0);
    assertEquals(easiestGame.getNumCardsInFoundationPile(1), 0);
    assertEquals(easiestGame.getNumCardsInFoundationPile(2), 0);
    assertEquals(easiestGame.getNumCardsInFoundationPile(3), 0);

    // this will win easiestGame, and we can test all foundation piles after
    winGame();

    assertEquals(easiestGame.getNumCardsInFoundationPile(0), 13);
    assertEquals(easiestGame.getNumCardsInFoundationPile(1), 13);
    assertEquals(easiestGame.getNumCardsInFoundationPile(2), 13);
    assertEquals(easiestGame.getNumCardsInFoundationPile(3), 13);

    // some more organic moves....
    // move Q♠, 4♦ from C1 to O1 and O2, then move A♣ from C1 to F1
    // then move 8♠, 4♥ from C2 to O2 and O3, then move 2♣ from C2 to F1
    assertEquals(midGame.getNumCardsInFoundationPile(0), 0);
    midGame.move(PileType.CASCADE, 0, 8, PileType.OPEN, 0);
    midGame.move(PileType.CASCADE, 0, 7, PileType.OPEN, 1);
    midGame.move(PileType.CASCADE, 0, 6, PileType.FOUNDATION, 0);
    midGame.move(PileType.CASCADE, 1, 8, PileType.OPEN, 2);
    midGame.move(PileType.CASCADE, 1, 7, PileType.OPEN, 3);
    midGame.move(PileType.CASCADE, 1, 6, PileType.FOUNDATION, 0);
    assertEquals(midGame.getNumCardsInFoundationPile(0), 2);
  }

  /**
   * Subclass of abstract test class for SimpleFreecellModel.
   */
  public static class TestSimpleFreecellModel extends AbstractFreecellModelTest {
    @Override
    protected FreecellModel constructModel1() {
      FreecellModel freecellModel1 = new SimpleFreecellModel(new Random(67));
      freecellModel1.startGame(freecellModel1.getDeck(), 5, 2, false);
      return freecellModel1;
    }

    @Override
    protected FreecellModel constructUnstartedGame() {
      return new SimpleFreecellModel();
    }

    @Override
    protected FreecellModel constructEasiestGame() {
      FreecellModel easiestGame = new SimpleFreecellModel(new Random(7));
      easiestGame.startGame(easiestDeckToWin(), 4, 4, false);
      return easiestGame;
    }

    @Override
    protected FreecellModel constructMidGame() {
      FreecellModel midGame = new SimpleFreecellModel(new Random(94));
      midGame.startGame(midGame.getDeck(), 6, 4, true);
      return midGame;
    }
  }

  /**
   * Subclass of abstract test class for MultiFreecellModel.
   */
  public static class TestMultiFreecellModel extends AbstractFreecellModelTest {
    @Override
    protected FreecellModel constructModel1() {
      FreecellModel freecellModel1 = new MultiFreecellModel(new Random(67));
      freecellModel1.startGame(freecellModel1.getDeck(), 5, 2, false);
      return freecellModel1;
    }

    @Override
    protected FreecellModel constructUnstartedGame() {
      return new MultiFreecellModel();
    }

    @Override
    protected FreecellModel constructEasiestGame() {
      FreecellModel easiestGame = new MultiFreecellModel(new Random(7));
      easiestGame.startGame(easiestDeckToWin(), 4, 4, false);
      return easiestGame;
    }

    @Override
    protected FreecellModel constructMidGame() {
      FreecellModel midGame = new MultiFreecellModel(new Random(94));
      midGame.startGame(midGame.getDeck(), 6, 4, true);
      return midGame;
    }
  }
}
