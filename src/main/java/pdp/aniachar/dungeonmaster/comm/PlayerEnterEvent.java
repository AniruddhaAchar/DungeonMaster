package pdp.aniachar.dungeonmaster.comm;

import org.jetbrains.annotations.NotNull;

import pdp.aniachar.dungeonmaster.character.player.IPlayer;

/**
 * An event that is emitted when player enters a specific location.
 */

public class PlayerEnterEvent {

  private final IPlayer player;

  /**
   * Creates the entered a specific location event.
   *
   * @param player The player who entered a specific location.
   */
  public PlayerEnterEvent(@NotNull IPlayer player) {
    this.player = player;
  }

  public IPlayer getPlayer() {
    return player;
  }


}
