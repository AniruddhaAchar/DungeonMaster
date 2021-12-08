package pdp.aniachar.dungeonmaster.character;

import pdp.aniachar.gamekit.Character;

/**
 * Abstract Character represents the common behaviours of all the characters in the game.
 */

public abstract class AbstractCharacter implements Character {
  protected double health;
  protected double startHealth;

  /**
   * Builds a new character with the specified health.
   *
   * @param startHealth The starting health of the character.
   */

  public AbstractCharacter(double startHealth) {
    this.startHealth = startHealth;
    this.health = startHealth;
  }

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

  protected abstract void bringBackToLifeHelper();

  @Override
  public void bringBackToLife() {
    bringBackToLifeHelper();
    health = startHealth;
  }

}
