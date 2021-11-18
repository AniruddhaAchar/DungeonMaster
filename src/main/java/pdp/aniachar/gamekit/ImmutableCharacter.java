package pdp.aniachar.gamekit;


import java.util.List;

/**
 * A read only character.
 */

public interface ImmutableCharacter {

  /**
   * Gets the current location that the character is in.
   *
   * @return The current {@link Location} the payer is in.
   */
  ImmutableLocation<?> getCurrentLocation();

  /**
   * Gets the name of the character.
   * This should not be used as a unique identifier.
   * Unique identifiers should be implemented in the classes that implement the Character.
   *
   * @return {@link String} name of the character.
   */
  String getName();

  /**
   * Gets the current health of the character.
   *
   * @return The current health of the character.
   */

  double getCurrentHealth();

  /**
   * Gets the {@link List} of all items that a Character possesses.
   *
   * @return A {@link List} of all items possessed by the player.
   */
  List<Item> getAllItems() throws NoSuchMethodException;

}
