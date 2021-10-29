package pdp.aniachar.dungeonmaster.player;

import org.jetbrains.annotations.NotNull;

import pdp.aniachar.dungeonmaster.item.Treasure;
import pdp.aniachar.gamekit.Character;
import pdp.aniachar.gamekit.Item;

/**
 * Helper Interface to expose certain implementation details of Player between packages.
 */

public interface IPlayer extends Character {

  /**
   * Only players will have ability to take a treasure. Treasure is a type of {@link Item}
   * @param treasurer The {@link Treasure} that needs to be assigned to the player.
   * @return true if assignment was successful, else false.
   */

  boolean assignTreasure(@NotNull Treasure treasurer);
}
