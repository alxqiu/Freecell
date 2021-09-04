package model;

import model.pilescards.ICard;

import java.util.Random;

/**
 * Builder pattern class that stores an enum encompassing all game types and a method
 * to create a new FreecellModel based on a given value from that enum type.
 */
public class FreecellModelCreator {

  /**
   * Enum for all game types that implement FreecellModel.
   */
  public enum GameType {
    SINGLEMOVE, MULTIMOVE;
  }

  /**
   * Constructs a new FreecellModel object based on given type.
   * @param type either SINGLEMOVE or MULTIMOVE
   * @return the constructed FreecellModel.
   */
  public static FreecellModel<ICard> create(GameType type) {
    return FreecellModelCreator.create(type, new Random());
  }

  /**
   * Constructs a new FreecellModel object based on given type.
   * @param type either SINGLEMOVE or MULTIMOVE
   * @param rand random object the model will be constructed with
   * @return the constructed FreecellModel.
   */
  public static FreecellModel<ICard> create(GameType type, Random rand) {
    switch (type) {
      case SINGLEMOVE:
        return new SimpleFreecellModel(rand);
      case MULTIMOVE:
        return new MultiFreecellModel(rand);
      default:
        // do nothing, shouldn't be able to arrive here.
    }
    // should not get here, but returns null to avoid java complaints
    return null;
  }
}
