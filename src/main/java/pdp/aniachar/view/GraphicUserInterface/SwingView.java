package pdp.aniachar.view.GraphicUserInterface;

import com.google.common.eventbus.Subscribe;

import org.apache.commons.lang3.tuple.MutablePair;
import org.jetbrains.annotations.NotNull;

import java.awt.Toolkit;

import lombok.Getter;
import lombok.extern.flogger.Flogger;
import pdp.aniachar.Communication;
import pdp.aniachar.view.IView;
import pdp.aniachar.view.ViewModel.IErrorViewModel;
import pdp.aniachar.view.ViewModel.IEvent;
import pdp.aniachar.view.ViewModel.ILocationViewModel;
import pdp.aniachar.view.ViewModel.IPlayerViewModel;
import pdp.aniachar.view.ViewModel.IQuit;


/**
 * A view built using Java swing.
 */

@Flogger
public class SwingView implements IView {

  @Getter
  private final int maxRow, maxCol;
  private final GameFrame gameFrame;

  /**
   * Builds the Java swing view.
   *
   * @param maxRow The number of rows of the maze.
   * @param maxCol The number of columns of the maze.
   */

  public SwingView(int maxRow, int maxCol) {
    this.maxRow = maxRow;
    this.maxCol = maxCol;
    gameFrame = new GameFrame(maxRow, maxCol);
    Communication.getViewControllerBus().register(this);
  }

  @Subscribe
  @Override
  public void renderPlayerViewModelListener(@NotNull IPlayerViewModel playerViewModel) {
    gameFrame.getPlayerViewPanel().updateView(playerViewModel);
  }

  @Subscribe
  @Override
  public void renderLocationViewModelListener(@NotNull ILocationViewModel locationViewModel) {
    log.atFine().log("Received a new location model");
    gameFrame.getMazeViewPanel().updateView(locationViewModel);
  }

  @Subscribe
  @Override
  public void renderEventDescriptionListener(@NotNull IEvent eventDescription) {
    gameFrame.getEventViewPanel().updateView(eventDescription);
    MutablePair<Integer, Integer> eventLocation = (MutablePair<Integer, Integer>) eventDescription
            .getLocation().getLocationDescription();

    switch (eventDescription.getDescription()) {
      case MONSTER_HIT:
        gameFrame.getMazeViewPanel().getMazeGridPanel()
                .setMonster(eventLocation);
        break;
      case MONSTER_KILLED:
        gameFrame.getMazeViewPanel().getMazeGridPanel()
                .unsetMonster(eventLocation);
        break;
      case PLAYER_DEATH:
        gameFrame.getMazeViewPanel().getMazeGridPanel()
                .playerDead(eventLocation);
        gameFrame.getMazeViewPanel().getMazeGridPanel()
                .setMonster(eventLocation);
        break;
      default:
        break;
    }


  }

  @Subscribe
  @Override
  public void renderErrorViewModelListener(@NotNull IErrorViewModel errorViewModel) {
    log.atWarning().log(errorViewModel.getErrorMessage());
    Toolkit.getDefaultToolkit().beep();
  }


  @Override
  public void start() {
    log.atFine().log("Starting the view");
    gameFrame.pack();
    gameFrame.setVisible(true);
  }

  @Override
  public void stop() {
    log.atFine().log("Stopping view");

  }

  @Subscribe
  @Override
  public void quitListener(@NotNull IQuit quit) {
    gameFrame.dispose();
  }


}
