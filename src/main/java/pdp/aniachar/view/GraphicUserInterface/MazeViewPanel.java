package pdp.aniachar.view.GraphicUserInterface;

import java.awt.Dimension;

import javax.swing.JScrollPane;

import lombok.AccessLevel;
import lombok.Getter;
import lombok.extern.flogger.Flogger;
import pdp.aniachar.view.ViewModel.ILocationViewModel;


/**
 * Describes the scrollable view that the game will be located in.
 */

@Flogger
public class MazeViewPanel extends JScrollPane {

  @Getter(AccessLevel.PACKAGE)
  private final MazeGridPanel mazeGridPanel;

  /**
   * Builds the scroll panel to hold the grid view.
   *
   * @param maxRows The number of rows this panel will house.
   * @param maxCol  The number of columns this panel will house.
   */

  public MazeViewPanel(int maxRows, int maxCol) {
    super();
    //Always show the scroll bars.
    setVerticalScrollBarPolicy(VERTICAL_SCROLLBAR_ALWAYS);
    setHorizontalScrollBarPolicy(HORIZONTAL_SCROLLBAR_ALWAYS);
    setMinimumSize(new Dimension(64 * maxRows, 64 * maxCol));
    setMaximumSize(new Dimension(64 * maxRows, 64 * maxCol));
    mazeGridPanel = new MazeGridPanel(maxRows, maxCol);
    setViewportView(mazeGridPanel);
  }


  void updateView(ILocationViewModel locationViewModel) {
    log.atInfo().log("Updating the view.");
    mazeGridPanel.updateView(locationViewModel);
  }

}
