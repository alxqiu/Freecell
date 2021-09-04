import cs3500.freecell.model.FreecellModelCreator;
import org.junit.Test;

import java.util.Random;

import static org.junit.Assert.assertEquals;

/**
 * Test class for the FreecellModelCreator class.
 */
public class TestCreatorClass {

  @Test
  public void testCreateSimpleOne() {
    // i don't have a means of using .equals() for FreecellModels, so I just check if they
    // have the right method and expected returns.
    assertEquals(FreecellModelCreator.create(
            FreecellModelCreator.GameType.SINGLEMOVE).isGameOver(),
            false);
    assertEquals(FreecellModelCreator.create(
            FreecellModelCreator.GameType.SINGLEMOVE).getNumOpenPiles(), -1);
  }

  @Test
  public void testCreateSimple() {
    assertEquals(FreecellModelCreator.create(FreecellModelCreator.GameType.SINGLEMOVE,
            new Random(1)).isGameOver(),
            false);
  }

  @Test
  public void testCreateMultiOne() {
    assertEquals(FreecellModelCreator.create(
            FreecellModelCreator.GameType.MULTIMOVE).isGameOver(),
            false);
    assertEquals(FreecellModelCreator.create(
            FreecellModelCreator.GameType.MULTIMOVE).getNumOpenPiles(), -1);
  }

  @Test
  public void testCreateMulti() {
    assertEquals(FreecellModelCreator.create(FreecellModelCreator.GameType.MULTIMOVE,
            new Random(1)).isGameOver(),
            false);
  }
}
