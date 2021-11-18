package pdp.aniachar.dungeonmaster.comm;

import org.jetbrains.annotations.NotNull;

import pdp.aniachar.dungeonmaster.gameworld.IMazeLocation;

/**
 * An event that indicates that an arrow hit a character.
 * Any character that subscribes to this event will take a hit.
 */

public class ArrowHitEvent {

  private final IMazeLocation locationToAffect;

  private final double damageDealt;

  /**
   * Creates an arrow hit event.
   *
   * @param locationToAffect The location where the arrow landed.
   * @param damageDealt      The damage that is dealt by the arrow.
   */
  public ArrowHitEvent(@NotNull IMazeLocation locationToAffect, double damageDealt) {
    this.locationToAffect = locationToAffect;
    this.damageDealt = damageDealt;
  }

  /**
   * This is the payload of the event.
   * Allows the subscriber to get to know where the event occurred.
   *
   * @return The location that was affected by the arrow.
   */

  public IMazeLocation getLocationToAffect() {
    return (IMazeLocation) locationToAffect.copy();
  }

  /**
   * This is the payload of the event.
   * Allows the subscriber to get to know how much damage was dealt.
   *
   * @return The damage dealt by the arrow.
   */

  public double getDamageDealt() {
    return damageDealt;
  }
}
