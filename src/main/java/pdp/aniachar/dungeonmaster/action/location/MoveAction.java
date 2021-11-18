package pdp.aniachar.dungeonmaster.action.location;

import org.apache.commons.lang3.builder.EqualsBuilder;
import org.apache.commons.lang3.builder.HashCodeBuilder;
import org.jetbrains.annotations.Contract;
import org.jetbrains.annotations.NotNull;

import java.util.Optional;

import pdp.aniachar.gamekit.Character;
import pdp.aniachar.gamekit.Direction;
import pdp.aniachar.gamekit.Location;

/**
 * A possible move action that can be done at a specific location.
 * When this is called i.e. "acted upon", the new location to which to move is returned.
 */

public class MoveAction implements IMoveAction {
  private final Location<?> whereTo;
  private final Direction direction;

  /**
   * Creates a MoveAction. When a move action's act is called,
   * the new location to be moved to is returned.
   *
   * @param whereTo   The new location to move to.
   * @param direction The {@link Direction} the new location is with
   *                  respect to the current location.
   */

  @Contract(pure = true)
  MoveAction(@NotNull Location<?> whereTo, @NotNull Direction direction) {
    this.whereTo = whereTo;
    this.direction = direction;
  }

  @Override
  public Optional<Location<?>> act() {
    return Optional.of(whereTo);
  }

  @Override
  public String describe() {
    return String.format("\nCan move %s and you will be at %s\n", direction, whereTo);
  }

  @Override
  public String toString() {
    return describe();
  }

  @Override
  public boolean equals(Object o) {
    if (this == o) {
      return true;
    }

    if (o == null || getClass() != o.getClass()) {
      return false;
    }

    MoveAction that = (MoveAction) o;

    return new EqualsBuilder().append(direction, that.direction).isEquals();
  }

  @Override
  public int hashCode() {
    return new HashCodeBuilder(17, 37).append(direction).toHashCode();
  }

  @Override
  public Optional<Boolean> actOn(Character character) {
    Boolean result = character.makeMove(whereTo);
    return Optional.of(result);
  }

  @Override
  public Direction getDirection() {
    return direction;
  }
}
