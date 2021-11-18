import org.junit.Before;
import org.junit.Test;

import pdp.aniachar.dungeonmaster.action.game.GameAction;
import pdp.aniachar.dungeonmaster.action.location.MoveAction;
import pdp.aniachar.dungeonmaster.gameworld.RandomMazeBuilder;
import pdp.aniachar.dungeonmaster.character.player.PlayerBuilder;
import pdp.aniachar.gamekit.Action;
import pdp.aniachar.gamekit.Character;
import pdp.aniachar.gamekit.GameWorld;
import pdp.aniachar.gamekit.WorldBuildStrategy;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;
import static org.junit.Assert.fail;

/**
 * Tests the functionalities of {@link GameAction}.
 * Tests if the functionalities work as defined.
 */

public class PlayerLocationActionTest {
  WorldBuildStrategy strategy;
  GameWorld world;
  Character player;

  @Before
  public void setup() {
    strategy = new RandomMazeBuilder(10, 10, false, 1, 100, 1);
    world = strategy.buildWorld();
    player = new PlayerBuilder(world.getStartLocation()).setName("foo").createPlayer();
  }

  @Test
  public void creation() {
    try {
      new GameAction(null, null);
      fail();
    } catch (IllegalArgumentException e) {
      assertTrue(e.getMessage().contains("null"));
    }

    try {
      MoveAction moveAction = (MoveAction) world.getStartLocation()
              .possibleActions()
              .stream().filter(action -> action instanceof MoveAction)
              .findFirst().orElseThrow();
      Action<?> playerLocationAction = new GameAction(player, moveAction);
      assertTrue(playerLocationAction.describe().contains("foo"));
    } catch (Exception e) {
      fail();
    }

  }

  @Test
  public void act() throws NoSuchMethodException {
    MoveAction moveAction = (MoveAction) world.getStartLocation()
            .possibleActions()
            .stream().filter(action -> action instanceof MoveAction)
            .findFirst().orElseThrow();
    Action<?> playerLocationAction = new GameAction(player, moveAction);
    var newLocation = moveAction.act().orElseThrow();
    assertTrue((boolean) playerLocationAction.act().orElseThrow());
    assertEquals(newLocation, player.getCurrentLocation());
  }

  @Test
  public void describe() {
    MoveAction moveAction = (MoveAction) world.getStartLocation()
            .possibleActions()
            .stream().filter(action -> action instanceof MoveAction)
            .findFirst().orElseThrow();
    Action<?> playerLocationAction = new GameAction(player, moveAction);
    assertTrue(playerLocationAction.describe().contains("foo"));
  }
}