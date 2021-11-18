package pdp.aniachar.gamekit;

/**
 * A common action builder interface that allows actions to be built from its components.
 * These components must be passed to the constructor of the implementer.
 *
 * @param <T> The return type of this ActionBuilder.
 */

public interface ActionBuilder<T> {

  /**
   * Builds and returns a new action.
   *
   * @return The Action that was built.
   */

  Action<T> build();
}
