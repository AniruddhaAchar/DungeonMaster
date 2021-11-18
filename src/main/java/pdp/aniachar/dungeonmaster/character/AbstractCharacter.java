package pdp.aniachar.dungeonmaster.character;

import pdp.aniachar.gamekit.Character;

/**
 * Abstract Character represents the common behaviours of all the characters in the game.
 */

public abstract class AbstractCharacter implements Character {
  protected double health;

  @Override
  public double getCurrentHealth() {
    return health;
  }

  @Override
  public void changeHealth(double changeBy) {
    if (changeBy == 0) {
      throw new IllegalArgumentException("Change by cannot be zero");
    }
    health += changeBy;
    if (health < 0) {
      health = 0;
    }

  }

}
