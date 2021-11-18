package pdp.aniachar.dungeonmaster.comm;

import org.jetbrains.annotations.NotNull;
/**
 * An event to call a specific monster.
 *
 * @param <T> The type of monster to be called.
 */

public class CallEvent<T> {
  private final T callTarget;


  /**
   * Creates a call event for a monster.
   *
   * @param callTarget The monster to be called.
   */
  public CallEvent(@NotNull T callTarget) {
    this.callTarget = callTarget;
  }

  /**
   * Gets the monster who is called.
   *
   * @return The called monster.
   */
  public T getCallTarget() {
    return callTarget;
  }
}
