package pdp.aniachar.dungeonmaster.item;

import org.apache.commons.lang3.builder.EqualsBuilder;
import org.apache.commons.lang3.builder.HashCodeBuilder;
import org.jetbrains.annotations.NotNull;

import pdp.aniachar.dungeonmaster.player.IPlayer;
import pdp.aniachar.gamekit.Character;
import pdp.aniachar.gamekit.Item;

public class Treasure implements Item {

  private final TreasureType name;

  public Treasure(@NotNull TreasureType treasureType) {
    this.name = treasureType;
  }

  @Override
  public boolean assignToCharacter(@NotNull Character character) {
    if (!(character instanceof IPlayer)) {
      throw new IllegalArgumentException("Treasure can only be assigned to Player");
    }

    IPlayer player = (IPlayer) character;
    return player.assignTreasure(this);
  }

  @Override
  public String getName() {
    return name.toString();
  }

  public TreasureType getTreasureType() {
    return name;
  }

  @Override
  public boolean equals(Object o) {
    if (this == o) {
      return true;
    }

    if (!(o instanceof Treasure)) {
      return false;
    }

    Treasure treasure = (Treasure) o;

    return new EqualsBuilder().append(name, treasure.name)
            .isEquals();
  }

  @Override
  public int hashCode() {
    return new HashCodeBuilder(17, 37)
            .append(name).toHashCode();
  }

  @Override
  public String toString() {
    return name.toString();
  }
}
