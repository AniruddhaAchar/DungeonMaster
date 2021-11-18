package pdp.aniachar.dungeonmaster.item;

import org.jetbrains.annotations.NotNull;

import pdp.aniachar.dungeonmaster.character.player.IPlayer;
import pdp.aniachar.dungeonmaster.gameworld.IMazeLocation;
import pdp.aniachar.gamekit.Character;
import pdp.aniachar.gamekit.ItemType;

/**
 * A weapon that a {@link Character} can used to attack another character.
 * An arrow can move between {@link pdp.aniachar.gamekit.Location}.
 */

public class Arrow implements IArrow {

  private IMazeLocation currentLocation;

  /**
   * A method to get the current location of an Arrow.
   *
   * @return The current location the arrow is in.
   */
  @Override
  public IMazeLocation getCurrentLocation() {
    return currentLocation;
  }


  @Override
  public void setCurrentLocation(IMazeLocation whereTo) {
    this.currentLocation = whereTo;
  }

  @Override
  public boolean assignToCharacter(@NotNull Character character) {
    if (!(character instanceof IPlayer)) {
      throw new IllegalArgumentException("Arrow can only be assigned to players");
    }
    IPlayer player = (IPlayer) character;
    player.assignArrow(this);
    return true;
  }

  @Override
  public String getName() {
    return "ARROW";
  }

  @Override
  public ItemType getItemType() {
    return ItemType.ARROW;
  }

  @Override
  public String toString() {
    return "Arrow";
  }
}
