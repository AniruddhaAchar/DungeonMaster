package pdp.aniachar.dungeonmaster.character.player;

import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ThreadLocalRandom;

import pdp.aniachar.dungeonmaster.item.Arrow;
import pdp.aniachar.gamekit.Character;
import pdp.aniachar.gamekit.Location;

/**
 * A way to generate a {@link Player}.
 * Player may be given a name. If a player is not given a name,
 * a default name is given to the player.
 */

public class PlayerBuilder {
  private static final int DEFAULT_NUMBER_OF_ARROWS = 3;
  private final @NotNull Location<?> startingLocation;
  private String name;
  private boolean register;

  /**
   * A player always needs a starting point. This helps with building the player.
   *
   * @param startingLocation The starting position of a player.
   */

  public PlayerBuilder(@NotNull Location<?> startingLocation) {
    this.startingLocation = startingLocation;
    register = false;
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
   * Makes sure if the player should register to the event bus or not.
   *
   * @param register Should the player register to the event bus.
   * @return The builder.
   */

  public PlayerBuilder setRegister(boolean register) {
    this.register = register;
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
    List<Arrow> quiver = new ArrayList<>();
    for (int i = 0; i < DEFAULT_NUMBER_OF_ARROWS; i++) {
      quiver.add(new Arrow());
    }
    return new Player(startingLocation, name, quiver, register);
  }
}