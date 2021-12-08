import org.apache.commons.lang3.tuple.MutablePair;

import java.util.ArrayList;
import java.util.List;

import pdp.aniachar.dungeonmaster.character.monster.Otyughs;
import pdp.aniachar.dungeonmaster.gameworld.IMazeLocation;
import pdp.aniachar.dungeonmaster.gameworld.IMazeLocationBuilder;
import pdp.aniachar.dungeonmaster.gameworld.MazeLocation;
import pdp.aniachar.dungeonmaster.item.Arrow;
import pdp.aniachar.dungeonmaster.item.Treasure;
import pdp.aniachar.dungeonmaster.item.TreasureType;
import pdp.aniachar.gamekit.GameWorld;
import pdp.aniachar.gamekit.Location;
import pdp.aniachar.gamekit.WorldBuildStrategy;


/**
 * A mocker class to build a deterministic world.
 */

public class FakeWorldBuilder implements WorldBuildStrategy {
  Otyughs otyughsAtSix;
  Otyughs otyughsAtEnd;
  List<IMazeLocation> locations = new ArrayList<>();

  FakeWorldBuilder() {
    buildMaze();
    otyughsAtEnd = new Otyughs(locations.get(8), "ender");
    otyughsAtSix = new Otyughs(locations.get(6), "sixer");
  }

  private void buildMaze() {
    for (int i = 0; i < 3; i++) {
      for (int j = 0; j < 3; j++) {
        if (i == 1 && j == 2) {
          locations.add(new IMazeLocationBuilder(new MutablePair<>(i, j))
                  .addTreasure(new Treasure(TreasureType.DIAMOND)).build());
        } else if (i == 0 && j == 1) {
          locations.add(new IMazeLocationBuilder(new MutablePair<>(i, j))
                  .addArrow(new Arrow()).build());
        } else {
          locations.add(new MazeLocation(new MutablePair<>(i, j)));
        }
      }
    }
    IMazeLocationBuilder.makeAdjacent(
            new MutablePair<>(locations.get(0), locations.get(1)), 3, 3);
    IMazeLocationBuilder.makeAdjacent(
            new MutablePair<>(locations.get(2), locations.get(1)), 3, 3);
    IMazeLocationBuilder.makeAdjacent(
            new MutablePair<>(locations.get(4), locations.get(1)), 3, 3);
    IMazeLocationBuilder.makeAdjacent(
            new MutablePair<>(locations.get(2), locations.get(5)), 3, 3);
    IMazeLocationBuilder.makeAdjacent(
            new MutablePair<>(locations.get(3), locations.get(4)), 3, 3);
    IMazeLocationBuilder.makeAdjacent(
            new MutablePair<>(locations.get(6), locations.get(7)), 3, 3);
    IMazeLocationBuilder.makeAdjacent(
            new MutablePair<>(locations.get(7), locations.get(8)), 3, 3);
    IMazeLocationBuilder.makeAdjacent(
            new MutablePair<>(locations.get(3), locations.get(6)), 3, 3);
    IMazeLocationBuilder.makeAdjacent(
            new MutablePair<>(locations.get(4), locations.get(5)), 3, 3);
    IMazeLocationBuilder.makeAdjacent(
            new MutablePair<>(locations.get(6), locations.get(8)), 3, 3);
    IMazeLocationBuilder.makeAdjacent(
            new MutablePair<>(locations.get(5), locations.get(8)), 3, 3);

  }


  @Override
  public GameWorld buildWorld() {
    return new GameWorld() {
      @Override
      public Location<?> getStartLocation() {
        return locations.get(0);
      }

      @Override
      public Location<?> getEndLocation() {
        return locations.get(8);
      }

      @Override
      public void restart() {
        otyughsAtEnd.bringBackToLife();
        otyughsAtSix.bringBackToLife();
        for (IMazeLocation location : locations) {
          location.restart();
        }
      }
    };
  }
}