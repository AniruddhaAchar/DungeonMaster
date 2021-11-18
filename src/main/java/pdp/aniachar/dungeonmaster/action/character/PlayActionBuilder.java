package pdp.aniachar.dungeonmaster.action.character;

/**
 * Builds a way for a dynamic player action to be built.
 */

public interface PlayActionBuilder {
  /**
   * Builds the {@link PlayerAction}.
   *
   * @return The {@link PlayerAction} that was built.
   */
  PlayerAction build();

}
