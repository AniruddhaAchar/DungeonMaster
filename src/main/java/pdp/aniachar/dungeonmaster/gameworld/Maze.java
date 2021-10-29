package pdp.aniachar.dungeonmaster.gameworld;

import java.util.Set;

import pdp.aniachar.gamekit.GameWorld;
import pdp.aniachar.gamekit.Location;

/**
 * Represents the world in which dungeonMaster is set.
 * A maze is a 2D grid. A {@link Location} can be a tunnel that connects two caves.
 * For a {@link Location} on the grid to be tunneled it should have exactly 2 entrances or exits.
 * A cave on the other hand is any {@link Location} that doesn't have two entrances or exits.
 * All the locations in the Maze are underpinned by the {@link MazeLocation}.
 * A Maze will have a start and an end location.
 */

public class Maze implements GameWorld {

  private final MazeLocation startLocation;
  private final MazeLocation endLocation;


  Maze(MazeLocation startLocation, MazeLocation endLocation, Set<MazeLocation> mazeLocations) {
    this.startLocation = startLocation;
    this.endLocation = endLocation;
  }

  @Override
  public Location<?> getStartLocation() {
    return startLocation;
  }

  @Override
  public Location<?> getEndLocation() {
    return endLocation;
  }
}
