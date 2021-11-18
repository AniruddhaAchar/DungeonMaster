package pdp.aniachar.dungeonmaster.gameworld;

import org.apache.commons.lang3.tuple.MutablePair;
import org.apache.commons.lang3.tuple.Pair;
import org.jetbrains.annotations.Contract;
import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Random;
import java.util.Set;
import java.util.concurrent.ThreadLocalRandom;
import java.util.stream.Collectors;

import pdp.aniachar.dungeonmaster.character.monster.Otyughs;
import pdp.aniachar.dungeonmaster.item.Arrow;
import pdp.aniachar.dungeonmaster.item.Treasure;
import pdp.aniachar.dungeonmaster.item.TreasureType;
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
 * These grids are built using {@link MazeLocation} only.
 */

public class RandomMazeBuilder implements WorldBuildStrategy {

  private static final int MIN_DISTANCE_BETWEEN_START_END = 5;
  private static final int MIN_GRID_SIZE = 10;
  private final int maxRows;
  private final int maxCols;
  private final boolean isWrapped;
  private final int percentTreasure;
  private final List<Set<IMazeLocation>> interConnectedLocations;
  private final List<Otyughs> otyughs;
  private long numberMonsters;
  private int degreeOfInterConnection;
  private Set<IMazeLocation> locationsWithPath;
  private Pair<IMazeLocation, IMazeLocation> startEndPoint;

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
                           int degreeOfInterConnection, int percentTreasure, long numberMonsters) {
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
    if (numberMonsters < 0) {
      throw new IllegalStateException("Cannot have negative number of Monsters");
    }
    this.percentTreasure = percentTreasure;
    this.maxRows = maxRows;
    this.maxCols = maxCols;
    this.isWrapped = isWrapped;
    this.degreeOfInterConnection = degreeOfInterConnection;
    interConnectedLocations = new ArrayList<>();
    locationsWithPath = new HashSet<>();
    this.numberMonsters = numberMonsters;
    otyughs = new ArrayList<>();
  }

  //region private
  private static TreasureType randomTreasure() {
    int x = new Random().nextInt((TreasureType.class).getEnumConstants().length);
    return (TreasureType.class).getEnumConstants()[x];
  }

  @Override
  public GameWorld buildWorld() {
    buildMaze();

    startEndPoint = getStartingEndPoint();
    addTreasure();
    addMonsters();
    addArrows();
    return new Maze(startEndPoint.getLeft(), startEndPoint.getRight(), locationsWithPath, otyughs);
  }

  private @NotNull List<IMazeLocation> buildAllNodes() {
    List<IMazeLocation> allNodes = new ArrayList<>();
    for (int row = 0; row < maxRows; row++) {
      for (int col = 0; col < maxCols; col++) {
        MazeLocation currLoc = new MazeLocation(new MutablePair<>(row, col));
        allNodes.add(currLoc);
        HashSet<IMazeLocation> tempSet = new HashSet<>();
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
  private @NotNull List<Pair<IMazeLocation, IMazeLocation>>
  buildAllPossibleAdjacentMazeLocations() {
    List<Pair<IMazeLocation, IMazeLocation>> adjList = new ArrayList<>();
    List<IMazeLocation> allNodes = buildAllNodes();
    for (int row = 0; row < maxRows; row++) {
      for (int col = 0; col < maxCols; col++) {
        int finalRow = row;
        int finalCol = col;
        IMazeLocation currLoc = allNodes.stream()
                .filter(node -> node.getLocationDescription()
                        .equals(new MutablePair<>(finalRow, finalCol)))
                .findFirst()
                .orElseThrow(() -> new IllegalStateException("Game is in a bad state."));
        // Set Adj Locations
        if (row < maxRows - 1 || isWrapped) {
          // add bottom node
          IMazeLocation bottom = allNodes.stream().filter(node -> node.getLocationDescription()
                          .equals(new MutablePair<>((finalRow + 1) % maxRows, finalCol)))
                  .findFirst()
                  .orElseThrow(() -> new IllegalStateException("Game is in a bad state."));
          Pair<IMazeLocation, IMazeLocation> bottomPair = new MutablePair<>(currLoc, bottom);
          adjList.add(bottomPair);
        }

        if (col < maxCols - 1 || isWrapped) {
          // add right node
          IMazeLocation right = allNodes.stream().filter(node -> node.getLocationDescription()
                          .equals(new MutablePair<>(finalRow, (finalCol + 1) % maxCols)))
                  .findFirst()
                  .orElseThrow(() -> new IllegalStateException("Game is in a bad state."));
          Pair<IMazeLocation, IMazeLocation> rightPair = new MutablePair<>(currLoc, right);
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

    List<Pair<IMazeLocation, IMazeLocation>> possibleAdj = buildAllPossibleAdjacentMazeLocations();
    List<Pair<IMazeLocation, IMazeLocation>> discardPair = new ArrayList<>();
    Set<IMazeLocation> locationsWithPath = new HashSet<>();
    Collections.shuffle(possibleAdj);
    for (Pair<IMazeLocation, IMazeLocation> mazeLocationPair :
            possibleAdj) {
      boolean areTheyConnected = false;

      for (Set<IMazeLocation> location :
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

  private void makeAdjacent(Pair<IMazeLocation, IMazeLocation> mazeLocationPair, boolean blind) {

    IMazeLocationBuilder.makeAdjacent(mazeLocationPair, maxRows, maxCols);

    if (!blind) {
      Set<IMazeLocation> setWithLeft = interConnectedLocations.stream()
              .filter(s -> s.contains(mazeLocationPair.getLeft())).findFirst().orElse(null);
      Set<IMazeLocation> setWithRight = interConnectedLocations.stream()
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

  @Contract(" -> new")
  private @NotNull Pair<IMazeLocation, IMazeLocation> getStartingEndPoint() {
    Set<IMazeLocation> temp = new HashSet<>(locationsWithPath);
    List<IMazeLocation> caves = temp.stream().filter(location -> location
            .getMoveAction().size() != 2).collect(Collectors.toList());
    IMazeLocation start = caves.stream()
            .skip(ThreadLocalRandom.current()
                    .nextInt(caves.size()))
            .findFirst().orElseThrow(() -> new IllegalStateException("No caves found"));
    Map<IMazeLocation, Integer> distanceToAllNodes;
    distanceToAllNodes = IMazeLocationBuilder.distanceToAllNodes(start);
    IMazeLocation possibleEnd = distanceToAllNodes.entrySet()
            .stream().filter(entry -> entry.getValue() >= MIN_DISTANCE_BETWEEN_START_END
                    && entry.getKey().getMoveAction().size() != 2)
            .findAny().orElseThrow(() -> new IllegalStateException("No end cave found")).getKey();
    return new MutablePair<>(start, possibleEnd);
  }


  private void addTreasure() {
    Set<IMazeLocation> allEmptyCaves = locationsWithPath.stream()
            .filter(location -> location.possibleActions().size() != 2)
            .collect(Collectors.toSet());
    int numCaves = allEmptyCaves.size();
    int cavesWithTreasure = (int) Math.ceil(numCaves * percentTreasure / 100d);
    while (cavesWithTreasure > 0) {
      MazeLocation randomCave = (MazeLocation) allEmptyCaves.stream()
              .skip(ThreadLocalRandom.current()
                      .nextInt(allEmptyCaves.size()))
              .findAny().orElseThrow();
      randomCave.addItem(new Treasure(randomTreasure()));
      allEmptyCaves.remove(randomCave);
      cavesWithTreasure--;
    }
  }

  private void addArrows() {
    Set<IMazeLocation> allEmptyLocation = new HashSet<>(locationsWithPath);
    int numLocations = allEmptyLocation.size();
    int locationsWithArrow = (int) Math.ceil(numLocations * percentTreasure / 100d);
    while (locationsWithArrow > 0) {
      MazeLocation randomCave = (MazeLocation) allEmptyLocation.stream()
              .skip(ThreadLocalRandom.current()
                      .nextInt(allEmptyLocation.size()))
              .findAny().orElseThrow();
      randomCave.addItem(new Arrow());
      allEmptyLocation.remove(randomCave);
      locationsWithArrow--;
    }
  }

  private void addMonsters() {
    otyughs.add(new Otyughs(startEndPoint.getRight(), "Ender"));
    if (numberMonsters != 0) {

      Set<IMazeLocation> allEmptyCaves = locationsWithPath.stream()
              .filter(location -> location.possibleActions().size() != 2)
              .collect(Collectors.toSet());
      allEmptyCaves.remove(startEndPoint.getRight());
      allEmptyCaves.remove(startEndPoint.getLeft());
      long numberOfCaves = locationsWithPath.stream()
              .filter(location -> location.possibleActions().size() != 2)
              .count() - 2;
      if (numberMonsters > numberOfCaves) {
        numberMonsters = numberOfCaves;
      }
      int i = 0;
      while (i < numberMonsters) {
        MazeLocation randomCave =
                (MazeLocation) allEmptyCaves.stream()
                        .skip(ThreadLocalRandom.current()
                                .nextInt(allEmptyCaves.size()))
                        .findAny().orElseThrow();
        allEmptyCaves.remove(randomCave);
        otyughs.add(new Otyughs(randomCave, String.format("Otyughs at %s", randomCave)));
        i++;
      }
    }
  }

  //endregion
}
