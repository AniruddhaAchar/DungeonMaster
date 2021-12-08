package pdp.aniachar.dungeonmaster.comm;

import com.google.common.eventbus.EventBus;

/**
 * A model specific event bus container.
 */

public class EventContainer {

  private static EventBus modelEventBus;

  /**
   * Gets a singleton model event bus.
   * This event bus is used only for communication between the model components.
   *
   * @return A singleton instance of the eventbus.
   */

  public static EventBus getModelEventBus() {
    if (modelEventBus == null) {
      modelEventBus = new EventBus();
    }
    return modelEventBus;
  }

  /**
   * Restarts the event bus for the model.
   */
  public static void reset() {
    modelEventBus = null;
  }

}
