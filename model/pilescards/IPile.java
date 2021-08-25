package model.pilescards;

/**
 * Interface defining behavior for piles of ICards, contains all public methods to be implemented
 * by any implementation of IPile.
 */
public interface IPile {

  /**
   * Effect: adds an ICard to the top of the pile.
   * @param c an ICard to add
   * @throws IllegalArgumentException when attempting to add null
   */
  void addCard(ICard c) throws IllegalArgumentException;

  /**
   * Effect: removes the indexed ICard from this IPile at the index n, starting at 0.
   * If this IPile is empty, or if the index is out of bounds, do nothing.
   *
   * <p>CHANGE in HW04: replaces removeTop(), so we aren't forced to remove from only the top.
   * I am aware that this change essentially makes an IPile an ArrayList<{@code ICard}> but after
   * consulting office hours, I think replacing each IPile with an ArrayList<{@code ICard}>
   * within SimpleFreecellModel is an even bigger design change that isn't as warranted.</p>
   */
  void removeAt(int n);

  /**
   * Grabs a reference to the ICard at the desired index.
   *
   * @param cardIndex index of the desired ICard
   * @return reference to ICard
   * @throws IllegalArgumentException when getting an cardIndex too low or higher than
   *        number of cards - 1, which includes when attempting to get from an empty pile
   */
  ICard getCard(int cardIndex) throws IllegalArgumentException;

  /**
   * Returns number of cards in this IPile as an int.
   *
   * @return number of ICards in this IPile
   */
  int cardCount();
}
