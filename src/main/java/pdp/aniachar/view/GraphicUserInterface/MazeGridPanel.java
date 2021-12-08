package pdp.aniachar.view.GraphicUserInterface;

import org.apache.commons.lang3.tuple.ImmutablePair;
import org.apache.commons.lang3.tuple.MutablePair;
import org.jetbrains.annotations.NotNull;

import java.awt.*;
import java.awt.event.ActionListener;
import java.awt.event.KeyEvent;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.net.URL;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.Optional;

import javax.swing.*;

import lombok.extern.flogger.Flogger;
import pdp.aniachar.Communication;
import pdp.aniachar.dungeonmaster.action.location.MoveAction;
import pdp.aniachar.dungeonmaster.action.location.PickItemAction;
import pdp.aniachar.dungeonmaster.gameworld.SmellStrength;
import pdp.aniachar.gamekit.Direction;
import pdp.aniachar.gamekit.ItemType;
import pdp.aniachar.view.ViewModel.ILocationViewModel;
import pdp.aniachar.view.ViewModel.Nouns;
import pdp.aniachar.view.ViewModel.PlayerInteraction;
import pdp.aniachar.view.ViewModel.Verbs;


/**
 * A panel that houses the maze grid and all the user interactions that can happen with the maze.
 * This provides the location information, how to move the player, how to handle the keyboard and
 * mouse interactions.
 * This is also responsible for handling the population of the popup context menu.
 */

@Flogger
public class MazeGridPanel extends JPanel {
  private final int maxRows, maxCols;
  private final GridBagConstraints gridBagConstraints;
  List<List<MazeLocationPanel>> mazeLocationButtons;
  private MazePopupMenu contextMenu;
  private MutablePair<Integer, Integer> currentLocation;
  private boolean playerDead;


  /**
   * Builds the maze location with the size specified.
   *
   * @param maxRows The number of rows in the grid.
   * @param maxCols The number of rows in the grid.
   */

  public MazeGridPanel(int maxRows, int maxCols) {
    super();
    this.maxRows = maxRows;
    this.maxCols = maxCols;
    mazeLocationButtons = new ArrayList<>();
    setLayout(new GridBagLayout());
    setMinimumSize(new Dimension(64 * maxRows, 64 * maxCols));
    setMaximumSize(new Dimension(64 * maxRows, 64 * maxCols));
    setPreferredSize(new Dimension(64 * maxRows, 64 * maxCols));
    gridBagConstraints = new GridBagConstraints();
    gridBagConstraints.fill = GridBagConstraints.HORIZONTAL;
    buildMaze();
    contextMenu = new MazePopupMenu();
    handleRightClickToShowPopup();
    setupKeyboardActionListener();
    playerDead = false;
  }

  private void buildMaze() {
    for (int row = 0; row < maxRows; row++) {
      List<MazeLocationPanel> locationButtons = new ArrayList<>();
      for (int col = 0; col < maxCols; col++) {
        var mazeButton = new MazeLocationPanel(row, col);
        locationButtons.add(mazeButton);
        gridBagConstraints.gridx = col;
        gridBagConstraints.gridy = row;
        add(mazeButton, gridBagConstraints);
      }
      mazeLocationButtons.add(locationButtons);
    }
  }

  void updateView(@NotNull ILocationViewModel locationViewModel) {
    //log.atFine().log("Updating the view");

    SmellStrength smellStrength = locationViewModel.getCurrentLocation().getSmellStrength();
    currentLocation = (MutablePair<Integer, Integer>) locationViewModel.getCurrentLocation()
            .getLocationDescription();
    updateLocationPanel(currentLocation.getLeft(), currentLocation.getRight(), locationViewModel
                    .getPossibleMoves(),
            smellStrength, locationViewModel.getItems().size() > 0);
    updatePopUpMenu(locationViewModel.getItems());
    movePlayer(currentLocation.getLeft(), currentLocation.getRight());
  }

  void setMonster(MutablePair<Integer, Integer> gridLocation) {
    mazeLocationButtons.get(gridLocation.left).get(gridLocation.right).setMonster();
  }

  void unsetMonster(MutablePair<Integer, Integer> gridLocation) {
    mazeLocationButtons.get(gridLocation.left).get(gridLocation.right).unsetMonster();
  }

  void playerDead(MutablePair<Integer, Integer> gridLocation) {
    clearPlayerIcon();
    Class<? extends MazeGridPanel> thisClass = getClass();
    URL resource = thisClass.getResource("/death.gif");
    assert resource != null;
    mazeLocationButtons.get(gridLocation.left).get(gridLocation.right)
            .setPlayerGif(new ImageIcon(resource));
    revalidate();
    playerDead = true;
  }

  void movePlayer(int row, int col) {
    log.atFine().log("Setting player at %d, %d", row, col);
    if (!playerDead) {
      clearPlayerIcon();
      Class<? extends MazeGridPanel> thisClass = getClass();
      URL resource = thisClass.getResource("/idle.gif");
      mazeLocationButtons.get(row).get(col)
              .setPlayerGif(new ImageIcon(Objects.requireNonNull(resource)));
    }
  }

  private void clearPlayerIcon() {
    Optional<List<MazeLocationPanel>> previous = mazeLocationButtons
            .stream()
            .filter(mazeLocationButtons1 -> mazeLocationButtons1
                    .stream().anyMatch(mazeLocationPanel -> mazeLocationPanel
                            .getPlayerGif().getIcon() != null)).findFirst();
    previous.ifPresent(mazeLocationButtons1 -> mazeLocationButtons1
            .forEach(mazeLocationPanel -> mazeLocationPanel.setPlayerGif(null)));
  }


  private void clearChestIcon(MazeLocationPanel mazeLocationPanel) {
    mazeLocationPanel.getChestImage().setIcon(null);
  }

  private void updateLocationPanel(int row, int col, Map<Direction, MoveAction> possibleMoves,
                                   SmellStrength smellStrength, boolean hasTreasure) {
    log.atFine().log("Updating button (%d, %d)", row, col);
    if (hasTreasure) {
      mazeLocationButtons.get(row).get(col).setChest();
    } else {
      clearChestIcon(mazeLocationButtons.get(row).get(col));
    }
    mazeLocationButtons.get(row).get(col).updateView(possibleMoves, smellStrength);
  }

  private void updatePopUpMenu(Map<ItemType, PickItemAction> items) {
    contextMenu = new MazePopupMenu(items);
    setComponentPopupMenu(contextMenu);
  }

  private void handleRightClickToShowPopup() {
    addMouseListener(new MouseAdapter() {
      @Override
      public void mouseClicked(MouseEvent e) {

        super.mouseClicked(e);
        if (e.getButton() == MouseEvent.BUTTON3) {
          log.atFine().log("Heard a right click");
          contextMenu.show(e.getComponent(), e.getX(), e.getY());
        }
      }
    });
  }

  private void setupKeyboardActionListener() {
    getKeyboardActions()
            .forEach((actionListener, keyStroke) ->
                    registerKeyboardAction(actionListener, keyStroke, WHEN_IN_FOCUSED_WINDOW));
  }

  private Map<ActionListener, KeyStroke> getKeyboardActions() {
    Map<ActionListener, KeyStroke> keyboardActions = new HashMap<>();

    //When Alt+S is typed, bring up the menu.
    keyboardActions.put(e -> {
      int x = MouseInfo.getPointerInfo().getLocation().x;
      int y = MouseInfo.getPointerInfo().getLocation().y;
      contextMenu.show(this, x, y);
      try {
        Robot robot = new Robot();
        robot.keyPress(KeyEvent.VK_ALT);
        robot.keyPress(KeyEvent.VK_O);
        robot.keyRelease(KeyEvent.VK_O);
        robot.keyRelease(KeyEvent.VK_ALT);
      } catch (AWTException ex) {
        ex.printStackTrace();
      }
    }, KeyStroke.getKeyStroke(KeyEvent.VK_S, 0));

    keyboardActions.put(e -> {
      int x = MouseInfo.getPointerInfo().getLocation().x;
      int y = MouseInfo.getPointerInfo().getLocation().y;
      contextMenu.show(this, x, y);

    }, KeyStroke.getKeyStroke(KeyEvent.VK_P, 0));

    keyboardActions.put(e -> {
      log.atFine().log("Up arrow pressed");
      Communication.getViewControllerBus().post(new PlayerInteraction(Verbs.MOVE, Nouns.NORTH));
    }, KeyStroke.getKeyStroke(KeyEvent.VK_UP, 0));
    keyboardActions.put(e -> {
      log.atFine().log("Down arrow pressed");
      Communication.getViewControllerBus().post(new PlayerInteraction(Verbs.MOVE, Nouns.SOUTH));
    }, KeyStroke.getKeyStroke(KeyEvent.VK_DOWN, 0));
    keyboardActions.put(e -> {
      log.atFine().log("Left arrow pressed");
      Communication.getViewControllerBus().post(new PlayerInteraction(Verbs.MOVE, Nouns.WEST));
    }, KeyStroke.getKeyStroke(KeyEvent.VK_LEFT, 0));
    keyboardActions.put(e -> {
      log.atFine().log("Right arrow pressed");
      Communication.getViewControllerBus().post(new PlayerInteraction(Verbs.MOVE, Nouns.EAST));
    }, KeyStroke.getKeyStroke(KeyEvent.VK_RIGHT, 0));

    return keyboardActions;
  }

  void movePlayerOnClick(ImmutablePair<Integer, Integer> clickLocation) {
    Optional<Nouns> direction = getDirection(clickLocation);
    direction.ifPresent(nouns -> Communication.getViewControllerBus()
            .post(new PlayerInteraction(Verbs.MOVE, nouns)));
  }

  private Optional<Nouns> getDirection(ImmutablePair<Integer, Integer> newLocation) {
    int rowHeading = getHeading(newLocation.getRight() - currentLocation.getRight(), maxRows);
    int columnHeading = getHeading(newLocation.getLeft() - currentLocation.getLeft(), maxCols);

    if (rowHeading == 0 && columnHeading == 0) {
      log.atFine().log("Not moving. Same location");
      return Optional.empty();
    }
    if (rowHeading != 0 && columnHeading != 0) {
      log.atFine().log("Not moving. Non orthogonal location.");
      return Optional.empty();
    }

    if (rowHeading < 0) {
      log.atFine().log("Moving WEST");
      return Optional.of(Nouns.WEST);
    }

    if (rowHeading > 0) {
      log.atFine().log("Moving EAST");
      return Optional.of(Nouns.EAST);
    }
    if (columnHeading < 0) {
      log.atFine().log("Moving NORTH");
      return Optional.of(Nouns.NORTH);
    }
    log.atFine().log("Moving SOUTH");
    return Optional.of(Nouns.SOUTH);
  }

  int getHeading(int delta, int maxValue) {
    int sign = (int) Math.signum(delta);
    if (maxValue - Math.abs(delta) == 1) {
      return sign * -1;
    }
    return sign;
  }

}
