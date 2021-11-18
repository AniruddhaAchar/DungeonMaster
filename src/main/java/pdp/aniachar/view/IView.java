package pdp.aniachar.view;

/**
 * Represents a view that the controller can interact with.
 *
 * @param <T> The type of data that the view will return after consuming the user input.
 * @param <R> The type of data that the view will consume to render the view.
 */

public interface IView<T, R> {
  /**
   * Gets the user input.
   *
   * @return The input that the user provided.
   */
  T getUserInput();

  /**
   * Renders the view model.
   *
   * @param viewModel The view model that needs to be rendered.
   */
  void renderView(IViewModel<R> viewModel);
}
