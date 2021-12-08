package pdp.aniachar.view.ViewModel;

import java.util.Optional;

import lombok.AllArgsConstructor;

/**
 * Defines how the view would restart the game.
 *
 * @param <T> The payload type of the restart.
 */
@AllArgsConstructor
public class RestartGame<T> implements IRestartGame<T> {

  private final T state;


  @Override
  public Optional<T> restartState() {
    if (state != null) {
      return Optional.of(state);
    }
    return Optional.empty();
  }
}
