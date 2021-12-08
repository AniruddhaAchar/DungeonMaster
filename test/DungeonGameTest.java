import com.google.common.eventbus.Subscribe;

import org.apache.commons.lang3.mutable.MutableBoolean;
import org.apache.commons.lang3.mutable.MutableInt;
import org.jetbrains.annotations.NotNull;
import org.junit.Before;
import org.junit.Test;

import java.util.ArrayList;
import java.util.List;
import java.util.Locale;
import java.util.NoSuchElementException;

import pdp.aniachar.Communication;
import pdp.aniachar.dungeonmaster.DungeonGame;
import pdp.aniachar.dungeonmaster.action.location.MoveAction;
import pdp.aniachar.dungeonmaster.action.location.PickItemAction;
import pdp.aniachar.dungeonmaster.character.monster.Otyughs;
import pdp.aniachar.dungeonmaster.character.player.IPlayer;
import pdp.aniachar.dungeonmaster.comm.CallEvent;
import pdp.aniachar.dungeonmaster.comm.DeathEvent;
import pdp.aniachar.dungeonmaster.comm.EventContainer;
import pdp.aniachar.dungeonmaster.comm.HitTakenEvent;
import pdp.aniachar.dungeonmaster.comm.RoarEvent;
import pdp.aniachar.dungeonmaster.gameworld.IMazeLocation;
import pdp.aniachar.dungeonmaster.gameworld.SmellStrength;
import pdp.aniachar.dungeonmaster.item.Arrow;
import pdp.aniachar.dungeonmaster.item.ShootArrowActionBuilder;
import pdp.aniachar.gamekit.Action;
import pdp.aniachar.gamekit.Direction;
import pdp.aniachar.gamekit.Game;
import pdp.aniachar.gamekit.ImmutableCharacter;
import pdp.aniachar.gamekit.ItemType;
import pdp.aniachar.gamekit.PlayerActionType;
import pdp.aniachar.gamekit.WorldBuildStrategy;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;
import static org.junit.Assert.fail;

/**
 * Tests the features of the model.
 */

public class DungeonGameTest {

  List<IMazeLocation> locations = new ArrayList<>();
  MutableInt hitCount = new MutableInt(0);
  MutableBoolean deathNotice = new MutableBoolean(false);
  List<ImmutableCharacter> monsters;
  MutableInt monsterCount;

  @Before
  public void setup() {
    Communication.getModelControllerBus().register(this);
    EventContainer.getModelEventBus().register(this);
    monsters = new ArrayList<>();
    monsterCount = new MutableInt(0);
  }

  @Test
  public void testPossibleActionsAt00() throws NoSuchMethodException {
    Game game = new DungeonGame(new FakeWorldBuilder());
    assertEquals(locations.get(0), game.getActivePlayer().getCurrentLocation());
    assertTrue(testAllActionsHelper(game, 1, 1, 0,
            List.of("east")));
    assertEquals(SmellStrength.NONE, game.getActivePlayer()
            .getCurrentLocation().getSmellStrength());
  }

  @Test
  public void testInteractAt00() throws NoSuchMethodException {
    Game game = new DungeonGame(new FakeWorldBuilder());
    assertEquals(locations.get(0), game.getActivePlayer().getCurrentLocation());
    var possibleActions = game.getPossibleActions();
    game.interact(possibleActions.get(0));
    assertEquals(locations.get(1), game.getActivePlayer().getCurrentLocation());
  }


  @Test
  public void testPossibleActionsAt01() throws NoSuchMethodException {
    Game game = new DungeonGame(new FakeWorldBuilder());
    moveTo01(game);
    assertTrue(testAllActionsHelper(game, 4, 3, 1,
            List.of("east", "west", "south", "arrow")));
    assertEquals(SmellStrength.NONE, game.getActivePlayer()
            .getCurrentLocation().getSmellStrength());
  }

  @Test
  public void testMoveTo00InteractAt01() throws NoSuchMethodException {
    Game game = new DungeonGame(new FakeWorldBuilder());
    moveTo01(game);
    MoveAction nextMove = game.getAllMoveActions().get(Direction.WEST);
    game.interact(nextMove);
    assertEquals(locations.get(0), game.getActivePlayer().getCurrentLocation());
  }

  @Test
  public void testMoveTo02InteractAt01() throws NoSuchMethodException {
    Game game = new DungeonGame(new FakeWorldBuilder());
    moveTo01(game);
    MoveAction nextMove = game.getAllMoveActions().get(Direction.EAST);
    game.interact(nextMove);
    assertEquals(locations.get(2), game.getActivePlayer().getCurrentLocation());
  }

  @Test
  public void testMoveTo01InteractAt01() throws NoSuchMethodException {
    Game game = new DungeonGame(new FakeWorldBuilder());
    moveTo01(game);
    MoveAction nextMove = game.getAllMoveActions().get(Direction.SOUTH);
    game.interact(nextMove);
    assertEquals(locations.get(4), game.getActivePlayer().getCurrentLocation());
  }

  @Test
  public void testPickArrowInteractAt01() throws NoSuchMethodException {
    Game game = new DungeonGame(new FakeWorldBuilder());
    moveTo01(game);
    PickItemAction nextAction = game.getAllPickActions().get(ItemType.ARROW);
    game.interact(nextAction);
    assertEquals(4, ((IPlayer) game.getActivePlayer()).getQuiver().size());
  }


  @Test
  public void testPossibleActionsAt02() throws NoSuchMethodException {
    Game game = new DungeonGame(new FakeWorldBuilder());
    moveTo02(game);
    assertTrue(testAllActionsHelper(game, 2, 2, 0,
            List.of("west", "south")));
    assertEquals(SmellStrength.NONE, game.getActivePlayer()
            .getCurrentLocation().getSmellStrength());
  }


  @Test
  public void testMoveTo12InteractAt02() throws NoSuchMethodException {
    Game game = new DungeonGame(new FakeWorldBuilder());
    moveTo02(game);
    MoveAction nextMove = game.getAllMoveActions().get(Direction.SOUTH);
    game.interact(nextMove);
    assertEquals(locations.get(5), game.getActivePlayer().getCurrentLocation());
  }

  @Test
  public void testMoveTo01InteractAt02() throws NoSuchMethodException {
    Game game = new DungeonGame(new FakeWorldBuilder());
    moveTo02(game);
    MoveAction nextMove = game.getAllMoveActions().get(Direction.WEST);
    game.interact(nextMove);
    assertEquals(locations.get(1), game.getActivePlayer().getCurrentLocation());
  }

  @Test
  public void testPossibleActionsAt12() throws NoSuchMethodException {
    Game game = new DungeonGame(new FakeWorldBuilder());
    moveTo12(game);
    assertTrue(testAllActionsHelper(game, 3, 2, 1,
            List.of("west", "north")));
    assertEquals(SmellStrength.NONE, game.getActivePlayer()
            .getCurrentLocation().getSmellStrength());
  }

  @Test
  public void testMoveTo02InteractAt12() throws NoSuchMethodException {
    Game game = new DungeonGame(new FakeWorldBuilder());
    moveTo12(game);
    MoveAction nextMove = game.getAllMoveActions().get(Direction.NORTH);
    game.interact(nextMove);
    assertEquals(locations.get(2), game.getActivePlayer().getCurrentLocation());
  }

  @Test
  public void testMoveTo11InteractAt12() throws NoSuchMethodException {
    Game game = new DungeonGame(new FakeWorldBuilder());
    moveTo12(game);
    MoveAction nextMove = game.getAllMoveActions().get(Direction.WEST);
    game.interact(nextMove);
    assertEquals(locations.get(4), game.getActivePlayer().getCurrentLocation());
  }

  @Test
  public void testPickTreasureInteractAt12() throws NoSuchMethodException {
    Game game = new DungeonGame(new FakeWorldBuilder());
    moveTo12(game);
    PickItemAction nextMove = game.getAllPickActions().get(ItemType.TREASURE);
    game.interact(nextMove);
    assertEquals(4, game.getActivePlayer().getAllItems().size());
  }

  @Test
  public void testPossibleActionsAt11() throws NoSuchMethodException {
    Game game = new DungeonGame(new FakeWorldBuilder());
    moveTo11(game);
    assertEquals(SmellStrength.WEAK,
            game.getActivePlayer().getCurrentLocation().getSmellStrength());
    assertTrue(testAllActionsHelper(game,
            3, 3, 0,
            List.of("east", "west", "north")));
  }

  @Test
  public void testKillMonsterAt11() throws NoSuchMethodException {
    Game game = new DungeonGame(new FakeWorldBuilder());
    moveTo11(game);
    assertEquals(SmellStrength.WEAK,
            game.getActivePlayer().getCurrentLocation().getSmellStrength());
    assertTrue(testAllActionsHelper(game,
            3, 3, 0,
            List.of("east", "west", "north")));
    ShootArrowActionBuilder actionBuilder = game.getAllPlayerAction()
            .get(PlayerActionType.SHOOT_ARROW);
    game.interact(actionBuilder.withDirection(Direction.WEST).withDistance(1).build());
    game.interact(actionBuilder.withDirection(Direction.WEST).withDistance(1).build());
    //Test smell goes away after killing a monster.
    assertEquals(SmellStrength.NONE,
            game.getActivePlayer().getCurrentLocation().getSmellStrength());
  }

  @Test
  public void testMovingTo10From11() throws NoSuchMethodException {
    Game game = new DungeonGame(new FakeWorldBuilder());
    moveTo11(game);
    var nextAction = game.getAllMoveActions().get(Direction.WEST);
    game.interact(nextAction);
    assertEquals(locations.get(3), game.getActivePlayer().getCurrentLocation());
  }

  @Test
  public void testMovingTo01From11() throws NoSuchMethodException {
    Game game = new DungeonGame(new FakeWorldBuilder());
    moveTo11(game);
    var nextAction = game.getAllMoveActions().get(Direction.NORTH);
    game.interact(nextAction);
    assertEquals(locations.get(1), game.getActivePlayer().getCurrentLocation());
  }

  @Test
  public void testMovingTo12From11() throws NoSuchMethodException {
    Game game = new DungeonGame(new FakeWorldBuilder());
    moveTo11(game);
    var nextAction = game.getAllMoveActions().get(Direction.EAST);
    game.interact(nextAction);
    assertEquals(locations.get(5), game.getActivePlayer().getCurrentLocation());
  }

  @Test
  public void testPossibleActionsAt10() throws NoSuchMethodException {
    Game game = new DungeonGame(new FakeWorldBuilder());
    moveTo10(game);
    assertEquals(SmellStrength.STRONG,
            game.getActivePlayer().getCurrentLocation().getSmellStrength());
    assertTrue(testAllActionsHelper(game,
            2, 2, 0,
            List.of("east", "south")));
  }

  @Test
  public void testMoveTo20From10() throws NoSuchMethodException {
    Game game = new DungeonGame(new FakeWorldBuilder());
    moveTo10(game);
    var nextMove = game.getAllMoveActions().get(Direction.SOUTH);
    game.interact(nextMove);
    assertEquals(locations.get(6), game.getActivePlayer().getCurrentLocation());
    assertTrue(game.isGameOver());
  }

  @Test
  public void testMoveTo11From10() throws NoSuchMethodException {
    Game game = new DungeonGame(new FakeWorldBuilder());
    moveTo10(game);
    var nextMove = game.getAllMoveActions().get(Direction.EAST);
    game.interact(nextMove);
    assertEquals(locations.get(4), game.getActivePlayer().getCurrentLocation());
    assertFalse(game.isGameOver());
  }

  @Test
  public void testKillingMonster() {
    Game game = new DungeonGame(new FakeWorldBuilder());
    moveTo10(game);
    ShootArrowActionBuilder nextMoveBuilder = game.getAllPlayerAction()
            .get(PlayerActionType.SHOOT_ARROW);
    nextMoveBuilder.withDirection(Direction.SOUTH);
    nextMoveBuilder.withDistance(1);
    game.interact(nextMoveBuilder.build());
    assertEquals(new MutableInt(1), hitCount);
    game.interact(nextMoveBuilder.build());
    assertEquals(new MutableInt(2), hitCount);
    assertEquals(new MutableBoolean(true), deathNotice);
  }


  @Test
  public void testPossibleActionsAt20() throws NoSuchMethodException {
    Game game = new DungeonGame(new FakeWorldBuilder());
    moveTo20(game);
    assertTrue(testAllActionsHelper(game,
            3, 3, 0,
            List.of("east", "west", "north")));
    assertEquals(SmellStrength.STRONG,
            game.getActivePlayer().getCurrentLocation().getSmellStrength());
  }

  @Test
  public void testMoveTo22From20() throws NoSuchMethodException {
    Game game = new DungeonGame(new FakeWorldBuilder());
    moveTo20(game);
    var nextAction = game.getAllMoveActions().get(Direction.WEST);
    game.interact(nextAction);
    assertEquals(locations.get(8), game.getActivePlayer().getCurrentLocation());
    assertTrue(game.isGameOver());
  }

  @Test
  public void testMoveTo10From20() throws NoSuchMethodException {
    Game game = new DungeonGame(new FakeWorldBuilder());
    moveTo20(game);
    var nextAction = game.getAllMoveActions().get(Direction.NORTH);
    game.interact(nextAction);
    assertEquals(locations.get(3), game.getActivePlayer().getCurrentLocation());
  }

  @Test
  public void testMoveTo21From20() throws NoSuchMethodException {
    Game game = new DungeonGame(new FakeWorldBuilder());
    moveTo20(game);
    var nextAction = game.getAllMoveActions().get(Direction.EAST);
    game.interact(nextAction);
    assertEquals(locations.get(7), game.getActivePlayer().getCurrentLocation());
  }

  @Test
  public void testPossibleActionsAt21() throws NoSuchMethodException {
    Game game = new DungeonGame(new FakeWorldBuilder());
    moveTo21(game);
    assertEquals(SmellStrength.STRONG,
            game.getActivePlayer().getCurrentLocation().getSmellStrength());
    assertTrue(
            testAllActionsHelper(game, 2, 2, 0,
                    List.of("west", "east")));
  }

  @Test
  public void testMove20From21() throws NoSuchMethodException {
    Game game = new DungeonGame(new FakeWorldBuilder());
    moveTo21(game);
    var nextAction = game.getAllMoveActions().get(Direction.WEST);
    game.interact(nextAction);
    assertEquals(locations.get(6), game.getActivePlayer().getCurrentLocation());
  }

  @Test
  public void testMove22From21() throws NoSuchMethodException {
    Game game = new DungeonGame(new FakeWorldBuilder());
    moveTo21(game);
    var nextAction = game.getAllMoveActions().get(Direction.EAST);
    game.interact(nextAction);
    assertEquals(locations.get(8), game.getActivePlayer().getCurrentLocation());
    assertTrue(game.isGameOver());
  }

  @Test
  public void testKillLastMonsterAndEndGame() throws NoSuchMethodException {
    Game game = new DungeonGame(new FakeWorldBuilder());
    moveTo21(game);
    ShootArrowActionBuilder nextAction = game.getAllPlayerAction()
            .get(PlayerActionType.SHOOT_ARROW);
    game.interact(nextAction.withDistance(1).withDirection(Direction.EAST).build());
    try {
      game.interact(nextAction.withDistance(1).withDirection(Direction.EAST).build());
      fail("Exception should be thrown.");
    } catch (NoSuchElementException e) {
      assertEquals("Player has no arrows", e.getMessage());
    }
    ((IPlayer) game.getActivePlayer()).assignArrow(new Arrow());
    game.interact(nextAction.withDistance(1).withDirection(Direction.EAST).build());
    MoveAction nextMoveAction = game.getAllMoveActions().get(Direction.EAST);
    game.interact(nextMoveAction);
    assertEquals(locations.get(8), game.getActivePlayer().getCurrentLocation());
    assertTrue(game.isGameOver());
    assertEquals(1d, game.getActivePlayer().getCurrentHealth(), 0);
  }


  @Test
  public void testRestart() throws NoSuchMethodException {
    WorldBuildStrategy builder = new FakeWorldBuilder();
    Game game = new DungeonGame(builder);
    moveTo21(game);
    game.restartGame();
    assertEquals(game.getActivePlayer().getCurrentLocation(),
            builder.buildWorld().getStartLocation());
    EventContainer.getModelEventBus()
            .post(
                    new CallEvent<>(
                            new Otyughs(builder.buildWorld().getStartLocation(), "foo", false)));
    assertEquals(new MutableInt(2), monsterCount);
  }

  //region moves
  private void moveTo01(Game game) {
    List<Action<?>> possibleActions = game.getPossibleActions();
    game.interact(possibleActions.get(0));
  }

  private void moveTo02(Game game) {
    moveTo01(game);
    MoveAction nextMove = game.getAllMoveActions().get(Direction.EAST);
    game.interact(nextMove);
  }

  private void moveTo12(Game game) {
    moveTo02(game);
    game.interact(game.getAllMoveActions().get(Direction.SOUTH));
  }

  private void moveTo11(Game game) {
    moveTo12(game);
    game.interact(game.getAllMoveActions().get(Direction.WEST));
  }

  private void moveTo10(Game game) {
    moveTo11(game);
    game.interact(game.getAllMoveActions().get(Direction.WEST));
  }

  private void moveTo20(Game game) {
    moveTo10(game);
    ShootArrowActionBuilder nextMoveBuilder = game.getAllPlayerAction()
            .get(PlayerActionType.SHOOT_ARROW);
    nextMoveBuilder.withDirection(Direction.SOUTH);
    nextMoveBuilder.withDistance(1);
    game.interact(nextMoveBuilder.build());
    game.interact(nextMoveBuilder.build());
    game.interact(game.getAllMoveActions().get(Direction.SOUTH));
  }

  private void moveTo21(Game game) {
    moveTo20(game);
    game.interact(game.getAllMoveActions().get(Direction.EAST));
  }

  private void moveTo22(Game game) {
    moveTo21(game);
    game.interact(game.getAllMoveActions().get(Direction.EAST));
  }
  //endregion

  private boolean testAllActionsHelper(Game game, int expectedAllActionSize,
                                       int expectedAllMoveActionSize,
                                       int expectedAllPickActionSize,
                                       List<String> expectedActionDescriptions) {
    List<Action<?>> possibleActions = game.getPossibleActions();
    if (possibleActions.size() != expectedAllActionSize) {
      return false;
    }
    if (game.getAllMoveActions().size() != expectedAllMoveActionSize) {
      return false;
    }
    if (game.getAllPlayerAction().size() != 1) {
      return false;
    }
    if (game.getAllPickActions().size() != expectedAllPickActionSize) {
      return false;
    }
    List<Boolean> hasAllExpectedActions = new ArrayList<>();
    for (Action<?> action : possibleActions) {
      for (String expectedDescription :
              expectedActionDescriptions) {
        if (action.describe().toLowerCase(Locale.ROOT).contains(expectedDescription)) {
          hasAllExpectedActions.add(true);
          break;
        }
      }
    }
    return hasAllExpectedActions.size() == expectedAllActionSize;
  }


  @Subscribe
  public void getHitNotification(HitTakenEvent<?> hitTakenEvent) {
    hitCount.increment();
  }

  @Subscribe
  public void getDeathNotification(DeathEvent<Otyughs> deathEvent) {
    deathNotice.setValue(true);
  }

  @Subscribe
  public void countMonsters(@NotNull RoarEvent<Otyughs> roarEvent) {
    Otyughs roarer = roarEvent.getRoarer();
    monsterCount.increment();
  }


}