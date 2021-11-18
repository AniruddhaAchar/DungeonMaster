package pdp.aniachar.dungeonmaster.action.game;

import org.jetbrains.annotations.NotNull;

import java.util.Optional;

import pdp.aniachar.gamekit.Action;
import pdp.aniachar.gamekit.Character;

/**
 * Defines how a player will interact a {@link pdp.aniachar.gamekit.Location}.
 */

public class GameAction implements Action<Boolean> {
  private final Character character;
  private final Action<?> otherAction;

  /**
   * Creates the action that will define how player interacts with the location.
   *
   * @param character   The character who will interact with the location.
   * @param otherAction The actions that are permitted at that location.
   */

  public GameAction(@NotNull Character character,
                    @NotNull Action<?> otherAction) {
    this.character = character;
    this.otherAction = otherAction;
  }

  @Override
  public Optional<Boolean> act() throws NoSuchMethodException {
    return otherAction.actOn(character);
  }

  @Override
  public String describe() {
    return String.format("%s %s", character.getName(), otherAction.describe());
  }

  @Override
  public Optional<Boolean> actOn(Character character) throws NoSuchMethodException {
    throw new NoSuchMethodException();
  }
}
