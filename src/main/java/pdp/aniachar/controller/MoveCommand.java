package pdp.aniachar.controller;

import java.util.Map;
import java.util.Optional;

import pdp.aniachar.dungeonmaster.action.location.MoveAction;
import pdp.aniachar.gamekit.Direction;
import pdp.aniachar.gamekit.Game;

/**
 * Defines how a move command should be processes and what part of the model it should act on.
 */

public class MoveCommand extends AbstractPlayCommand {

  private final Direction direction;

  /**
   * Creates a new move command.
   *
   * @param dungeonGame The model that this command will interact with.
   * @param direction   The direction that the player should move.
   */

  public MoveCommand(Game dungeonGame, Direction direction) {
    super(dungeonGame);
    this.direction = direction;
  }

  @Override
  public Optional<?> execute() {
    Map<Direction, MoveAction> possibleMoves = game.getAllMoveActions();
    if (possibleMoves.containsKey(direction)) {
      return game.interact(possibleMoves.get(direction));
    }
    throw new IllegalArgumentException("Cannot move in the specified direction");
  }
}
