package pdp.aniachar.view.ViewModel;

public interface IPlayerInteraction {

  /**
   * Gets the action verb.
   *
   * @return The verb.
   */

  Verbs getVerb();

  /**
   * Gets the action noun.
   *
   * @return The Noun.
   */
  Nouns getNouns();

  /**
   * Gets a qualifier.
   *
   * @return A qualifier.
   */
  int getQualifier();
}
