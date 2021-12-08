package pdp.aniachar.dungeonmaster.gameworld;

import org.apache.commons.lang3.builder.EqualsBuilder;
import org.apache.commons.lang3.builder.HashCodeBuilder;
import org.apache.commons.lang3.tuple.Pair;
import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Set;

import pdp.aniachar.dungeonmaster.action.location.LocationAction;
import pdp.aniachar.dungeonmaster.action.location.MoveAction;
import pdp.aniachar.dungeonmaster.action.location.PickItemAction;
import pdp.aniachar.dungeonmaster.action.location.PickItemActionBuilder;
import pdp.aniachar.gamekit.Action;
import pdp.aniachar.gamekit.Item;
import pdp.aniachar.gamekit.ItemType;
import pdp.aniachar.gamekit.Location;

/**
 * The 2D grid representation of a {@link Maze}.
 * The grid location is represented using a {@link Pair} of {@link Integer}s x, y (row, column).
 * MazeLocation can have other maze locations that are adjacent to it. This adjacency is
 * established using the {@link #moveActions}. Each location also has items that can be picked.
 * Once an item is picked, it is removed from the location.
 */

public class MazeLocation implements IMazeLocation {

  private final Pair<Integer, Integer> gridLocation;
  private List<Item> itemsAtLocation;
  private List<PickItemAction> pickupActions;
  private Set<MoveAction> moveActions;
  private Map<Item, PickItemAction> itemPickItemActionMap;
  private int smellStrength;
  private final List<Item> resetCopyItemsAtLocation;


  /**
   * Constructs a 2D maze locations that is at the grid location provided.
   *
   * @param gridLocation The (X, Y) location on the grid.
   */

  public MazeLocation(@NotNull Pair<Integer, Integer> gridLocation) {
    this.gridLocation = gridLocation;
    this.itemsAtLocation = new ArrayList<>();
    this.pickupActions = new ArrayList<>();
    this.moveActions = new HashSet<>();
    itemPickItemActionMap = new HashMap<>();
    resetCopyItemsAtLocation = new LinkedList<>();
    smellStrength = 0;
  }

  void addItem(Item item) {

    resetCopyItemsAtLocation.add(item);
    buildLocationWithItem(item);
  }

  private void buildLocationWithItem(Item item) {
    itemsAtLocation.add(item);
    PickItemAction pickupAction = new PickItemActionBuilder()
            .setActionLocation(this).setItemToPick(item).createPickItemAction();
    pickupActions.add(pickupAction);
    itemPickItemActionMap.put(item, pickupAction);
  }

  void addMoveAction(@NotNull MoveAction action) {
    moveActions.add(action);
  }


  @Override
  public List<Item> getItems() {
    return itemsAtLocation;
  }

  @Override
  public List<Action<?>> possibleActions() {
    List<Action<?>> possibleActions = new ArrayList<>();
    if (moveActions.size() != 0) {
      possibleActions.addAll(new ArrayList<>(moveActions));
    }
    if (pickupActions.size() != 0) {
      possibleActions.addAll(new ArrayList<>(pickupActions));
    }
    return possibleActions;
  }


  @Override
  public String toString() {
    return String.format("Location %s\n",
            gridLocation);
  }

  @Override
  public boolean equals(Object o) {
    if (this == o) {
      return true;
    }

    if (!(o instanceof MazeLocation)) {
      return false;
    }

    MazeLocation that = (MazeLocation) o;

    return new EqualsBuilder().append(gridLocation, that.gridLocation).isEquals();
  }

  @Override
  public int hashCode() {
    return new HashCodeBuilder(17, 37)
            .append(gridLocation).toHashCode();
  }

  @Override
  public Location<Pair<Integer, Integer>> copy() {
    MazeLocation copyLocation = new MazeLocation(this.gridLocation);
    copyLocation.itemPickItemActionMap = new HashMap<>(this.itemPickItemActionMap);
    copyLocation.moveActions = new HashSet<>(this.moveActions);
    copyLocation.pickupActions = new ArrayList<>(this.pickupActions);
    copyLocation.itemsAtLocation = new ArrayList<>(this.itemsAtLocation);
    return copyLocation;
  }

  @Override
  public boolean removeItem(@NotNull Item itemToRemove) {
    LocationAction<Item> action = itemPickItemActionMap.get(itemToRemove);
    pickupActions.remove(action);
    itemPickItemActionMap.remove(itemToRemove);
    return itemsAtLocation.remove(itemToRemove);
  }

  @Override
  public void restart() {
    for (var item : resetCopyItemsAtLocation) {
      buildLocationWithItem(item);
    }
  }

  @Override
  public Pair<Integer, Integer> getLocationDescription() {
    return gridLocation;
  }

  @Override
  public void addSmell(SmellStrength smellStrength) {
    if (smellStrength == SmellStrength.STRONG) {
      this.smellStrength += 2;
    } else if (smellStrength == SmellStrength.WEAK) {
      this.smellStrength += 1;
    }
  }

  @Override
  public void removeSmell(SmellStrength smellStrength) {
    if (smellStrength == SmellStrength.STRONG) {
      this.smellStrength -= 2;
    } else if (smellStrength == SmellStrength.WEAK) {
      this.smellStrength -= 1;
    }
  }


  @Override
  public List<MoveAction> getMoveAction() {
    return new ArrayList<>(moveActions);
  }

  @Override
  public Map<ItemType, PickItemAction> getPickItemActions() {
    Map<ItemType, PickItemAction> result = new HashMap<>();
    for (var pickItem :
            itemPickItemActionMap.entrySet()) {
      Item item = pickItem.getKey();
      result.put(item.getItemType(), pickItem.getValue());
    }
    return result;
  }

  @Override
  public SmellStrength getSmellStrength() {
    if (this.smellStrength >= 2) {
      return SmellStrength.STRONG;
    } else if (this.smellStrength == 1) {
      return SmellStrength.WEAK;
    }
    return SmellStrength.NONE;
  }

}
