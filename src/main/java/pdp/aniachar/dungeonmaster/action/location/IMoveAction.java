package pdp.aniachar.dungeonmaster.action.location;

import pdp.aniachar.gamekit.Direction;
import pdp.aniachar.gamekit.Location;

/**
 * A specific move action's definition.
 * A move action will have a specific direction associated with it.
 */

public interface IMoveAction extends LocationAction<Location<?>> {
  /**
   * Gets the direction the player should move in this action.
   *
   * @return The direction the player should move in this action.
   */
  Direction getDirection();
}
