import org.apache.commons.lang3.tuple.ImmutablePair;

import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.util.Map;

import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.WindowConstants;

import pdp.aniachar.Communication;
import pdp.aniachar.dungeonmaster.action.location.MoveAction;
import pdp.aniachar.dungeonmaster.action.location.MoveActionBuilder;
import pdp.aniachar.dungeonmaster.action.location.PickItemAction;
import pdp.aniachar.dungeonmaster.action.location.PickItemActionBuilder;
import pdp.aniachar.dungeonmaster.gameworld.IMazeLocationBuilder;
import pdp.aniachar.dungeonmaster.gameworld.MazeLocation;
import pdp.aniachar.dungeonmaster.item.Arrow;
import pdp.aniachar.gamekit.Direction;
import pdp.aniachar.gamekit.ImmutableLocation;
import pdp.aniachar.gamekit.ItemType;
import pdp.aniachar.view.GraphicUserInterface.NewGameDialog;
import pdp.aniachar.view.ViewModel.ILocationViewModel;
import pdp.aniachar.view.ViewModel.IPlayerViewModel;

/**
 * Some functionality testing of the GUI.
 */

public class TestGui {

  /**
   * A testing main method.
   *
   * @param args ignored.
   * @throws InterruptedException ignored.
   */

  public static void main(String[] args) throws InterruptedException {
    //SwingView swingView = new SwingView(10, 10);
    //swingView.start();
    JFrame jFrame = new JFrame();
    NewGameDialog dialog = new NewGameDialog();
    var btn = new JButton("Click to show dialog");
    btn.addMouseListener(new MouseAdapter() {
      @Override
      public void mouseClicked(MouseEvent e) {
        dialog.setVisible(true);
      }
    });
    jFrame.add(btn);
    jFrame.pack();
    jFrame.setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);
    jFrame.setVisible(true);
    Communication.getViewControllerBus().post(new IPlayerViewModel() {
      @Override
      public String getName() {
        return "Foo";
      }

      @Override
      public double getHealth() {
        return 100;
      }

      @Override
      public Map<String, Long> getInventory() {
        return Map.of("Diamond", 2L);
      }
    });
    Communication.getViewControllerBus().post(new ILocationViewModel() {
      @Override
      public Map<Direction, MoveAction> getPossibleMoves() {
        return Map.of(
                Direction.EAST, new MoveActionBuilder().setDirection(Direction.EAST)
                        .setWhereTo(
                                new IMazeLocationBuilder(new ImmutablePair<>(0, 1))
                                        .build()
                        )
                        .build()
        );
      }

      @Override
      public ImmutableLocation<?> getCurrentLocation() {
        return new IMazeLocationBuilder(new ImmutablePair<>(0, 0)).build();
      }

      @Override
      public Map<ItemType, PickItemAction> getItems() {
        return Map.of(
                ItemType.ARROW,
                new PickItemActionBuilder()
                        .setItemToPick(new Arrow())
                        .setActionLocation(
                                (MazeLocation) new IMazeLocationBuilder(
                                        new ImmutablePair<>(0, 0))
                                        .build()
                        )
                        .createPickItemAction()
        );
      }
    });
    Thread.sleep(4000);
    Communication.getViewControllerBus().post(new ILocationViewModel() {
      @Override
      public Map<Direction, MoveAction> getPossibleMoves() {
        return Map.of(
                Direction.EAST, new MoveActionBuilder().setDirection(Direction.EAST)
                        .setWhereTo(
                                new IMazeLocationBuilder(new ImmutablePair<>(0, 2))
                                        .build()
                        )
                        .build(),
                Direction.WEST, new MoveActionBuilder().setDirection(Direction.WEST)
                        .setWhereTo(
                                new IMazeLocationBuilder(new ImmutablePair<>(0, 0))
                                        .build()
                        )
                        .build()
        );
      }

      @Override
      public ImmutableLocation<?> getCurrentLocation() {
        return new IMazeLocationBuilder(new ImmutablePair<>(0, 1)).build();
      }

      @Override
      public Map<ItemType, PickItemAction> getItems() {
        return Map.of(
                ItemType.ARROW,
                new PickItemActionBuilder()
                        .setItemToPick(new Arrow())
                        .setActionLocation(
                                (MazeLocation) new IMazeLocationBuilder(
                                        new ImmutablePair<>(0, 0))
                                        .build()
                        )
                        .createPickItemAction()
        );
      }
    });
  }
}
