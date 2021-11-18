package pdp.aniachar.view;

/**
 * Represents a partial view that needs to be rendered by the {@link IView}.
 *
 * @param <T> The type of the view model that has.
 */

public interface IViewModel<T> {

  /**
   * Gets the view model that needs to be rendered.
   *
   * @return The view model that needs to be returned.
   */

  T getViewModelData();
}
