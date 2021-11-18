package pdp.aniachar.dungeonmaster.character.player;

import org.jetbrains.annotations.NotNull;

import java.util.List;

import pdp.aniachar.dungeonmaster.comm.DeathEvent;
import pdp.aniachar.dungeonmaster.item.Arrow;
import pdp.aniachar.dungeonmaster.item.Treasure;
import pdp.aniachar.gamekit.Character;
import pdp.aniachar.gamekit.Item;

/**
 * Helper Interface to expose certain implementation details of Player between packages.
 */

public interface IPlayer extends Character {

  /**
   * Only players will have ability to take a treasure. Treasure is a type of {@link Item}
   *
   * @param treasurer The {@link Treasure} that needs to be assigned to the player.
   * @return true if assignment was successful, else false.
   */

  boolean assignTreasure(@NotNull Treasure treasurer);

  /**
   * Assigns an {@link Arrow} to a player.
   *
   * @param arrow The arrow that will be assigned to the player.
   * @return true if assignment was successful, else false.
   */
  boolean assignArrow(@NotNull Arrow arrow);

  /**
   * Gets the arrows that the player has.
   *
   * @return All the {@link Arrow} that the player has.
   */
  List<Arrow> getQuiver();


  /**
   * Removes an arrow from the player's quiver.
   *
   * @return The arrow that was removed from the quiver.
   */
  Arrow removeArrow();

  /**
   * Listen to see if the player was killed when playing the game.
   *
   * @param playerDeathEvent The tomb stone that indicates killing of player.
   */
  void playerDeathListener(DeathEvent<Player> playerDeathEvent);
}
