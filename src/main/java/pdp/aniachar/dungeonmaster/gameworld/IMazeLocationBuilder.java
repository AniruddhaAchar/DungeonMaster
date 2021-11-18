package pdp.aniachar.dungeonmaster.gameworld;

import org.apache.commons.lang3.tuple.MutablePair;
import org.apache.commons.lang3.tuple.Pair;
import org.jetbrains.annotations.NotNull;

import java.util.ArrayDeque;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Queue;
import java.util.Set;

import pdp.aniachar.dungeonmaster.action.location.MoveAction;
import pdp.aniachar.dungeonmaster.action.location.MoveActionBuilder;
import pdp.aniachar.gamekit.Direction;
import pdp.aniachar.gamekit.Item;

/**
 * A {@link IMazeLocation} builder class.
 * Provides some generic methods to work on {@link IMazeLocation}.
 */

public class IMazeLocationBuilder {

  private final MazeLocation mazeLocation;

  /**
   * Generates an objet of the builder.
   *
   * @param gridLocation The maze location where the {@link IMazeLocation} should be initialized.
   */

  public IMazeLocationBuilder(Pair<Integer, Integer> gridLocation) {
    mazeLocation = new MazeLocation(gridLocation);
  }

  /**
   * Gets the direction to travel from one location to another.
   *
   * @param mazeLocationPair The locations we need to get directions between.
   * @param maxRow           The max row size of the maze.
   * @param maxCol           The max col size of the maze.
   * @return The directions to travel between the provided location.
   */

  static Pair<Direction, Direction> getDirectionsForLocation(
          @NotNull Pair<IMazeLocation, IMazeLocation> mazeLocationPair, int maxRow, int maxCol) {

    Pair<Integer, Integer> loc1 = mazeLocationPair.getRight().getLocationDescription();
    Pair<Integer, Integer> loc2 = mazeLocationPair.getLeft().getLocationDescription();

    if (loc1.equals(loc2)) {
      throw new IllegalArgumentException("Cannot make same locations adjacent");
    }
    if (loc1.getLeft() == (loc2.getLeft() + 1) % maxRow) {
      return new MutablePair<>(Direction.NORTH, Direction.SOUTH);
    }
    if ((loc1.getLeft() + 1) % maxRow == loc2.getLeft()) {
      return new MutablePair<>(Direction.SOUTH, Direction.NORTH);
    }
    if ((loc1.getRight() + 1) % maxCol == loc2.getRight()) {
      return new MutablePair<>(Direction.EAST, Direction.WEST);
    }
    if ((loc2.getRight() + 1) % maxCol == loc1.getRight()) {
      return new MutablePair<>(Direction.WEST, Direction.EAST);
    }
    throw new IllegalArgumentException("Cannot make locations adjacent");
  }

  /**
   * Makes two {@link IMazeLocation} adjacent.
   *
   * @param mazeLocationPair The two locations that needs to be paired.
   * @param maxRow           The max row size of the maze.
   * @param maxCol           The max column size of the maze.
   */

  public static void makeAdjacent(Pair<IMazeLocation, IMazeLocation> mazeLocationPair, int maxRow,
                                  int maxCol) {
    Pair<Direction, Direction> moveDirection = getDirectionsForLocation(mazeLocationPair, maxRow,
            maxCol);
    MazeLocation left = (MazeLocation) mazeLocationPair.getLeft();
    left.addMoveAction(new MoveActionBuilder()
            .setWhereTo(mazeLocationPair.getRight())
            .setDirection(moveDirection.getRight()).createMoveAction());
    MazeLocation right = (MazeLocation) mazeLocationPair.getRight();
    right.addMoveAction(new MoveActionBuilder()
            .setWhereTo(mazeLocationPair.getLeft())
            .setDirection(moveDirection.getLeft())
            .createMoveAction());
  }

  /**
   * Calculates the distance to all nodes from the given node.
   * This uses breath first search to traverse the maze.
   *
   * @param startLocation The start location.
   * @return The map of location and distance from start location.
   */

  public static Map<IMazeLocation, Integer> distanceToAllNodes(IMazeLocation startLocation) {
    var distanceMap = new HashMap<IMazeLocation, Integer>();
    distanceMap.put(startLocation, 0);
    Queue<IMazeLocation> queue = new ArrayDeque<>();
    Set<IMazeLocation> visited = new HashSet<>();
    queue.add(startLocation);
    while (!queue.isEmpty()) {
      IMazeLocation currentLocation = queue.remove();

      visited.add(currentLocation);
      for (MoveAction action :
              currentLocation.getMoveAction()) {
        var adjLocation = (IMazeLocation) action.act().orElseThrow();
        distanceMap.computeIfAbsent(adjLocation,
                value -> distanceMap.get(currentLocation) + 1);
        queue.add((IMazeLocation) action.act().orElseThrow());
      }
      queue.removeAll(visited);
    }
    return distanceMap;
  }


  /**
   * Adds treasure to the given location.
   *
   * @param treasure The treasure to be added.
   * @return this. This is a builder.
   */

  public IMazeLocationBuilder addTreasure(Item treasure) {
    mazeLocation.addItem(treasure);
    return this;
  }

  /**
   * Adds an arrow to the given location.
   *
   * @param arrow The arrow to be added.
   * @return this. This is a builder.
   */

  public IMazeLocationBuilder addArrow(Item arrow) {
    mazeLocation.addItem(arrow);
    return this;
  }

  /**
   * Builds the given location.
   *
   * @return the {@link IMazeLocation} that was built.
   */
  public IMazeLocation build() {
    return mazeLocation;
  }


}
