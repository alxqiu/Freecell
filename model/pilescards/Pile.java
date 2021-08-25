package model.pilescards;

import java.util.ArrayList;

/**
 * Implementation of IPile that stores all ICards in the pile as an ArrayList of ICard. Used in
 * {@code SimpleFreecellModel} to represent Cascade and Foundation piles.
 */
public class Pile implements IPile {
  // represents foundation and cascade piles....
  private final ArrayList<ICard> cards;

  /**
   *  Constructs a new Pile object with an empty ArrayList of ICard.
   */
  public Pile() {
    this.cards = new ArrayList<ICard>();
  }

  @Override
  public void addCard(ICard c) throws IllegalArgumentException {
    if (c == null) {
      throw new IllegalArgumentException("no null arg");
    }
    cards.add(c);
  }

  @Override
  public void removeAt(int n) {
    // NOTE: changed from removeTop() in HW04 to make it more versatile:
    // I need to be able to remove from any point in the pile rather than only
    // from the top for multimove to work. Doesn't change how SimpleFreecellModel behaves
    // at all.
    if (this.cardCount() < 1 || n > this.cardCount() - 1 || n < 0) {
      return;
    }
    cards.remove(n);
  }

  @Override
  public ICard getCard(int cardIndex) throws IllegalArgumentException {
    if (cardIndex < 0 || cardIndex > cardCount() - 1) {
      throw new IllegalArgumentException("invalid cardIndex");
    }
    return cards.get(cardIndex);
  }

  @Override
  public int cardCount() {
    return cards.size();
  }
}
