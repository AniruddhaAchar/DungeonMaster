package pdp.aniachar.dungeonmaster.item;

import org.jetbrains.annotations.NotNull;

import pdp.aniachar.dungeonmaster.action.character.PlayActionBuilder;
import pdp.aniachar.gamekit.Direction;

/**
 * A builder to build a dynamic shoot action.
 */

public class ShootArrowActionBuilder implements PlayActionBuilder {

  private Arrow arrow;
  private Direction direction;
  private int distance;

  /**
   * The arrow that will be used to shoot. This is optional.
   *
   * @param arrow The arrow that will be used to shoot.
   * @return The builder.
   */

  public ShootArrowActionBuilder withArrow(@NotNull Arrow arrow) {
    this.arrow = arrow;
    return this;
  }

  /**
   * The direction that the arrow should be shot.
   *
   * @param direction The direction the arrow will be shot.
   * @return The builder.
   */
  public ShootArrowActionBuilder withDirection(@NotNull Direction direction) {
    this.direction = direction;
    return this;
  }

  /**
   * How far the arrow should be shot.
   *
   * @param distance The distance the arrow should be shot.
   * @return The builder.
   */
  public ShootArrowActionBuilder withDistance(int distance) {
    if (distance <= 0 || distance > 5) {
      throw new IllegalArgumentException("Distance cannot be zero or negative or greater than 5");
    }
    this.distance = distance;
    return this;
  }


  @Override
  public ShootArrowAction build() {
    if (arrow == null) {
      return new ShootArrowAction(direction, distance);
    }
    return new ShootArrowAction(arrow, direction, distance);
  }
}