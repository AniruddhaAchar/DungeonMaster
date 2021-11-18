import org.apache.commons.lang3.tuple.MutablePair;
import org.junit.Before;
import org.junit.Test;

import java.util.List;

import pdp.aniachar.dungeonmaster.action.location.PickItemAction;
import pdp.aniachar.dungeonmaster.gameworld.MazeLocation;
import pdp.aniachar.dungeonmaster.gameworld.RandomMazeBuilder;
import pdp.aniachar.dungeonmaster.item.Treasure;
import pdp.aniachar.dungeonmaster.item.TreasureType;
import pdp.aniachar.gamekit.Action;
import pdp.aniachar.gamekit.GameWorld;
import pdp.aniachar.gamekit.Item;
import pdp.aniachar.gamekit.Location;
import pdp.aniachar.gamekit.WorldBuildStrategy;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;
import static org.junit.Assert.fail;

/**
 * Tests the behaviour of {@link MazeLocation}.
 * Tests if the functionalities work as defined.
 */

public class MazeLocationTest {
  Location<?> location;
  List<Item> items;
  GameWorld world;
  WorldBuildStrategy strategy;

  @Before
  public void setup() {
    items = List.of(new Treasure(TreasureType.RUBY), new Treasure(TreasureType.DIAMOND),
            new Treasure(TreasureType.SAPPHIRE));
    location = new MazeLocation(new MutablePair<>(0, 0));

    try {
      strategy = new RandomMazeBuilder(10, 10, false, 1, 100, 0);
    } catch (IllegalStateException e) {
      if (e.getMessage().contains("caves")) {
        //try to build the maze again.
        strategy = new RandomMazeBuilder(10, 10, false,
                1, 100, 0);
      }
    }
    world = strategy.buildWorld();
  }

  @Test
  public void creation() {
    try {
      new MazeLocation(null);
      fail();
    } catch (IllegalArgumentException e) {
      assertTrue(e.getMessage().contains("null"));
    }
  }

  @Test
  public void getItems() {
    Location<?> startLoc = world.getStartLocation();
    assertTrue(startLoc.getItems().size() > 0);
  }


  @Test
  public void possibleActions() {
    Location<?> startLoc = world.getStartLocation();
    assertTrue(startLoc.possibleActions().size() > 0);
  }

  @Test
  public void testTakeAction() throws NoSuchMethodException {
    Location<?> startLoc = world.getStartLocation();
    int originalNumItems = startLoc.getItems().size();
    int originalNumActions = startLoc.possibleActions().size();
    Action<?> takeAction = startLoc.possibleActions()
            .stream().filter(action -> action instanceof PickItemAction).findFirst().orElseThrow();
    var item = (Item) takeAction.act().orElseThrow();
    assertTrue(originalNumItems > startLoc.getItems().size());
    assertTrue(originalNumActions > startLoc.possibleActions().size());

  }

  @Test
  public void removeItem() {
    Location<?> startLoc = world.getStartLocation();
    var origNumItems = startLoc.getItems().size();
    Item item = startLoc.getItems().get(0);
    startLoc.removeItem(item);
    assertEquals(origNumItems - 1, startLoc.getItems().size());
  }

}