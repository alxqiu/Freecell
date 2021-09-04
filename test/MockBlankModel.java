import cs3500.freecell.model.PileType;
import cs3500.freecell.model.hw02.ICard;
import cs3500.freecell.model.hw02.SimpleFreecellModel;

import java.util.List;

/**
 * Mock model that doesn't do anything internally to set the game up,
 * just keeps a record of what methods were called with what arguments.
 */
public class MockBlankModel extends SimpleFreecellModel {
  private String log;

  /**
   * Constructs a new MockBlankModel with an empty log.
   */
  public MockBlankModel() {
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
  public void startGame(List<ICard> deck, int numCascadePiles, int numOpenPiles, boolean shuffle)
          throws IllegalArgumentException {
    log += String.format("startGame called with numCascadePile: %d, ", numCascadePiles)
            + String.format("numOpenPiles: %d, ", numOpenPiles)
            + String.format("shuffle: %b\n", shuffle);
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

  }
}
