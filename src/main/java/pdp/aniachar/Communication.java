package pdp.aniachar;

import com.google.common.eventbus.EventBus;

/**
 * A communications bus for communication between various Mode <--> Controller or
 * controller <--> View.
 * Communication happens over EventBus and the model, view and controller are expected to
 * subscribe to some specific events.
 */

public class Communication {
  private static EventBus controllerModelBus;

  /**
   * Gets the Event bus for communication between the Controller and Model.
   * This is a singleton that is returned.
   *
   * @return The event bus for communication between the model and controller.
   */

  public static EventBus getControllerModelBus() {
    if (controllerModelBus == null) {
      controllerModelBus = new EventBus();
    }
    return controllerModelBus;
  }
}
