package pdp.aniachar.dungeonmaster.player;

import org.jetbrains.annotations.NotNull;

import java.util.concurrent.ThreadLocalRandom;

import pdp.aniachar.gamekit.Character;
import pdp.aniachar.gamekit.Location;

/**
 * A way to generate a {@link Player}.
 * Player may be given a name. If a player is not given a name,
 * a default name is given to the player.
 */

public class PlayerBuilder {
  private final @NotNull Location<?> startingLocation;
  private String name;

  /**
   * A player always needs a starting point. This helps with building the player.
   *
   * @param startingLocation The starting position of a player.
   */

  public PlayerBuilder(@NotNull Location<?> startingLocation) {
    this.startingLocation = startingLocation;
  }

  /**
   * Optional name that a player may be given.
   *
   * @param name The name of the player.
   * @return The builder.
   */

  public PlayerBuilder setName(@NotNull String name) {
    if (name.isBlank()) {
      throw new IllegalArgumentException("Name cannot be blank");
    }
    this.name = name;
    return this;
  }

  /**
   * Creates the {@link Player} with the specifications given.
   *
   * @return The player created using the information provided.
   */

  public Character createPlayer() {
    if (name == null) {
      name = String.format("Player %d", ThreadLocalRandom.current()
              .nextInt(1000, 5000));
    }
    return new Player(startingLocation, name);
  }
}