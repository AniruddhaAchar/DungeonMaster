package pdp.aniachar.controller;

/**
 * A play command that can be called to interact with the model.
 *
 * @param <T> The type of result that will be returned from the execution of the command.
 */

public interface IPlayCommand<T> {
  /**
   * Executes the command.
   *
   * @return The result of the execution.
   */
  T execute();
}
