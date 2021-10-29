import org.apache.commons.lang3.tuple.ImmutablePair;
import org.apache.commons.lang3.tuple.Pair;
import org.junit.Before;
import org.junit.Test;

import java.util.List;

import pdp.aniachar.dungeonmaster.gameworld.MazeLocation;
import pdp.aniachar.dungeonmaster.item.Treasure;
import pdp.aniachar.dungeonmaster.item.TreasureType;
import pdp.aniachar.dungeonmaster.player.PlayerBuilder;
import pdp.aniachar.gamekit.Character;
import pdp.aniachar.gamekit.Item;
import pdp.aniachar.gamekit.Location;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;
import static org.junit.Assert.fail;
public class PlayerTest {

  List<Item> items;
  Location<Pair<Integer, Integer>> mazeLocation;
  Character player;

  @Before
  public void setup() {
    items = List.of(new Treasure(TreasureType.DIAMOND), new Treasure(TreasureType.RUBY),
            new Treasure(TreasureType.SAFARI));

    mazeLocation = new MazeLocation(new ImmutablePair<>(0, 0));


    player = new PlayerBuilder(mazeLocation).setName("elrick").createPlayer();
  }

  @Test
  public void creationTest() {
    Location<Pair<Integer, Integer>> mazeLocation = new MazeLocation(new ImmutablePair<>(0, 0));

    Character player = new PlayerBuilder(mazeLocation)
            .setName("elrick").createPlayer();

    assertEquals(0, player.getAllItems().size());
    assertEquals("elrick", player.getName());
    assertEquals(mazeLocation, player.getCurrentLocation());

    try {
      new PlayerBuilder(mazeLocation).setName("elrick").createPlayer();
      fail();
    } catch (IllegalArgumentException e) {
      assertTrue(e.getMessage().contains("null"));
    }
    try {
      new PlayerBuilder(mazeLocation).setName(null).createPlayer();
      fail();
    } catch (IllegalArgumentException e) {
      assertTrue(e.getMessage().contains("null"));
    }
    try {
      new PlayerBuilder(mazeLocation).setName("elrick").createPlayer();
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
    assertEquals(mazeLocation, player.getCurrentLocation());
    Location<Pair<Integer, Integer>> newLocation =
            new MazeLocation(new ImmutablePair<>(0, 1));

    assertTrue(player.makeMove(newLocation));
    assertEquals(newLocation, player.getCurrentLocation());
  }

  @Test
  public void takeItem() {
    int originalItems = mazeLocation.getItems().size();
    assertTrue(player.takeItem(mazeLocation.getItems().get(0)));
    assertEquals(1, player.getAllItems().size());


    assertFalse(player.takeItem(mazeLocation.getItems().get(0)));


    try {
      player.takeItem(null);
      fail();
    } catch (IllegalArgumentException e) {
      assertTrue(e.getMessage().contains("null"));
    }
  }

  @Test
  public void getAllItems() {

    assertEquals(0, player.getAllItems().size());
    assertTrue(player.takeItem(mazeLocation.getItems().get(0)));
    assertEquals(1, player.getAllItems().size());
    assertEquals(mazeLocation.getItems().get(0), player.getAllItems().get(0));

  }

  @Test
  public void getName() {
    assertEquals("elrick", player.getName());
  }
}