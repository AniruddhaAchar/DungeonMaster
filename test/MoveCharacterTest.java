import org.apache.commons.lang3.tuple.MutablePair;
import org.apache.commons.lang3.tuple.Pair;
import org.junit.Before;
import org.junit.Test;

import pdp.aniachar.dungeonmaster.action.character.MoveCharacter;
import pdp.aniachar.dungeonmaster.gameworld.MazeLocation;
import pdp.aniachar.dungeonmaster.character.player.PlayerBuilder;
import pdp.aniachar.gamekit.Character;
import pdp.aniachar.gamekit.Location;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;
import static org.junit.Assert.fail;

/**
 * Tests the behaviour of {@link MoveCharacter}.
 * Tests if the functionalities work as defined.
 */


public class MoveCharacterTest {
  Pair<Integer, Integer> gridLoc;
  Location<Pair<Integer, Integer>> location;
  Character character;

  @Before
  public void setup() {
    gridLoc = new MutablePair<>(1, 1);
    location = new MazeLocation(gridLoc);
    character = new PlayerBuilder(location).setName("foo").createPlayer();
  }

  @Test
  public void creation() {
    try {
      new MoveCharacter(null, location);
      fail();
    } catch (IllegalArgumentException e) {
      assertTrue(e.getMessage().contains("null"));
    }
    try {
      new MoveCharacter(character, null);
      fail();
    } catch (IllegalArgumentException e) {
      assertTrue(e.getMessage().contains("null"));
    }
    var action = new MoveCharacter(character, location);
    assertTrue(action.toString().contains("foo"));
  }

  @Test
  public void act() {
    var action = new MoveCharacter(character, location);
    assertEquals(true, action.act().orElseThrow());
    assertEquals(location, character.getCurrentLocation());
  }

  @Test
  public void describe() {
    var action = new MoveCharacter(character, location);
    assertTrue(action.toString().contains("foo"));
  }
}