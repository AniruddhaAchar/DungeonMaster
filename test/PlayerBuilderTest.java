import org.apache.commons.lang3.tuple.MutablePair;
import org.junit.Test;

import pdp.aniachar.dungeonmaster.gameworld.MazeLocation;
import pdp.aniachar.dungeonmaster.player.PlayerBuilder;
import pdp.aniachar.gamekit.Character;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;
import static org.junit.Assert.fail;
public class PlayerBuilderTest {

  @Test
  public void setName() {
    try {
      new PlayerBuilder(new MazeLocation(new MutablePair<>(1, 1))).setName("");
      fail();
    } catch (IllegalArgumentException e) {
      assertTrue(e.getMessage().contains("blank"));
    }
    try {
      new PlayerBuilder(new MazeLocation(new MutablePair<>(1, 1))).setName(null);
      fail();
    } catch (IllegalArgumentException e) {
      assertTrue(e.getMessage().contains("null"));
    }

    Character c = new PlayerBuilder(new MazeLocation(new MutablePair<>(1, 1)))
            .setName("Foo").createPlayer();
    assertEquals("Foo", c.getName());
  }

  @Test
  public void createPlayer() {
    try {
      new PlayerBuilder(null);
      fail();
    } catch (IllegalArgumentException e) {
      assertTrue(e.getMessage().contains("null"));
    }
    var loc = new MazeLocation(new MutablePair<>(1, 1));
    Character c = new PlayerBuilder(loc)
            .createPlayer();
    assertTrue(c.getName().contains("Player"));
    assertEquals(loc, c.getCurrentLocation());
    assertEquals(0, c.getAllItems().size());
  }
}