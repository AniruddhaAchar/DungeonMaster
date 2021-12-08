package pdp.aniachar.gamekit;

import org.jetbrains.annotations.NotNull;

import java.util.List;

/**
 * A character is an interactive element with which a user can play.
 * Characters can be playable characters or non-playable characters.
 * All characters will have a location that are in the {@link GameWorld}.
 * All characters can be moved from current location to a new location
 * if there is a path for the character to move in the {@link GameWorld}.
 * All characters will have a name.
 * Characters can pick up {@link Item}s that are in the {@link GameWorld}'s current location.
 * Characters also should have ability to provide a {@link List} of all the items that they
 * currently possess.
 */

public interface Character extends ImmutableCharacter {
  /**
   * Tries to move the character to the new position.
   *
   * @param whereTo The new location where the character should be moved to.
   * @return True if the player can be moved to the specified location, else false.
   */
  boolean makeMove(Location<?> whereTo);

  /**
   * Tries to take an item.
   * If a Character can possess that item, it will be assigned to the player,
   * if the player cannot possess the item, it will not be assigned.
   *
   * @param item The item to be taken.
   * @return True if the assignment was a success, else false.
   */

  boolean takeItem(@NotNull Item item) throws NoSuchMethodException;


  /**
   * Gets the current health of the character.
   *
   * @return The current health of the character.
   */

  double getCurrentHealth();


  /**
   * Changes the health of a character by the {@code changeBy} value.
   * Health of a character may be increased or decreased here.
   *
   * @param changeBy The value by which the health of the character is to be changed.
   */

  void changeHealth(double changeBy);

  /**
   * Brings the character back to life.
   */

  void bringBackToLife();
}
