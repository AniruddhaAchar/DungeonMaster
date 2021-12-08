package pdp.aniachar.view.GraphicUserInterface;

import org.jetbrains.annotations.NotNull;

import java.awt.Font;
import javax.swing.JLabel;

/**
 * Defines how each all labels in the game should be displayed.
 * The font, size is defined here.
 */

public class DisplayLabel extends JLabel {

  /**
   * A constructor that takes in the string that needs to be displayed.
   *
   * @param toDisplay The string to be displayed.
   */
  public DisplayLabel(@NotNull String toDisplay) {
    this();
    setText(toDisplay);
  }

  /**
   * A default constructor that sets the font, type style and size.
   */
  public DisplayLabel() {
    setFont(new Font("Consolas", Font.PLAIN, 20));
  }
}
