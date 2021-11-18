package pdp.aniachar.controller;

import java.util.Optional;

import pdp.aniachar.gamekit.Direction;
import pdp.aniachar.gamekit.Game;
import pdp.aniachar.gamekit.PlayerActionType;

/**
 * A command to let the model know that the user has requested for an arrow to be shot.
 */

public class ShootCommand extends AbstractPlayCommand {

  private final int distance;
  private final Direction direction;

  /**
   * Builds the shoot command.
   *
   * @param game      The model that this command should work with.
   * @param distance  The direction the arrow should travel.
   * @param direction The distance the arrow should travel.
   */

  protected ShootCommand(Game game, int distance, Direction direction) {
    super(game);
    this.distance = distance;
    this.direction = direction;
  }

  @Override
  public Optional<?> execute() {
    var builder = game.getAllPlayerAction().get(PlayerActionType.SHOOT_ARROW);
    var action = builder.withDirection(direction).withDistance(distance).build();
    return game.interact(action);
  }
}
