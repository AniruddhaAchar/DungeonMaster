package pdp.aniachar.dungeonmaster.comm;

import pdp.aniachar.dungeonmaster.character.player.Player;

/**
 * An event that is emitted when a player dies.
 * This is a tomb stone event that should be handled.
 */

public class PlayerDeathEvent {
  private final Player player;

  /**
   * Creates the tomb stone event.
   *
   * @param player The player who was dies.
   */
  public PlayerDeathEvent(Player player) {
    this.player = player;
  }

  /**
   * Gets the player who was died.
   *
   * @return The player was died.
   */
  public Player getPlayer() {
    return player;
  }
}
