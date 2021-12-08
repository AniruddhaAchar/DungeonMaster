package pdp.aniachar.controller;

import org.jetbrains.annotations.Contract;
import org.jetbrains.annotations.NotNull;

import java.util.Locale;

import pdp.aniachar.view.ViewModel.IPlayerInteraction;
import pdp.aniachar.view.ViewModel.Nouns;
import pdp.aniachar.view.ViewModel.PlayerInteraction;
import pdp.aniachar.view.ViewModel.Verbs;

/**
 * The parser used to parse the user's text commands and translate them into
 * {@link IPlayerInteraction}s.
 */

public class CommandLineParser {

  /**
   * The parser used to parse the user's text commands and translate them into
   *
   * @param input The user input.
   * @return The IPlayerInteraction that needs to be generated for that input.
   */
  public IPlayerInteraction parseCommand(String input) {
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
  private PlayerInteraction verbNounQualifierAction(String @NotNull [] inputs) {
    switch (inputs[0]) {
      case "s":
      case "shoot":
        return getShootInteraction(inputs[1], inputs[2]);
      default:
        throw new IllegalArgumentException("Not a valid command");
    }
  }

  @NotNull
  private IPlayerInteraction verbNounCommand(String @NotNull [] inputs) {
    switch (inputs[0].toLowerCase()) {
      case "m":
      case "move":
        return getMoveInteraction(inputs[1]);
      case "p":
      case "pick":
        return getPickInteraction(inputs[1]);
      default:
        throw new IllegalArgumentException("Not a valid command");
    }
  }

  @NotNull
  private PlayerInteraction onlyVerbCommand(String @NotNull [] inputs) {
    switch (inputs[0]) {
      case "help":
      case "h":
        return new PlayerInteraction(Verbs.HELP);

      case "quit":
      case "q":
        return new PlayerInteraction(Verbs.QUIT);

      default:
        throw new IllegalArgumentException("Not a valid command");
    }
  }


  private @NotNull PlayerInteraction getMoveInteraction(String direction) {
    Nouns cmdDirection = parseDirection(direction);
    return new PlayerInteraction(Verbs.MOVE, cmdDirection);
  }

  private @NotNull PlayerInteraction getPickInteraction(String itemType) {
    Nouns cmdItemType = parseItemType(itemType);
    return new PlayerInteraction(Verbs.PICK, cmdItemType);
  }

  @Contract("_, _ -> new")
  private @NotNull PlayerInteraction getShootInteraction(String direction, String distance) {
    Nouns cmdDirection = parseDirection(direction);
    int cmdDistance;
    try {
      cmdDistance = Integer.parseInt(distance);
    } catch (NumberFormatException e) {
      throw new IllegalArgumentException("Not a valid shoot distance. Distance must be integer");
    }
    return new PlayerInteraction(Verbs.SHOOT, cmdDirection, cmdDistance);
  }

  private Nouns parseItemType(@NotNull String inputItemType) {
    switch (inputItemType.toLowerCase(Locale.ROOT)) {
      case "t":
      case "treasure":
        return Nouns.TREASURE;
      case "a":
      case "arrow":
        return Nouns.ARROW;
      default:
        throw new IllegalArgumentException("Not a valid item type");
    }
  }

  private Nouns parseDirection(@NotNull String inputDirection) {
    switch (inputDirection.toLowerCase()) {
      case "n":
      case "north":
        return Nouns.NORTH;
      case "s":
      case "south":
        return Nouns.SOUTH;
      case "w":
      case "west":
        return Nouns.WEST;
      case "e":
      case "east":
        return Nouns.EAST;
      default:
        throw new IllegalArgumentException("Not a valid direction");
    }
  }


}
