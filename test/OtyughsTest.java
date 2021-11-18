import com.google.common.eventbus.Subscribe;

import org.apache.commons.lang3.mutable.MutableBoolean;
import org.apache.commons.lang3.tuple.MutablePair;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import java.util.ArrayList;
import java.util.List;

import pdp.aniachar.dungeonmaster.character.AbstractCharacter;
import pdp.aniachar.dungeonmaster.character.monster.Otyughs;
import pdp.aniachar.dungeonmaster.comm.DeathEvent;
import pdp.aniachar.dungeonmaster.comm.EventContainer;
import pdp.aniachar.dungeonmaster.gameworld.IMazeLocationBuilder;
import pdp.aniachar.dungeonmaster.gameworld.MazeLocation;
import pdp.aniachar.dungeonmaster.gameworld.SmellStrength;
import pdp.aniachar.dungeonmaster.item.Arrow;
import pdp.aniachar.dungeonmaster.item.ShootArrowAction;
import pdp.aniachar.dungeonmaster.item.ShootArrowActionBuilder;
import pdp.aniachar.gamekit.Direction;

import static org.junit.Assert.assertEquals;

/**
 * Tests the functionalities of am {@link Otyughs}.
 */

public class OtyughsTest {

  Arrow arrow;
  List<MazeLocation> locations;
  AbstractCharacter monster;
  MutableBoolean death;

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

    death = new MutableBoolean(false);
    EventContainer.getModelEventBus().register(this);
  }

  @After
  public void cleanup() {
    arrow = null;
    locations = null;
    monster = null;
    death = null;
  }

  @Test
  public void getCurrentLocation() {
    monster = new Otyughs(locations.get(3), "foo");
    assertEquals(locations.get(3), monster.getCurrentLocation());
  }

  @Test
  public void health() {
    monster = new Otyughs(locations.get(3), "foo");
    assertEquals(100, monster.getCurrentHealth(), 0);
  }

  @Test
  public void killMonster() {
    IMazeLocationBuilder.makeAdjacent(new MutablePair<>(locations.get(0), locations.get(1)), 6, 6);
    IMazeLocationBuilder.makeAdjacent(new MutablePair<>(locations.get(0), locations.get(6)), 6, 6);
    IMazeLocationBuilder.makeAdjacent(new MutablePair<>(locations.get(1), locations.get(2)), 6, 6);
    IMazeLocationBuilder.makeAdjacent(new MutablePair<>(locations.get(2), locations.get(3)), 6, 6);
    IMazeLocationBuilder.makeAdjacent(new MutablePair<>(locations.get(2), locations.get(8)), 6, 6);
    IMazeLocationBuilder.makeAdjacent(new MutablePair<>(locations.get(3), locations.get(4)), 6, 6);
    IMazeLocationBuilder.makeAdjacent(new MutablePair<>(locations.get(3), locations.get(9)), 6, 6);
    IMazeLocationBuilder.makeAdjacent(new MutablePair<>(locations.get(4), locations.get(5)), 6, 6);
    IMazeLocationBuilder.makeAdjacent(new MutablePair<>(locations.get(5), locations.get(11)), 6, 6);
    monster = new Otyughs(locations.get(3), "foo");
    ShootArrowAction action = new ShootArrowActionBuilder().withArrow(arrow)
            .withDirection(Direction.EAST).withDistance(2).build();
    action.act();
    assertEquals(50d, monster.getCurrentHealth(), 0);

    arrow.setCurrentLocation(locations.get(0));
    action = new ShootArrowActionBuilder().withArrow(arrow)
            .withDirection(Direction.EAST).withDistance(2).build();
    action.act();
    assertEquals(0d, monster.getCurrentHealth(), 0);
    assertEquals(new MutableBoolean(true), death);
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
    IMazeLocationBuilder.makeAdjacent(new MutablePair<>(locations.get(4), locations.get(10)), 6, 6);
    IMazeLocationBuilder.makeAdjacent(new MutablePair<>(locations.get(5), locations.get(11)), 6, 6);
    monster = new Otyughs(locations.get(3), "foo");
    ShootArrowAction action = new ShootArrowActionBuilder().withArrow(arrow)
            .withDirection(Direction.EAST).withDistance(3).build();
    action.act();
    assertEquals(locations.get(4), arrow.getCurrentLocation());
    assertEquals(100d, monster.getCurrentHealth(), 0);
  }

  @Test
  public void testSmell() {
    IMazeLocationBuilder.makeAdjacent(new MutablePair<>(locations.get(0), locations.get(1)), 6, 6);
    IMazeLocationBuilder.makeAdjacent(new MutablePair<>(locations.get(0), locations.get(6)), 6, 6);
    IMazeLocationBuilder.makeAdjacent(new MutablePair<>(locations.get(1), locations.get(2)), 6, 6);
    IMazeLocationBuilder.makeAdjacent(new MutablePair<>(locations.get(2), locations.get(3)), 6, 6);
    IMazeLocationBuilder.makeAdjacent(new MutablePair<>(locations.get(2), locations.get(8)), 6, 6);
    IMazeLocationBuilder.makeAdjacent(new MutablePair<>(locations.get(3), locations.get(4)), 6, 6);
    IMazeLocationBuilder.makeAdjacent(new MutablePair<>(locations.get(3), locations.get(9)), 6, 6);
    IMazeLocationBuilder.makeAdjacent(new MutablePair<>(locations.get(4), locations.get(5)), 6, 6);
    IMazeLocationBuilder.makeAdjacent(new MutablePair<>(locations.get(5), locations.get(11)), 6, 6);

    AbstractCharacter monster = new Otyughs(locations.get(3), "foo");
    assertEquals(SmellStrength.STRONG, locations.get(2).getSmellStrength());
    assertEquals(SmellStrength.NONE, locations.get(0).getSmellStrength());
    assertEquals(SmellStrength.WEAK, locations.get(1).getSmellStrength());
  }

  @Test
  public void testSmellWithTwoMonsters() {
    IMazeLocationBuilder.makeAdjacent(new MutablePair<>(locations.get(0), locations.get(1)), 6, 6);
    IMazeLocationBuilder.makeAdjacent(new MutablePair<>(locations.get(0), locations.get(6)), 6, 6);
    IMazeLocationBuilder.makeAdjacent(new MutablePair<>(locations.get(1), locations.get(2)), 6, 6);
    IMazeLocationBuilder.makeAdjacent(new MutablePair<>(locations.get(2), locations.get(3)), 6, 6);
    IMazeLocationBuilder.makeAdjacent(new MutablePair<>(locations.get(2), locations.get(8)), 6, 6);
    IMazeLocationBuilder.makeAdjacent(new MutablePair<>(locations.get(3), locations.get(4)), 6, 6);
    IMazeLocationBuilder.makeAdjacent(new MutablePair<>(locations.get(3), locations.get(9)), 6, 6);
    IMazeLocationBuilder.makeAdjacent(new MutablePair<>(locations.get(4), locations.get(5)), 6, 6);
    IMazeLocationBuilder.makeAdjacent(new MutablePair<>(locations.get(5), locations.get(11)), 6, 6);
    IMazeLocationBuilder.makeAdjacent(new MutablePair<>(locations.get(2), locations.get(8)), 6, 6);
    AbstractCharacter monster = new Otyughs(locations.get(3), "foo");
    AbstractCharacter monster2 = new Otyughs(locations.get(8), "foo");
    assertEquals(SmellStrength.STRONG, locations.get(2).getSmellStrength());
    assertEquals(SmellStrength.NONE, locations.get(0).getSmellStrength());
    assertEquals(SmellStrength.STRONG, locations.get(1).getSmellStrength());
  }

  @Subscribe
  public void getDeathNotification(DeathEvent<Otyughs> otyughsDeathEvent) {
    death.setValue(true);
  }

  @Test
  public void getName() {
    monster = new Otyughs(locations.get(3), "foo");
    assertEquals("foo", monster.getName());
  }
}