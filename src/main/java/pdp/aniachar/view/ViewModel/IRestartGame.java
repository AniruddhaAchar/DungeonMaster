package pdp.aniachar.view.ViewModel;

import java.util.Optional;

/**
 * Defines how the restart should be.
 *
 * @param <T> The state type.
 */

public interface IRestartGame<T> {
  Optional<T> restartState();
}
