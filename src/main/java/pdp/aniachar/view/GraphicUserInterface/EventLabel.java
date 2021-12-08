package pdp.aniachar.view.GraphicUserInterface;

import org.jetbrains.annotations.NotNull;

import java.awt.Color;
import java.awt.Font;

import javax.swing.JLabel;

import pdp.aniachar.view.ViewModel.EventDescription;
import pdp.aniachar.view.ViewModel.IEvent;
import pdp.aniachar.view.ViewModel.Severity;

/**
 * A label that reacts to events and the severity of the event.
 */
public class EventLabel extends JLabel {

  /**
   * Builds the event label with the event that it got.
   *
   * @param eventDescription The event that needs to be rendered.
   */

  public EventLabel(@NotNull IEvent eventDescription) {
    setFont(new Font("Consolas", Font.PLAIN, 20));
    displayEvent(eventDescription.getDescription(), eventDescription.getSeverity());
  }

  private void displayEvent(EventDescription description, @NotNull Severity severity) {
    switch (severity) {
      case LOW:
        break;
      case DEBUG:
        setBackground(Color.GREEN);
        break;
      case MEDIUM:
        setForeground(Color.WHITE);
        break;
      case HIGH:
        setForeground(Color.GREEN);
        break;
      case CRITICAL:
        setForeground(Color.RED);
        break;
    }
    setText(description.toString());
  }

}
