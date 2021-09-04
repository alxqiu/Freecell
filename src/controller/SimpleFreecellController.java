package controller;

import model.FreecellModel;
import model.pilescards.ICard;
import model.pilescards.PileType;
import view.FreecellTextView;
import view.FreecellView;

import java.io.IOException;
import java.util.Arrays;
import java.util.List;
import java.util.Scanner;

/**
 * Class that implements FreecellController under the ICard interface.
 *
 * <p> The controls are:
 * Q or q --> quits the game and transmits "Game quit prematurely".
 * A sequence of three inputs separated by spaces or newlines: Source pile letter and
 * number (C3, O2), card index beginning from 1, and the destination pile and
 * letter (F1, C3, O2). NOTE: source piles can only be Open and Foundation when appropriate,
 * and all index and pile choices must be valid and legal. Any part of input that causes an
 * illegal choice will trigger a chance to re-enter a choice that is legal. </p>
 */
public class SimpleFreecellController implements FreecellController<ICard> {
  // CHANGE in HW04: changed the type of view to be the interface type instead of the class
  // FreecellTextView, for better design
  private final FreecellView view;
  private final FreecellModel<ICard> model;
  private final Readable rd;

  /**
   * Constructs a new SimpleFreecellController object. Constructs a new FreecellTextView
   * object with the given model and appendable.
   *
   * @param model a FreecellModel specific to the ICard implementation
   * @param rd    readable object, like System.in, or StringReader
   * @param ap    appendable object, like System.out, or StringBuilder
   * @throws IllegalArgumentException if any argument is null
   */
  public SimpleFreecellController(FreecellModel<ICard> model, Readable rd, Appendable ap)
          throws IllegalArgumentException {
    if (model == null || rd == null || ap == null) {
      throw new IllegalArgumentException("no argument given can be null");
    }
    this.model = model;
    this.rd = rd;
    this.view = new FreecellTextView(model, ap);
  }

  @Override
  public void playGame(List<ICard> deck, int numCascades, int numOpens, boolean shuffle)
          throws IllegalArgumentException, IllegalStateException {
    // NOTE: choosing NOT to handle formatting here, leaving that for View to do.
    if (deck == null) {
      throw new IllegalArgumentException("deck cannot be null");
    }
    try {
      this.model.startGame(deck, numCascades, numOpens, shuffle);
    } catch (IllegalArgumentException ia) {
      try {
        // note for this one, I didn't add a newline to pass the JUnit tests
        this.view.renderMessage("Could not start game.");
      } catch (IOException io) {
        throw new IllegalStateException("attempted to render \"could not "
                + "start\" message but couldn't");
      }
      return;
    }
    try {
      this.view.renderBoard();
      this.view.renderMessage("Make your move: \n");
    } catch (IOException io) {
      throw new IllegalStateException("attempted to render board and "
              + "opening message but couldn't");
    }
    Scanner readingFrom = new Scanner(rd);
    try {
      // still will allow IllegalStateException from moveHandler to propagate to caller
      moveHandler(readingFrom);
    } catch (IOException io) {
      throw new IllegalStateException("appendable failed in moveHandler");
    }
  }

  /**
   * Helper method to handle each case of a possible move and all looping, as well
   * as the values needed to call .move on the model. Transmits board state and any
   * relevant messages to the user to the view.
   *
   * @param readingFrom Scanner object to take input from
   * @throws IOException           if at any point Appendable object of this class fails to append
   * @throws IllegalStateException if Scanner stops giving input at any point before game is over
   */
  private void moveHandler(Scanner readingFrom) throws IOException, IllegalStateException {
    // Cycle goes like: Source --> Card Idx --> Destination
    SearchType nextToFind = SearchType.SOURCE_PILE;

    // placeholder values...these are so IntelliJ doesn't complain when calling
    // move with these values. nextToFind starts as SOURCE_PILE, so these are
    // guaranteed to be updated to accurate values BEFORE .move() is ever called
    // on the model, as we can't get to DEST_PILE without hitting the SOURCE_PILE
    // and CARD_INDEX switch cases...
    PileType sourcePile = PileType.OPEN;
    int sourcePileIdx = 0;
    int cardIdx = 0;

    while (readingFrom.hasNext()) {
      String input = readingFrom.next();
      if (input.equals("q") || input.equals("Q")) {
        this.view.renderMessage("Game quit prematurely.\n");
        return;
      } else if (!this.validInput(input, nextToFind)) {
        // invalidTransmitter will handle the transmitting the message
        // to the user if necessary. continue restarts loop
        invalidTransmitter(nextToFind);
        continue;
      }
      // swapping out finding sourcePile/destPile and dest/sourcePileIdx
      // with helpers takes up the same, if not more lines of code
      switch (nextToFind) {
        // validInput ensures that charAt() and subString() calls
        // won't throw exceptions and no non-acceptable characters will be in input
        case SOURCE_PILE:
          sourcePile = pileGetter(input.charAt(0));
          sourcePileIdx = Integer.parseInt(input.substring(1));
          nextToFind = SearchType.CARD_INDEX;
          break;
        case CARD_INDEX:
          cardIdx = Integer.parseInt(input);
          nextToFind = SearchType.DEST_PILE;
          break;
        case DEST_PILE:
          PileType destPile = pileGetter(input.charAt(0));
          int destPileIdx = Integer.parseInt(input.substring(1));
          try {
            this.model.move(sourcePile, sourcePileIdx - 1,
                    cardIdx - 1, destPile, destPileIdx - 1);
            // rendering is successful if move is successful.
            this.view.renderBoard();
            // maybe get a move updater message or something here....
          } catch (IllegalArgumentException iae) {
            // prints according to the description of the IllegalArgumentException
            this.view.renderMessage("invalid move: " + iae.getMessage() + "\n");
          }
          nextToFind = SearchType.SOURCE_PILE;
          break;
        default:
          //no action needed, no other case applies.
      }
      // if at the end of any move game is over, end it.
      if (this.model.isGameOver()) {
        this.view.renderBoard();
        this.view.renderMessage("Game over.\n");
        return;
      }
    }
    // if .hasNext() ever returns false before game is over
    throw new IllegalStateException("ran out of input");
  }

  /**
   * Method to return the appropriate pile type associated with the given char (all uppercase).
   *
   * @param c either 'C', 'F', or 'O', first character from a valid Scanner input.
   * @return the matching PileType, either Cascade, Foundation, or Open.
   * @throws IllegalArgumentException if given character that is not 'C', 'F', or 'O'
   */
  private PileType pileGetter(char c) throws IllegalArgumentException {
    switch (c) {
      case 'C':
        return PileType.CASCADE;
      case 'F':
        return PileType.FOUNDATION;
      case 'O':
        return PileType.OPEN;
      default:
        throw new IllegalArgumentException("given invalid char as argument");
    }
  }

  /**
   * Method that assesses if the given String is valid user input given the type of
   * input we are searching for. Doesn't judge whether or not the input is legal
   * according to the game rules, just as long as it is a valid control.
   *
   * @param input      input given from startGame()
   * @param nextToFind the type of search that the while loop in startGame() is on
   * @return true if a valid, legal input: if searching piles leading letter is C, F, or O,
   *        and all trailing characters are numbers, and if searching for card index the entire
   *        input is a valid integer.
   * @throws IllegalArgumentException if given null in either argument
   */
  private boolean validInput(String input, SearchType nextToFind)
          throws IllegalArgumentException {
    if (input == null || nextToFind == null) {
      throw new IllegalArgumentException("given null as arg");
    }
    boolean isValid = true;

    // not letting me use deprecated wrapper class for char which is required for
    // .contains, so I made validChars one character Strings....
    String[] validChars = {"C", "F", "O"};
    String toParse = "";

    if (nextToFind == SearchType.CARD_INDEX) {
      toParse = input;
    } else {
      // if nextToFind is either pile
      // isValid will be true if validChars contains leading char
      isValid = Arrays.asList(validChars).contains(input.substring(0, 1));
      // toParse will be all non-leading chars
      if (input.length() < 2) {
        isValid = false;
        toParse = "this String will throw NumberFormatException";
      } else {
        toParse = input.substring(1);
      }
    }
    try {
      // checks if all given to this is numbers
      Integer.parseInt(toParse);
    } catch (NumberFormatException nf) {
      // cannot be valid input if exception is thrown...
      isValid = false;
    }
    return isValid;
  }

  /**
   * Transmits a new message to the view object given the current type of Search
   * being conducted. Will only be called if the input in playGame is not valid.
   *
   * @param nextToFind the current type of Search playGame's while loop is on
   * @throws IOException              if transmission fails
   * @throws IllegalArgumentException if given null
   */
  private void invalidTransmitter(SearchType nextToFind)
          throws IllegalArgumentException, IOException {
    if (nextToFind == null) {
      throw new IllegalArgumentException("given null argument");
    }
    String message = "Try again: input valid ";
    switch (nextToFind) {
      case SOURCE_PILE:
        message += "source pile identifier: O1, C3, O2, etc";
        break;
      case DEST_PILE:
        message += "destination pile identifier: O1, C3, F2, etc";
        break;
      case CARD_INDEX:
        message += "card index: 0, 1, 2, 3, etc";
        break;
      default:
        // no action needed, no other case applies
    }
    this.view.renderMessage(message + "\n");
  }
}
