package pdp.aniachar.dungeonmaster.gameworld;

import org.apache.commons.lang3.tuple.MutablePair;
import org.apache.commons.lang3.tuple.Pair;
import org.jetbrains.annotations.Contract;
import org.jetbrains.annotations.NotNull;

import java.util.ArrayDeque;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Queue;
import java.util.Random;
import java.util.Set;
import java.util.concurrent.ThreadLocalRandom;
import java.util.stream.Collectors;

import pdp.aniachar.dungeonmaster.action.location.MoveAction;
import pdp.aniachar.dungeonmaster.action.location.MoveActionBuilder;
import pdp.aniachar.dungeonmaster.item.Treasure;
import pdp.aniachar.dungeonmaster.item.TreasureType;
import pdp.aniachar.gamekit.Action;
import pdp.aniachar.gamekit.Direction;
import pdp.aniachar.gamekit.GameWorld;
import pdp.aniachar.gamekit.WorldBuildStrategy;


/**
 * A strategy to build a random {@link Maze}.
 * In this strategy, it is assumed that a {@link Maze} is at minimum has a dimension of
 * {@link #MIN_GRID_SIZE} X {@link #MIN_GRID_SIZE}. Two {@link pdp.aniachar.gamekit.Location}s
 * are picked in the game that are at least {@link #MIN_DISTANCE_BETWEEN_START_END} apart.
 * The distance is calculated using the number of other locations that needs to be visited before
 * coming to the end.
 * The {@link Maze} is a graph that may mor may not be wrapped. When a {@link Maze} is wrapped,
 * there is a path between the edge nodes i.e. 0X0 may have a path to nX0 where
 * 'n' is the last node. Wrapping can be enabled by setting {@link #isWrapped}.
 * All {@link Maze}s built using this strategy will have at least one path from any location to
 * any other location. The number paths can be increased by giving a non-zero value to
 * {@link #degreeOfInterConnection}
 * {@link Maze}s can have {@link Treasure}s in them.
 * These {@link Treasure}s can only be
 * in caves (see {@link Maze}'s doc for explanation of what a cave is).
 * The percentage of caves with treasure is specified by {@link #percentTreasure}.
 * Caves can have multiple {@link Treasure} in them.
 */

public class RandomMazeBuilder implements WorldBuildStrategy {

  private static final int MIN_DISTANCE_BETWEEN_START_END = 5;
  private static final int MIN_GRID_SIZE = 10;
  private final int maxRows;
  private final int maxCols;
  private final boolean isWrapped;
  private final int percentTreasure;
  private final List<Set<MazeLocation>> interConnectedLocations;
  private int degreeOfInterConnection;
  private Set<MazeLocation> locationsWithPath;

  /**
   * Builds a random {@link Maze} with the specification below.
   *
   * @param maxRows                 The maximum number of rows the maze can have.
   * @param maxCols                 The maximum number of columns the maze can have.
   * @param isWrapped               If the {@link Maze} should be wrapped or not.
   * @param degreeOfInterConnection The number of paths that must between locations.
   *                                Should be a non-negative number.
   * @param percentTreasure         The percentage of caves with treasures.
   * @see RandomMazeBuilder Explination of parameters.
   */

  public RandomMazeBuilder(int maxRows, int maxCols, boolean isWrapped,
                           int degreeOfInterConnection, int percentTreasure) {
    if (percentTreasure > 100 || percentTreasure < 0) {
      throw new IllegalArgumentException(
              "percentTreasure cannot be greater than 100 or less than 0");
    }
    if (maxCols < MIN_GRID_SIZE || maxRows < MIN_GRID_SIZE) {
      throw new IllegalArgumentException(String
              .format("Rows and columns should be at least %d.", MIN_GRID_SIZE));
    }
    if (degreeOfInterConnection < 0) {
      throw new IllegalArgumentException("Degree of interconnectivity cannot be negative");
    }
    this.percentTreasure = percentTreasure;
    this.maxRows = maxRows;
    this.maxCols = maxCols;
    this.isWrapped = isWrapped;
    this.degreeOfInterConnection = degreeOfInterConnection;
    interConnectedLocations = new ArrayList<>();
    locationsWithPath = new HashSet<>();
  }

  //region private
  private static TreasureType randomTreasure() {
    int x = new Random().nextInt((TreasureType.class).getEnumConstants().length);
    return (TreasureType.class).getEnumConstants()[x];
  }

  @Override
  public GameWorld buildWorld() {
    buildMaze();
    Pair<MazeLocation, MazeLocation> startEndPoint = getStartingEndPoint();
    addTreasure();
    return new Maze(startEndPoint.getLeft(), startEndPoint.getRight(), locationsWithPath);
  }

  private @NotNull List<MazeLocation> buildAllNodes() {
    List<MazeLocation> allNodes = new ArrayList<>();
    for (int row = 0; row < maxRows; row++) {
      for (int col = 0; col < maxCols; col++) {
        var currLoc = new MazeLocation(new MutablePair<>(row, col));
        allNodes.add(currLoc);
        var tempSet = new HashSet<MazeLocation>();
        tempSet.add(currLoc);
        interConnectedLocations.add(tempSet);
      }
    }
    return allNodes;
  }

  /**
   * Generates a list of all possible adjacent nodes that can be present in the {@link Maze}.
   * This is to guarantee there is a path between locations.
   * Many of the paths are dropped when a "spanning tree" is constructed.
   *
   * @return A list of all possible adjacent {@link MazeLocation}s
   */
  @Contract(pure = true)
  private @NotNull List<Pair<MazeLocation, MazeLocation>> buildAllPossibleAdjacentMazeLocations() {
    List<Pair<MazeLocation, MazeLocation>> adjList = new ArrayList<>();
    List<MazeLocation> allNodes = buildAllNodes();
    for (int row = 0; row < maxRows; row++) {
      for (int col = 0; col < maxCols; col++) {
        int finalRow = row;
        int finalCol = col;
        var currLoc = allNodes.stream()
                .filter(node -> node.getLocationDescription()
                        .equals(new MutablePair<>(finalRow, finalCol)))
                .findFirst()
                .orElseThrow(() -> new IllegalStateException("Game is in a bad state."));
        // Set Adj Locations
        if (row < maxRows - 1 || isWrapped) {
          // add bottom node
          MazeLocation bottom = allNodes.stream().filter(node -> node.getLocationDescription()
                          .equals(new MutablePair<>((finalRow + 1) % maxRows, finalCol)))
                  .findFirst()
                  .orElseThrow(() -> new IllegalStateException("Game is in a bad state."));
          var bottomPair = new MutablePair<>(currLoc, bottom);
          adjList.add(bottomPair);
        }

        if (col < maxCols - 1 || isWrapped) {
          // add right node
          MazeLocation right = allNodes.stream().filter(node -> node.getLocationDescription()
                          .equals(new MutablePair<>(finalRow, (finalCol + 1) % maxCols)))
                  .findFirst()
                  .orElseThrow(() -> new IllegalStateException("Game is in a bad state."));
          var rightPair = new MutablePair<>(currLoc, right);
          adjList.add(rightPair);
        }
      }
    }
    return adjList;
  }

  /**
   * Builds a {@link Maze} with no treasures using Kruskal's algorithm to build a minimum tree.
   * Here all the paths are considered to be of same weight,
   * and we are essentially building a graph where there is exactly one path between any two
   * locations.
   */

  private void buildMaze() {

    List<Pair<MazeLocation, MazeLocation>> possibleAdj = buildAllPossibleAdjacentMazeLocations();
    List<Pair<MazeLocation, MazeLocation>> discardPair = new ArrayList<>();
    Set<MazeLocation> locationsWithPath = new HashSet<>();
    Collections.shuffle(possibleAdj);
    for (Pair<MazeLocation, MazeLocation> mazeLocationPair :
            possibleAdj) {
      boolean areTheyConnected = false;

      for (Set<MazeLocation> location :
              interConnectedLocations) {
        /*
         Check if the two locations are connected.
         They can be either directly connected or they can have intermediate
         nodes that can connect them.
        */

        if (location.contains(mazeLocationPair.getLeft())
                && location.contains(mazeLocationPair.getRight())) {
          areTheyConnected = true;
        }
      }

      if (!areTheyConnected) {
        makeAdjacent(mazeLocationPair, false);
        locationsWithPath.add(mazeLocationPair.getLeft());
        locationsWithPath.add(mazeLocationPair.getRight());
      } else {
        discardPair.add(mazeLocationPair);
      }
    }
    if (degreeOfInterConnection > 0) {
      if (degreeOfInterConnection > discardPair.size()) {
        degreeOfInterConnection = discardPair.size();
      }
      for (int i = 0; i < degreeOfInterConnection; i++) {
        int indexToRemove = new Random().nextInt(discardPair.size());
        makeAdjacent(discardPair.get(indexToRemove), true);
        discardPair.remove(indexToRemove);
      }
    }
    this.locationsWithPath = locationsWithPath;
  }

  /**
   * Makes the two pairs adjacent. This is done by adding an action to move from one
   * location to the other and vic versa.
   *
   * @param mazeLocationPair The two nodes that needs to have the location added.
   * @param blind            This method is used to build a set as well.
   *                         To prevent the set expansion, we can use a "blind" add.
   */

  private void makeAdjacent(Pair<MazeLocation, MazeLocation> mazeLocationPair, boolean blind) {
    Pair<Direction, Direction> moveDirection = getDirectionsForLocation(mazeLocationPair);
    var left = mazeLocationPair.getLeft();
    left.addMoveAction((MoveAction) new MoveActionBuilder()
            .setWhereTo(mazeLocationPair.getRight())
            .setDirection(moveDirection.getRight()).createMoveAction());
    var right = mazeLocationPair.getRight();
    right.addMoveAction((MoveAction) new MoveActionBuilder()
            .setWhereTo(mazeLocationPair.getLeft())
            .setDirection(moveDirection.getLeft())
            .createMoveAction());
    if (!blind) {
      var setWithLeft = interConnectedLocations.stream()
              .filter(s -> s.contains(mazeLocationPair.getLeft())).findFirst().orElse(null);
      var setWithRight = interConnectedLocations.stream()
              .filter(s -> s.contains(mazeLocationPair.getRight())).findFirst().orElse(null);
      assert setWithLeft != null;
      assert setWithRight != null;
      if (setWithLeft.size() > setWithRight.size()) {
        interConnectedLocations.remove(setWithRight);
        setWithLeft.addAll(setWithRight);
      } else {
        interConnectedLocations.remove(setWithLeft);
        setWithRight.addAll(setWithLeft);
      }
    }
  }


  Pair<Direction, Direction> getDirectionsForLocation
          (@NotNull Pair<MazeLocation, MazeLocation> mazeLocationPair) {
    if (mazeLocationPair.getRight().getLocationDescription().getLeft() ==
            (mazeLocationPair.getLeft().getLocationDescription().getLeft() + 1) % maxRows) {
      return new MutablePair<>(Direction.NORTH, Direction.SOUTH);
    }
    return new MutablePair<>(Direction.WEST, Direction.EAST);
  }

  @Contract(" -> new")
  private @NotNull Pair<MazeLocation, MazeLocation> getStartingEndPoint() {
    var temp = new HashSet<>(locationsWithPath);
    var caves = temp.stream().filter(location -> location
            .getAllMoveActions().size() != 2).collect(Collectors.toList());
    var start = caves.stream()
            .skip(ThreadLocalRandom.current()
                    .nextInt(caves.size()))
            .findFirst().orElseThrow(() -> new IllegalStateException("No caves found"));
    caves.remove(start);
    var possibleEnd = caves.stream()
            .skip(ThreadLocalRandom.current()
                    .nextInt(caves.size()))
            .findAny().orElseThrow(() -> new IllegalStateException("No caves found"));

    while (distanceBetweenNodes(start, possibleEnd) < MIN_DISTANCE_BETWEEN_START_END) {
      caves.remove(possibleEnd);
      possibleEnd = temp.stream()
              .filter(location -> location.possibleActions().size() != 2)
              .skip(ThreadLocalRandom.current()
                      .nextInt(temp.size()))
              .findAny().orElseThrow(() -> new IllegalStateException("No caves found"));
    }
    if (temp.size() == 0) {
      throw new IllegalStateException("Cannot build a path");
    }
    return new MutablePair<>(start, possibleEnd);
  }


  /**
   * Calculates the distance between any two {@link MazeLocation}s
   *
   * @param start The starting location.
   * @param end   The destination.
   * @return The number of location from start to end. If not connected {@link Integer#MIN_VALUE}
   */

  private int distanceBetweenNodes(MazeLocation start, MazeLocation end) {
    var distanceMap = populateDistances(locationsWithPath);
    distanceMap.put(start, 0);
    Queue<MazeLocation> queue = new ArrayDeque<>();
    Set<MazeLocation> visited = new HashSet<>();
    queue.add(start);
    while (!queue.isEmpty()) {
      var currentLocation = queue.remove();
      if (currentLocation.equals(end)) {
        return distanceMap.get(currentLocation);
      } else {
        visited.add(currentLocation);
        for (Action<?> action :
                currentLocation.possibleActions()) {
          MoveAction moveAction = (MoveAction) action;
          var adjLocation = (MazeLocation) moveAction.act().orElseThrow(() ->
                  new IllegalStateException
                          ("Game is in the bad state. Trying to determine distance between node."));
          distanceMap.put(adjLocation,
                  distanceMap.get(currentLocation) + 1);
          queue.add((MazeLocation) moveAction.act().orElseThrow(() ->
                  new IllegalStateException
                          ("Game is in the bad state. Trying to determine distance between node.")
          ));
        }
        queue.removeAll(visited);
      }
    }
    return Integer.MIN_VALUE;
  }

  private void addTreasure() {
    var caves = locationsWithPath.stream()
            .filter(location -> location.possibleActions().size() != 2)
            .collect(Collectors.toList());
    int numCaves = caves.size();
    int cavesWithTreasure = (int) Math.ceil(numCaves * percentTreasure / 100d);

    while (cavesWithTreasure > 0) {
      var randomCave = caves.stream()
              .skip(ThreadLocalRandom.current()
                      .nextInt(caves.size()))
              .findAny().orElseThrow();
      randomCave.addItem(new Treasure(randomTreasure()));
      if (randomCave.getItems().size() == 1) {
        cavesWithTreasure--;
      }
    }
  }

  /**
   * Makes all locations "infinitely" far from one other.
   *
   * @param allLocations The set of whose distance needs to be calculated.
   * @return The Map of node and the distance between them.
   */

  private @NotNull Map<MazeLocation, Integer> populateDistances
  (@NotNull Set<MazeLocation> allLocations) {
    Map<MazeLocation, Integer> distanceMap = new HashMap<>();
    for (MazeLocation location :
            allLocations) {
      distanceMap.put(location, Integer.MAX_VALUE);
    }
    return distanceMap;
  }
  //endregion


}
