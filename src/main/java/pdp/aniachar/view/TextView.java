package pdp.aniachar.view;

import com.google.common.eventbus.Subscribe;

import org.jetbrains.annotations.Contract;
import org.jetbrains.annotations.NotNull;

import java.io.IOException;
import java.util.Locale;
import java.util.Scanner;

import pdp.aniachar.Communication;
import pdp.aniachar.controller.CommandLineParser;
import pdp.aniachar.dungeonmaster.gameworld.SmellStrength;
import pdp.aniachar.view.ViewModel.IErrorViewModel;
import pdp.aniachar.view.ViewModel.IEvent;
import pdp.aniachar.view.ViewModel.ILocationViewModel;
import pdp.aniachar.view.ViewModel.IPlayerInteraction;
import pdp.aniachar.view.ViewModel.IPlayerViewModel;
import pdp.aniachar.view.ViewModel.IQuit;

/**
 * A text specific view that consumes a String and produces a string.
 */

public class TextView implements IView {

  private final Scanner scanner;
  private final CommandLineParser parser;
  private final Appendable out;
  private boolean quit = false;

  /**
   * Builds a Text view.
   *
   * @param out The consumer.
   * @param in  The producer.
   */

  public TextView(@NotNull Appendable out, @NotNull Readable in, CommandLineParser parser) {
    this.out = out;
    scanner = new Scanner(in);
    this.parser = parser;
    Communication.getViewControllerBus().register(this);

  }

  @Override
  public void start() {
    while (!quit) {
      emitPlayerInteraction();
    }
  }

  @Override
  public void stop() {
    this.quit = true;
  }

  @Subscribe
  @Override
  public void renderPlayerViewModelListener(@NotNull IPlayerViewModel playerViewModel) {
    renderToOutput(getDivider());
    renderToOutput(String.format("%s's health is %f and is carrying\n", playerViewModel.getName(),
            playerViewModel.getHealth()));
    playerViewModel.getInventory().forEach((itemName, count) ->
            renderToOutput(String.format("%d - %s\n", count, itemName)));
    renderToOutput(getDivider());
  }

  @Subscribe
  @Override
  public void renderLocationViewModelListener(@NotNull ILocationViewModel locationViewModel) {
    renderToOutput(getDivider());
    renderToOutput(String.format("Player is at %s\n",
            locationViewModel.getCurrentLocation().getLocationDescription()));
    locationViewModel.getItems().forEach((s, action) ->
            renderToOutput(String.format("You can pick %s here.\n", s)));
    renderToOutput("You can move: \n");
    locationViewModel.getPossibleMoves().forEach((direction, action) ->
            renderToOutput(String.format("%s\n", direction.toString())));
    if (locationViewModel.getCurrentLocation().getSmellStrength() != SmellStrength.NONE) {
      renderToOutput(String.format("Player can detect a %s smell",
              locationViewModel.getCurrentLocation().getSmellStrength()
                      .toString().toLowerCase(Locale.ROOT)));
    }
    renderToOutput(getDivider());
  }

  @Subscribe
  @Override
  public void renderEventDescriptionListener(@NotNull IEvent eventDescription) {
    renderToOutput(getEmphasis());
    renderToOutput(String.format("%s occurred at %s", eventDescription.getDescription(),
            eventDescription.getLocation().getLocationDescription()));
    renderToOutput(getEmphasis());
  }

  @Subscribe
  @Override
  public void renderErrorViewModelListener(@NotNull IErrorViewModel errorViewModel) {
    renderToOutput(String.format("%s\n", errorViewModel.getErrorMessage()));
  }

  @Subscribe
  @Override
  public void quitListener(@NotNull IQuit quit) {
    this.quit = true;
  }


  private void emitPlayerInteraction() {
    try {
      IPlayerInteraction interaction = parser.parseCommand(scanner.nextLine());
      Communication.getViewControllerBus().post(interaction);
    } catch (IllegalArgumentException e) {
      renderToOutput(String
              .format("%s\n Use the 'help' command to get instructions on how to play the game\n",
                      e.getMessage()));
    }
  }

  @Contract(pure = true)
  private @NotNull String getDivider() {
    return "\n_________________\n";
  }

  @Contract(pure = true)
  private @NotNull String getEmphasis() {
    return "\n****************\n";
  }

  private void renderToOutput(String toRender) {
    try {
      out.append(toRender);
    } catch (IOException e) {
      throw new IllegalStateException("Cannot append to the appendable because: " + e.getMessage());
    }
  }
}
