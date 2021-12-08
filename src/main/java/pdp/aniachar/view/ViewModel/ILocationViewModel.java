package pdp.aniachar.view.ViewModel;

import java.util.Map;

import pdp.aniachar.dungeonmaster.action.location.MoveAction;
import pdp.aniachar.dungeonmaster.action.location.PickItemAction;
import pdp.aniachar.gamekit.Direction;
import pdp.aniachar.gamekit.ImmutableLocation;
import pdp.aniachar.gamekit.ItemType;
import pdp.aniachar.view.IView;

/**
 * Describes the location's view model that will be consumed by the {@link IView}.
 */

public interface ILocationViewModel {

  /**
   * Gets all the possible moves that the player can make.
   *
   * @return The possible moves and the directions.
   */

  Map<Direction, MoveAction> getPossibleMoves();

  /**
   * Gets the current location of the player.
   *
   * @return The current location of the player.
   */

  ImmutableLocation<?> getCurrentLocation();

  /**
   * Gets all the items that can be picked at the current location.
   *
   * @return The possible items that can be picked at the location.
   */

  Map<ItemType, PickItemAction> getItems();
}
