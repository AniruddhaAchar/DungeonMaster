package pdp.aniachar.view.ViewModel;

import org.jetbrains.annotations.NotNull;

import java.util.Map;

import lombok.AllArgsConstructor;
import pdp.aniachar.dungeonmaster.action.location.MoveAction;
import pdp.aniachar.dungeonmaster.action.location.PickItemAction;
import pdp.aniachar.gamekit.Direction;
import pdp.aniachar.gamekit.ImmutableLocation;
import pdp.aniachar.gamekit.ItemType;

/**
 * Defines and implements the characters of the Location view model that will be consumed by the
 * view.
 */

@AllArgsConstructor
public class LocationViewModel implements ILocationViewModel {
  @lombok.Getter
  @NotNull
  private final Map<Direction, MoveAction> possibleMoves;
  @lombok.Getter
  @NotNull
  private final ImmutableLocation<?> currentLocation;
  @lombok.Getter
  @NotNull
  private final Map<ItemType, PickItemAction> items;
}
