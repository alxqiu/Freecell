package model.pilescards;

import java.util.Arrays;
import java.util.Objects;

/**
 * Representation of a Card class, containing a value between 1 to 13, which covers
 * all face cards and values 2 - 10, and a suit of {@code CardType} that is either
 * CLUB, DIAMOND, HEART, or SPADE.
 */
public final class Card implements ICard {

  // Note: value is between 1 and 13, inclusive
  private final int value;
  private final CardType suit;

  /**
   * Constructs a Card object.
   *
   * @param value the value of the card expressed as an integer. Aces are 1, Jacks, Queens,
   *              Kings are 11, 12, and 13, respectively.
   * @param suit the suit of the card
   * @throws IllegalArgumentException If there is a null values given to CardType or value outside
   *              of range [1, 13]
   */
  public Card(int value, CardType suit) throws IllegalArgumentException {
    if (suit == null || value < 1 || value > 13) {
      throw new IllegalArgumentException("Invalid value and/or suit");
    }
    this.value = value;
    this.suit = suit;
  }

  @Override
  public int getValue() {
    return this.value;
  }

  @Override
  public CardType getSuit() {
    return this.suit;
  }

  @Override
  public boolean sameColor(ICard c) throws IllegalArgumentException {
    if (c == null) {
      throw new IllegalArgumentException("Card cannot be null");
    }
    CardType[] black = {CardType.CLUB, CardType.SPADE};
    boolean thisIsBlack = Arrays.asList(black).contains(this.getSuit());
    boolean cIsBlack = Arrays.asList(black).contains(c.getSuit());

    if (thisIsBlack) {
      return cIsBlack;
    } else {
      return !cIsBlack;
    }
  }

  /**
   * Override of equals to use Cards within HashSet.
   *
   * @param obj The object to compare to.
   * @return true if obj is logically equal to this Card, false if not identical
   *      or if given null or a non Card object
   */
  @Override
  public boolean equals(Object obj) {
    if (obj == null) {
      return false;
    }
    return (obj instanceof Card)
            // allowed to access .suit and .value of something of the same Class
            && ((Card) obj).suit == this.suit
            && ((Card) obj).value == this.value
            && (obj.hashCode() == this.hashCode());
  }

  /**
   * Override of hashCode due to override of .equals().
   *
   * @return integer hash code from the combination of this card's value and suit
   */
  @Override
  public int hashCode() {
    // find index of this card's suit index in CardType.values() to ensure
    // that each suit corresponds to a unique int.
    return Objects.hash(this.getValue(), Arrays.asList(CardType.values()).indexOf(this.getSuit()));
  }

  /**
   * Override of toString() that returns the value and suit of the card
   * mapped to the appropriate String representations.
   *
   * @return String represention of card in ValueSuit format (A♣, 10♥)
   */
  @Override
  public String toString() {
    if (this.value == 1 || this.value > 10) {
      return valueMapper(this.value) + suitMapper(this.suit);
    } else {
      return String.valueOf(this.value) + suitMapper(this.suit);
    }
  }

  /**
   * Handles mapping of face Card value to an String (A from 1, J, Q, K from 11, 12, 13).
   *
   * @param a card value, that should be a face card value (1, 11, 12, 13)
   * @return String representation matching the face card
   * @throws IllegalArgumentException if given an int that is not a face card number (1, 11, 12, 13)
   */
  private static String valueMapper(int a) throws IllegalArgumentException {
    switch (a) {
      case 1:
        return "A";
      case 11:
        return "J";
      case 12:
        return "Q";
      case 13:
        return "K";
      default:
        throw new IllegalArgumentException("Must be 1, 11, 12, or 13");
    }
  }

  /**
   * Handles mapping of Suit enum to a Card suit symbol.
   *
   * @param c card suit, that should be either CLUB, DIAMOND, HEART, or SPADE
   * @return String representation matching the suit
   * @throws IllegalArgumentException if given null instead of a CardType value
   */
  private static String suitMapper(CardType c) throws IllegalArgumentException {
    switch (c) {
      case CLUB:
        return "♣";
      case DIAMOND:
        return "♦";
      case HEART:
        return "♥";
      case SPADE:
        return "♠";
      default:
        throw new IllegalArgumentException("CardType cannot be null, must be valid CardType");
    }
  }
}
