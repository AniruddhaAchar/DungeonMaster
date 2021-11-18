package pdp.aniachar.controller;

import pdp.aniachar.dungeonmaster.comm.DeathEvent;
import pdp.aniachar.dungeonmaster.comm.HitTakenEvent;
import pdp.aniachar.dungeonmaster.comm.PlayerDeathEvent;
import pdp.aniachar.gamekit.ImmutableCharacter;

/**
 * A controller to interact with the dungeon master game.
 */

public interface IController {


  /**
   * Start the controller and process inputs.
   */

  void start();


  /**
   * Listens to a monster hit event.
   *
   * @param hitTakenEvent The event data that was sent from the publisher.
   */

  void monsterHitEventListener(HitTakenEvent<ImmutableCharacter> hitTakenEvent);

  /**
   * Listens to a player death event.
   *
   * @param deathEvent The death event published.
   */


  void playerDeathEventListener(PlayerDeathEvent deathEvent);

  /**
   * Listens to monster death event.
   *
   * @param deathEvent The death event of a Monster.
   */

  void monsterDeathEventListener(DeathEvent<ImmutableCharacter> deathEvent);
}
