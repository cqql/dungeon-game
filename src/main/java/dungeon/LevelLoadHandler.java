package dungeon;

import dungeon.events.Event;
import dungeon.events.EventHandler;
import dungeon.events.EventHost;
import dungeon.events.LifecycleEvent;
import dungeon.models.*;

import java.util.Arrays;

public class LevelLoadHandler implements EventHandler {
  private final EventHost eventHost;

  public LevelLoadHandler (EventHost eventHost) {
    this.eventHost = eventHost;
  }

  @Override
  public void handleEvent (Event event) {
    if (event == LifecycleEvent.INITIALIZE) {
      World world = new World(
        Arrays.asList(
          new Room(
            "warm-up",
            Arrays.asList(
              new Enemy(new Position(1, 1))
            ),
            Arrays.asList(
              Arrays.asList(new Tile(false), new Tile(false), new Tile(false)),
              Arrays.asList(new Tile(false), new Tile(false), new Tile(false)),
              Arrays.asList(new Tile(false), new Tile(false), new Tile(false))
            )
          )
        ),
        new Player("Link", 1, "warm-up", new Position(0, 0))
      );

      eventHost.publish(new LevelLoadedEvent(world));
    }
  }

  public static class LevelLoadedEvent implements Event {
    private final World world;

    public LevelLoadedEvent (World world) {
      this.world = world;
    }

    public World getWorld () {
      return world;
    }
  }
}
