import org.junit.Before;
import org.junit.Test;

import java.io.IOException;
import java.io.StringReader;
import java.util.ArrayList;
import java.util.List;
import java.util.NoSuchElementException;

import pdp.aniachar.controller.CommandLineParser;
import pdp.aniachar.controller.Controller;
import pdp.aniachar.controller.IController;
import pdp.aniachar.dungeonmaster.DungeonGame;
import pdp.aniachar.dungeonmaster.gameworld.IMazeLocation;
import pdp.aniachar.gamekit.Game;
import pdp.aniachar.view.TextView;

import static org.junit.Assert.assertTrue;

/**
 * Tests the Controller's behaviour.
 */

public class ControllerTest {

  List<IMazeLocation> locations = new ArrayList<>();
  private IController controller;
  private Game model;
  private TextView view;
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
    view = new TextView(viewLogger, in, new CommandLineParser());
    controller = new Controller(model, view);
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
    view = new TextView(viewLogger, in, new CommandLineParser());
    controller = new Controller(model, view);
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
    view = new TextView(viewLogger, in, new CommandLineParser());
    controller = new Controller(model, view);
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
    view = new TextView(viewLogger, in, new CommandLineParser());
    controller = new Controller(model, view);
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
    view = new TextView(viewLogger, in, new CommandLineParser());
    controller = new Controller(model, view);
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
    view = new TextView(viewLogger, in, new CommandLineParser());
    controller = new Controller(model, view);
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
    view = new TextView(viewLogger, in, new CommandLineParser());
    controller = new Controller(model, view);
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
    view = new TextView(viewLogger, in, new CommandLineParser());
    controller = new Controller(model, view);
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
    view = new TextView(viewLogger, in, new CommandLineParser());
    controller = new Controller(model, view);
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
    view = new TextView(viewLogger, in, new CommandLineParser());
    controller = new Controller(model, view);
    try {
      controller.start();
    } catch (NoSuchElementException e) {
      //expected exception. Do nothing.
    }
    assertTrue(viewLogger.toString().contains("bran"));
    assertTrue(viewLogger.toString().contains("Cannot move"));
    assertTrue(viewLogger.toString().contains("(0,1)"));
  }

  @Test
  public void testValidButNotPossiblePickInput() {
    in = new StringReader("move n\nmove east\npick arrow\npick treasure\n");
    view = new TextView(viewLogger, in, new CommandLineParser());
    controller = new Controller(model, view);
    try {
      controller.start();
    } catch (NoSuchElementException e) {
      //expected exception. Do nothing.
    }
    assertTrue(viewLogger.toString().contains("bran"));
    assertTrue(viewLogger.toString().contains("Cannot move"));
    assertTrue(viewLogger.toString().contains("(0,1)"));
    assertTrue(viewLogger.toString().contains("No such items present."));
  }

  @Test(expected = IllegalStateException.class)
  public void testFailingAppendable() {
    in = new StringReader("move n\nmove east\npick arrow\npick treasure\n");
    view = new TextView(new Appendable() {
      @Override
      public Appendable append(CharSequence csq) throws IOException {
        throw new IOException();
      }

      @Override
      public Appendable append(CharSequence csq, int start, int end) throws IOException {
        throw new IOException();
      }

      @Override
      public Appendable append(char c) throws IOException {
        throw new IOException();
      }
    }, in, new CommandLineParser());
    controller = new Controller(model, view);
    try {
      controller.start();
    } catch (NoSuchElementException e) {
      //expected exception. Do nothing.
    }
    assertTrue(viewLogger.toString().contains("bran"));
    assertTrue(viewLogger.toString().contains("Cannot move"));
    assertTrue(viewLogger.toString().contains("(0,1)"));
    assertTrue(viewLogger.toString().contains("No such items present."));
  }

  @Test
  public void testPickArrowItemInput() {
    in = new StringReader("move n\nmove east\npick arrow\n");
    view = new TextView(viewLogger, in, new CommandLineParser());
    controller = new Controller(model, view);
    try {
      controller.start();
    } catch (NoSuchElementException e) {
      //expected exception. Do nothing.
    }
    assertTrue(viewLogger.toString().contains("bran"));
    assertTrue(viewLogger.toString().contains("Cannot move"));
    assertTrue(viewLogger.toString().contains("(0,1)"));
    assertTrue(viewLogger.toString().contains("3 arrow"));
  }

  @Test
  public void testInvalidActionItemInput() {
    in = new StringReader("move n\nmove east\npick arrow\npick arrow\n");
    view = new TextView(viewLogger, in, new CommandLineParser());
    controller = new Controller(model, view);
    try {
      controller.start();
    } catch (NoSuchElementException e) {
      //expected exception. Do nothing.
    }
    assertTrue(viewLogger.toString().contains("bran"));
    assertTrue(viewLogger.toString().contains("Cannot move"));
    assertTrue(viewLogger.toString().contains("(0,1)"));
    assertTrue(viewLogger.toString().contains("4 arrow"));
    assertTrue(viewLogger.toString().contains("No such items present"));
  }

  @Test
  public void testInvalidPickItemInput() {
    in = new StringReader("move n\nmove east\npick arrow\npick t\n");
    view = new TextView(viewLogger, in, new CommandLineParser());
    controller = new Controller(model, view);
    try {
      controller.start();
    } catch (NoSuchElementException e) {
      //expected exception. Do nothing.
    }
    assertTrue(viewLogger.toString().contains("bran"));
    assertTrue(viewLogger.toString().contains("Cannot move"));
    assertTrue(viewLogger.toString().contains("(0,1)"));
    assertTrue(viewLogger.toString().contains("4 arrow"));
    assertTrue(viewLogger.toString().contains("No such items present"));
  }

  @Test
  public void testShootArrowInput() {
    in = new StringReader("move n\nmove east\npick arrow\nmove south\nshoot west 1\n");
    view = new TextView(viewLogger, in, new CommandLineParser());
    controller = new Controller(model, view);
    try {
      controller.start();
    } catch (NoSuchElementException e) {
      //expected exception. Do nothing.
    }
    assertTrue(viewLogger.toString().contains("bran"));
    assertTrue(viewLogger.toString().contains("Cannot move"));
    assertTrue(viewLogger.toString().contains("(1,1)"));
    assertTrue(viewLogger.toString().contains("3 - ARROW"));
    assertTrue(viewLogger.toString().contains("4 - ARROW"));
    assertTrue(viewLogger.toString().contains("Otyughs hit"));
    assertTrue(viewLogger.toString().contains("weak smell"));
  }

  @Test
  public void testInvalidDirectionShootArrowInput() {
    in = new StringReader("move n\nmove east\npick arrow\nmove south\nshoot asdasd 1\n");
    view = new TextView(viewLogger, in, new CommandLineParser());
    controller = new Controller(model, view);
    try {
      controller.start();
    } catch (NoSuchElementException e) {
      //expected exception. Do nothing.
    }
    assertTrue(viewLogger.toString().contains("bran"));
    assertTrue(viewLogger.toString().contains("Cannot move"));
    assertTrue(viewLogger.toString().contains("(1,1)"));
    assertTrue(viewLogger.toString().contains("4 - ARROW"));
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
    view = new TextView(viewLogger, in, new CommandLineParser());
    controller = new Controller(model, view);
    try {
      controller.start();
    } catch (NoSuchElementException e) {
      //expected exception. Do nothing.
    }
    assertTrue(viewLogger.toString().contains("bran"));
    assertTrue(viewLogger.toString().contains("Cannot move"));
    assertTrue(viewLogger.toString().contains("(1,1)"));
    assertTrue(viewLogger.toString().contains("4 - ARROW"));
    assertTrue(viewLogger.toString().contains("3 - ARROW"));
    assertTrue(viewLogger.toString().contains("2 - ARROW"));
    assertTrue(viewLogger.toString().contains("1 - ARROW"));
    assertTrue(viewLogger.toString().contains("Player has no more arrows."));
    assertTrue(viewLogger.toString().contains("weak smell"));
  }

  @Test
  public void testInvalidDistanceShootArrowInput() {
    in = new StringReader("move n\nmove east\npick arrow\nmove south\nshoot north asdas\n");
    view = new TextView(viewLogger, in, new CommandLineParser());
    controller = new Controller(model, view);
    try {
      controller.start();
    } catch (NoSuchElementException e) {
      //expected exception. Do nothing.
    }
    assertTrue(viewLogger.toString().contains("bran"));
    assertTrue(viewLogger.toString().contains("Cannot move"));
    assertTrue(viewLogger.toString().contains("(1,1)"));
    assertTrue(viewLogger.toString()
            .contains("Not a valid shoot distance. Distance must be integer"));
    assertTrue(viewLogger.toString().contains("weak smell"));
  }

  @Test
  public void testNegativeDistanceShootArrowInput() {
    in = new StringReader("move n\nmove east\npick arrow\nmove south\nshoot north -1\n");
    view = new TextView(viewLogger, in, new CommandLineParser());
    controller = new Controller(model, view);
    try {
      controller.start();
    } catch (NoSuchElementException e) {
      //expected exception. Do nothing.
    }
    assertTrue(viewLogger.toString().contains("bran"));
    assertTrue(viewLogger.toString().contains("Cannot move"));
    assertTrue(viewLogger.toString().contains("(1,1)"));
    assertTrue(viewLogger.toString()
            .contains("Distance cannot be zero or negative or greater than 5"));
    assertTrue(viewLogger.toString().contains("weak smell"));
  }

  @Test
  public void testLargeDistanceShootArrowInput() {
    in = new StringReader("move n\nmove east\npick arrow\nmove south\nshoot north 5000\n");
    view = new TextView(viewLogger, in, new CommandLineParser());
    controller = new Controller(model, view);
    try {
      controller.start();
    } catch (NoSuchElementException e) {
      //expected exception. Do nothing.
    }
    assertTrue(viewLogger.toString().contains("bran"));
    assertTrue(viewLogger.toString().contains("Cannot move"));
    assertTrue(viewLogger.toString().contains("(1,1)"));
    assertTrue(viewLogger.toString().contains("3 - ARROW"));
    assertTrue(viewLogger.toString()
            .contains("Distance cannot be zero or negative or greater than 5"));
    assertTrue(viewLogger.toString().contains("weak smell"));
  }

  @Test
  public void testStrongSmellInput() {
    in = new StringReader("move n\nmove east\npick arrow\nmove south\nshoot west 1\nmove west\n");
    view = new TextView(viewLogger, in, new CommandLineParser());
    controller = new Controller(model, view);
    try {
      controller.start();
    } catch (NoSuchElementException e) {
      //expected exception. Do nothing.
    }
    assertTrue(viewLogger.toString().contains("bran"));
    assertTrue(viewLogger.toString().contains("Cannot move"));
    assertTrue(viewLogger.toString().contains("(1,1)"));
    assertTrue(viewLogger.toString().contains("4 - ARROW"));
    assertTrue(viewLogger.toString().contains("3 - ARROW"));
    assertTrue(viewLogger.toString().contains("Otyughs hit"));
    assertTrue(viewLogger.toString().contains("weak smell"));
    assertTrue(viewLogger.toString().contains("strong smell"));
  }


  @Test
  public void testShootArrowAndKillInput() {
    in = new StringReader(
            "move n\nmove east\npick arrow\nmove south\nshoot west 1\nshoot west 1\n");
    view = new TextView(viewLogger, in, new CommandLineParser());
    controller = new Controller(model, view);
    try {
      controller.start();
    } catch (NoSuchElementException e) {
      //expected exception. Do nothing.
    }
    assertTrue(viewLogger.toString().contains("bran"));
    assertTrue(viewLogger.toString().contains("Cannot move"));
    assertTrue(viewLogger.toString().contains("(0,1)"));
    assertTrue(viewLogger.toString().contains("3 - ARROW"));
    assertTrue(viewLogger.toString().contains("2 - ARROW"));
    assertTrue(viewLogger.toString().contains("Otyughs hit"));
    assertTrue(viewLogger.toString().contains("Monster killed"));

  }

  @Test
  public void testPickTreasureInput() {
    in = new StringReader(
            "move n\nmove east\npick arrow\nmove south\nmove east\npick treasure\n");
    view = new TextView(viewLogger, in, new CommandLineParser());
    controller = new Controller(model, view);
    try {
      controller.start();
    } catch (NoSuchElementException e) {
      //expected exception. Do nothing.
    }
    assertTrue(viewLogger.toString().contains("bran"));
    assertTrue(viewLogger.toString().contains("Cannot move"));
    assertTrue(viewLogger.toString().contains("(0,1)"));
    assertTrue(viewLogger.toString().contains("(1,2)"));
    assertTrue(viewLogger.toString().contains("1 - DIAMOND"));
  }

  @Test
  public void testKillingPlayerInput() {
    in = new StringReader(
            new StringBuilder().append("move n\n")
                    .append("move east\n")
                    .append("pick arrow\n")
                    .append("move south\n")
                    .append("move west\n")
                    .append("move south\n")
                    .append("move south")
                    .toString());
    view = new TextView(viewLogger, in, new CommandLineParser());
    controller = new Controller(model, view);
    try {
      controller.start();
    } catch (NoSuchElementException e) {
      //expected exception. Do nothing.
    }
    assertTrue(viewLogger.toString().contains("bran"));
    assertTrue(viewLogger.toString().contains("Cannot move"));
    assertTrue(viewLogger.toString().contains("(1,1)"));
    assertTrue(viewLogger.toString().contains("weak smell"));
    assertTrue(viewLogger.toString().contains("strong smell"));
    assertTrue(viewLogger.toString().contains("Player is dead"));
    assertTrue(model.isGameOver());
  }

  @Test
  public void testShootArrowAndKillAndEnterLocationInput() {
    in = new StringReader(
            "move n\nmove east\npick arrow\nmove south\nshoot west 1\nshoot west 1" +
                    "\nmove west\nmove south\n");
    view = new TextView(viewLogger, in, new CommandLineParser());
    controller = new Controller(model, view);
    try {
      controller.start();
    } catch (NoSuchElementException e) {
      //expected exception. Do nothing.
    }
    assertTrue(viewLogger.toString().contains("bran"));
    assertTrue(viewLogger.toString().contains("Cannot move"));
    assertTrue(viewLogger.toString().contains("(0,1)"));
    assertTrue(viewLogger.toString().contains("2 - ARROW"));
    assertTrue(viewLogger.toString().contains("hit"));
    assertTrue(viewLogger.toString().contains("Monster killed"));
    assertTrue(viewLogger.toString().contains("(1,1)"));
    assertTrue(viewLogger.toString().contains("(1,0)"));
    assertTrue(viewLogger.toString().contains("(2,0)"));
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
    view = new TextView(viewLogger, in, new CommandLineParser());
    controller = new Controller(model, view);
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
    view = new TextView(viewLogger, in, new CommandLineParser());
    controller = new Controller(model, view);
    try {
      controller.start();
    } catch (NoSuchElementException e) {
      //expected exception. Do nothing.
    }
    assertTrue(viewLogger.toString().contains("bran"));
    assertTrue(viewLogger.toString().contains("Cannot move"));
    assertTrue(viewLogger.toString().contains("(0,1)"));
    assertTrue(viewLogger.toString().contains("3 ARROW"));
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


}