package pdp.aniachar.controller;

import java.util.Optional;

import pdp.aniachar.gamekit.Game;

/**
 * Encapsulates the common behaviour of all IPlayerCommands.
 */

abstract class AbstractPlayCommand implements IPlayCommand<Optional<?>> {
  protected final Game game;

  /**
   * Creates an abstract player command.
   *
   * @param game The model that this command will work with.
   */

  protected AbstractPlayCommand(Game game) {
    this.game = game;
  }
}
