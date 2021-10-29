package pdp.aniachar.gamekit;

import org.jetbrains.annotations.NotNull;
/**
 * Represents the in game items that a player can interact with.
 * Items can be something the player can take, can use or consume.
 * should always return true.
 */

public interface Item {
  /**
   * Assigns a given item to a character.
   * If an item cannot be assigned, this should return false.
   *
   * @param character The {@link Character} to whom this item should be assigned to.
   * @return True if the item was assigned, else false.
   */
  boolean assignToCharacter(@NotNull Character character);


  /**
   * Gets the name of the item.
   *
   * @return The name of the item.
   */
  String getName();
}
