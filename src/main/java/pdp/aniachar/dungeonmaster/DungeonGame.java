package pdp.aniachar.dungeonmaster;

import org.jetbrains.annotations.NotNull;

import java.util.List;
import java.util.Optional;

import pdp.aniachar.dungeonmaster.player.PlayerBuilder;
import pdp.aniachar.gamekit.Action;
import pdp.aniachar.gamekit.Character;
import pdp.aniachar.gamekit.Game;
import pdp.aniachar.gamekit.GameWorld;
import pdp.aniachar.gamekit.WorldBuildStrategy;

public class DungeonGame implements Game {

  Character player;
  GameWorld dungeon;


  public DungeonGame(@NotNull WorldBuildStrategy strategy, String playerName) {
    dungeon = strategy.buildWorld();
    player = new PlayerBuilder(dungeon.getStartLocation()).setName(playerName).createPlayer();
  }


  @Override
  public List<Action<?>> getPossibleMoves() {
    return player.getCurrentLocation().possibleActions();
  }

  @Override
  public Optional<?> interact(@NotNull Action<?> action) {
    return action.act();
  }


}
