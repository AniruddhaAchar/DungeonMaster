import com.google.common.eventbus.Subscribe;

import org.apache.commons.lang3.mutable.MutableBoolean;
import org.apache.commons.lang3.mutable.MutableInt;
import org.apache.commons.lang3.tuple.Pair;
import org.jetbrains.annotations.NotNull;
import org.junit.Before;
import org.junit.Test;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.stream.Collectors;

import pdp.aniachar.dungeonmaster.action.location.MoveAction;
import pdp.aniachar.dungeonmaster.action.location.PickItemAction;
import pdp.aniachar.dungeonmaster.character.monster.Otyughs;
import pdp.aniachar.dungeonmaster.comm.CallEvent;
import pdp.aniachar.dungeonmaster.comm.EventContainer;
import pdp.aniachar.dungeonmaster.comm.RoarEvent;
import pdp.aniachar.dungeonmaster.gameworld.IMazeLocation;
import pdp.aniachar.dungeonmaster.gameworld.IMazeLocationBuilder;
import pdp.aniachar.dungeonmaster.gameworld.RandomMazeBuilder;
import pdp.aniachar.gamekit.Action;
import pdp.aniachar.gamekit.GameWorld;
import pdp.aniachar.gamekit.ImmutableCharacter;
import pdp.aniachar.gamekit.Location;
import pdp.aniachar.gamekit.WorldBuildStrategy;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;
import static org.junit.Assert.fail;

/**
 * Tests the functionalities of {@link RandomMazeBuilder}.
 * Tests if the functionalities work as defined.
 */

public class RandomMazeBuilderTest {
  MutableBoolean isWrapped;
  GameWorld world;
  AtomicInteger visitedNodes;
  AtomicInteger numberTreasure;
  AtomicInteger numberOfCaves;
  WorldBuildStrategy buildStrategy;
  MutableInt monsterCount;
  List<ImmutableCharacter> monsters;

  @Before
  public void setup() {
    visitedNodes = new AtomicInteger(0);

    numberTreasure = new AtomicInteger(0);

    numberOfCaves = new AtomicInteger(0);

    isWrapped = new MutableBoolean(false);
    monsterCount = new MutableInt(0);
    EventContainer.getModelEventBus().register(this);
  }

//  @After
//  public void cleanup() {
//    isWrapped = null;
//    world = null;
//    visitedNodes = null;
//    numberTreasure = null;
//    numberOfCaves = null;
//    buildStrategy = null;
//    monsterCount = null;
//  }

  @Test()
  public void testBadInit() {
    try {
      new RandomMazeBuilder(5, 510, false, 0, 0, 0);
      fail();
    } catch (IllegalArgumentException e) {
      assertTrue(e.getMessage().contains("Rows and columns should be at least"));
    }
    try {
      new RandomMazeBuilder(15, 0, false, 0, 0, 0);
      fail();
    } catch (IllegalArgumentException e) {
      assertTrue(e.getMessage().contains("Rows and columns should be at least"));
    }
    try {
      new RandomMazeBuilder(15, 15, false, -1, 0, 0);
      fail();
    } catch (IllegalArgumentException e) {
      assertTrue(e.getMessage().contains("Degree of interconnectivity cannot be negative"));
    }
    try {
      new RandomMazeBuilder(15, 15, false, 0, -1, 0);
      fail();
    } catch (IllegalArgumentException e) {
      assertTrue(e.getMessage()
              .contains("percentTreasure cannot be greater than 100 or less than 0"));
    }
    try {
      new RandomMazeBuilder(15, 15, false, -1, 200, 0);
      fail();
    } catch (IllegalArgumentException e) {
      assertTrue(e.getMessage()
              .contains("percentTreasure cannot be greater than 100 or less than 0"));
    }
  }

  @Test
  public void testSimpleWorld() throws NoSuchMethodException {
    buildStrategy =
            new RandomMazeBuilder(10, 10, false, 0, 0, 0);
    world = buildStrategy.buildWorld();
    assertTrue(
            checkConnectivity(
                    world, 10, 10, visitedNodes,
                    numberTreasure, numberOfCaves, isWrapped));
    assertEquals(99, visitedNodes.get());
    assertEquals(0, numberTreasure.get());
    assertFalse(isWrapped.getValue());
    var distanceBetweenNodes = IMazeLocationBuilder
            .distanceToAllNodes((IMazeLocation) world.getStartLocation());
    assertTrue(distanceBetweenNodes.get((IMazeLocation) world.getEndLocation()) >= 5);
    assertTrue(((IMazeLocation) world.getEndLocation()).getMoveAction().size() != 2);
  }

  @Test
  public void testWorldWithTreasure() throws NoSuchMethodException {
    try {
      buildStrategy = new RandomMazeBuilder(10, 10, false, 1, 10, 0);
    } catch (IllegalStateException e) {
      if (e.getMessage().contains("caves")) {
        //try to build the maze again.
        buildStrategy = new RandomMazeBuilder(10, 10, false, 1, 10, 0);
      }
    }
    world = buildStrategy.buildWorld();
    visitedNodes = new AtomicInteger(0);
    numberTreasure = new AtomicInteger(0);
    numberOfCaves = new AtomicInteger(0);
    isWrapped = new MutableBoolean(false);
    assertTrue(checkConnectivity(world, 10, 10, visitedNodes, numberTreasure,
            numberOfCaves, isWrapped));
    assertEquals(101, visitedNodes.get());
    var numberItems = Math.ceil(numberOfCaves.get() * 10 / 100d) + Math.ceil(100d * 10d / 100d);
    assertEquals((int) numberItems, numberTreasure.get() / 2);
    assertFalse(isWrapped.getValue());
  }

  @Test
  public void testInterConnectedWorld() throws NoSuchMethodException {
    buildStrategy = new RandomMazeBuilder(10, 10, false, 2, 30, 0);
    world = buildStrategy.buildWorld();
    visitedNodes = new AtomicInteger(0);
    numberTreasure = new AtomicInteger(0);
    numberOfCaves = new AtomicInteger(0);
    isWrapped = new MutableBoolean(false);
    assertTrue(checkConnectivity(world, 10, 10, visitedNodes, numberTreasure,
            numberOfCaves, isWrapped));
    assertEquals(103, visitedNodes.get());
    var numberItems = Math.ceil(numberOfCaves.get() * 30 / 100d) + Math.ceil(100d * 30d / 100d);
    assertEquals((int) numberItems, numberTreasure.get() / 2);
    assertFalse(isWrapped.getValue());
  }

  @Test
  public void testLargerWorld() throws NoSuchMethodException {
    buildStrategy = new RandomMazeBuilder(11, 11, false,
            2, 30, 0);
    world = buildStrategy.buildWorld();
    visitedNodes = new AtomicInteger(0);
    numberTreasure = new AtomicInteger(0);
    numberOfCaves = new AtomicInteger(0);
    isWrapped = new MutableBoolean(false);
    assertTrue(checkConnectivity(world, 11, 11, visitedNodes, numberTreasure,
            numberOfCaves, isWrapped));
    assertEquals(124, visitedNodes.get());
    var numberItems = Math.ceil(numberOfCaves.get() * 30 / 100d) + Math.ceil(11 * 11d * 30d / 100d);
    assertEquals((int) numberItems, numberTreasure.get() / 2);
    assertFalse(isWrapped.getValue());
  }

  @Test
  public void testWrappedWorld() throws NoSuchMethodException {

    buildStrategy = new RandomMazeBuilder(10, 10, true,
            2, 30, 0);
    world = buildStrategy.buildWorld();
    visitedNodes = new AtomicInteger(0);
    numberTreasure = new AtomicInteger(0);
    numberOfCaves = new AtomicInteger(0);
    isWrapped = new MutableBoolean(false);
    assertTrue(checkConnectivity(world, 10, 10, visitedNodes, numberTreasure,
            numberOfCaves, isWrapped));
    assertEquals(103, visitedNodes.get());
    var numberItems = Math.ceil(numberOfCaves.get() * 30 / 100d) + Math.ceil(100d * 30d / 100d);
    assertEquals((int) numberItems, numberTreasure.get() / 2);
    assertTrue("World should be wrapped", isWrapped.getValue());
  }

  @Test
  public void checkNumberOfMonsters() {
    buildStrategy = new RandomMazeBuilder(10, 10, false, 2, 30, 20);
    world = buildStrategy.buildWorld();
    EventContainer.getModelEventBus()
            .post(
                    new CallEvent<>(
                            new Otyughs(world.getEndLocation(), "foo", false)));
    assertEquals(new MutableInt(21), monsterCount);
    boolean monsterAtEnd = false;
    boolean notMonsterAtStart = true;
    boolean allMonstersInCaves = true;
    for (ImmutableCharacter character :
            monsters) {
      if (world.getEndLocation().equals(character.getCurrentLocation())) {
        monsterAtEnd = true;
        int adj = ((IMazeLocation) character.getCurrentLocation()).getMoveAction().size();
        if (adj == 2) {
          allMonstersInCaves = false;
        }
      }
    }
    assertTrue(allMonstersInCaves);

    for (ImmutableCharacter character :
            monsters) {
      if (world.getStartLocation().equals(character.getCurrentLocation())) {
        notMonsterAtStart = false;
      }
    }

    assertTrue(notMonsterAtStart);
    assertTrue(monsterAtEnd);
  }

  @Test
  public void checkDefaultNumberOfMonsters() {
    buildStrategy = new RandomMazeBuilder(10, 10, false, 2, 30, 0);
    world = buildStrategy.buildWorld();
    EventContainer.getModelEventBus()
            .post(
                    new CallEvent<>(
                            new Otyughs(world.getEndLocation(), "foo", false)));
    assertEquals(new MutableInt(1), monsterCount);
    boolean monsterAtEnd = false;
    boolean notMonsterAtStart = true;
    boolean allMonstersInCaves = true;
    for (ImmutableCharacter character :
            monsters) {
      if (world.getEndLocation().equals(character.getCurrentLocation())) {
        monsterAtEnd = true;
        int adj = ((IMazeLocation) character.getCurrentLocation()).getMoveAction().size();
        if (adj == 2) {
          allMonstersInCaves = false;
        }
      }
    }
    assertTrue(allMonstersInCaves);

    for (ImmutableCharacter character :
            monsters) {
      if (world.getStartLocation().equals(character.getCurrentLocation())) {
        notMonsterAtStart = false;
      }
    }
    assertTrue(notMonsterAtStart);
    assertTrue(monsterAtEnd);
  }

  @Subscribe
  public void countMonsters(RoarEvent<Otyughs> roarEvent) {
    if (monsters == null) {
      monsters = new ArrayList<>();
    }
    monsters.add(roarEvent.getRoarer());
    monsterCount.increment();
  }

  private boolean checkConnectivity(@NotNull GameWorld world, int maxRow, int maxCol,
                                    AtomicInteger degreeOfInterConnection,
                                    AtomicInteger numberOfTreasure, AtomicInteger numberOfCaves,
                                    MutableBoolean isWrapped) throws NoSuchMethodException {
    Set<Location<?>> visited = new HashSet<>();

    dfs(world.getStartLocation(), visited, degreeOfInterConnection, numberOfTreasure,
            numberOfCaves, isWrapped, maxRow, maxCol);
    return visited.size() >= maxCol * maxRow;
  }

  /**
   * This method performs a DFS traversal throughout the maze that was generated.
   * When it does this, it checks for interconnectivity, number of visited nodes,
   * if the graph is wrapped etc.
   *
   * @param source                  The starting point/source of the maze.
   * @param visited                 The set of visited nodes.
   * @param degreeOfInterConnection The actual degree of interconnection.This is mutable.
   * @param numberOfTreasure        The actual number of treasures.
   * @param numberOfCaves           The actual number of caves.
   * @param isWrapped               If the graph is wrapped or not.
   * @param maxRow                  The number of rows in the graph's grid.
   * @param maxCol                  The number of columns in the graph's grid.
   */

  private void dfs(@NotNull Location<?> source, @NotNull Set<Location<?>> visited,
                   AtomicInteger degreeOfInterConnection, AtomicInteger numberOfTreasure,
                   AtomicInteger numberOfCaves, MutableBoolean isWrapped,
                   int maxRow,
                   int maxCol) throws NoSuchMethodException {
    visited.add(source);
    var adj = source.possibleActions().stream()
            .filter(action -> action instanceof MoveAction).collect(Collectors.toList());
    if (adj.size() != 2) {
      numberOfCaves.incrementAndGet();
    }
    if (!isWrapped.booleanValue()) {
      isWrapped.setValue(checkedWrapped(source, maxRow, maxCol));
    }
    var itemsAtLocation = source.possibleActions()
            .stream().filter(action -> action instanceof PickItemAction).count();
    numberOfTreasure.addAndGet((int) itemsAtLocation);

    for (Action<?> moveAction :
            adj) {
      Location<?> neighbor = (Location<?>) moveAction.act()
              .orElseThrow(() -> new IllegalStateException("Neighbor not found"));
      if (!visited.contains(neighbor)) {
        visited.add(neighbor);
        dfs(neighbor, visited, degreeOfInterConnection,
                numberOfTreasure, numberOfCaves, isWrapped, maxRow, maxCol);
      } else {
        degreeOfInterConnection.incrementAndGet();
      }
    }
  }

  private boolean checkedWrapped(@NotNull Location<?> current, int maxRow, int maxCol) {
    Pair<Integer, Integer> gridLocation = (Pair<Integer, Integer>) current.getLocationDescription();
    if (gridLocation.getLeft() == 0) {
      return wrappedHelper(maxRow, current, true);
    } else if (gridLocation.getLeft() == maxRow - 1) {
      return wrappedHelper(0, current, true);
    } else if (gridLocation.getRight() == 0) {
      return wrappedHelper(maxCol, current, false);
    } else if (gridLocation.getRight() == maxCol - 1) {
      return wrappedHelper(0, current, false);
    }
    return false;
  }

  private boolean wrappedHelper(int checkCondition, @NotNull Location<?> current,
                                boolean checkLeft) {
    var adjacent = current.possibleActions().stream()
            .filter(action -> action instanceof MoveAction).map(action -> {
              try {
                return action.act();
              } catch (NoSuchMethodException e) {
                throw new IllegalArgumentException();
              }
            })
            .collect(Collectors.toList());
    for (var adj : adjacent) {
      Location<Pair<Integer, Integer>> actual;
      actual = (Location<Pair<Integer, Integer>>) adj.orElseThrow();
      var loc = actual.getLocationDescription();
      if (checkLeft) {
        if (loc.getLeft() == checkCondition) {
          return true;
        }
      } else {
        if (loc.getRight() == checkCondition) {
          return true;
        }
      }
    }
    return false;
  }


}