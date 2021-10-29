import org.apache.commons.lang3.mutable.MutableBoolean;
import org.apache.commons.lang3.tuple.Pair;
import org.jetbrains.annotations.NotNull;
import org.junit.Before;
import org.junit.Test;

import java.util.HashSet;
import java.util.Set;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.stream.Collectors;

import pdp.aniachar.dungeonmaster.action.location.MoveAction;
import pdp.aniachar.dungeonmaster.action.location.PickItemAction;
import pdp.aniachar.dungeonmaster.gameworld.RandomMazeBuilder;
import pdp.aniachar.gamekit.Action;
import pdp.aniachar.gamekit.GameWorld;
import pdp.aniachar.gamekit.Location;
import pdp.aniachar.gamekit.WorldBuildStrategy;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;
import static org.junit.Assert.fail;
public class RandomMazeBuilderTest {
  MutableBoolean isWrapped;
  GameWorld world;
  AtomicInteger visitedNodes;
  AtomicInteger numberTreasure;
  AtomicInteger numberOfCaves;
  WorldBuildStrategy buildStrategy;

  @Before
  public void setup() {
    visitedNodes = new AtomicInteger(0);

    numberTreasure = new AtomicInteger(0);

    numberOfCaves = new AtomicInteger(0);

    isWrapped = new MutableBoolean(false);
  }

  @Test()
  public void testBadInit(){
    try{
      new RandomMazeBuilder(5, 510, false, 0, 0);
      fail();
    }catch (IllegalArgumentException e){
      assertTrue(e.getMessage().contains("Rows and columns should be at least"));
    }
    try{
      new RandomMazeBuilder(15, 0, false, 0, 0);
      fail();
    }catch (IllegalArgumentException e){
      assertTrue(e.getMessage().contains("Rows and columns should be at least"));
    }
    try{
      new RandomMazeBuilder(15, 15, false, -1, 0);
      fail();
    }catch (IllegalArgumentException e){
      assertTrue(e.getMessage().contains("Degree of interconnectivity cannot be negative"));
    }
    try{
      new RandomMazeBuilder(15, 15, false, 0, -1);
      fail();
    }catch (IllegalArgumentException e){
      assertTrue(e.getMessage().contains("percentTreasure cannot be greater than 100 or less than 0"));
    }
    try{
      new RandomMazeBuilder(15, 15, false, -1, 200);
      fail();
    }catch (IllegalArgumentException e){
      assertTrue(e.getMessage().contains("percentTreasure cannot be greater than 100 or less than 0"));
    }
  }

  @Test
  public void testSimpleWorld() {
    buildStrategy = new RandomMazeBuilder(10, 10, false, 0, 0);
    world = buildStrategy.buildWorld();
    assertTrue(checkConnectivity(world, 10, 10, visitedNodes, numberTreasure, numberOfCaves, isWrapped));
    assertEquals(99, visitedNodes.get());
    assertEquals(0, numberTreasure.get());
    assertFalse(isWrapped.getValue());
  }

  @Test
  public void testWorldWithTreasure() {
    try {
      buildStrategy = new RandomMazeBuilder(10, 10, false, 1, 10);
    } catch (IllegalStateException e) {
      if (e.getMessage().contains("caves")) {
        //try to build the maze again.
        buildStrategy = new RandomMazeBuilder(10, 10, false, 1, 10);
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
    assertEquals((int) Math.ceil(numberOfCaves.get() * 10 / 100d), numberTreasure.get());
    assertFalse(isWrapped.getValue());
  }

  @Test
  public void testInterConnectedWorld() {
    buildStrategy = new RandomMazeBuilder(10, 10, false, 2, 30);
    world = buildStrategy.buildWorld();
    visitedNodes = new AtomicInteger(0);
    numberTreasure = new AtomicInteger(0);
    numberOfCaves = new AtomicInteger(0);
    isWrapped = new MutableBoolean(false);
    assertTrue(checkConnectivity(world, 10, 10, visitedNodes, numberTreasure,
            numberOfCaves, isWrapped));
    assertEquals(103, visitedNodes.get());
    assertEquals((int) Math.ceil(numberOfCaves.get() * 30 / 100d), numberTreasure.get());
    assertFalse(isWrapped.getValue());
  }

  @Test
  public void testLargerWorld() {
    buildStrategy = new RandomMazeBuilder(11, 11, false,
            2, 30);
    world = buildStrategy.buildWorld();
    visitedNodes = new AtomicInteger(0);
    numberTreasure = new AtomicInteger(0);
    numberOfCaves = new AtomicInteger(0);
    isWrapped = new MutableBoolean(false);
    assertTrue(checkConnectivity(world, 11, 11, visitedNodes, numberTreasure,
            numberOfCaves, isWrapped));
    assertEquals(124, visitedNodes.get());
    assertEquals((int) Math.ceil(numberOfCaves.get() * 30 / 100d), numberTreasure.get());
    assertFalse(isWrapped.getValue());
  }

  @Test
  public void testWrappedWorld() {

    buildStrategy = new RandomMazeBuilder(10, 10, true,
            2, 30);
    world = buildStrategy.buildWorld();
    visitedNodes = new AtomicInteger(0);
    numberTreasure = new AtomicInteger(0);
    numberOfCaves = new AtomicInteger(0);
    isWrapped = new MutableBoolean(false);
    assertTrue(checkConnectivity(world, 10, 10, visitedNodes, numberTreasure,
            numberOfCaves, isWrapped));
    assertEquals(103, visitedNodes.get());
    assertEquals((int) Math.ceil(numberOfCaves.get() * 30 / 100d), numberTreasure.get());
    assertTrue("World should be wrapped", isWrapped.getValue());
  }

  private boolean checkConnectivity(@NotNull GameWorld world, int maxRow, int maxCol,
                                    AtomicInteger degreeOfInterConnection,
                                    AtomicInteger numberOfTreasure, AtomicInteger numberOfCaves,
                                    MutableBoolean isWrapped) {
    Set<Location<?>> visited = new HashSet<>();

    DFS(world.getStartLocation(), visited, degreeOfInterConnection, numberOfTreasure,
            numberOfCaves, isWrapped, maxRow, maxCol);
    return visited.size() >= maxCol * maxRow;
  }

  private void DFS(@NotNull Location<?> source, @NotNull Set<Location<?>> visited,
                   AtomicInteger degreeOfInterConnection, AtomicInteger numberOfTreasure,
                   AtomicInteger numberOfCaves, MutableBoolean isWrapped, int maxRow, int maxCol) {
    visited.add(source);
    var adj = source.possibleActions().stream()
            .filter(action -> action instanceof MoveAction).collect(Collectors.toList());
    if (adj.size() != 2) {
      numberOfCaves.incrementAndGet();
    }
    if (!isWrapped.booleanValue()) {
      isWrapped.setValue(checkedWrapped(source, maxRow, maxCol));
    }
    var treasureAtLocation = source.possibleActions()
            .stream().anyMatch(action -> action instanceof PickItemAction);
    if (treasureAtLocation) {
      numberOfTreasure.incrementAndGet();
    }
    for (Action<?> moveAction :
            adj) {
      Location<?> neighbor = (Location<?>) moveAction.act()
              .orElseThrow(() -> new IllegalStateException("Neighbor not found"));
      if (!visited.contains(neighbor)) {
        visited.add(neighbor);
        DFS(neighbor, visited, degreeOfInterConnection,
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

  private boolean wrappedHelper(int checkCondition, @NotNull Location<?> current, boolean checkLeft) {
    var adjacent = current.possibleActions().stream()
            .filter(action -> action instanceof MoveAction).map(Action::act)
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