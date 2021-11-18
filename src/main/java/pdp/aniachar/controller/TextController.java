package pdp.aniachar.controller;

import com.google.common.eventbus.Subscribe;

import org.apache.commons.lang3.tuple.MutablePair;
import org.jetbrains.annotations.Contract;
import org.jetbrains.annotations.NotNull;

import java.util.List;
import java.util.Locale;
import java.util.Optional;

import lombok.Setter;
import pdp.aniachar.Communication;
import pdp.aniachar.dungeonmaster.character.monster.Otyughs;
import pdp.aniachar.dungeonmaster.comm.CallEvent;
import pdp.aniachar.dungeonmaster.comm.DeathEvent;
import pdp.aniachar.dungeonmaster.comm.EventContainer;
import pdp.aniachar.dungeonmaster.comm.HitTakenEvent;
import pdp.aniachar.dungeonmaster.comm.PlayerDeathEvent;
import pdp.aniachar.dungeonmaster.comm.RoarEvent;
import pdp.aniachar.dungeonmaster.gameworld.MazeLocation;
import pdp.aniachar.gamekit.Action;
import pdp.aniachar.gamekit.Direction;
import pdp.aniachar.gamekit.Game;
import pdp.aniachar.gamekit.ImmutableCharacter;
import pdp.aniachar.gamekit.ItemType;
import pdp.aniachar.view.IView;
import pdp.aniachar.view.IViewModel;
import pdp.aniachar.view.PlayerLocationViewModel;


/**
 * A text controller that works with a String input output view.
 */

public class TextController implements IController {

  private final Game game;
  private final String helpText;
  private final IView<String, String> view;
  @Setter
  private boolean debugHelper;

  /**
   * Builds a text controller.
   *
   * @param model The model that this controller will work with.
   * @param view  The view that this controller will work with.
   */

  public TextController(Game model, IView<String, String> view) {
    this.game = model;
    this.view = view;
    helpText = getHelperText();
    Communication.getControllerModelBus().register(this);
  }

  @Override
  public void start() {
    renderToView("Starting the game now.\n");
    boolean invalidCommand = false;
    while (!game.isGameOver()) {
      try {
        if (!invalidCommand) {
          renderToView(new PlayerLocationViewModel(game.getActivePlayer()));
          renderPossibleActions(game.getPossibleActions());
        }
        invalidCommand = false;
        renderToView("> ");
        String userInput = view.getUserInput();
        if (debugHelper) {
          renderToView(String.format("%s\n", userInput));
        }
        Optional<IPlayCommand<?>> command = parseInput(userInput);
        if (command.isEmpty()) {
          invalidCommand = true;
          continue;
        }
        command.get().execute();
      } catch (IndexOutOfBoundsException | IllegalArgumentException | NoSuchMethodException e) {
        invalidCommand = true;
        renderToView(String
                .format("%s.\nYou can use 'help' command to see how to play the game.\n",
                        e.getMessage()));
      }
    }
    try {
      if (game.getActivePlayer().getCurrentHealth() == 1) {
        renderToView("Player entered end cave and has won the game!");
      }
    } catch (NoSuchMethodException e) {
      throw new IllegalStateException("The game crashed!");
    }
  }

  private void renderPossibleActions(@NotNull List<Action<?>> possibleActions) {
    for (Action<?> action :
            possibleActions) {
      renderToView(action.describe());
    }
  }

  private Optional<IPlayCommand<?>> parseInput(String input) {
    input = input.toLowerCase().trim();
    String[] inputs = input.split(" ");
    switch (inputs.length) {
      case 1: {
        return onlyVerbCommand(inputs);
      }
      case 2: {
        return verbNounCommand(inputs);
      }
      case 3: {
        return verbNounQualifierAction(inputs);
      }
      default:
        throw new IllegalArgumentException("Not a valid command");
    }
  }

  @NotNull
  private Optional<IPlayCommand<?>> verbNounQualifierAction(String @NotNull [] inputs) {
    switch (inputs[0]) {
      case "s":
      case "shoot":
        return Optional.of((getShootCommand(inputs[1], inputs[2])));
      default:
        throw new IllegalArgumentException("Not a valid command");
    }
  }

  @NotNull
  private Optional<IPlayCommand<?>> verbNounCommand(String @NotNull [] inputs) {
    switch (inputs[0].toLowerCase()) {
      case "m":
      case "move":
        return Optional.of(getMoveCommand(inputs[1]));
      case "p":
      case "pick":
        return Optional.of((getPickCommand(inputs[1])));
      default:
        throw new IllegalArgumentException("Not a valid command");
    }
  }

  @NotNull
  private Optional<IPlayCommand<?>> onlyVerbCommand(String @NotNull [] inputs) {
    switch (inputs[0]) {
      case "help":
        renderToView(helpText);
        return Optional.empty();

      case "cheat":
        gameCheat();
        return Optional.empty();

      case "quit":
      case "q":
        renderToView("Quitting game.");
        System.exit(0);
        return Optional.empty();

      default:
        throw new IllegalArgumentException("Not a valid command");
    }
  }


  private @NotNull MoveCommand getMoveCommand(String direction) {
    Direction cmdDirection = parseDirection(direction);
    return new MoveCommand(game, cmdDirection);
  }

  private @NotNull PickCommand getPickCommand(String itemType) {
    ItemType cmdItemType = parseItemType(itemType);
    return new PickCommand(game, cmdItemType);
  }

  @Contract("_, _ -> new")
  private @NotNull ShootCommand getShootCommand(String direction, String distance) {
    Direction cmdDirection = parseDirection(direction);
    int cmdDistance;
    try {
      cmdDistance = Integer.parseInt(distance);
    } catch (NumberFormatException e) {
      throw new IllegalArgumentException("Not a valid shoot distance. Distance must be integer");
    }
    return new ShootCommand(game, cmdDistance, cmdDirection);
  }

  @Subscribe
  @Override
  public void monsterHitEventListener(HitTakenEvent<ImmutableCharacter> hitTakenEvent) {
    renderToView(String.format("Monster hit at at: %s", hitTakenEvent.getTarget()));
  }

  @Subscribe
  @Override
  public void playerDeathEventListener(PlayerDeathEvent deathEvent) {
    renderToView("Player died after entering a cave with a monster!");
  }

  private void gameCheat() {
    EventContainer.getModelEventBus()
            .post(
                    new CallEvent<>(
                            new Otyughs(new MazeLocation(new MutablePair<>(0, 0)),
                                    "foo", false)));
  }

  /**
   * This is a cheat in the game!
   *
   * @param otyughsRoarEvent Gets the location of all the monsters in the game.
   */
  @Subscribe
  public void cheatEvent(RoarEvent<Otyughs> otyughsRoarEvent) {
    renderToView(String.format("Monster is at %s",
            otyughsRoarEvent.getRoarer().getCurrentLocation()));
  }

  @Subscribe
  @Override
  public void monsterDeathEventListener(DeathEvent<ImmutableCharacter> deathEvent) {
    renderToView(String.format("Monster killed at at: %s",
            deathEvent.getPayload().getCurrentLocation()));
  }

  private ItemType parseItemType(@NotNull String inputItemType) {
    switch (inputItemType.toLowerCase(Locale.ROOT)) {
      case "t":
      case "treasure":
        return ItemType.TREASURE;
      case "a":
      case "arrow":
        return ItemType.ARROW;
      default:
        throw new IllegalArgumentException("Not a valid item type");
    }
  }

  private Direction parseDirection(@NotNull String inputDirection) {
    switch (inputDirection.toLowerCase()) {
      case "n":
      case "north":
        return Direction.NORTH;
      case "s":
      case "south":
        return Direction.SOUTH;
      case "w":
      case "west":
        return Direction.WEST;
      case "e":
      case "east":
        return Direction.EAST;
      default:
        throw new IllegalArgumentException("Not a valid direction");
    }
  }

  private void renderToView(String text) {
    IViewModel<String> viewModel = () -> text;
    view.renderView(viewModel);
  }

  private void renderToView(IViewModel<String> viewModel) {
    view.renderView(viewModel);
  }

  private String getHelperText() {
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

}
