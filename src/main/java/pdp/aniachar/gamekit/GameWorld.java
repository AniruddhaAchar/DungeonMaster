package pdp.aniachar.gamekit;

/**
 * Provides the functionalities for the world in which a {@link Character} can play.
 * A GameWorld would have multiple locations that a player can visit.
 * The GameWorld as well as the player keep track of the current location they are in.
 * The current location of the GameWorld can be different from the player's current location.
 * This is to implement direction in which the player is facing or mouse is pointing.
 * For simple implementation of a game they are the same.
 */

public interface GameWorld {

  /**
   * Gets the start location of the world.
   *
   * @return The start {@link Location}.
   */

  Location<?> getStartLocation();

  /**
   * Gets the end location of the world.
   *
   * @return The end {@link Location} the world.
   */

  Location<?> getEndLocation();

}
