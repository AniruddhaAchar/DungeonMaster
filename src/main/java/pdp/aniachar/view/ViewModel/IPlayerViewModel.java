package pdp.aniachar.view.ViewModel;

import java.util.Map;

public interface IPlayerViewModel {
  /**
   * Gets the name of the player.
   *
   * @return The name of the player.
   */
  String getName();

  /**
   * Gets the current health of the player.
   *
   * @return The current health of the player.
   */
  double getHealth();

  /**
   * Gets the player's inventory.
   *
   * @return The inventory of the player.
   */

  Map<String, Long> getInventory();
}

