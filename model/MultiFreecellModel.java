package model;


import model.pilescards.ICard;
import model.pilescards.PileType;

import java.util.Random;

/**
 * Subclass of SimpleFreecellModel, having the exact same rules but now allowing multi-card moves.
 * Multi-card moves can be attempted by specifying the lowest card index of a build when calling
 * move(), and will attempt to move that card and all cards above, assuming they are a build, to
 * another cascade pile such that the added cards will form a valid build with the last card of
 * the destination pile, or if the destination pile is empty.
 */
public class MultiFreecellModel extends SimpleFreecellModel {

  /**
   * Constructs a MultiFreecellModel with a new Random object.
   */
  public MultiFreecellModel() {
    super();
  }

  /**
   * Constructs a MultiFreecellModel with a given Random object.
   *
   * @param rand random object that may be seeded.
   * @throws IllegalArgumentException if given null instead of a Random.
   */
  public MultiFreecellModel(Random rand) throws IllegalArgumentException {
    super(rand);
  }

  @Override
  public void move(PileType source, int pileNumber, int cardIndex,
                   PileType destination, int destPileNumber)
          throws IllegalArgumentException, IllegalStateException {
    if ((!(source == PileType.CASCADE && destination == PileType.CASCADE))
            || cardIndex == this.getNumCardsInCascadePile(pileNumber) - 1) {
      // if neither source or destination is CASCADE, then the move cannot be
      // a multi-move, so we let super.move() handle it.
      // OR: if the cardIndex is equal to the index of the last card
      // of the CascadePile corresponding to the given pileNumber
      super.move(source, pileNumber, cardIndex, destination, destPileNumber);
      return;
    }
    // here, we can assume that source AND destination are both CASCADE
    // and that cardIndex isn't the last card of the matching cascade pile

    // if num cards is 13, and cardIndex is 12, then 1 card to move.
    int numCardsToMove = this.getNumCardsInCascadePile(pileNumber) - cardIndex;
    if (numCardsToMove > ((this.numFreeOpens() + 1) * Math.pow(2, this.numFreeCascades()))) {
      throw new IllegalArgumentException("insufficient free piles for multi-card move");
    }
    if (pileNumber < 0 || pileNumber > this.getNumCascadePiles() - 1
            || destPileNumber < 0 || destPileNumber > this.getNumCascadePiles() - 1
            || this.getNumCardsInCascadePile(pileNumber) == 0) {
      throw new IllegalArgumentException("pile index invalid, or empty source pile");
    }
    if (cardIndex < 0 || cardIndex > this.getNumCardsInCascadePile(pileNumber) - 1) {
      // at this case, we are guaranteed source is CASCADE and pileNumber is valid index
      throw new IllegalArgumentException("invalid card index");
    }

    // can assume pile has at least one card...
    // ArrayList<ICard> buildToMove = buildBuilder(pileNumber, cardIndex);
    buildMoveHandler(pileNumber, cardIndex, destPileNumber);
  }

  /**
   * Enforces and attempts to execute a legal multi-card move given pileNumber, cardIndex,
   * and destPileNumber. Legal if the individual cards above and including the card at the given
   * cardIndex within the specified pile form a valid descending value alternating color build
   * with themselves AND the top card of the destination pile. Legal if cards to move form build
   * and destination cascade pile is empty.
   *
   * @param pileNumber     index of the pile to move from starting from 0.
   * @param cardIndex      index of lowest card to move starting from 0.
   * @param destPileNumber index of pile to move to starting from 0.
   * @throws IllegalArgumentException if a build cannot be moved or formed.
   */
  private void buildMoveHandler(int pileNumber, int cardIndex, int destPileNumber)
          throws IllegalArgumentException {
    // check if destination is empty or not...
    if (this.cascade.get(destPileNumber).cardCount() != 0) {
      int topOfDestIdx = this.cascade.get(destPileNumber).cardCount() - 1;

      // these conditions check if the top value of the destination pile is both one
      // greater than the highest in the build that could be moved, and is of a different color
      boolean oneAboveHighestInBuild =
              this.getCascadeCardAt(destPileNumber, topOfDestIdx).getValue()
                      - this.getCascadeCardAt(pileNumber, cardIndex).getValue() == 1;
      boolean altColorWithHighestInBuild =
              !(this.getCascadeCardAt(pileNumber, cardIndex).sameColor(
                      this.getCascadeCardAt(destPileNumber, topOfDestIdx)));
      if (!(oneAboveHighestInBuild && altColorWithHighestInBuild)) {
        // if either condition is false, then the top of destination cannot form
        // a valid build with the source pile build....
        throw new IllegalArgumentException("cannot form build with destination");
      }
    }

    // NOTE: even if the top card of the destination pile adds to the end of the build you are
    // moving, and the card below it fits into the build pattern, that would constitute three
    // individual moves: moving top of dest card out to free pile, moving build in, then
    // move the top of dest card back to the destination pile. So this method shouldn't care about
    // that case, as of 6/3 2:05pm PT Office Hours visit.

    int lastIdxToMove = this.cascade.get(pileNumber).cardCount() - 1;
    ICard previousCard = this.cascade.get(pileNumber).getCard(cardIndex);

    // adding one at a time to the destination from cardIndex to top of the source pile
    for (int i = cardIndex; i <= lastIdxToMove; i++) {
      ICard toMove = this.cascade.get(pileNumber).getCard(cardIndex);
      if (i != cardIndex && ((toMove.getValue() - previousCard.getValue() != -1)
              || toMove.sameColor(previousCard))) {
        throw new IllegalArgumentException("multi-move doesn't move valid build");
      }
      this.cascade.get(pileNumber).removeAt(cardIndex);
      this.cascade.get(destPileNumber).addCard(toMove);
      previousCard = toMove;
    }
  }

  /**
   * Gets the number of empty cascade piles in this MultiFreecellModel.
   *
   * @return 0 if no free empty cascade piles, otherwise n for n free cascade piles.
   */
  private int numFreeCascades() {
    // can't be called from public, and only time this will be called is when game is started
    // so no need to check for that condition.
    int freePileCounter = 0;
    for (int i = 0; i < this.getNumCascadePiles(); i++) {
      if (this.getNumCardsInCascadePile(i) == 0) {
        freePileCounter++;
      }
    }
    return freePileCounter;
  }

  /**
   * Gets the number of empty open piles in this MultiFreecellModel.
   *
   * @return 0 if no free open piles, otherwise n for n free open piles.
   */
  private int numFreeOpens() {
    // not checking for if game has started, for the same reason that we don't in
    // numFreeCascades
    int freePileCounter = 0;
    for (int i = 0; i < this.getNumOpenPiles(); i++) {
      if (this.open[i] == null) {
        freePileCounter++;
      }
    }
    return freePileCounter;
  }
}
