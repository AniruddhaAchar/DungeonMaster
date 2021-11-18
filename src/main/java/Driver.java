import java.io.InputStreamReader;
import java.util.concurrent.Callable;

import pdp.aniachar.controller.IController;
import pdp.aniachar.controller.TextController;
import pdp.aniachar.dungeonmaster.DungeonGame;
import pdp.aniachar.dungeonmaster.gameworld.RandomMazeBuilder;
import pdp.aniachar.gamekit.Game;
import pdp.aniachar.gamekit.WorldBuildStrategy;
import pdp.aniachar.view.IView;
import pdp.aniachar.view.TextView;
import picocli.CommandLine;

/**
 * A driver class to demonstrate how the model works.
 */

public class Driver {
  /**
   * The entry point for the driver.
   *
   * @param args used as defined in options.
   */
  public static void main(String[] args) {
    int exitCode = new CommandLine(new NonInteractivePlay()).execute(args);
    System.exit(exitCode);
  }


  @CommandLine.Command(name = "noninteractive", mixinStandardHelpOptions = true,
          description = "Automatically plays dungeon master from start to end.")
  private static class NonInteractivePlay implements Callable<Integer> {

    @CommandLine.Option(names = {"-mr", "--maxrow"}, defaultValue = "10",
            description = "Maximum number of rows in the game.")
    private int maxRows;

    @CommandLine.Option(names = {"-mc", "--maxcolumns"}, defaultValue = "10",
            description = "Maximum number of columns in the game.")
    private int maxCols;

    @CommandLine.Option(names = {"-w", "--iswrappeds"},
            description = "Make the grid wrapped or not.")
    private boolean isWrapped;

    @CommandLine.Option(names = {"-dic", "--degIntConn"}, defaultValue = "0",
            description = "Degree of inter-connectivity between the grid locations.")
    private int degreeOfInterConnections;

    @CommandLine.Option(names = {"-pt", "--perTreasure"}, defaultValue = "10",
            description = "Percentage of caves with treasure.")
    private int percentTreasure;
    @CommandLine.Option(names = {"-nm", "--numberMonsters"}, defaultValue = "0",
            description = "Number of monsters in the game.")
    private int numberMonsters;

    @Override
    public Integer call() {
      WorldBuildStrategy buildStrategy = new RandomMazeBuilder(maxRows, maxCols, isWrapped,
              degreeOfInterConnections, percentTreasure, numberMonsters);
      Game model = new DungeonGame(buildStrategy);
      IView<String, String> view = new TextView(System.out, new InputStreamReader(System.in));
      IController controller = new TextController(model, view);
      controller.start();
      return 0;


    }

  }
}


