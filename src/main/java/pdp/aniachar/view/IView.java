package pdp.aniachar.view;

import org.jetbrains.annotations.NotNull;

import pdp.aniachar.view.ViewModel.IErrorViewModel;
import pdp.aniachar.view.ViewModel.IEvent;
import pdp.aniachar.view.ViewModel.ILocationViewModel;
import pdp.aniachar.view.ViewModel.IPlayerViewModel;
import pdp.aniachar.view.ViewModel.IQuit;
/**
 * Represents a view that the controller can interact with.
 */

public interface IView {

  /**
   * Renders the player view model to the screen.
   * Update player description, inventory.
   * Rendering is done through events and the view should subscribe to this event.
   *
   * @param playerViewModel The view model to be rendered.
   */
  void renderPlayerViewModelListener(@NotNull IPlayerViewModel playerViewModel);

  /**
   * Renders the location details that the player is currently in.
   * Update player's current location details.
   *
   * @param locationViewModel The location details that needs to be rendered.
   */
  void renderLocationViewModelListener(@NotNull ILocationViewModel locationViewModel);

  /**
   * Renders an asynchronous that occurred during the turn.
   * Triggered when there is an event in any location.
   *
   * @param eventDescription The event to be rendered.
   */

  void renderEventDescriptionListener(@NotNull IEvent eventDescription);


  /**
   * Renders an error that may have occurred when playing the game.
   *
   * @param errorViewModel The error that occurred.
   */

  void renderErrorViewModelListener(@NotNull IErrorViewModel errorViewModel);


  /**
   * Starts the view.
   */
  void start();

  /**
   * Stops the view.
   */
  void stop();

  /**
   * Triggered when the game needs to be terminated.
   *
   * @param quit The quit event.
   */

  void quitListener(@NotNull IQuit quit);

}

