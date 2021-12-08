package pdp.aniachar.view.ViewModel;


import pdp.aniachar.view.IView;
/**
 * Describes how the {@link IView} would send the new game settings to the controller.
 */

public interface INewGameState {
  /**
   * Gets the number of rows in the game.
   *
   * @return The number of rows in the game.
   */
  int getMaxRows();

  /**
   * Gets the number of columns in the game.
   *
   * @return The number of columns in the game.
   */

  int getMaxCols();

  /**
   * Gets if the game world is wrapped or not.
   *
   * @return Is wrapped or not.
   */

  boolean getIsWrapped();

  /**
   * Gets the percentage of caves that will have treasure and the percentage of location that
   * will have arrows.
   *
   * @return The percentage of caves/location that can have treasure/arrows.
   */

  int getPercentTreasure();

  /**
   * Gets the number of monsters in the game.
   *
   * @return The number of monsters in the game.
   */

  long getNumberMonsters();

  /**
   * Gets the degree of interconnectivity in the game.
   *
   * @return The degree of interconnectivity in the game.
   */

  int getDegreeOfInterConnection();
}
