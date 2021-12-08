import com.google.common.eventbus.Subscribe;

import org.apache.commons.lang3.mutable.MutableBoolean;
import org.apache.commons.lang3.tuple.ImmutablePair;
import org.apache.commons.lang3.tuple.MutablePair;
import org.apache.commons.lang3.tuple.Pair;
import org.junit.Before;
import org.junit.Test;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.NoSuchElementException;

import pdp.aniachar.dungeonmaster.character.monster.Otyughs;
import pdp.aniachar.dungeonmaster.character.player.IPlayer;
import pdp.aniachar.dungeonmaster.character.player.PlayerBuilder;
import pdp.aniachar.dungeonmaster.comm.DeathEvent;
import pdp.aniachar.dungeonmaster.comm.EventContainer;
import pdp.aniachar.dungeonmaster.gameworld.IMazeLocation;
import pdp.aniachar.dungeonmaster.gameworld.IMazeLocationBuilder;
import pdp.aniachar.dungeonmaster.gameworld.MazeLocation;
import pdp.aniachar.dungeonmaster.item.Arrow;
import pdp.aniachar.dungeonmaster.item.Treasure;
import pdp.aniachar.dungeonmaster.item.TreasureType;
import pdp.aniachar.gamekit.Character;
import pdp.aniachar.gamekit.Item;
import pdp.aniachar.gamekit.Location;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;
import static org.junit.Assert.fail;

/**
 * Tests the functionalities of {@link Character}.
 * Tests if the functionalities work as defined.
 */

public class PlayerTest {

  Character player;

  IMazeLocation location;
  IMazeLocation location2;
  IMazeLocation location3;
  IMazeLocation location4;
  List<Item> items;
  Item arrow;
  Character monster;
  IPlayer player2;
  MutableBoolean deathNoticeReceived;

  @Before
  public void setup() {
    items = List.of(new Treasure(TreasureType.RUBY), new Treasure(TreasureType.DIAMOND),
            new Treasure(TreasureType.SAPPHIRE));
    arrow = new Arrow();

    location = new IMazeLocationBuilder(new MutablePair<>(0, 0))
            .addArrow(arrow).addTreasure(items.get(0)).build();
    location2 = new IMazeLocationBuilder(new MutablePair<>(1, 0)).build();
    location3 = new IMazeLocationBuilder(new MutablePair<>(1, 1)).build();
    location4 = new IMazeLocationBuilder(new MutablePair<>(10, 1)).build();
    IMazeLocationBuilder.makeAdjacent(
            new MutablePair<>(location, location2), 6, 6);

    deathNoticeReceived = new MutableBoolean(false);

    player = new PlayerBuilder(location).setName("elrick").createPlayer();
    player2 = (IPlayer) new PlayerBuilder(location2).setRegister(true).setName("Player 2")
            .createPlayer();
    EventContainer.getModelEventBus().register(this);
  }


  @Test
  public void creationTest() throws NoSuchMethodException {
    Location<Pair<Integer, Integer>> mazeLocation = new MazeLocation(new ImmutablePair<>(0, 0));

    Character player = new PlayerBuilder(mazeLocation)
            .setName("elrick").createPlayer();

    assertEquals(3, player.getAllItems().size());
    assertEquals("elrick", player.getName());
    assertEquals(mazeLocation, player.getCurrentLocation());

    try {
      new PlayerBuilder(mazeLocation).setName("elrick").createPlayer();
      assertEquals("elrick", player.getName());
    } catch (IllegalArgumentException e) {
      fail();
    }
    try {
      new PlayerBuilder(mazeLocation).setName(null).createPlayer();
      fail();
    } catch (IllegalArgumentException e) {
      assertTrue(e.getMessage().contains("null"));
    }
    try {
      new PlayerBuilder(mazeLocation).setName("").createPlayer();
      fail();
    } catch (IllegalArgumentException e) {
      assertTrue(e.getMessage().contains("blank"));
    }

  }

  @Test
  public void makeMove() {
    Location<Pair<Integer, Integer>> newLocation = new MazeLocation(new ImmutablePair<>(0, 1));

    assertTrue(player.makeMove(newLocation));
    assertEquals(newLocation, player.getCurrentLocation());

    try {
      player.makeMove(null);
      fail();
    } catch (IllegalArgumentException e) {
      assertTrue(e.getMessage().contains("null"));
    }

  }

  @Test
  public void getCurrentLocation() {
    assertEquals(location, player.getCurrentLocation());
    Location<Pair<Integer, Integer>> newLocation =
            new MazeLocation(new ImmutablePair<>(0, 1));

    assertTrue(player.makeMove(newLocation));
    assertEquals(newLocation, player.getCurrentLocation());
  }

  @Test
  public void takeItem() throws NoSuchMethodException {
    var result = player.takeItem(new Treasure(TreasureType.DIAMOND));
    assertTrue(result);
    assertEquals(4, player.getAllItems().size());


    try {
      player.takeItem(null);
      fail();
    } catch (IllegalArgumentException e) {
      assertTrue(e.getMessage().contains("null"));
    }
  }

  @Test
  public void getAllItems() throws NoSuchMethodException {

    assertEquals(3, player.getAllItems().size());
    assertTrue(player.takeItem(new Arrow()));
    assertEquals(4, player.getAllItems().size());
  }

  @Test
  public void getName() {
    assertEquals("elrick", player.getName());
  }

  @Test
  public void testArrows() {
    assertEquals(3, player2.getQuiver().size());
  }

  @Test
  public void killingPlayer() {
    monster = new Otyughs(location4, "foo");
    assertEquals(0, Double.compare(1, player2.getCurrentHealth()));
    player2.makeMove(location3);
    assertEquals(0, Double.compare(1, player2.getCurrentHealth()));
    player2.makeMove(location4);
    assertEquals(0, Double.compare(0, player2.getCurrentHealth()));
    assertEquals(true, deathNoticeReceived.getValue());
  }

  @Test
  public void tryEscape() {
    monster = new Otyughs(location4, "foo");
    monster.changeHealth(-50);
    Map<Integer, Integer> distMap = new HashMap<>();
    for (int i = 0; i < 100; i++) {
      int deathCount = 0;
      for (int j = 0; j < 30; j++) {
        player2.makeMove(location4);
        if (player2.getCurrentHealth() == 0) {
          deathCount++;
          player2.changeHealth(1);
          deathNoticeReceived.setValue(false);
        }
      }
      distMap.computeIfPresent(deathCount, (key, count) -> count = count + 1);
      distMap.putIfAbsent(deathCount, 1);
    }
    assertTrue(distMap.size() > 0);
    assertTrue(Math.abs(distMap.get(0) - 100 * 30 / 2) < 100);
  }

  @Test
  public void testNumberOfArrows() {
    assertEquals(3, player2.getQuiver().size());
  }

  @Subscribe
  public void playerDeathNotification(DeathEvent<IPlayer> playerDeathEvent) {
    deathNoticeReceived.setValue(true);
  }

  @Test
  public void removeArrow() {
    player2.removeArrow();
    assertEquals(2, player2.getQuiver().size());
    player2.removeArrow();
    player2.removeArrow();
    try {
      player2.removeArrow();
      fail();
    } catch (NoSuchElementException e) {
      assertTrue(e.getMessage().contains("arrow"));
    }
  }

}