package pdp.aniachar.view.ViewModel;

/**
 * Defines how a quit should be handled by the view.
 */
public interface IQuit {
  /**
   * Lets the view know if a state needs to be saved.
   *
   * @return The flag that indicates the save state.
   */
  boolean isStateSaved();
}
