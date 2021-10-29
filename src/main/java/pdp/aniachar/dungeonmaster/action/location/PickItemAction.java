package pdp.aniachar.dungeonmaster.action.location;

import org.jetbrains.annotations.NotNull;

import java.util.Optional;

import pdp.aniachar.dungeonmaster.gameworld.MazeLocation;
import pdp.aniachar.gamekit.Character;
import pdp.aniachar.gamekit.Item;

/**
 * Picks up an item that is present at the current location.
 * When "acted upon", the item to be picked is returned. The item is also removed from the location.
 */

public class PickItemAction implements LocationAction<Item> {

  private final MazeLocation actionLocation;
  private final Item itemToPick;

  /**
   * Creates a Pick Item Action object. This object picks up the specified
   * item from the specified location.
   *
   * @param actionLocation The location from where the item needs to be picked from.
   * @param itemToPick     The item to be picked.
   */
  public PickItemAction(@NotNull MazeLocation actionLocation, @NotNull Item itemToPick) {
    this.actionLocation = actionLocation;
    this.itemToPick = itemToPick;
  }

  @Override
  public Optional<Item> act() {
    if (actionLocation.removeItem(itemToPick)) {
      return Optional.of(itemToPick);
    }
    return Optional.empty();
  }

  @Override
  public String describe() {
    return String.format("\nCan pick up %s here\n", itemToPick);
  }

  @Override
  public String toString() {
    return describe();
  }

  @Override
  public Optional<Boolean> actOn(Character character) {
    Item itemToAssign = act().orElse(null);
    if (itemToAssign == null) {
      return Optional.of(false);
    }
    Boolean result = character.takeItem(itemToAssign);
    return Optional.of(result);
  }
}
