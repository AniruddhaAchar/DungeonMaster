package pdp.aniachar.controller;

import pdp.aniachar.dungeonmaster.comm.DeathEvent;
import pdp.aniachar.dungeonmaster.comm.GameOverEvent;
import pdp.aniachar.dungeonmaster.comm.HitTakenEvent;
import pdp.aniachar.dungeonmaster.comm.PlayerDeathEvent;
import pdp.aniachar.gamekit.ImmutableCharacter;
import pdp.aniachar.gamekit.ImmutableLocation;
import pdp.aniachar.view.ViewModel.IEvent;
import pdp.aniachar.view.ViewModel.ILocationViewModel;
import pdp.aniachar.view.ViewModel.INewGameState;
import pdp.aniachar.view.ViewModel.IPlayerInteraction;
import pdp.aniachar.view.ViewModel.IPlayerViewModel;
import pdp.aniachar.view.ViewModel.IRestartGame;

/**
 * A controller to interact with the dungeon master game.
 */

public interface IController {


  /**
   * Start the controller and process inputs.
   */

  void start();


  /**
   * Listens to a monster hit event sent by the model.
   *
   * @param hitTakenEvent The event data that was sent from the publisher.
   */

  void monsterHitEventListener(HitTakenEvent<ImmutableLocation<?>> hitTakenEvent);

  /**
   * Listens to a player death event sent from the player.
   *
   * @param deathEvent The death event published.
   */


  void playerDeathEventListener(PlayerDeathEvent deathEvent);

  /**
   * Listens to monster death event sent by the model.
   *
   * @param deathEvent The death event of a Monster.
   */

  void monsterDeathEventListener(DeathEvent<ImmutableCharacter> deathEvent);

  /**
   * Listens to user interactions sent by the view.
   *
   * @param interaction The interaction that the user did.
   */

  void playerInteractionEventListener(IPlayerInteraction interaction);

  /**
   * Listens to game over event from the model.
   */

  void gameOverEventListener(GameOverEvent event);

  /**
   * Publishes the player's view model.
   *
   * @param playerViewModel The view model to be consumed by a view.
   */

  void playerViewModelPublisher(IPlayerViewModel playerViewModel);

  /**
   * Publishes an event that occurred during the game play.
   *
   * @param eventDescription The event that occurred.
   */

  void eventDescriptionPublisher(IEvent eventDescription);

  /**
   * Publishes the location view model that will be consumed by a view.
   *
   * @param locationViewModel The location view model to be consumed by the view.
   */
  void locationViewModelPublisher(ILocationViewModel locationViewModel);

  /**
   * Listens to the view's event for restarting the game.
   *
   * @param restartGame Indicates how the game needs to be restarted.
   */

  void restartGameListener(IRestartGame<Boolean> restartGame);

  /**
   * Creates a new game model.
   *
   * @param gameState Defines how the new model should be.
   */
  void createNewGame(INewGameState gameState);
}
