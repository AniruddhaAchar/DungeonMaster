package pdp.aniachar.dungeonmaster.comm;

import org.jetbrains.annotations.NotNull;
/**
 * A monster's roar event.
 * The sound a monster emits when they are called.
 *
 * @param <T> The type of monster.
 */


public class RoarEvent<T> {
  private final T roarer;


  /**
   * Cretaes a RoarEvent.
   *
   * @param roarer The monster who is roaring.
   */

  public RoarEvent(@NotNull T roarer) {
    this.roarer = roarer;
  }

  /**
   * Gets the monster who roared.
   *
   * @return The monster who roared.
   */

  public T getRoarer() {
    return roarer;
  }
}
