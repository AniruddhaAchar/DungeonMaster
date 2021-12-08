package pdp.aniachar.view.GraphicUserInterface;

import org.jetbrains.annotations.Contract;
import org.jetbrains.annotations.NotNull;

import java.awt.BorderLayout;
import java.awt.event.KeyEvent;

import javax.swing.JFrame;
import javax.swing.JMenu;
import javax.swing.JMenuBar;
import javax.swing.JMenuItem;

import lombok.AccessLevel;
import lombok.Getter;
import lombok.extern.flogger.Flogger;
import pdp.aniachar.Communication;
import pdp.aniachar.view.ViewModel.RestartGame;

/**
 * The main window for the game.
 * This holds all the views together.
 */

@Flogger
public class GameFrame extends JFrame {
  private final int maxRows;
  private final int maxCols;
  @Getter(AccessLevel.PACKAGE)
  private final MazeViewPanel mazeViewPanel;
  @Getter(AccessLevel.PACKAGE)
  private final PlayerViewPanel playerViewPanel;
  @Getter(AccessLevel.PACKAGE)
  private final EventViewPanel eventViewPanel;


  /**
   * Builds the view based on the number of rows and columns.
   *
   * @param maxRow The number of rows.
   * @param maxCol The number of columns.
   */

  public GameFrame(int maxRow, int maxCol) {
    super();
    setExtendedState(JFrame.MAXIMIZED_BOTH);
    this.maxRows = maxRow;
    this.maxCols = maxCol;
    setTitle("Dungeon Master");
    setDefaultCloseOperation(EXIT_ON_CLOSE);
    setLayout(new BorderLayout());
    eventViewPanel = buildEventView();
    mazeViewPanel = buildMazeView();
    playerViewPanel = buildPlayerView();
    this.setJMenuBar(buildMenu());
    add(mazeViewPanel, BorderLayout.CENTER);
    add(playerViewPanel, BorderLayout.PAGE_END);
    add(eventViewPanel, BorderLayout.LINE_END);

  }

  @Contract(" -> new")
  private @NotNull PlayerViewPanel buildPlayerView() {
    return new PlayerViewPanel();
  }

  private @NotNull JMenuBar buildMenu() {
    final JMenuBar menuBar = new JMenuBar();
    final JMenu menu = new JMenu("Options");
    menu.setMnemonic(KeyEvent.VK_O);
    JMenuItem restartGame = buildRestartMenuItem();
    JMenuItem newGame = buildNewGameMenuItem();
    menu.add(restartGame);
    menu.add(newGame);
    menu.add(buildQuitGameMenuItem());
    menuBar.add(menu);
    return menuBar;
  }


  private @NotNull JMenuItem buildRestartMenuItem() {
    JMenuItem restartGame = new JMenuItem("Restart");
    restartGame.addActionListener(e -> Communication.getViewControllerBus()
            .post(new RestartGame<>(true)));
    return restartGame;
  }

  private @NotNull JMenuItem buildNewGameMenuItem() {
    JMenuItem newGame = new JMenuItem("New game");
    NewGameDialog dialog = new NewGameDialog();
    newGame.addActionListener(e -> dialog.setVisible(true));
    return newGame;
  }

  private @NotNull JMenuItem buildQuitGameMenuItem() {
    JMenuItem quit = new JMenuItem("Quit");
    quit.addActionListener(e -> System.exit(0));
    return quit;
  }

  @Contract(" -> new")
  private @NotNull EventViewPanel buildEventView() {
    return new EventViewPanel();
  }

  @Contract(" -> new")
  private @NotNull MazeViewPanel buildMazeView() {
    return new MazeViewPanel(maxRows, maxCols);
  }

}
