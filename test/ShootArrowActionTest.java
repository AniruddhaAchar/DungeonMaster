import com.google.common.eventbus.Subscribe;

import org.apache.commons.lang3.tuple.MutablePair;
import org.apache.commons.lang3.tuple.Pair;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import java.util.ArrayList;
import java.util.List;

import pdp.aniachar.dungeonmaster.comm.EventContainer;
import pdp.aniachar.dungeonmaster.comm.ArrowHitEvent;
import pdp.aniachar.dungeonmaster.gameworld.IMazeLocation;
import pdp.aniachar.dungeonmaster.gameworld.IMazeLocationBuilder;
import pdp.aniachar.dungeonmaster.gameworld.MazeLocation;
import pdp.aniachar.dungeonmaster.item.Arrow;
import pdp.aniachar.dungeonmaster.item.ShootArrowAction;
import pdp.aniachar.dungeonmaster.item.ShootArrowActionBuilder;
import pdp.aniachar.gamekit.Direction;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

/**
 * Test the strategy to move an arrow.
 */

public class ShootArrowActionTest {
  Arrow arrow;
  List<MazeLocation> locations;
  Pair<IMazeLocation, Double> eventPayload;

  @Before
  public void setup() {
    arrow = new Arrow();
    locations = new ArrayList<>();
    for (int row = 0; row < 6; row++) {
      for (int col = 0; col < 6; col++) {
        locations.add((MazeLocation) new IMazeLocationBuilder(new MutablePair<>(row, col)).build());
      }
    }
    arrow.setCurrentLocation(locations.get(0));
    eventPayload = new MutablePair<>();
    EventContainer.getModelEventBus().register(this);
  }

  @Test
  public void moveNorth() {
    IMazeLocationBuilder.makeAdjacent(new MutablePair<>(locations.get(0), locations.get(1)), 6, 6);
    IMazeLocationBuilder.makeAdjacent(new MutablePair<>(locations.get(0), locations.get(6)), 6, 6);
    IMazeLocationBuilder.makeAdjacent(new MutablePair<>(locations.get(1), locations.get(2)), 6, 6);
    IMazeLocationBuilder.makeAdjacent(new MutablePair<>(locations.get(2), locations.get(3)), 6, 6);
    IMazeLocationBuilder.makeAdjacent(new MutablePair<>(locations.get(2), locations.get(8)), 6, 6);
    IMazeLocationBuilder.makeAdjacent(new MutablePair<>(locations.get(3), locations.get(4)), 6, 6);
    IMazeLocationBuilder.makeAdjacent(new MutablePair<>(locations.get(3), locations.get(9)), 6, 6);

    ShootArrowAction action = new ShootArrowActionBuilder().withArrow(arrow)
            .withDirection(Direction.NORTH).withDistance(2).build();
    assertTrue(action.act().orElseThrow());
  }

  @Test
  public void moveEast() {
    IMazeLocationBuilder.makeAdjacent(new MutablePair<>(locations.get(0), locations.get(1)), 6, 6);
    IMazeLocationBuilder.makeAdjacent(new MutablePair<>(locations.get(0), locations.get(6)), 6, 6);
    IMazeLocationBuilder.makeAdjacent(new MutablePair<>(locations.get(1), locations.get(2)), 6, 6);
    IMazeLocationBuilder.makeAdjacent(new MutablePair<>(locations.get(2), locations.get(3)), 6, 6);
    IMazeLocationBuilder.makeAdjacent(new MutablePair<>(locations.get(2), locations.get(8)), 6, 6);
    IMazeLocationBuilder.makeAdjacent(new MutablePair<>(locations.get(3), locations.get(4)), 6, 6);
    IMazeLocationBuilder.makeAdjacent(new MutablePair<>(locations.get(3), locations.get(9)), 6, 6);
    IMazeLocationBuilder.makeAdjacent(new MutablePair<>(locations.get(4), locations.get(5)), 6, 6);
    IMazeLocationBuilder.makeAdjacent(new MutablePair<>(locations.get(5), locations.get(11)), 6, 6);

    ShootArrowAction action = new ShootArrowActionBuilder().withArrow(arrow)
            .withDirection(Direction.EAST).withDistance(2).build();
    assertTrue(action.act().orElseThrow());
    assertEquals(locations.get(3), arrow.getCurrentLocation());
    assertEquals(arrow.getCurrentLocation(), eventPayload.getLeft());
    assertEquals(50d, eventPayload.getRight(), 0);
  }


  @Test
  public void travelCrookedTunnel() {
    assertEquals(locations.get(0), arrow.getCurrentLocation());
    IMazeLocationBuilder.makeAdjacent(new MutablePair<>(locations.get(0), locations.get(1)), 6, 6);
    IMazeLocationBuilder.makeAdjacent(new MutablePair<>(locations.get(0), locations.get(6)), 6, 6);
    IMazeLocationBuilder.makeAdjacent(new MutablePair<>(locations.get(1), locations.get(7)), 6, 6);
    IMazeLocationBuilder.makeAdjacent(new MutablePair<>(locations.get(2), locations.get(3)), 6, 6);
    IMazeLocationBuilder.makeAdjacent(new MutablePair<>(locations.get(2), locations.get(8)), 6, 6);
    IMazeLocationBuilder.makeAdjacent(new MutablePair<>(locations.get(3), locations.get(4)), 6, 6);
    IMazeLocationBuilder.makeAdjacent(new MutablePair<>(locations.get(3), locations.get(9)), 6, 6);
    IMazeLocationBuilder.makeAdjacent(new MutablePair<>(locations.get(4), locations.get(5)), 6, 6);
    IMazeLocationBuilder.makeAdjacent(new MutablePair<>(locations.get(5), locations.get(11)), 6, 6);
    IMazeLocationBuilder.makeAdjacent(new MutablePair<>(locations.get(7), locations.get(8)), 6, 6);
    IMazeLocationBuilder.makeAdjacent(new MutablePair<>(locations.get(8), locations.get(14)), 6, 6);
    IMazeLocationBuilder.makeAdjacent(new MutablePair<>(locations.get(8), locations.get(9)), 6, 6);

    int numberOfCavesToMove = 1;
    IMazeLocation expectedLocation = locations.get(8);
    ShootArrowAction action = new ShootArrowActionBuilder()
            .withArrow(arrow).withDirection(Direction.EAST)
            .withDistance(numberOfCavesToMove).build();
    boolean actionResult = action.act().orElseThrow();
    assertTrue(actionResult);
    assertEquals(expectedLocation, arrow.getCurrentLocation());
    assertEquals(arrow.getCurrentLocation(), eventPayload.getLeft());
    assertEquals(50d, eventPayload.getRight(), 0);
  }

  @Test
  public void travelCrookedTunnel3() {
    IMazeLocationBuilder.makeAdjacent(new MutablePair<>(locations.get(0), locations.get(1)), 6, 6);
    IMazeLocationBuilder.makeAdjacent(new MutablePair<>(locations.get(0), locations.get(6)), 6, 6);
    IMazeLocationBuilder.makeAdjacent(new MutablePair<>(locations.get(1), locations.get(7)), 6, 6);
    IMazeLocationBuilder.makeAdjacent(new MutablePair<>(locations.get(2), locations.get(3)), 6, 6);
    IMazeLocationBuilder.makeAdjacent(new MutablePair<>(locations.get(2), locations.get(8)), 6, 6);
    IMazeLocationBuilder.makeAdjacent(new MutablePair<>(locations.get(3), locations.get(4)), 6, 6);
    IMazeLocationBuilder.makeAdjacent(new MutablePair<>(locations.get(3), locations.get(9)), 6, 6);
    IMazeLocationBuilder.makeAdjacent(new MutablePair<>(locations.get(4), locations.get(5)), 6, 6);
    IMazeLocationBuilder.makeAdjacent(new MutablePair<>(locations.get(5), locations.get(11)), 6, 6);
    IMazeLocationBuilder.makeAdjacent(new MutablePair<>(locations.get(7), locations.get(8)), 6, 6);
    IMazeLocationBuilder.makeAdjacent(new MutablePair<>(locations.get(8), locations.get(14)), 6, 6);
    IMazeLocationBuilder.makeAdjacent(new MutablePair<>(locations.get(8), locations.get(9)), 6, 6);

    int numberOfCavesToMove = 2;
    IMazeLocation expectedLocation = locations.get(3);
    ShootArrowAction action = new ShootArrowActionBuilder().withArrow(arrow)
            .withDirection(Direction.EAST).withDistance(numberOfCavesToMove).build();
    assertTrue(action.act().orElseThrow());
    assertEquals(expectedLocation, arrow.getCurrentLocation());
    assertEquals(arrow.getCurrentLocation(), eventPayload.getLeft());
    assertEquals(50d, eventPayload.getRight(), 0);
  }

  @Test
  public void testLoopedTunnel() {
    IMazeLocationBuilder.makeAdjacent(new MutablePair<>(locations.get(0), locations.get(1)), 6, 6);
    IMazeLocationBuilder.makeAdjacent(new MutablePair<>(locations.get(0), locations.get(6)), 6, 6);
    IMazeLocationBuilder.makeAdjacent(new MutablePair<>(locations.get(1), locations.get(7)), 6, 6);
    IMazeLocationBuilder.makeAdjacent(new MutablePair<>(locations.get(7), locations.get(6)), 6, 6);

    ShootArrowAction action = new ShootArrowActionBuilder().withArrow(arrow)
            .withDirection(Direction.WEST).withDistance(1).build();
    assertFalse(action.act().orElseThrow());
    assertEquals(locations.get(6), arrow.getCurrentLocation());
    assertEquals(arrow.getCurrentLocation(), eventPayload.getLeft());
    assertEquals(50d, eventPayload.getRight(), 0);
  }

  @Test
  public void moveEast2() {
    IMazeLocationBuilder.makeAdjacent(new MutablePair<>(locations.get(0), locations.get(1)), 6, 6);
    IMazeLocationBuilder.makeAdjacent(new MutablePair<>(locations.get(0), locations.get(6)), 6, 6);
    IMazeLocationBuilder.makeAdjacent(new MutablePair<>(locations.get(1), locations.get(2)), 6, 6);
    IMazeLocationBuilder.makeAdjacent(new MutablePair<>(locations.get(2), locations.get(3)), 6, 6);
    IMazeLocationBuilder.makeAdjacent(new MutablePair<>(locations.get(2), locations.get(8)), 6, 6);
    IMazeLocationBuilder.makeAdjacent(new MutablePair<>(locations.get(3), locations.get(4)), 6, 6);
    IMazeLocationBuilder.makeAdjacent(new MutablePair<>(locations.get(3), locations.get(9)), 6, 6);
    IMazeLocationBuilder.makeAdjacent(new MutablePair<>(locations.get(4), locations.get(5)), 6, 6);
    IMazeLocationBuilder.makeAdjacent(new MutablePair<>(locations.get(5), locations.get(11)), 6, 6);

    ShootArrowAction action = new ShootArrowActionBuilder().withArrow(arrow)
            .withDirection(Direction.EAST).withDistance(5).build();
    assertFalse(action.act().orElseThrow());
    assertEquals(locations.get(11), arrow.getCurrentLocation());
    //Check event is sent
    assertEquals(arrow.getCurrentLocation(), eventPayload.getLeft());
    assertEquals(50d, eventPayload.getRight(), 0);

  }

  @Test
  public void overShootArrow() {
    IMazeLocationBuilder.makeAdjacent(new MutablePair<>(locations.get(0), locations.get(1)), 6, 6);
    IMazeLocationBuilder.makeAdjacent(new MutablePair<>(locations.get(0), locations.get(6)), 6, 6);
    IMazeLocationBuilder.makeAdjacent(new MutablePair<>(locations.get(1), locations.get(2)), 6, 6);
    IMazeLocationBuilder.makeAdjacent(new MutablePair<>(locations.get(2), locations.get(3)), 6, 6);
    IMazeLocationBuilder.makeAdjacent(new MutablePair<>(locations.get(2), locations.get(8)), 6, 6);
    IMazeLocationBuilder.makeAdjacent(new MutablePair<>(locations.get(3), locations.get(4)), 6, 6);
    IMazeLocationBuilder.makeAdjacent(new MutablePair<>(locations.get(3), locations.get(9)), 6, 6);
    IMazeLocationBuilder.makeAdjacent(new MutablePair<>(locations.get(4), locations.get(5)), 6, 6);
    IMazeLocationBuilder.makeAdjacent(new MutablePair<>(locations.get(5), locations.get(11)), 6, 6);
    arrow.setCurrentLocation(locations.get(2));
    ShootArrowAction action = new ShootArrowActionBuilder().withArrow(arrow)
            .withDirection(Direction.EAST).withDistance(5).build();
    assertFalse(action.act().orElseThrow());
    assertEquals(locations.get(11), arrow.getCurrentLocation());
    //Check event is sent
    assertEquals(arrow.getCurrentLocation(), eventPayload.getLeft());
    assertEquals(50d, eventPayload.getRight(), 0);

  }

  @Subscribe
  public void getHitEvent(ArrowHitEvent event) {
    double health = event.getDamageDealt();
    IMazeLocation location = event.getLocationToAffect();
    eventPayload = new MutablePair<>(location, health);
  }

  @After
  public void cleanup() {
    locations.clear();
    arrow = null;
  }
}