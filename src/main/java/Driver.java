import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.HashSet;
import java.util.Set;
import java.util.stream.Collectors;

import pdp.aniachar.dungeonmaster.action.location.MoveAction;
import pdp.aniachar.dungeonmaster.gameworld.RandomMazeBuilder;
import pdp.aniachar.gamekit.Action;
import pdp.aniachar.gamekit.GameWorld;
import pdp.aniachar.gamekit.Location;
public class Driver {
  public static void main(String[] args) {

    var buildStrategy = new RandomMazeBuilder(10, 10, true, 0, 10);
    GameWorld world = buildStrategy.buildWorld();
    Set<Location<?>> visited = new HashSet<>();
    visitAllNodes(world.getStartLocation(), visited, world.getEndLocation());

  }

  private static void visitAllNodes(@NotNull Location<?> source, @NotNull Set<Location<?>> visited,
                                    @Nullable Location<?> stopLocation) {

    visited.add(source);
    var adj = source.possibleActions().stream()
            .filter(action -> action instanceof MoveAction).collect(Collectors.toList());
    printLocationDetails(source);
    if(source.equals(stopLocation)){
      print("End location reached");
      System.exit(0);
    }
    for (Action<?> moveAction :
            adj) {
      Location<?> neighbor = (Location<?>) moveAction.act()
              .orElseThrow(() -> new IllegalStateException("Neighbor not found"));
      if (!visited.contains(neighbor)) {

        visited.add(neighbor);
        visitAllNodes(neighbor, visited, stopLocation);
      }
    }
  }

  private static void printLocationDetails(@NotNull Location<?> location) {
    printDiv();
    print(String.format("Player is at %s", location.getLocationDescription()));
    print("");
    print("Player");
    for (int i = 0; i < location.possibleActions().size(); i++) {
      print(String.format("%d. %s", i + 1, location.possibleActions().get(i).describe()));
    }
    printDiv();
  }

  private static void print(String str) {
    System.out.println(str);
  }

  private static void printDiv() {
    System.out.println("=============================================");
  }

}
