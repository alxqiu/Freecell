import cs3500.freecell.controller.FreecellController;
import cs3500.freecell.controller.SimpleFreecellController;
import cs3500.freecell.model.FreecellModel;
import cs3500.freecell.model.hw02.Card;
import cs3500.freecell.model.hw02.CardType;
import cs3500.freecell.model.hw02.ICard;
import cs3500.freecell.model.hw02.SimpleFreecellModel;
import org.junit.Before;
import org.junit.Test;

import static org.junit.Assert.assertEquals;

import java.io.StringReader;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;

/**
 * Class testing all the gameplay features behind SimpleFreecellController using
 * playGame. Doesn't test controller exceptions, that is tested in TestControllerExceptions
 */
public class TestControllerGameplay {
  private FreecellModel<ICard> simpleModel;
  private MockBlankModel newBlankModel;
  private MockDynamicModel newDynamicMock;
  private StringBuilder outputText;

  @Before
  public void init() {
    simpleModel = new SimpleFreecellModel(new Random(552));
    newBlankModel = new MockBlankModel();
    newDynamicMock = new MockDynamicModel(new Random(37));
    outputText = new StringBuilder();
  }

  // test could not start game: attempting to play game with invalid args
  @Test
  public void couldNotStart() {
    FreecellController newController = new SimpleFreecellController(simpleModel,
            new StringReader("q"), outputText);
    newController.playGame(newDynamicMock.getDeck(), 3, 2, true);
    assertEquals(outputText.toString(), "Could not start game.");
  }

  // test starting of game....
  @Test
  public void testLogStartGame() {
    FreecellController newController = new SimpleFreecellController(newBlankModel,
            new StringReader("q "), outputText);
    newController.playGame(newBlankModel.getDeck(), 4, 3, false);
    // this mock doesn't have any "board" to display, so the view it makes appends
    // empty String when it tries to do renderBoard().
    // should exit the game immediately...
    assertEquals(newBlankModel.log(),
            "startGame called with numCascadePile: 4, numOpenPiles: 3, shuffle: false\n");
    assertEquals(outputText.toString(), "Make your move: \n"
            + "Game quit prematurely.\n");
  }

  // test opening messages
  @Test
  public void testOpeningMessages() {
    FreecellController newCont = new SimpleFreecellController(simpleModel,
            new StringReader("q"), outputText);
    newCont.playGame(simpleModel.getDeck(), 7, 2, true);
    assertEquals(outputText.toString(),
            "F1:\nF2:\nF3:\nF4:\nO1:\nO2:\n"
                    + "C1: A♥, 8♠, 4♦, 5♠, 7♣, 10♠, 5♥, 3♠\n"
                    + "C2: 9♦, K♠, 3♥, A♦, A♠, J♥, A♣, J♠\n"
                    + "C3: K♣, 8♦, 2♥, 2♣, 9♥, 5♣, 6♠, 4♣\n"
                    + "C4: 5♦, 10♦, 6♥, Q♠, J♦, 6♦, 3♣\n"
                    + "C5: 10♥, 7♦, Q♣, 8♣, K♥, K♦, Q♦\n"
                    + "C6: 4♥, 6♣, 2♠, 2♦, J♣, 9♠, 9♣\n"
                    + "C7: 7♥, Q♥, 3♦, 7♠, 4♠, 8♥, 10♣\n"
                    + "Make your move: \n"
                    + "Game quit prematurely.\n");
  }

  // test quit
  @Test
  public void testQuit() {
    FreecellController newCont = new SimpleFreecellController(newBlankModel,
            new StringReader("q"), outputText);
    newCont.playGame(newBlankModel.getDeck(), 7, 2, true);
    // newBlankModel has no board, so its output text will only be Make your move: and
    // Game quit prematurely.
    // confirm that game started...
    assertEquals(newBlankModel.log(),
            "startGame called with numCascadePile: 7, numOpenPiles: 2, shuffle: true\n");
    assertEquals(outputText.toString(), "Make your move: \n"
            + "Game quit prematurely.\n");
  }

  // testing quit mixed with other inputs, valid and invalid...
  @Test
  public void testQuitMixed() {
    FreecellController newCont = new SimpleFreecellController(newBlankModel,
            new StringReader("cr C6 4 q "), outputText);
    newCont.playGame(newBlankModel.getDeck(), 7, 2, true);
    // newBlankModel has no board, so its output text will only be Make your move: and
    // Game quit prematurely.
    // confirm that game started...
    assertEquals(newBlankModel.log(),
            "startGame called with numCascadePile: 7, numOpenPiles: 2, shuffle: true\n");
    assertEquals(outputText.toString(), "Make your move: \n"
            + "Try again: input valid source pile identifier: O1, C3, O2, etc\n"
            + "Game quit prematurely.\n");
  }

  // test that playGame asks for cardIndex if source pile was entered correctly but
  // cardIndex was not
  @Test
  public void testInvalidCardIdx() {
    FreecellController newCont = new SimpleFreecellController(simpleModel,
            new StringReader("C6 buges q"), outputText);
    newCont.playGame(simpleModel.getDeck(), 7, 2, true);
    assertEquals(outputText.toString(),
            "F1:\nF2:\nF3:\nF4:\nO1:\nO2:\n"
                    + "C1: A♥, 8♠, 4♦, 5♠, 7♣, 10♠, 5♥, 3♠\n"
                    + "C2: 9♦, K♠, 3♥, A♦, A♠, J♥, A♣, J♠\n"
                    + "C3: K♣, 8♦, 2♥, 2♣, 9♥, 5♣, 6♠, 4♣\n"
                    + "C4: 5♦, 10♦, 6♥, Q♠, J♦, 6♦, 3♣\n"
                    + "C5: 10♥, 7♦, Q♣, 8♣, K♥, K♦, Q♦\n"
                    + "C6: 4♥, 6♣, 2♠, 2♦, J♣, 9♠, 9♣\n"
                    + "C7: 7♥, Q♥, 3♦, 7♠, 4♠, 8♥, 10♣\n"
                    + "Make your move: \n"
                    + "Try again: input valid card index: 0, 1, 2, 3, etc\n"
                    + "Game quit prematurely.\n");
  }

  // test that playGame asks for pileindex if destination pile was entered incorrectly
  // but source pile and card index were entered correctly
  // return when playGame given q instead of cardIndex
  @Test
  public void testAsksForDestPileIdx() {
    FreecellController newCont = new SimpleFreecellController(simpleModel,
            new StringReader("C6 4 gibberish q"), outputText);
    newCont.playGame(simpleModel.getDeck(), 7, 2, true);
    assertEquals(outputText.toString(),
            "F1:\nF2:\nF3:\nF4:\nO1:\nO2:\n"
                    + "C1: A♥, 8♠, 4♦, 5♠, 7♣, 10♠, 5♥, 3♠\n"
                    + "C2: 9♦, K♠, 3♥, A♦, A♠, J♥, A♣, J♠\n"
                    + "C3: K♣, 8♦, 2♥, 2♣, 9♥, 5♣, 6♠, 4♣\n"
                    + "C4: 5♦, 10♦, 6♥, Q♠, J♦, 6♦, 3♣\n"
                    + "C5: 10♥, 7♦, Q♣, 8♣, K♥, K♦, Q♦\n"
                    + "C6: 4♥, 6♣, 2♠, 2♦, J♣, 9♠, 9♣\n"
                    + "C7: 7♥, Q♥, 3♦, 7♠, 4♠, 8♥, 10♣\n"
                    + "Make your move: \n"
                    + "Try again: input valid destination pile identifier: O1, C3, F2, etc\n"
                    + "Game quit prematurely.\n");
  }

  // return when playGame given q instead of cardIndex
  @Test
  public void testGivenQInsteadOfCardIdx() {
    FreecellController newCont = new SimpleFreecellController(simpleModel,
            new StringReader("C6 q"), outputText);
    newCont.playGame(simpleModel.getDeck(), 7, 2, true);
    assertEquals(outputText.toString(),
            "F1:\nF2:\nF3:\nF4:\nO1:\nO2:\n"
                    + "C1: A♥, 8♠, 4♦, 5♠, 7♣, 10♠, 5♥, 3♠\n"
                    + "C2: 9♦, K♠, 3♥, A♦, A♠, J♥, A♣, J♠\n"
                    + "C3: K♣, 8♦, 2♥, 2♣, 9♥, 5♣, 6♠, 4♣\n"
                    + "C4: 5♦, 10♦, 6♥, Q♠, J♦, 6♦, 3♣\n"
                    + "C5: 10♥, 7♦, Q♣, 8♣, K♥, K♦, Q♦\n"
                    + "C6: 4♥, 6♣, 2♠, 2♦, J♣, 9♠, 9♣\n"
                    + "C7: 7♥, Q♥, 3♦, 7♠, 4♠, 8♥, 10♣\n"
                    + "Make your move: \n"
                    + "Game quit prematurely.\n");
  }

  // return when playGame given q instead of destination pile Index
  @Test
  public void testGivenQInsteadOfDestPileIdx() {
    FreecellController newCont = new SimpleFreecellController(simpleModel,
            new StringReader("C6 7 q"), outputText);
    newCont.playGame(simpleModel.getDeck(), 7, 2, true);
    assertEquals(outputText.toString(),
            "F1:\nF2:\nF3:\nF4:\nO1:\nO2:\n"
                    + "C1: A♥, 8♠, 4♦, 5♠, 7♣, 10♠, 5♥, 3♠\n"
                    + "C2: 9♦, K♠, 3♥, A♦, A♠, J♥, A♣, J♠\n"
                    + "C3: K♣, 8♦, 2♥, 2♣, 9♥, 5♣, 6♠, 4♣\n"
                    + "C4: 5♦, 10♦, 6♥, Q♠, J♦, 6♦, 3♣\n"
                    + "C5: 10♥, 7♦, Q♣, 8♣, K♥, K♦, Q♦\n"
                    + "C6: 4♥, 6♣, 2♠, 2♦, J♣, 9♠, 9♣\n"
                    + "C7: 7♥, Q♥, 3♦, 7♠, 4♠, 8♥, 10♣\n"
                    + "Make your move: \n"
                    + "Game quit prematurely.\n");
  }

  // test with multiple valid moves
  @Test
  public void testBoardMultValid() {
    FreecellController newCon = new SimpleFreecellController(newDynamicMock,
            new StringReader("C5\n6 F2 \n C8 6 F2 q"), outputText);
    newCon.playGame(newDynamicMock.getDeck(), 8, 3, true);

    // logging two valid moves
    assertEquals(newDynamicMock.log(),
            "startGame called with numCascadePile: 8, numOpenPiles: 3, shuffle: true\n"
                    + "move called with source: CASCADE, pileNumber: 4, cardIndex: 5, "
                    + "destination: FOUNDATION, destPileNumber: 1\n"
                    + "move called with source: CASCADE, pileNumber: 7, cardIndex: 5, "
                    + "destination: FOUNDATION, destPileNumber: 1\n");
    // checking the final board state
    assertEquals(outputText.toString().substring(544, 830),
            "F1:\n"
                    + "F2: A♦, 2♦\n"
                    + "F3:\n"
                    + "F4:\n"
                    + "O1:\n"
                    + "O2:\n"
                    + "O3:\n"
                    + "C1: Q♠, 3♣, 3♥, J♣, 5♦, 3♦, 8♥\n"
                    + "C2: 8♣, 4♥, 7♥, 2♥, 5♣, 6♣, 5♥\n"
                    + "C3: 7♦, 9♥, K♥, 6♦, 9♦, K♠, K♣\n"
                    + "C4: J♦, 10♦, 2♠, 3♠, 9♣, 5♠, 2♣\n"
                    + "C5: 8♦, J♠, 7♠, A♠, Q♦\n"
                    + "C6: 7♣, A♥, Q♣, A♣, 8♠, J♥\n"
                    + "C7: 6♥, 9♠, 10♠, 6♠, 4♦, 10♣\n"
                    + "C8: 10♥, 4♣, Q♥, K♦, 4♠\n"
                    + "Game quit prematurely.\n");
  }

  // test valid moves
  @Test
  public void testLogValidMove() {
    FreecellController newController = new SimpleFreecellController(newBlankModel,
            new StringReader("C3 13 O2 q"), outputText);
    newController.playGame(newBlankModel.getDeck(), 4, 3, false);

    // checking that move is successfully logged
    assertEquals(newBlankModel.log(),
            "startGame called with numCascadePile: 4, numOpenPiles: 3, shuffle: false\n"
                    + "move called with source: CASCADE, pileNumber: 2, cardIndex: 12, "
                    + "destination: OPEN, destPileNumber: 1\n");
    assertEquals(outputText.toString(), "Make your move: \n"
            + "Game quit prematurely.\n");
  }

  // testing with each input separated by newline: output/moves should be same
  // as the test above
  @Test
  public void testValidMoveNewlines() {
    FreecellController newController = new SimpleFreecellController(newBlankModel,
            new StringReader("C3\n13\nO2\nq"), outputText);
    newController.playGame(newBlankModel.getDeck(), 4, 3, false);

    // checking that move is successfully logged
    assertEquals(newBlankModel.log(),
            "startGame called with numCascadePile: 4, numOpenPiles: 3, shuffle: false\n"
                    + "move called with source: CASCADE, pileNumber: 2, cardIndex: 12, "
                    + "destination: OPEN, destPileNumber: 1\n");
    assertEquals(outputText.toString(), "Make your move: \n"
            + "Game quit prematurely.\n");
  }

  // test invalid moves logging and output to appendable
  @Test
  public void testLogInvalidMove() {
    FreecellController newController = new SimpleFreecellController(newBlankModel,
            new StringReader("R3 13 C1f q"), outputText);
    newController.playGame(newBlankModel.getDeck(), 4, 3, false);
    // no "moves" are recorded in the log for this, as move hasn't been called yet...
    assertEquals(newBlankModel.log(),
            "startGame called with numCascadePile: 4, numOpenPiles: 3, shuffle: false\n");
    assertEquals(outputText.toString(), "Make your move: \n"
            + "Try again: input valid source pile identifier: O1, C3, O2, etc\n"
            + "Try again: input valid source pile identifier: O1, C3, O2, etc\n"
            + "Try again: input valid source pile identifier: O1, C3, O2, etc\n"
            + "Game quit prematurely.\n");
  }

  // test illegal moves
  // see if the game complains or not...
  // tested here: attempting to move from foundation, and moving to cascade
  // with invalid card
  @Test
  public void testIllegalMoves() {
    FreecellController sampleController = new SimpleFreecellController(newDynamicMock,
            new StringReader("C1 13 F1 F1 1 O1 C2 13 C3 q"), outputText);
    sampleController.playGame(easiestDeckToWin(), 4, 1, false);
    // the input will move Ace of clubs to Foundation 1, then attempt to move from F1 to
    // O1: illegal move under game rules, but not invalid input

    assertEquals(newDynamicMock.log(),
            "startGame called with numCascadePile: 4, numOpenPiles: 1, shuffle: false\n"
                    + "move called with source: CASCADE, pileNumber: 0, cardIndex: 12, "
                    + "destination: FOUNDATION, destPileNumber: 0\n"
                    + "move called with source: FOUNDATION, pileNumber: 0, cardIndex: 0, "
                    + "destination: OPEN, destPileNumber: 0\n"
                    + "move called with source: CASCADE, pileNumber: 1, cardIndex: 12, " +
                    "destination: CASCADE, destPileNumber: 2\n");
    assertEquals(outputText.toString(), "F1:\nF2:\nF3:\nF4:\nO1:\n"
            + "C1: K♣, Q♣, J♣, 10♣, 9♣, 8♣, 7♣, 6♣, 5♣, 4♣, 3♣, 2♣, A♣\n"
            + "C2: K♦, Q♦, J♦, 10♦, 9♦, 8♦, 7♦, 6♦, 5♦, 4♦, 3♦, 2♦, A♦\n"
            + "C3: K♥, Q♥, J♥, 10♥, 9♥, 8♥, 7♥, 6♥, 5♥, 4♥, 3♥, 2♥, A♥\n"
            + "C4: K♠, Q♠, J♠, 10♠, 9♠, 8♠, 7♠, 6♠, 5♠, 4♠, 3♠, 2♠, A♠\n"
            + "Make your move: \n"
            + "F1: A♣\nF2:\nF3:\nF4:\nO1:\n"
            + "C1: K♣, Q♣, J♣, 10♣, 9♣, 8♣, 7♣, 6♣, 5♣, 4♣, 3♣, 2♣\n"
            + "C2: K♦, Q♦, J♦, 10♦, 9♦, 8♦, 7♦, 6♦, 5♦, 4♦, 3♦, 2♦, A♦\n"
            + "C3: K♥, Q♥, J♥, 10♥, 9♥, 8♥, 7♥, 6♥, 5♥, 4♥, 3♥, 2♥, A♥\n"
            + "C4: K♠, Q♠, J♠, 10♠, 9♠, 8♠, 7♠, 6♠, 5♠, 4♠, 3♠, 2♠, A♠\n"
            + "invalid move: source cannot be null or foundation\n"
            + "invalid move: Invalid move to cascade pile: card to move is "
            + "same color or is not one less than top of pile\n"
            + "Game quit prematurely.\n");
  }

  // attempting to move to occupied open space, and out of bounds card index
  // and out of bound pile index
  @Test
  public void testIllegalMovesSet2() {
    FreecellController sampleController = new SimpleFreecellController(newDynamicMock,
            new StringReader("C1 13 O1 C2 13 O1 C5 13 F1 C3 14 F1 C3 13 F5 q"), outputText);
    sampleController.playGame(easiestDeckToWin(), 4, 1, false);
    assertEquals(newDynamicMock.log(),
            "startGame called with numCascadePile: 4, numOpenPiles: 1, shuffle: false\n"
                    + "move called with source: CASCADE, pileNumber: 0, cardIndex: 12, "
                    + "destination: OPEN, destPileNumber: 0\n"
                    + "move called with source: CASCADE, pileNumber: 1, cardIndex: 12, "
                    + "destination: OPEN, destPileNumber: 0\n"
                    + "move called with source: CASCADE, pileNumber: 4, cardIndex: 12, "
                    + "destination: FOUNDATION, destPileNumber: 0\n"
                    + "move called with source: CASCADE, pileNumber: 2, cardIndex: 13, "
                    + "destination: FOUNDATION, destPileNumber: 0\n"
                    + "move called with source: CASCADE, pileNumber: 2, cardIndex: 12, "
                    + "destination: FOUNDATION, destPileNumber: 4\n");
    assertEquals(outputText.toString(), "F1:\nF2:\nF3:\nF4:\nO1:\n"
            + "C1: K♣, Q♣, J♣, 10♣, 9♣, 8♣, 7♣, 6♣, 5♣, 4♣, 3♣, 2♣, A♣\n"
            + "C2: K♦, Q♦, J♦, 10♦, 9♦, 8♦, 7♦, 6♦, 5♦, 4♦, 3♦, 2♦, A♦\n"
            + "C3: K♥, Q♥, J♥, 10♥, 9♥, 8♥, 7♥, 6♥, 5♥, 4♥, 3♥, 2♥, A♥\n"
            + "C4: K♠, Q♠, J♠, 10♠, 9♠, 8♠, 7♠, 6♠, 5♠, 4♠, 3♠, 2♠, A♠\n"
            + "Make your move: \n"
            + "F1:\nF2:\nF3:\nF4:\nO1: A♣\n"
            + "C1: K♣, Q♣, J♣, 10♣, 9♣, 8♣, 7♣, 6♣, 5♣, 4♣, 3♣, 2♣\n"
            + "C2: K♦, Q♦, J♦, 10♦, 9♦, 8♦, 7♦, 6♦, 5♦, 4♦, 3♦, 2♦, A♦\n"
            + "C3: K♥, Q♥, J♥, 10♥, 9♥, 8♥, 7♥, 6♥, 5♥, 4♥, 3♥, 2♥, A♥\n"
            + "C4: K♠, Q♠, J♠, 10♠, 9♠, 8♠, 7♠, 6♠, 5♠, 4♠, 3♠, 2♠, A♠\n"
            + "invalid move: Open slot occupied\n"
            + "invalid move: Pile index invalid\n"
            + "invalid move: Card index invalid\n"
            + "invalid move: foundation pile index out of bounds\n"
            + "Game quit prematurely.\n");
  }

  // testing win and that multiple moves can be made towards victory.
  @Test
  public void testGameOverMessage() {
    FreecellController newControls = new SimpleFreecellController(simpleModel,
            new StringReader(winGameString()), outputText);
    newControls.playGame(easiestDeckToWin(), 4, 1, false);

    // just want the last ten chars that say "Game over." not the entire thing...
    int outLength = outputText.length();
    assertEquals(outputText.toString().substring(outLength - 11), "Game over.\n");
  }

  // testing completion even with invalid inputs:
  @Test
  public void testGameOverMessageInvalidInputs() {
    FreecellController newControls = new SimpleFreecellController(simpleModel,
            new StringReader(" ehaehoe " + winGameString().substring(0, 65)
                    + " cheeese "
                    + winGameString().substring(65)), outputText);
    newControls.playGame(easiestDeckToWin(), 4, 1, false);

    // just want the last ten chars that say "Game over." not the entire thing...
    int outLength = outputText.length();
    assertEquals(outputText.toString().substring(outLength - 11), "Game over.\n");
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

  // tester method that builds a set of inputs that win the game
  // if the deck used is easiestDeckToWin and is not shuffled
  private static String winGameString() {
    String result = "";
    // iterate through each Cascade pile, moving each card at a time
    for (int i = 1; i <= 4; i++) {
      for (int j = 13; j > 0; j--) {
        result += "C" + i + " " + j + " F" + i + " ";
      }
    }
    return result;
  }
}
