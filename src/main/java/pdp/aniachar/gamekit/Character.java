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
 *
 */

public interface Character {
  /**
   * Tries to move the character to the new position.
   *
   * @param whereTo The new location where the character should be moved to.
   * @return True if the player can be moved to the specified location, else false.
   */
  boolean makeMove(Location<?> whereTo);

  /**
   * Gets the current location that the character is in.
   *
   * @return The current {@link Location} the payer is in.
   */
  Location<?> getCurrentLocation();

  /**
   * Tries to take an item.
   * If a Character can possess that item, it will be assigned to the player,
   * if the player cannot possess the item, it will not be assigned.
   *
   * @param item The item to be taken.
   * @return True if the assignment was a success, else false.
   */

  boolean takeItem(@NotNull Item item);

  /**
   * Gets the {@link List} of all items that a Character possesses.
   *
   * @return A {@link List} of all items possessed by the player.
   */
  List<Item> getAllItems();

  /**
   * Gets the name of the character.
   * This should not be used as a unique identifier.
   * Unique identifiers should be implemented in the classes that implement the Character.
   *
   * @return {@link String} name of the character.
   */
  String getName();
}
