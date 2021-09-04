import controller.FreecellController;
import controller.SimpleFreecellController;
import model.FreecellModel;
import model.MultiFreecellModel;
import model.pilescards.ICard;

import java.io.InputStreamReader;
import java.util.Random;

public class FreecellMain {

  public static void main(String[] args) {
    FreecellModel<ICard> model = new MultiFreecellModel(new Random());
    FreecellController<ICard> controller = new SimpleFreecellController(model,
            new InputStreamReader(System.in), System.out);
    controller.playGame(model.getDeck(), 6, 4, true);
  }
}
