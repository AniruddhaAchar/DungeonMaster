package pdp.aniachar.view.GraphicUserInterface;

import org.apache.commons.lang3.tuple.ImmutablePair;
import org.jetbrains.annotations.Contract;
import org.jetbrains.annotations.NotNull;

import java.awt.BorderLayout;
import java.awt.Dimension;
import java.awt.Image;
import java.awt.Rectangle;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.io.IOException;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.Objects;
import java.util.Set;
import java.util.stream.Collectors;

import javax.imageio.ImageIO;
import javax.swing.Icon;
import javax.swing.ImageIcon;
import javax.swing.JLabel;
import javax.swing.JLayeredPane;
import javax.swing.JPanel;

import lombok.AccessLevel;
import lombok.Getter;
import lombok.extern.flogger.Flogger;
import pdp.aniachar.dungeonmaster.action.location.MoveAction;
import pdp.aniachar.dungeonmaster.gameworld.SmellStrength;
import pdp.aniachar.gamekit.Direction;


/**
 * Describes how each of the individual cells in the game should look like.
 */

@Flogger
public class MazeLocationPanel extends JPanel {

  private final int gridRow;
  private final int gridColumn;
  @Getter(AccessLevel.PACKAGE)
  private JLabel playerGif;
  private JLabel smellImg;
  private JLabel background;
  private JLabel monsterImage;
  @Getter(AccessLevel.PACKAGE)
  private JLabel chestImage;


  /**
   * Builds an individual cell at the specified location.
   *
   * @param gridRow    The row this cell is located in.
   * @param gridColumn The column this cell is located in.
   */

  public MazeLocationPanel(int gridRow, int gridColumn) {
    super();
    setMinimumSize(new Dimension(64, 64));
    setMaximumSize(new Dimension(64, 64));
    setPreferredSize(new Dimension(64, 64));
    this.gridColumn = gridColumn;
    this.gridRow = gridRow;
    setLayout(new BorderLayout());

    Rectangle defaultBounds = new Rectangle(32, 32, 64, 64);
    setupLocation();
    addClickHandler();
  }

  private void setupLocation() {
    smellImg = new JLabel();
    JLayeredPane layeredPane = new JLayeredPane();
    layeredPane.setPreferredSize(new Dimension(64, 64));

    background = new JLabel();
    setImageToBlank();
    background.setPreferredSize(new Dimension(64, 64));
    background.setBounds(new Rectangle(0, 0, 64, 64));
    layeredPane.add(background, 0);

    playerGif = new JLabel();

    playerGif.setBounds(new Rectangle(-18, -10, 64, 64));
    layeredPane.add(playerGif, 10, 10);

    smellImg.setBounds(new Rectangle(0, 0, 64, 64));
    layeredPane.add(smellImg, 1, 1);

    monsterImage = new JLabel();
    monsterImage.setBounds(new Rectangle(0, 0, 64, 64));
    layeredPane.add(monsterImage, 2, 2);

    chestImage = new JLabel();
    chestImage.setBounds(new Rectangle(10, 10, 24, 24));
    layeredPane.add(chestImage, 14, 14);

    add(layeredPane);
  }

  void setChest() {
    log.atFine().log("Setting treasure.");
    chestImage.setIcon(new ImageIcon(Objects
            .requireNonNull(getClass().getResource("/chest.gif"))));
  }

  void setPlayerGif(Icon icon) {
    log.atFine().log("Setting player at %d, %d to %s", gridRow, gridColumn, icon);
    playerGif.setIcon(icon);
  }

  void setMonster() {
    monsterImage.setIcon(new ImageIcon(Objects
            .requireNonNull(getClass().getResource("/monster.png"))));
  }

  void unsetMonster() {
    monsterImage.setIcon(null);
  }

  void updateView(@NotNull Map<Direction, MoveAction> possibleMoves, SmellStrength smellStrength) {
    try {
      background.setIcon(getImageIconFromDirections(possibleMoves.keySet()));
      setSmellInLocation(smellStrength);
    } catch (IOException e) {
      log.atSevere().log(e.getMessage());
    }
  }

  private void setSmellInLocation(@NotNull SmellStrength smellStrength) {

    switch (smellStrength) {
      case NONE:
        log.atFine().log("Setting smell to %s", smellStrength);
        smellImg.setIcon(null);
        break;
      case STRONG:
        log.atFine().log("Setting smell to %s", smellStrength);
        URL img = getClass().getResource("/stench02.png");
        assert img != null;
        smellImg.setIcon(new ImageIcon(img));
        break;
      case WEAK:
        log.atFine().log("Setting smell to %s", smellStrength);
        img = getClass().getResource("/stench01.png");
        assert img != null;
        smellImg.setIcon(new ImageIcon(img));
        break;
    }
  }

  private void setImageToBlank() {
    try {
      Class<? extends MazeLocationPanel> thisClass = getClass();
      URL resource = thisClass.getResource("/blank.png");
      Image bgImage = ImageIO.read(Objects.requireNonNull(resource));
      background.setIcon(new ImageIcon(bgImage));
    } catch (IOException e) {
      log.atSevere().log(e.getMessage());
    }
  }

  @Contract("_ -> new")
  private @NotNull ImageIcon getImageIconFromDirections(Set<Direction> directionSet)
          throws IOException {
    List<Direction> directions = new ArrayList<>(directionSet);
    String sortedDirections = directions.stream()
            .map(direction -> direction.toString().toLowerCase(Locale.ROOT))
            .sorted().collect(Collectors.joining());
    try {
      log.atFine().log("Rendering %s image", sortedDirections);
      Image bgImage = ImageIO.read(Objects
              .requireNonNull(getClass()
                      .getResource(String.format("/%s.png", sortedDirections))));
      return new ImageIcon(bgImage);
    } catch (IOException e) {
      log.atSevere().log(e.getMessage());
      throw e;
    }
  }


  private void addClickHandler() {
    addMouseListener(new MouseAdapter() {
      @Override
      public void mouseClicked(MouseEvent e) {
        super.mouseClicked(e);
        if (e.getButton() == MouseEvent.BUTTON1) {
          log.atFine().log("Maze grid location %d, %d clicked", gridRow, gridColumn);
          var parent = (MazeGridPanel) getParent();
          parent.movePlayerOnClick(new ImmutablePair<>(gridRow, gridColumn));
        } else {
          log.atFine().log("Sending event to parent");
          getParent().dispatchEvent(e);
        }
      }
    });
  }


}
