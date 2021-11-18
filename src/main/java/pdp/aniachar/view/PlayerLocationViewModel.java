package pdp.aniachar.view;

import pdp.aniachar.dungeonmaster.gameworld.SmellStrength;
import pdp.aniachar.gamekit.ImmutableCharacter;
import pdp.aniachar.gamekit.ItemType;

/**
 * A view model of the Player that we are playing.
 */

public class PlayerLocationViewModel implements IViewModel<String> {
  private final ImmutableCharacter character;

  /**
   * Creates a player stats view model for the given player.
   *
   * @param character The player who needs to be described.
   */

  public PlayerLocationViewModel(ImmutableCharacter character) {
    this.character = character;
  }


  @Override
  public String getViewModelData() {
    StringBuilder builder = new StringBuilder();
    builder.append("Player ").append(character.getName()).append(" is at ")
            .append(character.getCurrentLocation().toString()).append("\n");

    renderItemsPossessed(builder);
    builder.append("Player has a health of ").append(character.getCurrentHealth()).append("/1\n");
    renderAnySmell(builder);
    return builder.toString();
  }

  private void renderAnySmell(StringBuilder builder) {
    if (!character.getCurrentLocation().getSmellStrength().equals(SmellStrength.NONE)) {
      builder.append("Monster near! Can smell a ")
              .append(character.getCurrentLocation().getSmellStrength().toString().toLowerCase())
              .append(" smell");
    }
  }

  private void renderItemsPossessed(StringBuilder builder) {
    builder.append("Player is carrying\n");
    try {
      for (ItemType itemType :
              ItemType.values()) {
        long numItems = character.getAllItems().stream()
                .filter(item -> item.getItemType().equals(itemType)).count();
        builder.append(String.format("%d %s\n", numItems, itemType.toString().toLowerCase()));
      }
    } catch (NoSuchMethodException e) {
      throw new IllegalStateException("Game in a bad state");
    }
  }

}
