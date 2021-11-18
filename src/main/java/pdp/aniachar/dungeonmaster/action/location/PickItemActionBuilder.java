package pdp.aniachar.dungeonmaster.action.location;

import org.jetbrains.annotations.NotNull;

import pdp.aniachar.dungeonmaster.gameworld.MazeLocation;
import pdp.aniachar.gamekit.Action;
import pdp.aniachar.gamekit.ActionBuilder;
import pdp.aniachar.gamekit.Item;

/**
 * Creates a Pick Item Action object. This object picks up the specified
 * item from the specified location.
 */

public class PickItemActionBuilder implements ActionBuilder<Item> {
  private MazeLocation actionLocation;
  private Item itemToPick;

  /**
   * Sets the location from where the item needs to be picked from.
   *
   * @param actionLocation The location from where the item needs to be picked from.
   * @return itself as this is a builder.
   */

  public PickItemActionBuilder setActionLocation(@NotNull MazeLocation actionLocation) {
    this.actionLocation = actionLocation;
    return this;
  }

  /**
   * Sets the item to be picked.
   *
   * @param itemToPick The item to be picked.
   * @return itself as this is a builder.
   */
  public PickItemActionBuilder setItemToPick(@NotNull Item itemToPick) {
    this.itemToPick = itemToPick;
    return this;
  }

  /**
   * Creates a {@link PickItemAction} with the parameters.
   *
   * @return The created {@link PickItemAction}
   */
  public Action<?> createPickItemAction() {
    return new PickItemAction(actionLocation, itemToPick);
  }

  @Override
  public Action<Item> build() {
    return new PickItemAction(actionLocation, itemToPick);
  }
}