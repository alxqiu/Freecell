package model.pilescards;

/**
 * Interface for ICard public methods, everything that a ICard should be able to do
 * from the perspective of other classes.
 */
public interface ICard {

  /**
   * Observer method for an integer value of this ICard.
   * @return
   */
  int getValue();

  /**
   * Observer method for CardType of this ICard.
   * @return
   */
  CardType getSuit();

  /**
   * Checking if this Card have the same color as the given Card.
   *
   * @param c Another Card to compare to.
   * @return true if this has same color, false otherwise
   * @throws IllegalArgumentException if given null
   */
  boolean sameColor(ICard c) throws IllegalArgumentException;
}
