import org.apache.commons.lang3.tuple.MutablePair;
import org.junit.Before;
import org.junit.Test;

import java.util.List;

import pdp.aniachar.dungeonmaster.action.location.LocationAction;
import pdp.aniachar.dungeonmaster.action.location.PickItemAction;
import pdp.aniachar.dungeonmaster.action.location.PickItemActionBuilder;
import pdp.aniachar.dungeonmaster.character.player.IPlayer;
import pdp.aniachar.dungeonmaster.character.player.PlayerBuilder;
import pdp.aniachar.dungeonmaster.gameworld.IMazeLocation;
import pdp.aniachar.dungeonmaster.gameworld.IMazeLocationBuilder;
import pdp.aniachar.dungeonmaster.gameworld.MazeLocation;
import pdp.aniachar.dungeonmaster.gameworld.RandomMazeBuilder;
import pdp.aniachar.dungeonmaster.item.Arrow;
import pdp.aniachar.dungeonmaster.item.Treasure;
import pdp.aniachar.dungeonmaster.item.TreasureType;
import pdp.aniachar.gamekit.Action;
import pdp.aniachar.gamekit.GameWorld;
import pdp.aniachar.gamekit.Item;
import pdp.aniachar.gamekit.WorldBuildStrategy;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;
import static org.junit.Assert.fail;

/**
 * Tests the functionalities of {@link PickItemAction}.
 * Tests if the functionalities work as defined.
 */

public class PickItemActionTest {

  WorldBuildStrategy strategy;
  GameWorld world;
  IMazeLocation location;
  IPlayer player;

  @Before
  public void setup() {
    strategy = new RandomMazeBuilder(10, 10, false, 1, 100, 0);
    world = strategy.buildWorld();
    location = new IMazeLocationBuilder(new MutablePair<>(1, 1)).addArrow(new Arrow())
            .build();
    player = (IPlayer) new PlayerBuilder(location).setRegister(true).setName("Player 2")
            .createPlayer();
  }

  @Test
  public void creation() {
    try {
      Action<?> pickAction = new PickItemActionBuilder().createPickItemAction();
      fail();
    } catch (IllegalArgumentException e) {
      assertTrue(e.getMessage().contains("null"));
    }
    try {
      Action<?> pickAction = new PickItemActionBuilder().setItemToPick(null).createPickItemAction();
      fail();
    } catch (IllegalArgumentException e) {
      assertTrue(e.getMessage().contains("null"));
    }

    try {
      Action<?> pickAction = new PickItemActionBuilder().setActionLocation(null)
              .createPickItemAction();
      fail();
    } catch (IllegalArgumentException e) {
      assertTrue(e.getMessage().contains("null"));
    }
    try {
      Action<?> pickAction = new PickItemActionBuilder()
              .setActionLocation(new MazeLocation(new MutablePair<>(1, 1)))
              .setItemToPick(new Treasure(TreasureType.DIAMOND))
              .createPickItemAction();
      assertEquals("\nCan pick up DIAMOND here\n", pickAction.describe());
    } catch (IllegalArgumentException e) {
      fail();
    }
  }

  @Test
  public void act() throws NoSuchMethodException {
    Action<?> pickAction = world.getStartLocation().possibleActions()
            .stream().filter(action -> action instanceof PickItemAction).findFirst().orElseThrow();
    int originalNumItems = world.getStartLocation().getItems().size();
    Item item = (Item) pickAction.act().orElseThrow();
    boolean nameIsValid = (item.getName().contains("DIAMOND")
            || item.getName().contains("RUBY") || item.getName().contains("SAFARI")
            || item.getName().contains("ARROW"));
    assertTrue(nameIsValid);
    assertTrue(originalNumItems > world.getStartLocation().getItems().size());
  }

  @Test
  public void pickArrow() throws NoSuchMethodException {
    Action<?> pickArrow = location.possibleActions().stream()
            .filter(action -> action instanceof PickItemAction).findFirst().orElseThrow();
    List<Item> originalItems = location.getItems();
    Item arrow = (Arrow) pickArrow.act().orElseThrow();
    assertEquals(0, location.getMoveAction().size());
  }

  @Test
  public void describe() {
    Action<?> pickAction = world.getStartLocation().possibleActions()
            .stream().filter(action -> action instanceof PickItemAction).findFirst().orElseThrow();
    assertTrue(pickAction.describe().contains("Can pick up"));
  }

  @Test
  public void assignArrowToPlayer() throws NoSuchMethodException {
    LocationAction<?> pickArrow = (LocationAction<?>) location.possibleActions().stream()
            .filter(action -> action instanceof PickItemAction).findFirst().orElseThrow();
    pickArrow.actOn(player);
    assertEquals(4, player.getQuiver().size());
  }

}