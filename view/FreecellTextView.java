package view;

import model.FreecellModelState;
import model.pilescards.PileType;

import java.io.IOException;

/**
 * Class representing a text view of a Freecell game, implements FreecellView.
 * Specific to Freecell implementations that possess the sufficient getter methods
 * in FreecellModelState. Design decision: renderBoard adds a newline as handling
 * formatting is a task for the View. However, renderMessage prints the message as given
 * to avoid confusion and ease of design on end of controller.
 */
public class FreecellTextView implements FreecellView {

  private final FreecellModelState<?> model;
  private final Appendable output;

  /**
   * Constructs a FreecellTextView object out of a model.
   *
   * @param model a model of a Freecell game
   * @throws IllegalArgumentException when given null
   */
  public FreecellTextView(FreecellModelState<?> model) throws IllegalArgumentException {
    if (model == null) {
      throw new IllegalArgumentException("null argument invalid");
    }
    this.model = model;
    this.output = System.out;
  }

  /**
   * Constructs a FreecellTextView object out of a model and an Appendable
   * object for output.
   *
   * @param model a model of a Freecell game
   * @param output an Appendable that renders are transmitted to. If printing to this object
   *              would throw IOException, this.output will be replaced with console.
   * @throws IllegalArgumentException if either model or output are null
   */
  public FreecellTextView(FreecellModelState<?> model, Appendable output)
          throws IllegalArgumentException {
    if (model == null || output == null) {
      throw new IllegalArgumentException("null arguments invalid");
    }
    this.model = model;

    // interpreting "invalid" appendable as an Appendable that throws an exception when
    // attempting to use it...
    Appendable temp;
    try {
      output.append("");
      temp = output;
    } catch (IOException io) {
      temp = System.out;
    }
    this.output = temp;
  }

  @Override
  public String toString() {
    // return empty String if the game has not started: see what getter will
    // return if not started...
    if (model.getNumCascadePiles() != -1) {
      String result = "";
      // iterate through all foundation piles
      result += grabPiles(PileType.FOUNDATION);

      // adding all open piles
      for (int i = 0; i < model.getNumOpenPiles(); i++) {
        String line = "O" + (i + 1) + ":";
        if (model.getOpenCardAt(i) != null) {
          line += " " + model.getOpenCardAt(i);
        }
        result += line + "\n";
      }
      // adding all cascade piles
      result += grabPiles(PileType.CASCADE);
      // remove last newline character
      return result.substring(0, result.length() - 1);
    }
    return "";
  }

  /**
   * Method that handles the building of a String representation of all piles of the given
   * PileType belonging to this FreecellTextView's model.
   *
   * @param source a PileType, either FOUNDATION or CASCADE
   * @return Sting representation of all the piles that the model has for the given PileType
   * @throws IllegalArgumentException if given OPEN or null PileType
   */
  private String grabPiles(PileType source) throws IllegalArgumentException {
    // null case handled in switch....
    String result = "";
    int numPiles = 0;
    String identifier;
    switch (source) {
      case FOUNDATION:
        numPiles = 4;
        identifier = "F";
        break;
      case CASCADE:
        numPiles = model.getNumCascadePiles();
        identifier = "C";
        break;
      default:
        throw new IllegalArgumentException("source cannot be Open or null");
    }

    for (int i = 0; i < numPiles; i++) {
      String line = identifier + (i + 1) + ":";

      int numCardsIn;

      // didn't put switch statement in here or in following lines
      // because we have already used switch to ensure that source can only be CASCADE
      // or FOUNDATION
      if (source == PileType.CASCADE) {
        numCardsIn = model.getNumCardsInCascadePile(i);
      } else {
        numCardsIn = model.getNumCardsInFoundationPile(i);
      }

      // can't use switch to get a Card that will either be from model.getCascadeCardAt
      // or .getFoundationCardAt as there is no guarantee either method returns a Card
      for (int j = 0; j < numCardsIn; j++) {
        if (source == PileType.CASCADE) {
          line += " " + model.getCascadeCardAt(i, j).toString() + ",";
        } else {
          line += " " + model.getFoundationCardAt(i, j).toString() + ",";
        }
      }
      if (line.charAt(line.length() - 1) == ',') {
        line = line.substring(0, line.length() - 1);
      }
      result += line + "\n";
    }
    return result;
  }

  @Override
  public void renderBoard() throws IOException {
    // Design decision: should print empty String if the game hasn't started.
    // should also append a newline if not printing an empty String.
    String toAppend = "";
    if (this.toString().length() != 0) {
      toAppend = this.toString() + "\n";
    }

    output.append(toAppend);
  }

  @Override
  public void renderMessage(String message) throws IOException {
    // per Piazza @787: do nothing when given null
    if (message == null) {
      return;
    }
    output.append(message);
  }
}
