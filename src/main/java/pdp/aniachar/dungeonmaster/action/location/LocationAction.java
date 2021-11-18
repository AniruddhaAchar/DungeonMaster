package pdp.aniachar.dungeonmaster.action.location;

import java.util.Optional;

import pdp.aniachar.gamekit.Action;
import pdp.aniachar.gamekit.Character;

/**
 * The union of all the actions that can be performed at a location and that can interact with
 * character.
 *
 * @param <T> The type that is expected to be returned by the action.
 */

public interface LocationAction<T> extends Action<T> {

  /**
   * Links the way in which the location action will interact with the character.
   *
   * @param character The character who the location should interact with.
   * @return If the action was successful or not.
   */

  Optional<Boolean> actOn(Character character) throws NoSuchMethodException;
}
