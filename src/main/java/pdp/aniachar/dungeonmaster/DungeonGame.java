package pdp.aniachar.dungeonmaster;

import com.google.common.eventbus.Subscribe;

import org.jetbrains.annotations.NotNull;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;

import pdp.aniachar.Communication;
import pdp.aniachar.dungeonmaster.action.game.GameAction;
import pdp.aniachar.dungeonmaster.action.location.MoveAction;
import pdp.aniachar.dungeonmaster.action.location.PickItemAction;
import pdp.aniachar.dungeonmaster.character.monster.Otyughs;
import pdp.aniachar.dungeonmaster.character.player.PlayerBuilder;
import pdp.aniachar.dungeonmaster.comm.DeathEvent;
import pdp.aniachar.dungeonmaster.comm.EventContainer;
import pdp.aniachar.dungeonmaster.comm.HitTakenEvent;
import pdp.aniachar.dungeonmaster.comm.PlayerDeathEvent;
import pdp.aniachar.dungeonmaster.comm.RoarEvent;
import pdp.aniachar.dungeonmaster.gameworld.IMazeLocation;
import pdp.aniachar.dungeonmaster.item.ShootArrowActionBuilder;
import pdp.aniachar.gamekit.Action;
import pdp.aniachar.gamekit.Character;
import pdp.aniachar.gamekit.Direction;
import pdp.aniachar.gamekit.Game;
import pdp.aniachar.gamekit.GameWorld;
import pdp.aniachar.gamekit.ImmutableCharacter;
import pdp.aniachar.gamekit.ImmutableLocation;
import pdp.aniachar.gamekit.ItemType;
import pdp.aniachar.gamekit.Location;
import pdp.aniachar.gamekit.PlayerActionType;
import pdp.aniachar.gamekit.WorldBuildStrategy;

/**
 * A class that implements the rules for a Dungeon game.
 * In this game there is only one player and the player moves through the maze.
 */
public class DungeonGame implements Game {

  private final Character player;
  private final GameWorld dungeon;
  private boolean isGameOver;


  /**
   * Constructs a Dungeon game using the strategy provided below.
   *
   * @param worldBuilder The builder used to build the {@link GameWorld}
   * @param playerName   The name of the player who will play the game.
   */

  public DungeonGame(@NotNull WorldBuildStrategy worldBuilder, String playerName) {
    dungeon = worldBuilder.buildWorld();
    this.player = new PlayerBuilder(dungeon.getStartLocation()).setName(playerName).createPlayer();
    EventContainer.getModelEventBus().register(this);
  }

  /**
   * Constructs a Dungeon game using the strategy provided below.
   *
   * @param worldBuilder The builder used to build the {@link GameWorld}
   */

  public DungeonGame(@NotNull WorldBuildStrategy worldBuilder) {
    dungeon = worldBuilder.buildWorld();
    player = new PlayerBuilder(dungeon.getStartLocation()).createPlayer();
    EventContainer.getModelEventBus().register(this);
  }


  @Override
  public List<Action<?>> getPossibleActions() {
    return ((Location<?>) player.getCurrentLocation()).possibleActions();
  }

  @Override
  public Map<Direction, MoveAction> getAllMoveActions() {
    Map<Direction, MoveAction> possibleMoves = new HashMap<>();
    IMazeLocation playerLocation = (IMazeLocation) player.getCurrentLocation();
    for (MoveAction ma :
            playerLocation.getMoveAction()) {
      possibleMoves.put(ma.getDirection(), ma);
    }
    return possibleMoves;
  }

  @Override
  public Map<ItemType, PickItemAction> getAllPickActions() {
    return ((IMazeLocation) player.getCurrentLocation()).getPickItemActions();
  }

  @Override
  public Map<PlayerActionType, ShootArrowActionBuilder> getAllPlayerAction() {
    return Map.of(PlayerActionType.SHOOT_ARROW, new ShootArrowActionBuilder());
  }

  @Override
  public Optional<?> interact(@NotNull Action<?> action) {
    if (isGameOver) {
      throw new IllegalStateException("Cannot play game. Gave is over");
    } else {
      Action<Boolean> playerAction = new GameAction(player, action);

      try {
        Optional<?> result = playerAction.act();
        if (player.getCurrentLocation().equals(dungeon.getEndLocation())) {
          isGameOver = true;
        }
        return result;
      } catch (NoSuchMethodException e) {
        throw new IllegalStateException("Game is in a bad state.");
      }
    }
  }

  @Override
  public ImmutableCharacter getActivePlayer() {

    return player;
  }

  @Override
  public boolean isGameOver() {
    return isGameOver;
  }

  @Override
  @Subscribe
  public void playerDeathEventListener(PlayerDeathEvent playerDeathEvent) {
    this.isGameOver = true;
    player.changeHealth(-1d);
    Communication.getControllerModelBus().post(playerDeathEvent);
  }

  @Override
  @Subscribe
  public void monsterDeathEventListener(DeathEvent<Otyughs> otyughsDeathEvent) {
    Communication.getControllerModelBus()
            .post(new DeathEvent<ImmutableCharacter>(otyughsDeathEvent.getPayload()));
  }

  @Override
  @Subscribe
  public void monsterHitListener(HitTakenEvent<ImmutableLocation<?>> hitEvent) {
    Communication.getControllerModelBus().post(hitEvent);
  }

  /**
   * This is a cheat method. This allows players to cheat and get the locations of all the
   * monsters in the game.
   *
   * @param roarEvent The roaring of the monster.
   */

  @Subscribe
  public void monsterRoarListener(RoarEvent<Otyughs> roarEvent) {
    Communication.getControllerModelBus().post(roarEvent);
  }
}
