package pdp.aniachar.dungeonmaster.gameworld;

import org.apache.commons.lang3.tuple.Pair;

import java.util.List;
import java.util.Map;

import pdp.aniachar.dungeonmaster.action.location.MoveAction;
import pdp.aniachar.dungeonmaster.action.location.PickItemAction;
import pdp.aniachar.gamekit.ItemType;
import pdp.aniachar.gamekit.Location;

/**
 * A specific location that can be used in the maze.
 * A maze location will have smell associated with it.
 */

public interface IMazeLocation extends Location<Pair<Integer, Integer>> {

  /**
   * Gets the move actions that are at this location.
   *
   * @return ALl legal move actions that are in the location.
   */

  List<MoveAction> getMoveAction();

  /**
   * Gets all legal pick actions in the location.
   *
   * @return All legal pick action in the location.
   */

  Map<ItemType, PickItemAction> getPickItemActions();

  /**
   * Adds smell to a location.
   *
   * @param smellStrength The strength of smell to be added.
   */

  void addSmell(SmellStrength smellStrength);

  /**
   * Removes smell from the location.
   *
   * @param smellStrength The strength of the smell to be removed.
   */

  void removeSmell(SmellStrength smellStrength);

}
