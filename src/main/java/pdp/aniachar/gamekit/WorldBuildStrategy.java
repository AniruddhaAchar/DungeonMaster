package pdp.aniachar.gamekit;

/**
 * A strategy to build the {@link GameWorld}.
 * The implementer decides how the world should be built and what items need
 * to be populated in the world.
 * To build the world, call {@link #buildWorld()}.
 */

public interface WorldBuildStrategy {

  /**
   * Builds the world as defined by the implementer.
   *
   * @return The {@link GameWorld} that was built.
   */

  GameWorld buildWorld();
}
