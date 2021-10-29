import org.apache.commons.lang3.tuple.MutablePair;
import org.junit.Test;

import pdp.aniachar.dungeonmaster.action.location.MoveActionBuilder;
import pdp.aniachar.dungeonmaster.gameworld.MazeLocation;
import pdp.aniachar.gamekit.Direction;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;
import static org.junit.Assert.fail;
public class MoveActionTest {

  @Test
  public void testCreation() {
    try {
      new MoveActionBuilder().setWhereTo(null).setDirection(Direction.EAST).createMoveAction();
      fail();
    } catch (IllegalArgumentException e) {
      assertTrue(e.getMessage().contains("null"));
    }
    try {
      new MoveActionBuilder().setWhereTo(new MazeLocation(new MutablePair<>(1, 1))).setDirection(null).createMoveAction();
      fail();
    } catch (IllegalArgumentException e) {
      assertTrue(e.getMessage().contains("null"));
    }
  }

  @Test
  public void act() {
    var whereTo = new MazeLocation(new MutablePair<>(1,1));
    var action = new MoveActionBuilder().setWhereTo(whereTo).setDirection(Direction.EAST).createMoveAction();
    assertEquals(whereTo, action.act().orElseThrow());
  }

  @Test
  public void describe() {
    var whereTo = new MazeLocation(new MutablePair<>(1,1));
    var action = new MoveActionBuilder().setWhereTo(whereTo).setDirection(Direction.EAST).createMoveAction();
    assertTrue(action.describe().contains("Can move"));
  }
}