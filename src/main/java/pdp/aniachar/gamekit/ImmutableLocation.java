package pdp.aniachar.gamekit;

import pdp.aniachar.dungeonmaster.gameworld.SmellStrength;

/**
 * Read only definition of a location.
 *
 * @param <T> The type of data that the location will store.
 */

public interface ImmutableLocation<T> {

  /**
   * Gets the description of the location.
   *
   * @return The description of the location.
   */
  T getLocationDescription();

  /**
   * Gets the {@link SmellStrength} at that location.
   *
   * @return The {@link SmellStrength} at this location.
   */

  SmellStrength getSmellStrength();


}
