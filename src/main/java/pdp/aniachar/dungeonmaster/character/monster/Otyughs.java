package pdp.aniachar.dungeonmaster.character.monster;

import com.google.common.eventbus.Subscribe;

import org.jetbrains.annotations.NotNull;

import java.util.List;
import java.util.Map;
import java.util.Random;
import java.util.stream.Collectors;

import pdp.aniachar.dungeonmaster.character.AbstractCharacter;
import pdp.aniachar.dungeonmaster.character.player.IPlayer;
import pdp.aniachar.dungeonmaster.comm.ArrowHitEvent;
import pdp.aniachar.dungeonmaster.comm.CallEvent;
import pdp.aniachar.dungeonmaster.comm.DeathEvent;
import pdp.aniachar.dungeonmaster.comm.EventContainer;
import pdp.aniachar.dungeonmaster.comm.HitTakenEvent;
import pdp.aniachar.dungeonmaster.comm.PlayerDeathEvent;
import pdp.aniachar.dungeonmaster.comm.PlayerEnterEvent;
import pdp.aniachar.dungeonmaster.comm.RoarEvent;
import pdp.aniachar.dungeonmaster.gameworld.IMazeLocation;
import pdp.aniachar.dungeonmaster.gameworld.IMazeLocationBuilder;
import pdp.aniachar.dungeonmaster.gameworld.SmellStrength;
import pdp.aniachar.gamekit.ImmutableLocation;
import pdp.aniachar.gamekit.Item;
import pdp.aniachar.gamekit.Location;

/**
 * Otyughs are smelly monsters that eat flesh.
 * If a player battles with a healthy Otyughs, the player dies.
 * Because they smell, they have the smell that carry to adjacent locations.
 */

public class Otyughs extends AbstractCharacter {

  private final Location<?> currentLocation;
  private final String name;

  private Map<Location<?>, Integer> smellPermeatedLocations;

  /**
   * Creates an Otyughs at the specified location and with the given name.
   *
   * @param currentLocation The location where the Otyughs will live.
   * @param name            The name of the Otyughs.
   */

  public Otyughs(@NotNull Location<?> currentLocation, @NotNull String name) {
    super(100d);
    if (name.isBlank()) {
      throw new IllegalArgumentException("Name cannot be blank");
    }
    this.currentLocation = currentLocation;
    this.name = name;
    EventContainer.getModelEventBus().register(this);
    setSmellPermeatedLocations();
    publishSmellChanges(true);
  }

  /**
   * Creates a copy Otyughs at the specified location and with the given name.
   *
   * @param currentLocation The location where the Otyughs will live.
   * @param name            The name of the Otyughs.
   * @param subEvents       Because this is a copy, it should not subscribe to the event bus.
   */

  public Otyughs(@NotNull Location<?> currentLocation, @NotNull String name, boolean subEvents) {
    super(100d);
    if (name.isBlank()) {
      throw new IllegalArgumentException("Name cannot be blank");
    }
    this.currentLocation = currentLocation;
    this.name = name;
    setSmellPermeatedLocations();
    if (subEvents) {
      EventContainer.getModelEventBus().register(this);
    }
  }

  private void setSmellPermeatedLocations() {

    smellPermeatedLocations = IMazeLocationBuilder
            .distanceToAllNodes((IMazeLocation) currentLocation).entrySet().stream()
            .filter(e -> e.getValue() <= 2 && e.getValue() > 0)
            .collect(Collectors.toMap(Map.Entry::getKey, Map.Entry::getValue));
  }

  /**
   * Publish Smell Changes to all locations where smell can permeate.
   *
   * @param shouldAdd Flag to add or remove smell from locations.
   */
  private void publishSmellChanges(boolean shouldAdd) {

    for (var kv : smellPermeatedLocations.entrySet()) {
      SmellStrength smellStrength;

      if (kv.getValue() < 2) {
        smellStrength = SmellStrength.STRONG;
      } else {
        smellStrength = SmellStrength.WEAK;
      }
      if (shouldAdd) {
        ((IMazeLocation) kv.getKey()).addSmell(smellStrength);
      } else {
        ((IMazeLocation) kv.getKey()).removeSmell(smellStrength);
      }
    }
  }

  @Override
  public boolean makeMove(Location<?> whereTo) {
    return false;
  }

  @Override
  public Location<?> getCurrentLocation() {
    return currentLocation;
  }

  @Override
  public boolean takeItem(@NotNull Item item) throws NoSuchMethodException {
    throw new NoSuchMethodException();
  }

  @Override
  public List<Item> getAllItems() throws NoSuchMethodException {
    throw new NoSuchMethodException();
  }

  @Override
  public String getName() {
    return name;
  }


  /**
   * Monster listens to the hit event.
   * If hit event is for the monster's location, it will take a hit, else does nothing.
   * If the health of the less than zero then kill the monster by unsubscribing to the eventbus.
   *
   * @param arrowHitEvent A arrow hit event.
   */

  @Subscribe
  public void takeHit(@NotNull ArrowHitEvent arrowHitEvent) {
    if (currentLocation.equals(arrowHitEvent.getLocationToAffect())) {
      changeHealth(-1 * arrowHitEvent.getDamageDealt());
      EventContainer.getModelEventBus()
              .post(new HitTakenEvent<ImmutableLocation<?>>((currentLocation)));
      if (health <= 0) {
        EventContainer.getModelEventBus().post(new DeathEvent<>(this));
        EventContainer.getModelEventBus().unregister(this);
        publishSmellChanges(false);
      }
    }
  }

  /**
   * Listens to players entering the new location.
   * If player enters this monster's location, then kill the player if monster's health is >50.
   * Else player has a 50% chance of survival. If the player survives, the monster dies.
   * Monster death is done by just unsubscribing to the eventbus.
   *
   * @param enterEvent A player enter location event.
   */
  @Subscribe
  public void playerEntered(@NotNull PlayerEnterEvent enterEvent) {
    ImmutableLocation<?> eventLocation = enterEvent.getPlayer().getCurrentLocation();
    if (eventLocation.equals(currentLocation)) {
      battlePlayer(enterEvent.getPlayer());
    }
  }

  private void battlePlayer(IPlayer player) {
    if (health > 50) {
      EventContainer.getModelEventBus().post(new PlayerDeathEvent(player));
    } else {
      int rand = new Random().nextInt(2);
      if (rand == 0) {
        EventContainer.getModelEventBus().post(new PlayerDeathEvent(player));
      } else {
        EventContainer.getModelEventBus().post(new DeathEvent<>(this));
        EventContainer.getModelEventBus().unregister(this);
        publishSmellChanges(false);
      }
    }
  }


  /**
   * This is a cheat!
   * This is so that I can test killing monsters in a random maze.
   *
   * @param event A call event to get the location of a {@link Otyughs}.
   */
  @Subscribe
  public void roar(CallEvent<Otyughs> event) {
    EventContainer.getModelEventBus().post(new RoarEvent<>(this));
  }

  @Override
  protected void bringBackToLifeHelper() {
    EventContainer.getModelEventBus().register(this);
    publishSmellChanges(false);
    publishSmellChanges(true);
  }
}
