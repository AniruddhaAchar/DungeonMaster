package pdp.aniachar.view.GraphicUserInterface;

import org.apache.commons.lang3.text.WordUtils;
import org.jetbrains.annotations.NotNull;

import java.awt.event.KeyEvent;
import java.util.Locale;
import java.util.Map;

import javax.swing.JMenu;
import javax.swing.JMenuItem;
import javax.swing.JPopupMenu;

import lombok.extern.flogger.Flogger;
import pdp.aniachar.Communication;
import pdp.aniachar.dungeonmaster.action.location.PickItemAction;
import pdp.aniachar.gamekit.Direction;
import pdp.aniachar.gamekit.ItemType;
import pdp.aniachar.view.ViewModel.Nouns;
import pdp.aniachar.view.ViewModel.PlayerInteraction;
import pdp.aniachar.view.ViewModel.Verbs;

/**
 * Describes how the popup menu should look like.
 * The popup menu by default has a shoot option for the user to shoot arrows.
 */

@Flogger
public class MazePopupMenu extends JPopupMenu {
  private Map<ItemType, PickItemAction> possiblePickActions;


  /**
   * Builds the popup menu with the actions specified.
   *
   * @param possiblePickActions The actions that can be performed with the popup menu.
   */

  public MazePopupMenu(Map<ItemType, PickItemAction> possiblePickActions) {
    this.possiblePickActions = possiblePickActions;
    log.atFine().log("Building a new context menu.");
    populateShootMenu();
    populatePickMenu();
  }

  /**
   * Default constructor to build the popup menu.
   */

  public MazePopupMenu() {
    populateShootMenu();
  }

  private void populateShootMenu() {
    JMenu shootMenu = new JMenu("Shoot");
    shootMenu.setMnemonic('o');
    for (Direction direction :
            Direction.values()) {
      shootMenu.add(getDirectionDistanceMenu(direction));
    }
    add(shootMenu);
  }

  private void populatePickMenu() {
    log.atFine().log("Building the pick items.");
    JMenu pickMenu = new JMenu("Pick");
    pickMenu.setMnemonic('i');
    for (ItemType itemType :
            possiblePickActions.keySet()) {
      String itemName = WordUtils.capitalizeFully(itemType.toString());
      JMenuItem itemMenuItem = new JMenuItem(itemName);
      itemMenuItem.addActionListener(e -> {
        Nouns pickNoun = translatorViewModel(itemType);
        log.atFine().log("Pick clicked. Sending %s to IView model.", pickNoun);
        Communication.getViewControllerBus().post(new PlayerInteraction(Verbs.PICK, pickNoun));
      });
      pickMenu.add(itemMenuItem);
    }
    if (possiblePickActions.size() > 0) {
      add(pickMenu);
    }
  }

  private @NotNull JMenu getDirectionDistanceMenu(@NotNull Direction direction) {
    JMenu directionMenu = new JMenu(direction.toString().toLowerCase(Locale.ROOT));
    for (int i = 1; i <= 5; i++) {
      var distanceMenu = new JMenuItem(String.valueOf(i));
      int finalI = i;
      distanceMenu.addActionListener(e -> {
        Nouns directionNoun = translatorViewModel(direction);
        log.atFine().log("Shoot selected. Shooting %d caves %s", finalI, directionNoun);
        Communication.getViewControllerBus()
                .post(new PlayerInteraction(Verbs.SHOOT, directionNoun, finalI));
      });
      var x = direction.toString().toLowerCase(Locale.ROOT);
      directionMenu.setMnemonic(mapDirectionsToArrows(direction));
      directionMenu.add(distanceMenu);
    }
    return directionMenu;
  }

  private Nouns translatorViewModel(ItemType itemType) {
    log.atFine().log("Translating model's %s to noun", itemType);
    switch (itemType) {
      case ARROW:
        return Nouns.ARROW;
      case TREASURE:
        return Nouns.TREASURE;
      default:
        log.atWarning().log(String.format("Got an invalid item type %s", itemType));
        throw new IllegalArgumentException("Not a valid item type");

    }
  }

  private Nouns translatorViewModel(Direction direction) {
    log.atFinest().log("Something");
    log.atFine().log("Translating model's %s to noun", direction);
    switch (direction) {
      case NORTH:
        return Nouns.NORTH;
      case SOUTH:
        return Nouns.SOUTH;
      case WEST:
        return Nouns.WEST;
      case EAST:
        return Nouns.EAST;
      default:
        log.atWarning().log(String.format("Got an invalid direction %s", direction));
        throw new IllegalArgumentException("Not a valid direction");
    }
  }

  private int mapDirectionsToArrows(Direction direction) {
    switch (direction) {
      case NORTH:
        return KeyEvent.VK_W;
      case SOUTH:
        return KeyEvent.VK_S;
      case WEST:
        return KeyEvent.VK_A;
      case EAST:
        return KeyEvent.VK_D;
      default:
        log.atWarning().log(String.format("Got an invalid direction %s", direction));
        throw new IllegalArgumentException("Not a valid direction");
    }
  }

}
