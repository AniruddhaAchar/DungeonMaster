import pdp.aniachar.controller.Controller;
import pdp.aniachar.dungeonmaster.DungeonGame;
import pdp.aniachar.gamekit.Game;
import pdp.aniachar.gamekit.WorldBuildStrategy;
import pdp.aniachar.view.GraphicUserInterface.SwingView;
import pdp.aniachar.view.IView;

/**
 * A GUI complete integration test.
 * This is to mock a true driver.
 */

public class GuiIntegrationTest {
  private static Game model;

  /**
   * The main method.
   *
   * @param args ignored.
   */

  public static void main(String[] args) {
    WorldBuildStrategy builder = new FakeWorldBuilder();
    model = new DungeonGame(builder, "Bran The wise");
    IView view = new SwingView(3, 3);
    Controller controller = new Controller(model, view);
    controller.start();
  }
}
