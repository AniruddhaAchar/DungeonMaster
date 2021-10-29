package pdp.aniachar.dungeonmaster.action.location;

import java.util.Optional;

import pdp.aniachar.gamekit.Action;
import pdp.aniachar.gamekit.Character;

public interface LocationAction<T> extends Action<T> {
  Optional<Boolean> actOn(Character character);
}
