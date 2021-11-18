import org.apache.commons.lang3.tuple.MutablePair;
import org.junit.Before;
import org.junit.Test;

import java.io.StringReader;
import java.util.ArrayList;
import java.util.List;
import java.util.NoSuchElementException;

import pdp.aniachar.controller.IController;
import pdp.aniachar.controller.TextController;
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

import static org.junit.Assert.assertTrue;

/**
 * Tests the Controller's behaviour.
 */

public class TextControllerTest {

  List<IMazeLocation> locations = new ArrayList<>();
  private IController controller;
  private Game model;
  private IView<String, String> view;
  private StringBuilder viewLogger;
  private Readable in;

  @Before
  public void setup() {
    viewLogger = new StringBuilder();

    model = new DungeonGame(new FakeWorldBuilder(), "bran");

  }

  @Test
  public void testInvalidInput() {
    in = new StringReader("asdasd");
    view = new TextView(viewLogger, in);
    controller = new TextController(model, view);
    try {
      controller.start();
    } catch (NoSuchElementException e) {
      //expected exception. Do nothing.
    }
    assertTrue(viewLogger.toString().contains("bran"));
    assertTrue(viewLogger.toString().contains("Not a valid command"));
  }

  @Test
  public void testHelpInput() {
    in = new StringReader("help");
    view = new TextView(viewLogger, in);
    controller = new TextController(model, view);
    try {
      controller.start();
    } catch (NoSuchElementException e) {
      //expected exception. Do nothing.
    }
    assertTrue(viewLogger.toString().contains(new StringBuilder()
            .append("The game uses verb-[noun]+[qualifier] syntax for the command.\n")
            .append("Some verbs may have nouns while some verbs may have qualifiers as well. \n")
            .append("Valid commands:\n")
            .append("help\n")
            .append("\tMove command:\n")
            .append("move <direction>\n")
            .append("\tValid directions are:\n")
            .append("north\n")
            .append("south\n")
            .append("east\n")
            .append("west\n")
            .append("\tTo move east, you can enter:\n")
            .append("\t\t`move east`\n")
            .append("\tPick command:\n")
            .append("pick <item>\n")
            .append("\tValid items:\n")
            .append("treasure\n")
            .append("arrow\n")
            .append("\tValid pick command:\n")
            .append("\t\t`pick arrow`\n")
            .append("\tShoot arrow command:\n")
            .append("shoot <direction> <distance>\n")
            .append("\tValid shoot command:\n")
            .append("\t\t`shoot north 3`\n").append("\tValid directions are the same as above.")
            .append(" Distance should be a positive integer.\n")
            .append("\n").append("All these commands have a shorthand.")
            .append(" Just use the first letter of the command.\n")
            .toString()));
  }

  @Test
  public void testInvalidDirectionInput() {
    in = new StringReader("move asas");
    view = new TextView(viewLogger, in);
    controller = new TextController(model, view);
    try {
      controller.start();
    } catch (NoSuchElementException e) {
      //expected exception. Do nothing.
    }
    assertTrue(viewLogger.toString().contains("bran"));
    assertTrue(viewLogger.toString().contains("Not a valid direction"));
  }

  @Test
  public void testInvalidMoveButValidDirectionInput() {
    in = new StringReader("asdas north");
    view = new TextView(viewLogger, in);
    controller = new TextController(model, view);
    try {
      controller.start();
    } catch (NoSuchElementException e) {
      //expected exception. Do nothing.
    }
    assertTrue(viewLogger.toString().contains("bran"));
    assertTrue(viewLogger.toString().contains("Not a valid command"));
  }

  @Test
  public void testInvalidCommandsInput() {
    in = new StringReader("asdas asas\nasdasdas asdasd\n");
    view = new TextView(viewLogger, in);
    controller = new TextController(model, view);
    try {
      controller.start();
    } catch (NoSuchElementException e) {
      //expected exception. Do nothing.
    }
    assertTrue(viewLogger.toString().contains("bran"));
    assertTrue(viewLogger.toString().contains("Not a valid command"));
  }


  @Test
  public void testValidMoveButNotPossibleInput() {
    in = new StringReader("move north");
    view = new TextView(viewLogger, in);
    controller = new TextController(model, view);
    try {
      controller.start();
    } catch (NoSuchElementException e) {
      //expected exception. Do nothing.
    }
    assertTrue(viewLogger.toString().contains("bran"));
    assertTrue(viewLogger.toString().contains("Cannot move"));
  }

  @Test
  public void testValidMoveButNotPossibleAliasInput() {
    in = new StringReader("m n");
    view = new TextView(viewLogger, in);
    controller = new TextController(model, view);
    try {
      controller.start();
    } catch (NoSuchElementException e) {
      //expected exception. Do nothing.
    }
    assertTrue(viewLogger.toString().contains("bran"));
    assertTrue(viewLogger.toString().contains("Cannot move"));
  }

  @Test
  public void testValidMoveButNotPossibleAliasVerbInput() {
    in = new StringReader("m north");
    view = new TextView(viewLogger, in);
    controller = new TextController(model, view);
    try {
      controller.start();
    } catch (NoSuchElementException e) {
      //expected exception. Do nothing.
    }
    assertTrue(viewLogger.toString().contains("bran"));
    assertTrue(viewLogger.toString().contains("Cannot move"));
  }

  @Test
  public void testValidMoveButNotPossibleAliasNounInput() {
    in = new StringReader("move n");
    view = new TextView(viewLogger, in);
    controller = new TextController(model, view);
    try {
      controller.start();
    } catch (NoSuchElementException e) {
      //expected exception. Do nothing.
    }
    assertTrue(viewLogger.toString().contains("bran"));
    assertTrue(viewLogger.toString().contains("Cannot move"));
  }

  @Test
  public void testValidMoveAndCanMoveInput() {
    in = new StringReader("move n\nmove east\n");
    view = new TextView(viewLogger, in);
    controller = new TextController(model, view);
    try {
      controller.start();
    } catch (NoSuchElementException e) {
      //expected exception. Do nothing.
    }
    assertTrue(viewLogger.toString().contains("bran"));
    assertTrue(viewLogger.toString().contains("Cannot move"));
    assertTrue(viewLogger.toString().contains("Player bran is at Location (0,1)"));
  }

  @Test
  public void testPickArrowItemInput() {
    in = new StringReader("move n\nmove east\npick arrow\n");
    view = new TextView(viewLogger, in);
    controller = new TextController(model, view);
    try {
      controller.start();
    } catch (NoSuchElementException e) {
      //expected exception. Do nothing.
    }
    assertTrue(viewLogger.toString().contains("bran"));
    assertTrue(viewLogger.toString().contains("Cannot move"));
    assertTrue(viewLogger.toString().contains("Player bran is at Location (0,1)"));
    assertTrue(viewLogger.toString().contains("3 arrow"));
  }

  @Test
  public void testInvalidActionItemInput() {
    in = new StringReader("move n\nmove east\npick arrow\npick arrow\n");
    view = new TextView(viewLogger, in);
    controller = new TextController(model, view);
    try {
      controller.start();
    } catch (NoSuchElementException e) {
      //expected exception. Do nothing.
    }
    assertTrue(viewLogger.toString().contains("bran"));
    assertTrue(viewLogger.toString().contains("Cannot move"));
    assertTrue(viewLogger.toString().contains("Player bran is at Location (0,1)"));
    assertTrue(viewLogger.toString().contains("4 arrow"));
    assertTrue(viewLogger.toString().contains("No such items present"));
  }

  @Test
  public void testInvalidPickItemInput() {
    in = new StringReader("move n\nmove east\npick arrow\npick t\n");
    view = new TextView(viewLogger, in);
    controller = new TextController(model, view);
    try {
      controller.start();
    } catch (NoSuchElementException e) {
      //expected exception. Do nothing.
    }
    assertTrue(viewLogger.toString().contains("bran"));
    assertTrue(viewLogger.toString().contains("Cannot move"));
    assertTrue(viewLogger.toString().contains("Player bran is at Location (0,1)"));
    assertTrue(viewLogger.toString().contains("4 arrow"));
    assertTrue(viewLogger.toString().contains("No such items present"));
  }

  @Test
  public void testShootArrowInput() {
    in = new StringReader("move n\nmove east\npick arrow\nmove south\nshoot west 1\n");
    view = new TextView(viewLogger, in);
    controller = new TextController(model, view);
    try {
      controller.start();
    } catch (NoSuchElementException e) {
      //expected exception. Do nothing.
    }
    assertTrue(viewLogger.toString().contains("bran"));
    assertTrue(viewLogger.toString().contains("Cannot move"));
    assertTrue(viewLogger.toString().contains("Player bran is at Location (1,1)"));
    assertTrue(viewLogger.toString().contains("3 arrow"));
    assertTrue(viewLogger.toString().contains("4 arrow"));
    assertTrue(viewLogger.toString().contains("Monster hit at at:"));
    assertTrue(viewLogger.toString().contains("weak smell"));
  }

  @Test
  public void testInvalidDirectionShootArrowInput() {
    in = new StringReader("move n\nmove east\npick arrow\nmove south\nshoot asdasd 1\n");
    view = new TextView(viewLogger, in);
    controller = new TextController(model, view);
    try {
      controller.start();
    } catch (NoSuchElementException e) {
      //expected exception. Do nothing.
    }
    assertTrue(viewLogger.toString().contains("bran"));
    assertTrue(viewLogger.toString().contains("Cannot move"));
    assertTrue(viewLogger.toString().contains("Player bran is at Location (1,1)"));
    assertTrue(viewLogger.toString().contains("4 arrow"));
    assertTrue(viewLogger.toString().contains("Not a valid direction"));
    assertTrue(viewLogger.toString().contains("weak smell"));
  }


  @Test
  public void testShootArrowFromEmptyQuiverInput() {
    in = new StringReader(new StringBuilder().append("move n\n")
            .append("move east\n")
            .append("pick arrow\n")
            .append("move south\n")
            .append("shoot west 1")
            .append("\n")
            .append("shoot west 1\n")
            .append("shoot west 1\n")
            .append("shoot west 1\n")
            .append("shoot west 1\n")
            .toString());
    view = new TextView(viewLogger, in);
    controller = new TextController(model, view);
    try {
      controller.start();
    } catch (NoSuchElementException e) {
      //expected exception. Do nothing.
    }
    assertTrue(viewLogger.toString().contains("bran"));
    assertTrue(viewLogger.toString().contains("Cannot move"));
    assertTrue(viewLogger.toString().contains("Player bran is at Location (1,1)"));
    assertTrue(viewLogger.toString().contains("4 arrow"));
    assertTrue(viewLogger.toString().contains("3 arrow"));
    assertTrue(viewLogger.toString().contains("2 arrow"));
    assertTrue(viewLogger.toString().contains("1 arrow"));
    assertTrue(viewLogger.toString().contains("0 arrow"));
    assertTrue(viewLogger.toString().contains("Player has no more arrows."));
    assertTrue(viewLogger.toString().contains("weak smell"));
  }

  @Test
  public void testInvalidDistanceShootArrowInput() {
    in = new StringReader("move n\nmove east\npick arrow\nmove south\nshoot north asdas\n");
    view = new TextView(viewLogger, in);
    controller = new TextController(model, view);
    try {
      controller.start();
    } catch (NoSuchElementException e) {
      //expected exception. Do nothing.
    }
    assertTrue(viewLogger.toString().contains("bran"));
    assertTrue(viewLogger.toString().contains("Cannot move"));
    assertTrue(viewLogger.toString().contains("Player bran is at Location (1,1)"));
    assertTrue(viewLogger.toString()
            .contains("Not a valid shoot distance. Distance must be integer"));
    assertTrue(viewLogger.toString().contains("weak smell"));
  }

  @Test
  public void testNegativeDistanceShootArrowInput() {
    in = new StringReader("move n\nmove east\npick arrow\nmove south\nshoot north -1\n");
    view = new TextView(viewLogger, in);
    controller = new TextController(model, view);
    try {
      controller.start();
    } catch (NoSuchElementException e) {
      //expected exception. Do nothing.
    }
    assertTrue(viewLogger.toString().contains("bran"));
    assertTrue(viewLogger.toString().contains("Cannot move"));
    assertTrue(viewLogger.toString().contains("Player bran is at Location (1,1)"));
    assertTrue(viewLogger.toString()
            .contains("Distance cannot be zero or negative or greater than 5"));
    assertTrue(viewLogger.toString().contains("weak smell"));
  }

  @Test
  public void testLargeDistanceShootArrowInput() {
    in = new StringReader("move n\nmove east\npick arrow\nmove south\nshoot north 5000\n");
    view = new TextView(viewLogger, in);
    controller = new TextController(model, view);
    try {
      controller.start();
    } catch (NoSuchElementException e) {
      //expected exception. Do nothing.
    }
    assertTrue(viewLogger.toString().contains("bran"));
    assertTrue(viewLogger.toString().contains("Cannot move"));
    assertTrue(viewLogger.toString().contains("Player bran is at Location (1,1)"));
    assertTrue(viewLogger.toString().contains("3 arrow"));
    assertTrue(viewLogger.toString()
            .contains("Distance cannot be zero or negative or greater than 5"));
    assertTrue(viewLogger.toString().contains("weak smell"));
  }

  @Test
  public void testStrongSmellInput() {
    in = new StringReader("move n\nmove east\npick arrow\nmove south\nshoot west 1\nmove west\n");
    view = new TextView(viewLogger, in);
    controller = new TextController(model, view);
    try {
      controller.start();
    } catch (NoSuchElementException e) {
      //expected exception. Do nothing.
    }
    assertTrue(viewLogger.toString().contains("bran"));
    assertTrue(viewLogger.toString().contains("Cannot move"));
    assertTrue(viewLogger.toString().contains("Player bran is at Location (1,1)"));
    assertTrue(viewLogger.toString().contains("4 arrow"));
    assertTrue(viewLogger.toString().contains("3 arrow"));
    assertTrue(viewLogger.toString().contains("Monster hit at at:"));
    assertTrue(viewLogger.toString().contains("weak smell"));
    assertTrue(viewLogger.toString().contains("strong smell"));
  }


  @Test
  public void testShootArrowAndKillInput() {
    in = new StringReader(
            "move n\nmove east\npick arrow\nmove south\nshoot west 1\nshoot west 1\n");
    view = new TextView(viewLogger, in);
    controller = new TextController(model, view);
    try {
      controller.start();
    } catch (NoSuchElementException e) {
      //expected exception. Do nothing.
    }
    assertTrue(viewLogger.toString().contains("bran"));
    assertTrue(viewLogger.toString().contains("Cannot move"));
    assertTrue(viewLogger.toString().contains("Player bran is at Location (0,1)"));
    assertTrue(viewLogger.toString().contains("3 arrow"));
    assertTrue(viewLogger.toString().contains("2 arrow"));
    assertTrue(viewLogger.toString().contains("4 arrow"));
    assertTrue(viewLogger.toString().contains("Monster hit at at:"));
    assertTrue(viewLogger.toString().contains("Monster killed at at:"));

  }

  @Test
  public void testPickTreasureInput() {
    in = new StringReader(
            "move n\nmove east\npick arrow\nmove south\nmove east\npick treasure\n");
    view = new TextView(viewLogger, in);
    controller = new TextController(model, view);
    try {
      controller.start();
    } catch (NoSuchElementException e) {
      //expected exception. Do nothing.
    }
    assertTrue(viewLogger.toString().contains("bran"));
    assertTrue(viewLogger.toString().contains("Cannot move"));
    assertTrue(viewLogger.toString().contains("Player bran is at Location (0,1)"));
    assertTrue(viewLogger.toString().contains("Player bran is at Location (1,2)"));
    assertTrue(viewLogger.toString().contains("1 treasure"));
  }

  @Test
  public void testKillingPlayerInput() {
    in = new StringReader(
            new StringBuilder().append("move n\n")
                    .append("move east\n")
                    .append("pick arrow\n")
                    .append("move south\n")
                    .append("move west\n")
                    .append("move south")
                    .toString());
    view = new TextView(viewLogger, in);
    controller = new TextController(model, view);
    try {
      controller.start();
    } catch (NoSuchElementException e) {
      //expected exception. Do nothing.
    }
    assertTrue(viewLogger.toString().contains("bran"));
    assertTrue(viewLogger.toString().contains("Cannot move"));
    assertTrue(viewLogger.toString().contains("Player bran is at Location (1,1)"));
    assertTrue(viewLogger.toString().contains("weak smell"));
    assertTrue(viewLogger.toString().contains("strong smell"));
    assertTrue(viewLogger.toString().contains("Player died after entering a cave with a monster!"));
    assertTrue(model.isGameOver());
  }

  @Test
  public void testShootArrowAndKillAndEnterLocationInput() {
    in = new StringReader(
            "move n\nmove east\npick arrow\nmove south\nshoot west 1\nshoot west 1" +
                    "\nmove west\nmove south\n");
    view = new TextView(viewLogger, in);
    controller = new TextController(model, view);
    try {
      controller.start();
    } catch (NoSuchElementException e) {
      //expected exception. Do nothing.
    }
    assertTrue(viewLogger.toString().contains("bran"));
    assertTrue(viewLogger.toString().contains("Cannot move"));
    assertTrue(viewLogger.toString().contains("Player bran is at Location (0,1)"));
    assertTrue(viewLogger.toString().contains("2 arrow"));
    assertTrue(viewLogger.toString().contains("Monster hit at at:"));
    assertTrue(viewLogger.toString().contains("Monster killed at at:"));
    assertTrue(viewLogger.toString().contains("Player bran is at Location (1,1)"));
    assertTrue(viewLogger.toString().contains("Player bran is at Location (1,0)"));
    assertTrue(viewLogger.toString().contains("Player bran is at Location (2,0)"));
  }

  @Test
  public void testShootArrowAndKillLastMonsterGameInput() {
    in = new StringReader(
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
                    .toString());
    view = new TextView(viewLogger, in);
    controller = new TextController(model, view);
    try {
      controller.start();
    } catch (NoSuchElementException e) {
      //expected exception. Do nothing.
    }
    assertTrue(viewLogger.toString().contains("bran"));
    assertTrue(viewLogger.toString().contains("Cannot move"));
    assertTrue(viewLogger.toString().contains("Player bran is at Location (0,1)"));
    assertTrue(viewLogger.toString().contains("Monster hit at at: Location (2,0)"));
    assertTrue(viewLogger.toString().contains("Monster killed at at: Location (2,0)"));
    assertTrue(viewLogger.toString().contains("Player bran is at Location (1,1)"));
    assertTrue(viewLogger.toString().contains("Player bran is at Location (1,0)"));
    assertTrue(viewLogger.toString().contains("Player bran is at Location (2,0)"));
    assertTrue(viewLogger.toString().contains("Monster hit at at: Location (2,2)"));
    assertTrue(viewLogger.toString().contains("Monster killed at at: Location (2,2)"));
  }

  @Test
  public void testShootArrowKillLastMonsterAndWinGameInput() {
    in = new StringReader(
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
    view = new TextView(viewLogger, in);
    controller = new TextController(model, view);
    try {
      controller.start();
    } catch (NoSuchElementException e) {
      //expected exception. Do nothing.
    }
    assertTrue(viewLogger.toString().contains("bran"));
    assertTrue(viewLogger.toString().contains("Cannot move"));
    assertTrue(viewLogger.toString().contains("Player bran is at Location (0,1)"));
    assertTrue(viewLogger.toString().contains("3 arrow"));
    assertTrue(viewLogger.toString().contains("Monster hit at at: Location (2,0)"));
    assertTrue(viewLogger.toString().contains("Monster killed at at: Location (2,0)"));
    assertTrue(viewLogger.toString().contains("Player bran is at Location (1,1)"));
    assertTrue(viewLogger.toString().contains("Player bran is at Location (1,0)"));
    assertTrue(viewLogger.toString().contains("Player bran is at Location (2,0)"));
    assertTrue(viewLogger.toString().contains("Monster hit at at: Location (2,2)"));
    assertTrue(viewLogger.toString().contains("Monster killed at at: Location (2,2)"));
    assertTrue(viewLogger.toString().contains("Player bran is at Location (2,1)"));
    assertTrue(model.isGameOver());
    assertTrue(viewLogger.toString().contains("0 arrow"));
    assertTrue(viewLogger.toString().contains("Player entered end cave and has won the game!"));
  }



  private class FakeWorldBuilder implements WorldBuildStrategy {
    Otyughs otyughsAtSix;
    Otyughs otyughsAtEnd;

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
      };
    }
  }
}