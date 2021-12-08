package pdp.aniachar.view.GraphicUserInterface;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.ScrollPane;

import javax.swing.BoxLayout;
import javax.swing.JPanel;

import pdp.aniachar.view.ViewModel.IEvent;

/**
 * A scrollable panel to display all the events that occurred.
 */

public class EventViewPanel extends ScrollPane {
  JPanel scrollPanel;

  /**
   * Builds the event view panel.
   */
  public EventViewPanel() {
    scrollPanel = new JPanel();
    setPreferredSize(new Dimension(250, 500));
    scrollPanel.setLayout(new BoxLayout(scrollPanel, BoxLayout.Y_AXIS));
    scrollPanel.setBackground(Color.BLACK);
    add(scrollPanel);
  }

  /**
   * Updates the view current view with the event.
   *
   * @param eventDescription An event that needs to be rendered.
   */
  public void updateView(IEvent eventDescription) {
    scrollPanel.add(new EventLabel(eventDescription), 0);
  }

}
