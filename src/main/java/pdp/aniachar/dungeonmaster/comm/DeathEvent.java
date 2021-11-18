package pdp.aniachar.dungeonmaster.comm;

import org.jetbrains.annotations.NotNull;

/**
 * An event that is sent when a death occurs in the game.
 *
 * @param <T> The type of the payload that this event will carry.
 */

public class DeathEvent<T> {

  private final T payload;

  /**
   * Creates a death event.
   *
   * @param payload The payload that this event will carry.
   */

  public DeathEvent(@NotNull T payload) {
    this.payload = payload;
  }

  /**
   * Used by the listener to get the payload of the event.
   *
   * @return The payload that was carried.
   */
  public T getPayload() {
    return payload;
  }
}
