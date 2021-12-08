import org.apache.commons.lang3.tuple.MutablePair;

import java.io.InputStreamReader;
import java.io.StringReader;
import java.util.ArrayList;
import java.util.List;
import java.util.NoSuchElementException;

import pdp.aniachar.controller.CommandLineParser;
import pdp.aniachar.controller.Controller;
import pdp.aniachar.dungeonmaster.DungeonGame;
import pdp.aniachar.dungeonmaster.character.monster.Otyughs;
import pdp.aniachar.dungeonmaster.gameworld.IMazeLocation;
import pdp.aniachar.dungeonmaster.gameworld.IMazeLocationBuilder;
import pdp.aniachar.dungeonmaster.gameworld.MazeLocation;
import pdp.aniachar.dungeonmaster.item.Arrow;
import pdp.aniachar.dungeonmaster.item.Treasure;
import pdp.aniachar.dungeonmaster.item.TreasureType;
import pdp.aniachar.gamekit.Game;
import pdp.aniachar.gamekit.GameWorld;
import pdp.aniachar.gamekit.Location;
import pdp.aniachar.gamekit.WorldBuildStrategy;
import pdp.aniachar.view.IView;
import pdp.aniachar.view.TextView;
/**
 * An interactive driver to demonstrate moving the player through the maze.
 * Here players interact with a 10 X 10 game world.
 */

public class Driver2 {
  private static Game model;
  private static Appendable viewLogger;
  private static Readable in;

  /**
   * Entry point for the driver.
   *
   * @param args Ignored.
   */
  public static void main(String[] args) {
    WorldBuildStrategy builder = new FakeWorldBuilder();
    model = new DungeonGame(builder, "Bran The wise");
    viewLogger = new StringBuilder();
    in = new InputStreamReader(System.in);
    IView view = new TextView(System.out, in, new CommandLineParser());
    Controller controller = new Controller(model, view);
    try {
      controller.start();
    } catch (NoSuchElementException e) {
      //expected exception. Do nothing.
    }

  }

  private static StringReader pickTreasure() {
    return new StringReader(
            "move n\nmove east\npick arrow\nmove south\nmove east\npick treasure\n");
  }

  private static StringReader killMonster() {
    return new StringReader(
            "move n\nmove east\npick arrow\nmove south\nshoot west 1\nshoot west 1\n");
  }

  private static StringReader pickArrow() {
    return new StringReader("move n\nmove east\npick arrow\n");
  }

  private static StringReader killPlayer() {
    return new StringReader(
            new StringBuilder().append("move n\n")
                    .append("move east\n")
                    .append("pick arrow\n")
                    .append("move south\n")
                    .append("move west\n")
                    .append("move south")
                    .toString());
  }

  private static StringReader winGameEnterEndCave() {
    return new StringReader(
            new StringBuilder().append("move n\n")
                    .append("move east\n")
                    .append("pick arrow\n")
                    .append("move south\n")
                    .append("shoot west 1\n")
                    .append("shoot west 1\n")
                    .append("move west\n")
                    .append("move south\n")
                    .append("shoot east 1\n")
                    .append("shoot east 1\n")
                    .append("move east\n")
                    .append("move east\n")
                    .toString());
  }

  private static class FakeWorldBuilder implements WorldBuildStrategy {
    Otyughs otyughsAtSix;
    Otyughs otyughsAtEnd;
    List<IMazeLocation> locations = new ArrayList<>();

    FakeWorldBuilder() {
      buildMaze();
      otyughsAtEnd = new Otyughs(locations.get(8), "ender");
      otyughsAtSix = new Otyughs(locations.get(6), "sixer");
    }

    private void buildMaze() {
      for (int i = 0; i < 3; i++) {
        for (int j = 0; j < 3; j++) {
          if (i == 1 && j == 2) {
            locations.add(new IMazeLocationBuilder(new MutablePair<>(i, j))
                    .addTreasure(new Treasure(TreasureType.DIAMOND)).build());
          } else if (i == 0 && j == 1) {
            locations.add(new IMazeLocationBuilder(new MutablePair<>(i, j))
                    .addArrow(new Arrow()).build());
          } else {
            locations.add(new MazeLocation(new MutablePair<>(i, j)));
          }
        }
      }
      IMazeLocationBuilder.makeAdjacent(
              new MutablePair<>(locations.get(0), locations.get(1)), 3, 3);
      IMazeLocationBuilder.makeAdjacent(
              new MutablePair<>(locations.get(2), locations.get(1)), 3, 3);
      IMazeLocationBuilder.makeAdjacent(
              new MutablePair<>(locations.get(4), locations.get(1)), 3, 3);
      IMazeLocationBuilder.makeAdjacent(
              new MutablePair<>(locations.get(2), locations.get(5)), 3, 3);
      IMazeLocationBuilder.makeAdjacent(
              new MutablePair<>(locations.get(3), locations.get(4)), 3, 3);
      IMazeLocationBuilder.makeAdjacent(
              new MutablePair<>(locations.get(6), locations.get(7)), 3, 3);
      IMazeLocationBuilder.makeAdjacent(
              new MutablePair<>(locations.get(7), locations.get(8)), 3, 3);
      IMazeLocationBuilder.makeAdjacent(
              new MutablePair<>(locations.get(3), locations.get(6)), 3, 3);
      IMazeLocationBuilder.makeAdjacent(
              new MutablePair<>(locations.get(4), locations.get(5)), 3, 3);
      IMazeLocationBuilder.makeAdjacent(
              new MutablePair<>(locations.get(6), locations.get(8)), 3, 3);

    }


    @Override
    public GameWorld buildWorld() {
      return new GameWorld() {
        @Override
        public Location<?> getStartLocation() {
          return locations.get(0);
        }

        @Override
        public Location<?> getEndLocation() {
          return locations.get(8);
        }

        @Override
        public void restart() {
          //Do nothing. This is a mocker.
        }
      };
    }
  }

}
