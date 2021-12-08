package pdp.aniachar.view.GraphicUserInterface;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Component;
import java.awt.Dimension;
import java.awt.GridLayout;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;

import javax.swing.BorderFactory;
import javax.swing.InputVerifier;
import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JComponent;
import javax.swing.JDialog;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JSpinner;
import javax.swing.JTextField;
import javax.swing.SpinnerNumberModel;
import javax.swing.SwingUtilities;

import lombok.extern.flogger.Flogger;
import pdp.aniachar.Communication;
import pdp.aniachar.view.ViewModel.INewGameState;
import pdp.aniachar.view.ViewModel.NewGameState;

import static javax.swing.JOptionPane.showMessageDialog;


/**
 * Describes how the new game panel should look like.
 */

@Flogger

public class NewGameDialog extends JDialog {

  private final JSpinner maxRowsSpinner;
  private final JSpinner maxColumnSpinner;
  private final JSpinner treasurePercentageSpinner;
  private final JTextField degreeOfInterConnectivityText;
  private final JTextField numberOfMonsterText;
  private final JComboBox<Boolean> isWrappedComboBox;


  /**
   * Builds a new-game dialog.
   */

  public NewGameDialog() {
    super();
    setTitle("New game!");
    setLayout(new BorderLayout());

    JPanel mainPanel = new JPanel();
    mainPanel.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));
    mainPanel.setLayout(new GridLayout(0, 2, 10, 10));

    maxRowsSpinner = new JSpinner(new SpinnerNumberModel(10, 10, 200, 1));
    maxColumnSpinner = new JSpinner(new SpinnerNumberModel(10, 10, 200, 1));
    treasurePercentageSpinner = new JSpinner(new SpinnerNumberModel(1, 0, 100, 1));
    degreeOfInterConnectivityText = buildNumberValidationTextFiled();
    numberOfMonsterText = buildNumberValidationTextFiled();
    isWrappedComboBox = new JComboBox<>(new Boolean[]{true, false});


    JLabel maxRowLabel = new JLabel("Number of rows:");
    maxRowLabel.setLabelFor(maxRowsSpinner);
    JLabel maxColLabel = new JLabel("Number of columns:");
    maxColLabel.setLabelFor(maxColumnSpinner);
    JLabel treasurePercentLabel = new JLabel("Percent of treasures and arrows in the dungeon:");
    treasurePercentLabel.setLabelFor(treasurePercentageSpinner);
    JLabel dicLabel = new JLabel("Degree of interconnectivity:");
    dicLabel.setLabelFor(degreeOfInterConnectivityText);
    JLabel monsterNumberLabel = new JLabel("Number of monsters:");
    monsterNumberLabel.setLabelFor(numberOfMonsterText);
    JLabel wrappingLabel = new JLabel("Should the dungeon be wrapped:");
    wrappingLabel.setLabelFor(isWrappedComboBox);


    mainPanel.add(maxRowLabel);
    mainPanel.add(maxRowsSpinner);
    mainPanel.add(maxColLabel);
    mainPanel.add(maxColumnSpinner);
    mainPanel.add(treasurePercentLabel);
    mainPanel.add(treasurePercentageSpinner);
    mainPanel.add(dicLabel);
    mainPanel.add(degreeOfInterConnectivityText);
    mainPanel.add(monsterNumberLabel);
    mainPanel.add(numberOfMonsterText);
    mainPanel.add(wrappingLabel);
    mainPanel.add(isWrappedComboBox);
    mainPanel.add(buildSubmitButton());
    add(mainPanel);
    pack();
    setVisible(false);
  }


  private JTextField buildNumberValidationTextFiled() {
    JTextField textField = new JTextField();
    textField.setPreferredSize(new Dimension(20, 20));
    textField.setInputVerifier(new InputVerifier() {
      @Override
      public boolean verify(JComponent input) {
        String text = ((JTextField) input).getText();
        if (validateNumber(text)) {
          input.setBackground(Color.RED);
          input.setForeground(Color.WHITE);
          return false;
        }
        return true;
      }
    });
    return textField;
  }

  private JButton buildSubmitButton() {
    JButton button = new JButton("Submit");

    button.addMouseListener(new MouseAdapter() {
      @Override
      public void mouseClicked(MouseEvent e) {
        super.mouseClicked(e);
        String numberMonstersString = numberOfMonsterText.getText();
        if (validateNumber(numberMonstersString)) {
          showErrorMessage("Not a valid number of monsters. Should be a number.");
          return;
        }
        String degreeOfInterConnectivityString = degreeOfInterConnectivityText.getText();
        if (validateNumber(degreeOfInterConnectivityString)) {
          showErrorMessage("Not a valid number of interconnectivity. Should be numbers.");
          return;
        }
        sendNewGameModel(numberMonstersString, degreeOfInterConnectivityString);
        Component component = (Component) e.getSource();
        JDialog dialog = (JDialog) SwingUtilities.getRoot(component);
        dialog.dispose();
      }
    });

    return button;
  }

  private void sendNewGameModel(String numberMonstersString,
                                String degreeOfInterConnectivityString) {
    int numberOfMonsters = Integer.parseInt(numberMonstersString);
    int degreeOfInterConnectivity = Integer.parseInt(degreeOfInterConnectivityString);
    int maxRows = (int) maxRowsSpinner.getValue();
    int maxCols = (int) maxColumnSpinner.getValue();
    int percentTreasure = (int) treasurePercentageSpinner.getValue();
    assert isWrappedComboBox.getSelectedItem() != null;
    boolean isWrapped = (boolean) isWrappedComboBox.getSelectedItem();
    INewGameState newGameState = NewGameState.builder()
            .maxRows(maxRows)
            .maxCols(maxCols)
            .degreeOfInterConnection(degreeOfInterConnectivity)
            .isWrapped(isWrapped)
            .numberMonsters(numberOfMonsters)
            .percentTreasure(percentTreasure).build();
    log.atFine().log("Sending new restart event to controller");
    Communication.getViewControllerBus().post(newGameState);
  }


  private boolean validateNumber(String input) {
    try {
      Integer.parseInt(input);
      return false;
    } catch (NumberFormatException e) {
      return true;
    }
  }


  private void showErrorMessage(String errorMessage) {
    showMessageDialog(null, errorMessage, "",
            JOptionPane.ERROR_MESSAGE);
  }

}
