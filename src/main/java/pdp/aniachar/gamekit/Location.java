package pdp.aniachar.gamekit;

import org.jetbrains.annotations.NotNull;

import java.util.List;

/**
 * This interface specifies a specific location in the game world.
 * This provides ways in which a {@link Character} can interact with the location.
 *
 * @param <T> The implementation detail of how the location is represented.
 */

public interface Location<T> extends ImmutableLocation<T> {

  /**
   * Gets a {@link List} of all {@link Item}s that are in the current location.
   * If there are no {@link Item}s  or the location cannot hold items an empty {@link List}
   * is expected.
   *
   * @return {@link List} of all {@link Item}s that are in the current location.
   */
  List<Item> getItems();

  /**
   * Gets the possible {@link Action} that can be done at the location.
   *
   * @return The list of actions that can be done at this location.
   */
  List<Action<?>> possibleActions();

  /**
   * Makes a new copy of the Location object.
   *
   * @return A new copy of the location object.
   */
  Location<T> copy();

  /**
   * Removes an {@link Item} from the location.
   *
   * @param itemToRemove The {@link Item} to be removed.
   * @return true if item was removed, else false.
   */
  boolean removeItem(@NotNull Item itemToRemove);


}
