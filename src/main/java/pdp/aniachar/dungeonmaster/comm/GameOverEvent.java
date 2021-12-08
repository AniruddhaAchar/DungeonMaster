package pdp.aniachar.dungeonmaster.comm;

import lombok.Getter;

/**
 * Emits a Game over event with the player's liveliness.
 */
@Getter
public class GameOverEvent {
  private final boolean isPlayerAlive;

  /**
   * Creates a new game over event.
   *
   * @param isPlayerAlive used to check if the player is alive.
   */
  public GameOverEvent(boolean isPlayerAlive) {
    this.isPlayerAlive = isPlayerAlive;
  }
}
