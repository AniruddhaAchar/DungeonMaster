package pdp.aniachar.dungeonmaster.action.game;

import java.util.Optional;

import pdp.aniachar.dungeonmaster.action.location.LocationAction;
import pdp.aniachar.gamekit.Action;
import pdp.aniachar.gamekit.Character;

public class PlayerLocationAction implements Action<Boolean> {
  private final Character character;
  private final LocationAction<?> otherAction;

  public PlayerLocationAction(Character character, LocationAction<?> otherAction) {
    this.character = character;
    this.otherAction = otherAction;
  }

  @Override
  public Optional<Boolean> act() {
    return otherAction.actOn(character);
  }

  @Override
  public String describe() {
    return String.format("%s %s", character.getName(), otherAction.describe());
  }
}
