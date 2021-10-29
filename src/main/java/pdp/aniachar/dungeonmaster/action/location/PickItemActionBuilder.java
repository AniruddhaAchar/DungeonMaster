package pdp.aniachar.dungeonmaster.action.location;

import org.jetbrains.annotations.NotNull;

import pdp.aniachar.dungeonmaster.gameworld.MazeLocation;
import pdp.aniachar.gamekit.Action;
import pdp.aniachar.gamekit.Item;
public class PickItemActionBuilder {
  private @NotNull MazeLocation actionLocation;
  private @NotNull Item itemToPick;

  public PickItemActionBuilder setActionLocation(@NotNull MazeLocation actionLocation) {
    this.actionLocation = actionLocation;
    return this;
  }

  public PickItemActionBuilder setItemToPick(@NotNull Item itemToPick) {
    this.itemToPick = itemToPick;
    return this;
  }

  public Action<?> createPickItemAction() {
    return new PickItemAction(actionLocation, itemToPick);
  }
}