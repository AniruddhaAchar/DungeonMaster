package pdp.aniachar.dungeonmaster.item;

import pdp.aniachar.dungeonmaster.gameworld.IMazeLocation;
import pdp.aniachar.gamekit.Item;

/**
 * Represents an arrow.
 * An arrow has current location.
 */

public interface IArrow extends Item {

  /**
   * Gets the Arrow's current location.
   *
   * @return The arrows current location.
   */

  IMazeLocation getCurrentLocation();

  /**
   * Sets the new location of the arrow.
   *
   * @param whereTo The new location the arrow should go to.
   */

  void setCurrentLocation(IMazeLocation whereTo);
}
