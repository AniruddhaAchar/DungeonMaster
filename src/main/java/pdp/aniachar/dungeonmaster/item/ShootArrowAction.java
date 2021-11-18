package pdp.aniachar.dungeonmaster.item;

import java.util.HashSet;
import java.util.List;
import java.util.Optional;
import java.util.Set;
import java.util.stream.Collectors;

import pdp.aniachar.dungeonmaster.action.character.PlayerAction;
import pdp.aniachar.dungeonmaster.action.location.MoveAction;
import pdp.aniachar.dungeonmaster.character.player.IPlayer;
import pdp.aniachar.dungeonmaster.comm.ArrowHitEvent;
import pdp.aniachar.dungeonmaster.comm.EventContainer;
import pdp.aniachar.dungeonmaster.gameworld.IMazeLocation;
import pdp.aniachar.gamekit.Character;
import pdp.aniachar.gamekit.Direction;

/**
 * A strategy to move the an {@link Arrow} in the maze.
 * An arrow will move only in the direction specified if it is in a cave, if it is in a tunnel,
 * then the arrow will follow the tunnel.
 * The distance the arrow can traverse is the number of caves the arrow can visit, if there is a
 * path.
 */

public class ShootArrowAction implements PlayerAction {

  private final int distance;
  private final Set<IMazeLocation> visited;
  private Arrow arrow;
  private Direction direction;
  private int distanceTravelled;

  /**
   * Creates a new move action for an  arrow.
   *
   * @param arrow     The arrow that needs to move.
   * @param direction The direction in which the arrow should move.
   * @param distance  The distance i.e. the number caves the arrow will try to traverse.
   */

  ShootArrowAction(Arrow arrow, Direction direction, int distance) {

    this.arrow = arrow;
    this.direction = direction;
    this.distance = distance;
    distanceTravelled = 0;
    visited = new HashSet<>();
  }

  ShootArrowAction(Direction direction, int distance) {
    this.direction = direction;
    this.distance = distance;
    distanceTravelled = 0;
    visited = new HashSet<>();
  }

  @Override
  public Optional<Boolean> act() {
    visited.add(arrow.getCurrentLocation());
    boolean hasMoved = nextPossibleMove(true);
    while (hasMoved) {
      hasMoved = nextPossibleMove(false);
    }
    EventContainer.getModelEventBus().post(new ArrowHitEvent(arrow.getCurrentLocation(), 50d));
    return Optional.of(distanceTravelled == distance);
  }


  private boolean setNextLocationIfPossible(MoveAction potentialMove) {
    IMazeLocation nextPotentialLocation = (IMazeLocation) potentialMove.act().orElseThrow();
    if (!visited.contains(nextPotentialLocation)) {
      visited.add(nextPotentialLocation);
      arrow.setCurrentLocation(nextPotentialLocation);
      return true;
    }
    return false;
  }

  private boolean nextPossibleMove(boolean firstMove) {

    List<MoveAction> possibleMoves = arrow.getCurrentLocation().getMoveAction();

    boolean isCave = possibleMoves.size() != 2;
    if (isCave) {
      if (!firstMove) {
        distanceTravelled++;
      }
      if (distanceTravelled == distance) {
        return false;
      }
    }

    // Always move straight if possible
    MoveAction moveInStraightPath = possibleMoves.stream()
            .filter(possibleMove -> possibleMove.getDirection().equals(direction))
            .findFirst().orElse(null);

    if (moveInStraightPath != null) {
      if (setNextLocationIfPossible(moveInStraightPath)) {
        return true;
      }
    }

    //In a cave, no straight path = cannot move any further.
    if (isCave) {
      return false;
    }

    List<MoveAction> otherPotentialMoveActions = possibleMoves.stream()
            .filter(moveAction -> !moveAction.equals(moveInStraightPath))
            .collect(Collectors.toList());

    for (MoveAction otherPotentialMoveAction :
            otherPotentialMoveActions) {
      if (setNextLocationIfPossible(otherPotentialMoveAction)) {
        this.direction = otherPotentialMoveAction.getDirection();
        return true;
      }
    }
    return false;
  }


  @Override
  public String describe() {
    return String.format("Shooting an arrow %d caves in the %s direction", distance, direction);
  }

  @Override
  public Optional<Boolean> actOn(Character character) {
    if (!(character instanceof IPlayer)) {
      throw new IllegalArgumentException("Arrow can only be used by a IPlayer");
    }
    IPlayer player = (IPlayer) character;
    if (player.getQuiver().size() == 0) {
      throw new IndexOutOfBoundsException("Player has no more arrows.");
    }
    this.arrow = player.removeArrow();
    act();
    return Optional.empty();
  }
}
