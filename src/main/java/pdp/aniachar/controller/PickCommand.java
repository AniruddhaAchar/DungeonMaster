package pdp.aniachar.controller;

import java.util.Optional;

import pdp.aniachar.gamekit.Game;
import pdp.aniachar.gamekit.ItemType;

/**
 * Defines how a pick command should interact with the model.
 */

public class PickCommand extends AbstractPlayCommand {

  private final ItemType itemType;

  protected PickCommand(Game game, ItemType itemType) {
    super(game);
    this.itemType = itemType;
  }

  @Override
  public Optional<?> execute() {
    var possibleAction = game.getAllPickActions();
    if (!possibleAction.containsKey(itemType)) {
      throw new IllegalArgumentException("No such items present.");
    }
    return game.interact(possibleAction.get(itemType));
  }
}
