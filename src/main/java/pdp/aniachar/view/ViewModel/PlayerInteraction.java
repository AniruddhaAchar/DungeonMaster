package pdp.aniachar.view.ViewModel;

/**
 * Defines and implements how a user would interact with the model from the view through the
 * controller.
 */

public class PlayerInteraction implements IPlayerInteraction {

  private final Verbs verb;
  private final Nouns noun;
  private final int qualifier;

  public PlayerInteraction(Verbs verb) {
    this.verb = verb;
    this.noun = Nouns.NONE;
    this.qualifier = 0;
  }

  public PlayerInteraction(Verbs verb, Nouns noun) {
    this.verb = verb;
    this.noun = noun;
    this.qualifier = 0;
  }

  public PlayerInteraction(Verbs verb, Nouns noun, int qualifier) {
    this.verb = verb;
    this.noun = noun;
    this.qualifier = qualifier;
  }

  @Override
  public Verbs getVerb() {
    return verb;
  }

  @Override
  public Nouns getNouns() {
    return noun;
  }

  @Override
  public int getQualifier() {
    return qualifier;
  }
}
