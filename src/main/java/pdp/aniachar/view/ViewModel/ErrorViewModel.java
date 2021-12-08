package pdp.aniachar.view.ViewModel;


/**
 * Defines a string error that occurred.
 */

public class ErrorViewModel implements IErrorViewModel {

  private final String error;


  /**
   * Builds an error.
   *
   * @param error The error description.
   */

  public ErrorViewModel(String error) {
    this.error = error;
  }

  @Override
  public String getErrorMessage() {
    return error;
  }
}
