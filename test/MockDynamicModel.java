import cs3500.freecell.model.PileType;
import cs3500.freecell.model.hw02.ICard;
import cs3500.freecell.model.hw02.SimpleFreecellModel;

import java.util.List;
import java.util.Random;

/**
 * This mock class uses super to actually have changes to the gamestate, unlike MockBlankModel.
 * Meant to behave as closely to SimpleFreecellModel as possible while keeping a log of
 * methods called.
 */
public class MockDynamicModel extends SimpleFreecellModel {

  private String log;

  /**
   * Constructs a MockDynamicModel with given Random object.
   *
   * @param rand Random object to make.
   */
  public MockDynamicModel(Random rand) {
    super(rand);
    log = "";
  }

  /**
   * Getter for the log of this Mock.
   *
   * @return log as a String
   */
  public String log() {
    return log;
  }

  @Override
  public void startGame(List<ICard> deck, int numCascadePiles,
                        int numOpenPiles, boolean shuffle)
          throws IllegalArgumentException {
    log += String.format("startGame called with numCascadePile: %d, ", numCascadePiles)
            + String.format("numOpenPiles: %d, ", numOpenPiles)
            + String.format("shuffle: %b\n", shuffle);
    super.startGame(deck, numCascadePiles, numOpenPiles, shuffle);
  }

  @Override
  public void move(PileType source, int pileNumber, int cardIndex,
                   PileType destination, int destPileNumber)
          throws IllegalArgumentException, IllegalStateException {
    log += "move called with source: " + source.toString()
            + String.format(", pileNumber: %d, ", pileNumber)
            + String.format("cardIndex: %d, ", cardIndex)
            + "destination: " + destination.toString()
            + String.format(", destPileNumber: %d\n", destPileNumber);
    super.move(source, pileNumber, cardIndex, destination, destPileNumber);
  }
}
