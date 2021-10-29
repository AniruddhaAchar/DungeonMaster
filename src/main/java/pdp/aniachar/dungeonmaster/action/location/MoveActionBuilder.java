package pdp.aniachar.dungeonmaster.action.location;

import org.jetbrains.annotations.NotNull;

import pdp.aniachar.gamekit.Action;
import pdp.aniachar.gamekit.Direction;
import pdp.aniachar.gamekit.Location;
public class MoveActionBuilder {
  private @NotNull Location<?> whereTo;
  private @NotNull Direction direction;

  public MoveActionBuilder setWhereTo(@NotNull Location<?> whereTo) {
    this.whereTo = whereTo;
    return this;
  }

  public MoveActionBuilder setDirection(@NotNull Direction direction) {
    this.direction = direction;
    return this;
  }

  public Action<?> createMoveAction() {
    return new MoveAction(whereTo, direction);
  }
}