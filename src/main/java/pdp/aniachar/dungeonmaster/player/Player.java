package pdp.aniachar.dungeonmaster.player;

import org.apache.commons.lang3.builder.EqualsBuilder;
import org.apache.commons.lang3.builder.HashCodeBuilder;
import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;
import java.util.List;

import pdp.aniachar.dungeonmaster.gameworld.Maze;
import pdp.aniachar.dungeonmaster.item.Treasure;
import pdp.aniachar.gamekit.Character;
import pdp.aniachar.gamekit.Item;
import pdp.aniachar.gamekit.Location;

/**
 * A player is a specific implementation of the {@link Character} who can play in DungeonMaster game.
 * A player can be moved to any place in the {@link Maze}.
 * The implementor of the action class should enforce the game rules.
 * The player is not aware of the rules of the game.
 */

class Player implements IPlayer {

  private final String name;
  private final List<Item> possessedItems;
  private Location<?> currentLocation;

  Player(@NotNull Location<?> startingLocation, @NotNull String name) {
    if (name.isBlank()) {
      throw new IllegalArgumentException("Player cannot be blank");
    }
    this.currentLocation = startingLocation.copy();
    this.name = name;
    possessedItems = new ArrayList<>();
  }

  /**
   * No check is made here to if the player can be moved. It is the responsibility of the
   * {@link pdp.aniachar.gamekit.Action} that implements this method to see if the move
   * is possible on the {@link pdp.aniachar.gamekit.GameWorld}. The player is unaware of the rules.
   *
   * @param whereTo The new location where the character should be moved to.
   * @return Always true.
   */

  public boolean makeMove(@NotNull Location<?> whereTo) {
    currentLocation = whereTo.copy();
    return true;
  }

  @Override
  public Location<?> getCurrentLocation() {
    return currentLocation.copy();
  }

  @Override
  public boolean takeItem(@NotNull Item item) {
    return item.assignToCharacter(this);
  }

  @Override
  public List<Item> getAllItems() {
    return new ArrayList<>(possessedItems);
  }

  @Override
  public String getName() {
    return name;
  }

  /**
   * Assigns a treasure to the player.
   *
   * @param treasurer The {@link Treasure} that the player will take.
   * @return always true since the player can take infinite number of treasures.
   */
  @Override
  public boolean assignTreasure(@NotNull Treasure treasurer) {
    this.possessedItems.add(new Treasure(treasurer.getTreasureType()));
    return true;
  }

  @Override
  public boolean equals(Object o) {
    if (this == o) return true;

    if (o == null || getClass() != o.getClass()) return false;

    Player player = (Player) o;

    return new EqualsBuilder().append(name, player.name)
            .append(possessedItems, player.possessedItems)
            .append(currentLocation, player.currentLocation).isEquals();
  }

  @Override
  public int hashCode() {
    return new HashCodeBuilder(17, 37)
            .append(name).append(possessedItems).append(currentLocation).toHashCode();
  }
}
