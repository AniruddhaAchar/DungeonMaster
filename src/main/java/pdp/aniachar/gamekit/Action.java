package pdp.aniachar.gamekit;

import java.util.Optional;

/**
 * Performs an action on the game model.
 * The implementer is responsible for taking the necessary data about the game model
 * to interact with the model.
 * An example of an action would be moving a player. The class should have a constructor
 * that takes the player and moves them in the direction specified.
 * The model will call {@link #act()} to "execute" the action.
 * If the action has a return, then it can be returned to the mode. If no return is expected,
 * return a {@link Optional#empty()}
 *
 * @param <T> The type that this action expects to return.
 */

public interface Action<T> {
  /**
   * This is how an action is executed.
   * The model or the controller of the model will call act on an action to execute the action.
   *
   * @return {@link Optional} if there is a state change that needs to be communicated.
   */
  Optional<T> act() throws NoSuchMethodException;

  /**
   * Describes what the action does.
   *
   * @return The description of the action.
   */
  String describe();

  /**
   * Links the way in which the location action will interact with the character.
   *
   * @param character The character who the location should interact with.
   * @return If the action was successful or not.
   */

  Optional<Boolean> actOn(Character character) throws NoSuchMethodException;
}
