package pdp.aniachar.dungeonmaster.action.location;

import org.jetbrains.annotations.NotNull;

import pdp.aniachar.gamekit.Direction;
import pdp.aniachar.gamekit.Location;

/**
 * A builder pattern to build a {@link MoveAction}.
 */

public class MoveActionBuilder {
  private Location<?> whereTo;
  private Direction direction;

  /**
   * Sets the location to where the {@link Character} needs to move when the action is acted upon.
   *
   * @param whereTo location to where the {@link Character} needs to move when the action called.
   * @return this, this is a builder pattern.
   */
  public MoveActionBuilder setWhereTo(@NotNull Location<?> whereTo) {
    this.whereTo = whereTo;
    return this;
  }


  /**
   * Sets the direction in which the {@link Character} needs to move when the action is acted upon.
   *
   * @param direction direction in which the {@link Character} needs to move.
   * @return this, this is a builder pattern.
   */

  public MoveActionBuilder setDirection(@NotNull Direction direction) {
    this.direction = direction;
    return this;
  }

  /**
   * Creates the {@link MoveAction} action.
   *
   * @return A {@link MoveAction}
   */
  public MoveAction createMoveAction() {
    return new MoveAction(whereTo, direction);
  }
}