package controller;

/**
 * Designations for the different searches of unique types of tokens in SimpleFreecellController.
 * <br/> SOURCE_PILE --> while loop in playGame of SimpleFreecellController is looking for source
 * pile index/pile letter combination, like C1 or O3.
 *
 * <br/> CARD_INDEX --> looking for card index, beginning at 1.
 *
 * <br/> DEST_PILE --> looking for destination pile token made up of index/pile letter
 * combination, like C1, O2, or F3
 */
public enum SearchType {
  SOURCE_PILE, CARD_INDEX, DEST_PILE;
}
