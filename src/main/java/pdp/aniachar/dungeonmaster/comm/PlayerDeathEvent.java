package pdp.aniachar.dungeonmaster.comm;

import pdp.aniachar.gamekit.ImmutableCharacter;

/**
 * An event that is emitted when a player dies.
 * This is a tomb stone event that should be handled.
 */

public class PlayerDeathEvent {
  private final ImmutableCharacter player;

  /**
   * Creates the tomb stone event.
   *
   * @param player The player who was dies.
   */
  public PlayerDeathEvent(ImmutableCharacter player) {
    this.player = player;
  }

  /**
   * Gets the player who was died.
   *
   * @return The player was died.
   */
  public ImmutableCharacter getPlayer() {
    return player;
  }
}
