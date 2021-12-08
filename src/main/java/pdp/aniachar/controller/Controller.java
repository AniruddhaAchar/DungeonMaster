package pdp.aniachar.controller;

import com.google.common.eventbus.Subscribe;

import org.apache.commons.lang3.tuple.MutablePair;
import org.jetbrains.annotations.Contract;
import org.jetbrains.annotations.NotNull;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import lombok.SneakyThrows;
import pdp.aniachar.Communication;
import pdp.aniachar.dungeonmaster.DungeonGame;
import pdp.aniachar.dungeonmaster.character.monster.Otyughs;
import pdp.aniachar.dungeonmaster.comm.CallEvent;
import pdp.aniachar.dungeonmaster.comm.DeathEvent;
import pdp.aniachar.dungeonmaster.comm.EventContainer;
import pdp.aniachar.dungeonmaster.comm.GameOverEvent;
import pdp.aniachar.dungeonmaster.comm.HitTakenEvent;
import pdp.aniachar.dungeonmaster.comm.PlayerDeathEvent;
import pdp.aniachar.dungeonmaster.gameworld.MazeLocation;
import pdp.aniachar.dungeonmaster.gameworld.RandomMazeBuilder;
import pdp.aniachar.gamekit.Direction;
import pdp.aniachar.gamekit.Game;
import pdp.aniachar.gamekit.ImmutableCharacter;
import pdp.aniachar.gamekit.ImmutableLocation;
import pdp.aniachar.gamekit.Item;
import pdp.aniachar.gamekit.ItemType;
import pdp.aniachar.gamekit.WorldBuildStrategy;
import pdp.aniachar.view.GraphicUserInterface.SwingView;
import pdp.aniachar.view.IView;
import pdp.aniachar.view.ViewModel.ErrorViewModel;
import pdp.aniachar.view.ViewModel.Event;
import pdp.aniachar.view.ViewModel.EventDescription;
import pdp.aniachar.view.ViewModel.IEvent;
import pdp.aniachar.view.ViewModel.ILocationViewModel;
import pdp.aniachar.view.ViewModel.INewGameState;
import pdp.aniachar.view.ViewModel.IPlayerInteraction;
import pdp.aniachar.view.ViewModel.IPlayerViewModel;
import pdp.aniachar.view.ViewModel.IRestartGame;
import pdp.aniachar.view.ViewModel.LocationViewModel;
import pdp.aniachar.view.ViewModel.Nouns;
import pdp.aniachar.view.ViewModel.PlayerViewModel;
import pdp.aniachar.view.ViewModel.Quit;
import pdp.aniachar.view.ViewModel.Severity;


/**
 * A text controller that works with a String input output view.
 */

public class Controller implements IController {

  private Game game;
  private IView view;

  /**
   * Builds a text controller.
   *
   * @param model The model that this controller will work with.
   * @param view  The view that this controller will work with.
   */

  public Controller(Game model, IView view) {
    this.game = model;
    this.view = view;
    Communication.getModelControllerBus().register(this);
    Communication.getViewControllerBus().register(this);
  }

  @Override
  public void start() {
    playerViewModelPublisher(getPlayerViewModel());
    locationViewModelPublisher(getLocationViewModel());
    view.start();
  }


  @Subscribe
  @Override
  public void monsterHitEventListener(@NotNull HitTakenEvent<ImmutableLocation<?>> hitTakenEvent) {
    Event event = new Event(EventDescription.MONSTER_HIT,
            hitTakenEvent.getTarget(), Severity.HIGH);
    eventDescriptionPublisher(event);
  }

  @Subscribe
  @Override
  public void playerDeathEventListener(@NotNull PlayerDeathEvent deathEvent) {

    Event event = new Event(EventDescription.PLAYER_DEATH,
            deathEvent.getPlayer().getCurrentLocation(), Severity.CRITICAL);
    eventDescriptionPublisher(event);
  }

  private void gameCheat() {
    EventContainer.getModelEventBus()
            .post(
                    new CallEvent<>(
                            new Otyughs(new MazeLocation(new MutablePair<>(0, 0)),
                                    "foo", false)));
  }

  @Subscribe
  @Override
  public void monsterDeathEventListener(@NotNull DeathEvent<ImmutableCharacter> deathEvent) {

    Event event = new Event(EventDescription.MONSTER_KILLED,
            deathEvent.getPayload().getCurrentLocation(), Severity.HIGH);
    eventDescriptionPublisher(event);
  }

  @Subscribe
  @Override
  public void playerInteractionEventListener(IPlayerInteraction interaction) {
    if (!game.isGameOver()) {
      switch (interaction.getVerb()) {
        case MOVE:
          try {
            new MoveCommand(game, mapViewNounToDirection(interaction.getNouns())).execute();
            playerViewModelPublisher(getPlayerViewModel());
            locationViewModelPublisher(getLocationViewModel());
          } catch (IllegalArgumentException e) {
            Communication.getViewControllerBus().post(new ErrorViewModel(e.getMessage()));
          }
          break;
        case PICK:
          try {
            new PickCommand(game, mapViewNounToItemType(interaction.getNouns())).execute();
            playerViewModelPublisher(getPlayerViewModel());
            locationViewModelPublisher(getLocationViewModel());
          } catch (IllegalArgumentException e) {
            Communication.getViewControllerBus().post(new ErrorViewModel(e.getMessage()));
          }
          break;
        case SHOOT:
          try {
            new ShootCommand(game, interaction.getQualifier(),
                    mapViewNounToDirection(interaction.getNouns())).execute();
            playerViewModelPublisher(getPlayerViewModel());
            locationViewModelPublisher(getLocationViewModel());
          } catch (IllegalArgumentException | IndexOutOfBoundsException e) {
            Communication.getViewControllerBus().post(new ErrorViewModel(e.getMessage()));
          }
          break;
        case QUIT:
          try {
            eventDescriptionPublisher(new Event(EventDescription.QUIT,
                    game.getActivePlayer().getCurrentLocation(), Severity.HIGH));
          } catch (NoSuchMethodException e) {
            throw new IllegalStateException("Game is in a bad state");
          }
          Communication.getViewControllerBus().post(new Quit(true));
          break;
        default:
          throw new IllegalArgumentException(String.format("Not a valid interaction: %s",
                  interaction.getVerb()));
      }
    }
  }

  @SneakyThrows
  @Subscribe
  @Override
  public void gameOverEventListener(@NotNull GameOverEvent event) {
    if (event.isPlayerAlive()) {
      Communication.getViewControllerBus()
              .post(new Event(EventDescription.WIN, game.getActivePlayer().getCurrentLocation(),
                      Severity.CRITICAL));
    } else {
      Communication.getViewControllerBus()
              .post(new Event(EventDescription.LOST, game.getActivePlayer().getCurrentLocation(),
                      Severity.CRITICAL));
    }
  }

  @Override
  public void playerViewModelPublisher(IPlayerViewModel playerViewModel) {
    Communication.getViewControllerBus().post(playerViewModel);
  }

  @Override
  public void eventDescriptionPublisher(IEvent eventDescription) {
    Communication.getViewControllerBus().post(eventDescription);
  }

  @Override
  public void locationViewModelPublisher(ILocationViewModel locationViewModel) {
    Communication.getViewControllerBus().post(locationViewModel);
  }

  @Subscribe
  @Override
  public void restartGameListener(IRestartGame<Boolean> restartGame) {
    game.restartGame();
    if (view instanceof SwingView) {
      int maxCols = ((SwingView) view).getMaxCol();
      int maxRows = ((SwingView) view).getMaxRow();
      Communication.getViewControllerBus().post(new Quit(false));
      view = new SwingView(maxRows, maxCols);
      this.start();
    }
  }

  @Subscribe
  @Override
  public void createNewGame(@NotNull INewGameState gameState) {
    final long numberOfMonsters = gameState.getNumberMonsters();
    final int degreeOfInterConnectivity = gameState.getDegreeOfInterConnection();
    final int maxRows = gameState.getMaxRows();
    final int maxCols = gameState.getMaxCols();
    final int percentTreasure = gameState.getPercentTreasure();
    final boolean isWrapped = gameState.getIsWrapped();
    Communication.getViewControllerBus().post(new Quit(false));
    EventContainer.reset();
    Communication.reset();
    Communication.getViewControllerBus().register(this);
    Communication.getModelControllerBus().register(this);
    WorldBuildStrategy worldBuilder = new RandomMazeBuilder(maxRows, maxCols, isWrapped,
            degreeOfInterConnectivity, percentTreasure, numberOfMonsters);
    game = new DungeonGame(worldBuilder);
    view = new SwingView(maxRows, maxCols);
    this.start();
  }

  @Contract(" -> new")
  private @NotNull ILocationViewModel getLocationViewModel() {
    try {
      return new LocationViewModel(game.getAllMoveActions(),
              game.getActivePlayer().getCurrentLocation(),
              game.getAllPickActions());
    } catch (NoSuchMethodException e) {
      throw new IllegalStateException("Game is in a bad state");
    }
  }

  @Contract(" -> new")
  private @NotNull IPlayerViewModel getPlayerViewModel() {

    try {
      List<Item> items = game.getActivePlayer().getAllItems();

      Map<String, Long> inventory = new HashMap<>();
      for (Item item : items) {
        if (!inventory.containsKey(item.getName())) {
          inventory.put(item.getName(), 1L);
        } else {
          inventory.computeIfPresent(item.getName(), (name, count) -> count + 1);
        }
      }
      return new PlayerViewModel(game.getActivePlayer().getName(),
              game.getActivePlayer().getCurrentHealth(), inventory);
    } catch (NoSuchMethodException e) {
      throw new IllegalStateException("Game is not in a valid state");
    }
  }

  private @NotNull String getHelperText() {
    return new StringBuilder()
            .append("The game uses verb-[noun]+[qualifier] syntax for the command.\n")
            .append("Some verbs may have nouns while some verbs may have qualifiers as well. \n")
            .append("Valid commands:\n")
            .append("help\n")
            .append("\tMove command:\n")
            .append("move <direction>\n")
            .append("\tValid directions are:\n")
            .append("north\n")
            .append("south\n")
            .append("east\n")
            .append("west\n")
            .append("\tTo move east, you can enter:\n")
            .append("\t\t`move east`\n")
            .append("\tPick command:\n")
            .append("pick <item>\n")
            .append("\tValid items:\n")
            .append("treasure\n")
            .append("arrow\n")
            .append("\tValid pick command:\n")
            .append("\t\t`pick arrow`\n")
            .append("\tShoot arrow command:\n")
            .append("shoot <direction> <distance>\n")
            .append("\tValid shoot command:\n")
            .append("\t\t`shoot north 3`\n").append("\tValid directions are the same as above.")
            .append(" Distance should be a positive integer.\n")
            .append("\n").append("All these commands have a shorthand.")
            .append(" Just use the first letter of the command.\n")
            .append("You can quit the game by entering `quit`.\n")
            .toString();
  }

  @Contract(pure = true)
  private Direction mapViewNounToDirection(@NotNull Nouns direction) {
    switch (direction) {
      case EAST:
        return Direction.EAST;
      case WEST:
        return Direction.WEST;
      case SOUTH:
        return Direction.SOUTH;
      case NORTH:
        return Direction.NORTH;
      default:
        throw new IllegalArgumentException("Not a valid direction");
    }
  }

  @Contract(pure = true)
  private ItemType mapViewNounToItemType(@NotNull Nouns itemType) {
    switch (itemType) {
      case TREASURE:
        return ItemType.TREASURE;
      case ARROW:
        return ItemType.ARROW;
      default:
        throw new IllegalArgumentException("Not a valid item type");
    }
  }

}
