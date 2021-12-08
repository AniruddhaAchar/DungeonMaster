package pdp.aniachar.view.ViewModel;

import pdp.aniachar.gamekit.ImmutableLocation;

/**
 * Defines what event occurred and where a specific event occurred in the game.
 */

public interface IEvent {

  /**
   * Gets the description of the event.
   *
   * @return The description of the event.
   */
  EventDescription getDescription();

  /**
   * Gets the location where the event happened.
   *
   * @return The location of the event.
   */

  ImmutableLocation<?> getLocation();

  /**
   * Gets the severity of the event.
   *
   * @return The severity of the event.
   */

  Severity getSeverity();
}
