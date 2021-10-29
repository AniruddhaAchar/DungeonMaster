package pdp.aniachar.dungeonmaster.gameworld;

import org.apache.commons.lang3.builder.EqualsBuilder;
import org.apache.commons.lang3.builder.HashCodeBuilder;
import org.apache.commons.lang3.tuple.Pair;
import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

import pdp.aniachar.dungeonmaster.action.location.MoveAction;
import pdp.aniachar.dungeonmaster.action.location.PickItemAction;
import pdp.aniachar.dungeonmaster.action.location.PickItemActionBuilder;
import pdp.aniachar.gamekit.Action;
import pdp.aniachar.gamekit.Item;
import pdp.aniachar.gamekit.Location;

/**
 * The 2D grid representation of a {@link Maze}.
 * The grid location is represented using a {@link Pair} of {@link Integer}s x, y (row, column).
 * MazeLocation can have other maze locations that are adjacent to it. This adjacency is
 * established using the {@link #moveActions}. Each location also has items that can be picked.
 * Once an item is picked, it is removed from the location.
 */

public class MazeLocation implements Location<Pair<Integer, Integer>> {

  private final Pair<Integer, Integer> gridLocation;
  private final List<Item> itemsAtLocation;
  private final List<PickItemAction> pickupActions;
  private final Set<MoveAction> moveActions;
  private final Map<Item, PickItemAction> itemPickItemActionMap;

  public MazeLocation(@NotNull Pair<Integer, Integer> gridLocation) {
    this.gridLocation = gridLocation;
    this.itemsAtLocation = new ArrayList<>();
    this.pickupActions = new ArrayList<>();
    this.moveActions = new HashSet<>();
    itemPickItemActionMap = new HashMap<>();
  }

  void addItem(Item item) {
    itemsAtLocation.add(item);
    PickItemAction pickupAction = (PickItemAction) new PickItemActionBuilder()
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
    List<Action<?>> possibleActions = new ArrayList<>(pickupActions);
    if (moveActions.size() != 0) {
      possibleActions.addAll(new ArrayList<>(moveActions));
    }
    if (pickupActions.size() != 0) {
      possibleActions.addAll(pickupActions);
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
    return new MazeLocation(this.gridLocation);
  }

  @Override
  public boolean removeItem(@NotNull Item itemToRemove) {
    var action = itemPickItemActionMap.get(itemToRemove);
    pickupActions.remove(action);
    itemPickItemActionMap.remove(itemToRemove);
    return itemsAtLocation.remove(itemToRemove);
  }

  @Override
  public Pair<Integer, Integer> getLocationDescription() {
    return gridLocation;
  }

  List<MoveAction> getAllMoveActions() {
    return new ArrayList<>(moveActions);
  }
}
