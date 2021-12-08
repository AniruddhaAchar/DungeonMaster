package pdp.aniachar.view.ViewModel;

import lombok.AllArgsConstructor;


/**
 * Defines and implements how the view should handle a quit event.
 */

@AllArgsConstructor
public class Quit implements IQuit {
  private final boolean stateSaved;


  @Override
  public boolean isStateSaved() {
    return stateSaved;
  }
}
