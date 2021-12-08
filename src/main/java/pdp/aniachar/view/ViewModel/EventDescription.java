package pdp.aniachar.view.ViewModel;


/**
 * Describes the legal events that can occur in the game.
 */

public enum EventDescription {
  MONSTER_HIT("Otyughs hit"),
  MONSTER_KILLED("Monster killed"),
  PLAYER_DEATH("Player is dead."),
  PLAYER_WON("Player wins the game"),
  QUIT("Quitting game"),
  LOST("You loose the game."),
  WIN("Won the game! Entered the end cave.");
  private final String description;

  EventDescription(String description) {
    this.description = description;
  }

  @Override
  public String toString() {
    return description;
  }
}
