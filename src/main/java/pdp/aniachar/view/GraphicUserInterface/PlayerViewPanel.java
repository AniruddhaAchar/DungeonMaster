package pdp.aniachar.view.GraphicUserInterface;

import org.jetbrains.annotations.Contract;
import org.jetbrains.annotations.NotNull;

import java.awt.BorderLayout;
import java.awt.GridLayout;
import java.awt.Image;
import java.io.IOException;
import java.util.Locale;
import java.util.Objects;

import javax.imageio.ImageIO;
import javax.swing.ImageIcon;
import javax.swing.JLabel;
import javax.swing.JPanel;

import lombok.extern.flogger.Flogger;
import pdp.aniachar.view.ViewModel.IPlayerViewModel;

/**
 * Describes the player details and the player's inventory.
 */

@Flogger
public class PlayerViewPanel extends JPanel {

  DisplayLabel playerHealthValueLabel;
  DisplayLabel playerHealthLabel;
  DisplayLabel playerNameLabel;
  DisplayLabel playerNameValueLabel;

  JPanel inventoryPanel;

  /**
   * Builds the player's view.
   */
  public PlayerViewPanel() {
    setLayout(new BorderLayout());
    playerHealthLabel = new DisplayLabel("Health: ");
    playerHealthValueLabel = new DisplayLabel();
    playerNameLabel = new DisplayLabel("Name: ");
    playerNameValueLabel = new DisplayLabel("Foo");
    setLayout(new GridLayout(0, 4));
    inventoryPanel = new JPanel(new GridLayout(0, 2));
    add(playerNameLabel);
    add(playerNameValueLabel);
    add(playerHealthLabel);
    add(playerHealthValueLabel);
    add(inventoryPanel);
  }

  void updateView(@NotNull IPlayerViewModel playerViewModel) {
    playerNameValueLabel.setText(playerViewModel.getName());
    playerHealthValueLabel.setText(String.valueOf(playerViewModel.getHealth()));
    remove(inventoryPanel);
    inventoryPanel = new JPanel(new GridLayout(0, 2));
    playerViewModel.getInventory().forEach(this::buildAndAddInventory);
    add(inventoryPanel);
    revalidate();
  }

  private void buildAndAddInventory(@NotNull String inventoryItemName, long count) {
    JLabel labelName = new JLabel();
    try {
      labelName.setIcon(getInventoryImage(inventoryItemName));
    } catch (IOException e) {
      log.atSevere().log("Failed to display image.");
    }
    DisplayLabel labelValue = new DisplayLabel(String.valueOf(count));
    inventoryPanel.add(labelName);
    inventoryPanel.add(labelValue);
  }

  @Contract("_ -> new")
  private @NotNull ImageIcon getInventoryImage(@NotNull String inventoryName) throws IOException {
    String fileName = inventoryName.toLowerCase(Locale.ROOT);
    Image bgImage;
    try {
      bgImage = ImageIO.read(Objects
              .requireNonNull(getClass()
                      .getResource(String.format("/%s.png", fileName))));
      return new ImageIcon(bgImage);
    } catch (IOException e) {
      log.atSevere().log("Couldn't find the resource %s.png", fileName);
      throw e;
    }
  }

}
