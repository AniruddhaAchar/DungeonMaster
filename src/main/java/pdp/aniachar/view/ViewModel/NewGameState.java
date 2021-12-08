package pdp.aniachar.view.ViewModel;

import lombok.Builder;
import pdp.aniachar.view.IView;

/**
 * Describes how the {@link IView}  is sending the new game setting to the game view.
 */

@Builder
public class NewGameState implements INewGameState {

  private final int maxRows;
  private final int maxCols;
  private final boolean isWrapped;
  private final int percentTreasure;
  private final long numberMonsters;
  private final int degreeOfInterConnection;


  @Override
  public int getMaxRows() {
    return maxRows;
  }

  @Override
  public int getMaxCols() {
    return maxCols;
  }

  @Override
  public boolean getIsWrapped() {
    return isWrapped;
  }

  @Override
  public int getPercentTreasure() {
    return percentTreasure;
  }

  @Override
  public long getNumberMonsters() {
    return numberMonsters;
  }

  @Override
  public int getDegreeOfInterConnection() {
    return degreeOfInterConnection;
  }
}
