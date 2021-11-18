package pdp.aniachar.view;

import org.jetbrains.annotations.NotNull;

import java.io.IOException;
import java.util.Scanner;

/**
 * A text specific view that consumes a String and produces a string.
 */

public class TextView implements IView<String, String> {

  private final Appendable out;
  private final Scanner scanner;

  /**
   * Builds a Text view.
   *
   * @param out The consumer.
   * @param in  The producer.
   */

  public TextView(@NotNull Appendable out, @NotNull Readable in) {
    this.out = out;
    scanner = new Scanner(in);
  }

  @Override
  public String getUserInput() {
    return scanner.nextLine();
  }

  @Override
  public void renderView(@NotNull IViewModel<String> viewModel) {
    try {
      out.append(viewModel.getViewModelData());
    } catch (IOException e) {
      throw new IllegalStateException("Something bad happened here!");
    }
  }
}
