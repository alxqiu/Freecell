package model;

import model.pilescards.Card;
import model.pilescards.CardType;
import model.pilescards.ICard;
import model.pilescards.IPile;
import model.pilescards.Pile;
import model.pilescards.PileType;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashSet;
import java.util.List;
import java.util.Random;
import java.util.Set;


/**
 * Implementation of Freecell's Model, specific to the ICard type, that holds cards in foundation,
 * cascade, and open piles. This model only allows one card at the top of each pile to be moved
 * each time a move is made.
 */
public class SimpleFreecellModel implements FreecellModel<ICard> {

  // field checking if game has started, will be false otherwise
  private boolean gameStarted;
  private final Random rand;
  private ArrayList<IPile> foundation;

  // changing private access to protected so fields
  // can be accessed by subclasses of this class
  protected ArrayList<IPile> cascade;
  // after creation, will be fixed length, and each slot holds one card...
  protected ICard[] open;

  /**
   * Constructs a SimpleFreecellModel object that doesn't initialize any fields
   * except the boolean representation of if the game has started. foundation, cascade, and open
   * fields will be initialized when the game has started. Creates new Random object.
   */
  public SimpleFreecellModel() {
    this(new Random());
  }

  /**
   * Constructs a SimpleFreecellModel object with a potentially seeded random object.
   * Used to test shuffling feature of startGame.
   *
   * @param rand A random object that may or may not be seeded.
   * @throws IllegalArgumentException if given null instead of a Random
   */
  public SimpleFreecellModel(Random rand) throws IllegalArgumentException {
    if (rand == null) {
      throw new IllegalArgumentException("given null instead of Random");
    }
    this.gameStarted = false;
    this.rand = rand;
  }

  /**
   * Checks to see if the given deck is valid, if it contains 52 cards and if it contains no
   * duplicates. Each card is immutable and can only be created with a value in the range
   * [1, 13] and with a non-null suit, so each card in itself must be valid. These conditions
   * also ensure that each possible combination of card value and suit is present, so we don't
   * need a separate way to check for the presence of all possible combinations.
   *
   * @param deck Any List of Card
   * @return true if given deck fits all the requirements above, false otherwise
   */
  private static boolean validDeck(List<ICard> deck) {
    if (deck == null || deck.size() != 52) {
      return false;
    }
    //using HashSet to check for no duplicates
    Set<ICard> deckCopy = new HashSet<ICard>();

    for (ICard c : deck) {
      // if !(deckCopy.add(c)) is true...then that means card c has been
      // added some time before meaning deck has duplicates
      if (!deckCopy.add(c)) {
        return false;
      }
    }
    return true;
  }

  @Override
  public List<ICard> getDeck() {
    // this implementation has a nested for loop but is still O(1)
    ArrayList<ICard> deck = new ArrayList<ICard>();

    // iterate over the different card suits, creating Cards values 1 through 13 each time
    for (CardType c : CardType.values()) {
      for (int i = 1; i <= 13; i++) {
        deck.add(new Card(i, c));
      }
    }
    // NOTE: returns the same deck each time this method is called.
    return deck;
  }

  @Override
  public void startGame(List<ICard> deck, int numCascadePiles, int numOpenPiles, boolean shuffle)
          throws IllegalArgumentException {
    if (!this.validDeck(deck) || numCascadePiles < 4 || numOpenPiles < 1) {
      // NOTE: if deck is null, validDeck must return false, so this exception will still be thrown
      throw new IllegalArgumentException("Invalid input arguments");
    }

    // creating new Cascade and Open piles:
    cascade = new ArrayList<IPile>(numCascadePiles);
    for (int i = 0; i < numCascadePiles; i++) {
      cascade.add(new Pile());
    }
    foundation = new ArrayList<IPile>(4);
    for (int i = 0; i < 4; i++) {
      foundation.add(new Pile());
    }

    // creating open Card slots:
    open = new ICard[numOpenPiles];

    // shuffling and dealing...
    ArrayList<ICard> deckCopy = new ArrayList<ICard>(deck);
    if (shuffle) {
      // need to create a new ArrayList for the shuffling to avoid
      // changing the aliased deck
      Collections.shuffle(deckCopy, rand);
    }
    dealCards(deckCopy, cascade);
    gameStarted = true;
  }

  /**
   * EFFECT: deals the cards from the List of ICard into the various piles represented
   * by piles, starting from the first in the ArrayList and wrapping around
   * once it has reached the last IPile.
   *
   * @param deck  all the cards to deal in a round robin fashion
   * @param piles the piles to distribute cards to
   * @throws IllegalArgumentException if either argument is null
   */
  private static void dealCards(List<ICard> deck, ArrayList<IPile> piles)
          throws IllegalArgumentException {
    if (deck == null || piles == null) {
      throw new IllegalArgumentException("no null args");
    }
    int pileIdx = 0;
    for (ICard c : deck) {
      piles.get(pileIdx).addCard(c);
      // pileIdx allows us to "wrap" around to the beginning when
      // pileIdx == piles.size() - 1, so that we start over at
      // pileIdx == 0 (first pile)
      pileIdx = (pileIdx + 1) % piles.size();
    }
  }

  @Override
  public void move(PileType source, int pileNumber, int cardIndex,
                   PileType destination, int destPileNumber)
          throws IllegalArgumentException, IllegalStateException {
    if (!gameStarted) {
      throw new IllegalStateException("can't move before game has started");
    } else if (source == null || destination == null) {
      throw new IllegalArgumentException("no null args");
    }

    // CHANGE AS OF HW4: changed these two case handlers to protected....
    ICard toMove = retrievalCaseHandler(source, pileNumber, cardIndex);
    placementCaseHandler(toMove, destination, destPileNumber);
  }

  /**
   * Handles all the cases to grab a card given the pile type and card/pile indexes.
   * EFFECT: removes the ICard from the pile if it is a foundation or cascade pile, if
   * it is an open pile, replaces the ICard with null.
   *
   * @param source     type of pile, either OPEN or CASCADE
   * @param pileNumber index of pile to search from, starting with 0
   * @param cardIndex  index of card within desired pile. Should be the top card,
   *                   otherwise illegal move
   * @return ICard of the desired source pile, assumning a valid move
   * @throws IllegalArgumentException if given a null value or FOUNDATION as source,
   *                                  or is illegal retrieval by rules of the game.
   */
  protected ICard retrievalCaseHandler(PileType source, int pileNumber, int cardIndex)
          throws IllegalArgumentException {
    if (source == null || source == PileType.FOUNDATION) {
      throw new IllegalArgumentException("source cannot be null or foundation");
    } else if (pileNumber < 0 || cardIndex < 0) {
      // CHANGE in HW04: ensures that pileNumber and cardIndex cannot be < 0
      throw new IllegalArgumentException("pile and card indices cannot be less than 0");
    }
    ICard temp;
    switch (source) {
      case OPEN:
        // check if its a non-zero index used to move FROM an open...
        if (cardIndex != 0) {
          throw new IllegalArgumentException("Can't access non-zero index of an Open pile");
        } else if (open[pileNumber] == null) {
          throw new IllegalArgumentException("Trying to move from empty Open slot");
        } else if (this.getNumOpenPiles() - 1 < pileNumber) {
          // CHANGE in HW04: forgot to check if open pile index was invalid....
          throw new IllegalArgumentException("Open pile doesn't exist.");
        }
        temp = open[pileNumber];
        open[pileNumber] = null;
        return temp;
      case CASCADE:
        // this call ensures that exceptions will be thrown if a invalid pile index
        // or cardIndex
        temp = getCascadeCardAt(pileNumber, cardIndex);
        // we can only check for cardCount of the appropriate pile if we know it exists
        // so we do this after we have called getCascadeCardAt...
        if (cardIndex != getNumCardsInCascadePile(pileNumber) - 1) {
          throw new IllegalArgumentException("Attempting to move non-top card");
        }
        // NOTE: in HW04, changed IPile.removeTop() with removeAt() to make
        // life a whole lot easier.
        cascade.get(pileNumber).removeAt(this.getNumCardsInCascadePile(pileNumber) - 1);
        return temp;
      default:
        // nothing happens here, case where source is null or foundation is handled above
        // not supposed to be here, but return null to not have intellij mad
        return null;
    }
  }

  /**
   * Handles all the cases and exceptions for moving a card to a specific spot. EFFECT:
   * if a valid move, the new IPile has the desired card to move on top of it.
   *
   * @param toMove         the ICard we are trying to move
   * @param destination    the type of Pile we want to move to
   * @param destPileNumber the index of the Pile we want to move to
   */
  protected void placementCaseHandler(ICard toMove, PileType destination, int destPileNumber) {
    if (toMove == null || destination == null) {
      throw new IllegalArgumentException("no null arguments");
    }
    ICard toExamine;
    switch (destination) {
      case OPEN:
        if (destPileNumber > open.length - 1 || destPileNumber < 0) {
          throw new IllegalArgumentException("Invalid open pile index");
        } else if (open[destPileNumber] != null) {
          throw new IllegalArgumentException("Open slot occupied");
        }
        open[destPileNumber] = toMove;
        break;
      case CASCADE:
        if (destPileNumber > getNumCascadePiles() - 1 || destPileNumber < 0) {
          throw new IllegalArgumentException("cascade pile index out of bounds");
        }
        IPile temp = cascade.get(destPileNumber);
        if (temp.cardCount() != 0) {
          toExamine = temp.getCard(temp.cardCount() - 1);
          // we want toMove to have a value of exactly one less than toExamine
          // toMove.value - toExamine.value == -1
          if (toExamine.sameColor(toMove)
                  || (toMove.getValue() - toExamine.getValue() != -1)) {
            throw new IllegalArgumentException("Invalid move to cascade pile: card"
                    + " to move is same color or is not one less than top of pile");
          }
        }
        cascade.get(destPileNumber).addCard(toMove);
        break;
      case FOUNDATION:
        if (destPileNumber > 3 || destPileNumber < 0) {
          throw new IllegalArgumentException("foundation pile index out of bounds");
        }
        temp = foundation.get(destPileNumber);
        if (temp.cardCount() != 0) {
          toExamine = temp.getCard(temp.cardCount() - 1);
          // we want toMove to have a value one more than toExamine:
          // toMove.value - toExamine.value == 1
          if ((toExamine.getSuit() != toMove.getSuit())
                  || ((toMove.getValue() - toExamine.getValue()) != 1)) {
            throw new IllegalArgumentException("Invalid move to foundation pile: card"
                    + "to move is not the same suit or is not one more than top of pile");
          }
        } else if (toMove.getValue() != 1) {
          throw new IllegalArgumentException("Invalid move to foundation pile: attempting"
                  + "to place non-Ace card on empty foundation pile");
        }
        foundation.get(destPileNumber).addCard(toMove);
        break;
      default:
        //no action needed, no other case applies.
    }
  }

  @Override
  public boolean isGameOver() {
    if (!gameStarted) {
      return false;
    }
    boolean allFoundationsFull = true;
    for (IPile p : foundation) {
      if (p.cardCount() != 13) {
        return false;
      }
    }
    return allFoundationsFull;
  }

  @Override
  public int getNumCardsInFoundationPile(int index)
          throws IllegalArgumentException, IllegalStateException {
    if (!gameStarted) {
      throw new IllegalStateException("Game has not started");
    } else if (index > foundation.size() - 1 || index < 0) {
      throw new IllegalArgumentException("invalid arguments");
    }
    return checkPileIdx(index, foundation);
  }

  @Override
  public int getNumCascadePiles() {
    if (!gameStarted) {
      return -1;
    }
    return cascade.size();
  }

  @Override
  public int getNumCardsInCascadePile(int index)
          throws IllegalArgumentException, IllegalStateException {
    if (!gameStarted) {
      throw new IllegalStateException("Game has not started");
    } else if (index > cascade.size() - 1 || index < 0) {
      throw new IllegalArgumentException("invalid arguments");
    }
    return checkPileIdx(index, cascade);
  }

  /**
   * Grabs the number of cards in the desired pile.
   *
   * @param index index of the desired pile, starting from 0
   * @param piles ArrayList of IPile to search within
   * @return the number of cards in the desired pile
   */
  private int checkPileIdx(int index, ArrayList<IPile> piles) {
    // note: i would delegate checking exceptions here as both methods that call this
    // have similar conditions for exceptions, but I can't get piles in cases of unstarted
    // games.
    return piles.get(index).cardCount();
  }

  @Override
  public int getNumCardsInOpenPile(int index)
          throws IllegalArgumentException, IllegalStateException {
    if (!gameStarted) {
      throw new IllegalStateException("Game has not started");
    } else if (index >= open.length || index < 0) {
      throw new IllegalArgumentException("Pile index invalid");
    }
    ICard item = open[index];
    if (item == null) {
      return 0;
    } else {
      return 1;
    }
  }

  @Override
  public int getNumOpenPiles() {
    if (!gameStarted) {
      return -1;
    }
    return open.length;
  }

  @Override
  public ICard getFoundationCardAt(int pileIndex, int cardIndex)
          throws IllegalArgumentException, IllegalStateException {
    return pilesStatusCheck(pileIndex, cardIndex, foundation);
  }

  @Override
  public ICard getCascadeCardAt(int pileIndex, int cardIndex)
          throws IllegalArgumentException, IllegalStateException {
    return pilesStatusCheck(pileIndex, cardIndex, cascade);
  }


  /**
   * Method that returns the ICard at a specific index from an ArrayList of IPile, from
   * either the foundation or cascade.
   *
   * @param pileIndex integer index of the pile to search
   * @param cardIndex integer index of the card within one of the piles
   * @param piles     the ArrayList of IPile holding the piles to search
   * @return the ICard at the desired pileIndex and cardIndex
   * @throws IllegalArgumentException if pileIndex or cardIndex are out of range
   * @throws IllegalStateException    if game has not yet started
   */
  private ICard pilesStatusCheck(int pileIndex, int cardIndex, ArrayList<IPile> piles)
          throws IllegalArgumentException, IllegalStateException {
    // NOTE: meant to only be used on Cascade and Foundation piles, as they use the Pile class.
    if (!gameStarted) {
      throw new IllegalStateException("Game has not started");
    } else if (piles == null) {
      throw new IllegalArgumentException("piles must not be null");
    }

    if (pileIndex > piles.size() - 1 || pileIndex < 0) {
      throw new IllegalArgumentException("Pile index invalid");
    } else if (cardIndex > piles.get(pileIndex).cardCount() - 1 || cardIndex < 0) {
      throw new IllegalArgumentException("Card index invalid");
    }
    return piles.get(pileIndex).getCard(cardIndex);
  }

  @Override
  public ICard getOpenCardAt(int pileIndex)
          throws IllegalArgumentException, IllegalStateException {
    if (!gameStarted) {
      throw new IllegalStateException("Game has not started");
    } else if (pileIndex >= this.getNumOpenPiles() || pileIndex < 0) {
      throw new IllegalArgumentException("Pile index invalid");
    }
    // storing empty spaces as null anyways, so this will return null if empty open slot
    return open[pileIndex];
  }
}
