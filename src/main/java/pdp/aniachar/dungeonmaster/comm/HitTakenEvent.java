package pdp.aniachar.dungeonmaster.comm;

import org.jetbrains.annotations.NotNull;

/**
 * Hit event indicates who needs to take the hit.
 * The payload has the target who needs to take the hit.
 *
 * @param <T> The type of the target.
 */

public class HitTakenEvent<T> {

  private final T target;

  /**
   * Generates a new HitTakenEvent.
   *
   * @param target The target who should take the hit.
   */

  public HitTakenEvent(@NotNull T target) {
    this.target = target;
  }

  /**
   * Gets the target who should take the hit.
   *
   * @return The target that needs to tae the hit.
   */

  public T getTarget() {
    return target;
  }

}
