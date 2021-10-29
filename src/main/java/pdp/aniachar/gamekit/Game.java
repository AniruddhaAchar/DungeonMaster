package pdp.aniachar.gamekit;

import java.util.List;
import java.util.Optional;
/**
 * A Game is what ties all the pieces together. A game encompasses the {@link Character}s,
 * {@link GameWorld} and {@link Item}s.
 * A game lets a client set up the {@link GameWorld} that the game will be played in,
 * the rules that will govern the game and how the {@link Character}s will interact with each other.
 * in which the game will be played and the {@link Character}s who will play the game.
 * To perform interactions between {@link Character}s or
 * between {@link GameWorld} and {@link Character}, use {@link #interact(Action)}.
 */

public interface Game {

  /**
   * Gets the possible moves at the given location.
   *
   * @return The possible moves at the current location.
   */
  List<Action<?>> getPossibleMoves();

  /**
   * Gets the possible moves at the given location.
   *
   * @return The possible moves at the current location.
   */
  Optional<?> interact(Action<?> action);
}
