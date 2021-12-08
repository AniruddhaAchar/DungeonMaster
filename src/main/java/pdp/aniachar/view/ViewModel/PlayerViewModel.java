package pdp.aniachar.view.ViewModel;

import java.util.Map;

import lombok.AllArgsConstructor;

/**
 * Defines and implements how the view consumes the Player information.
 */
@AllArgsConstructor
public class PlayerViewModel implements IPlayerViewModel {
  private final String name;
  private final double health;
  private final Map<String, Long> inventory;


  @Override
  public String getName() {
    return name;
  }

  @Override
  public double getHealth() {
    return health;
  }

  @Override
  public Map<String, Long> getInventory() {
    return inventory;
  }
}
