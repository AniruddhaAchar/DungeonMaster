package pdp.aniachar.gamekit;

import java.util.List;
import java.util.Map;
import java.util.Optional;

import pdp.aniachar.dungeonmaster.action.location.MoveAction;
import pdp.aniachar.dungeonmaster.action.location.PickItemAction;
import pdp.aniachar.dungeonmaster.character.monster.Otyughs;
import pdp.aniachar.dungeonmaster.comm.DeathEvent;
import pdp.aniachar.dungeonmaster.comm.HitTakenEvent;
import pdp.aniachar.dungeonmaster.comm.PlayerDeathEvent;
import pdp.aniachar.dungeonmaster.comm.RoarEvent;
import pdp.aniachar.dungeonmaster.item.ShootArrowActionBuilder;
/**
 * A Game is what ties all the pieces together. A game encompasses the {@link Character}s,
 * {@link GameWorld} and {@link Item}s.
 * A game lets a client set up the {@link GameWorld} that the game will be played in,
 * the rules that will govern the game and how the {@link Character}s will interact with each other.
 * in which the game will be played and the {@link Character}s who will play the game.
 * To perform interactions between {@link Character}s or
 * between {@link GameWorld} and {@link Character}, use {@link #interact(Action)}.
 */

public interface Game {

  /**
   * Gets the possible moves at the current active player's location.
   *
   * @return The possible moves at the current active player's location.
   */
  List<Action<?>> getPossibleActions();

  /**
   * Gets all the possible move actions for the player at the current location.
   *
   * @return A map of directions and to move the player in that direction.
   */
  Map<Direction, MoveAction> getAllMoveActions();

  /**
   * Gets the all possible pick actions for the player at the current location.
   *
   * @return A map of items that can be picked and how they can be picked.
   */

  Map<ItemType, PickItemAction> getAllPickActions();

  /**
   * Gets all possible Player Actions.
   *
   * @return A map of player action type and all actions.
   */

  Map<PlayerActionType, ShootArrowActionBuilder> getAllPlayerAction();

  /**
   * Gets the possible moves at the given location.
   *
   * @return The possible moves at the current location.
   */
  Optional<?> interact(Action<?> action);

  /**
   * Gets the current active player.
   *
   * @return The current active player.
   */
  ImmutableCharacter getActivePlayer() throws NoSuchMethodException;

  /**
   * Gets if the game is over.
   *
   * @return Is the game over.
   */

  boolean isGameOver();


  /**
   * Listens to any player death events.
   *
   * @param playerDeathEvent Event that a player is dead.
   */

  void playerDeathEventListener(PlayerDeathEvent playerDeathEvent);


  /**
   * Listens to a Monster death event.
   *
   * @param otyughsDeathEvent A monster death event.
   */

  void monsterDeathEventListener(DeathEvent<Otyughs> otyughsDeathEvent);

  /**
   * Listens to a monster hit event.
   *
   * @param hitEvent The monster hit event.
   */

  void monsterHitListener(HitTakenEvent<ImmutableLocation<?>> hitEvent);

  /**
   * Listens to a Monster Roar.
   *
   * @param roarEvent The roaring of the monster.
   */
  void monsterRoarListener(RoarEvent<Otyughs> roarEvent);


  /**
   * Restarts the game.
   */

  void restartGame();

  /**
   * Emits a game over event.
   *
   */

  void emitGameOver(boolean playerALive);
}
