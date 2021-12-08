package pdp.aniachar.view.ViewModel;

import org.jetbrains.annotations.NotNull;

import lombok.AllArgsConstructor;
import pdp.aniachar.gamekit.ImmutableLocation;

/**
 * Describes how view will consume a async event passed by the model.
 */
@AllArgsConstructor
public class Event implements IEvent {

  @NotNull
  private final EventDescription description;
  @NotNull
  private final ImmutableLocation<?> location;
  @NotNull
  private final Severity severity;


  @Override
  public @NotNull EventDescription getDescription() {
    return description;
  }

  @Override
  public @NotNull ImmutableLocation<?> getLocation() {
    return location;
  }

  @Override
  public @NotNull Severity getSeverity() {
    return severity;
  }
}
