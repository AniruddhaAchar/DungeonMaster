package pdp.aniachar.dungeonmaster.action.character;

import org.jetbrains.annotations.NotNull;

import java.util.Optional;

import pdp.aniachar.gamekit.Action;
import pdp.aniachar.gamekit.Character;
import pdp.aniachar.gamekit.Location;

/**
 * Moves the {@link Character} to a new {@link Location}.
 * When "acted upon", the {@link Character}'s current location is updated.
 */

public class MoveCharacter implements Action<Boolean> {

  private final Character character;
  private final Location<?> whereTo;

  /**
   * Creates a MoveCharacter object that will move the given character to the specified location.
   *
   * @param character The {@link Character} to be moved.
   * @param whereTo   The {@link Location} to where the character has to be moved to.
   */

  public MoveCharacter(@NotNull Character character, @NotNull Location<?> whereTo) {
    this.character = character;
    this.whereTo = whereTo;
  }


  @Override
  public Optional<Boolean> act() {
    return Optional.of(character.makeMove(whereTo));
  }

  @Override
  public String describe() {
    return String.format("Move the %s to %s", character.getName(), whereTo);
  }

  @Override
  public Optional<Boolean> actOn(Character character) throws NoSuchMethodException {
    throw new NoSuchMethodException();
  }

  @Override
  public String toString() {
    return describe();
  }
}
