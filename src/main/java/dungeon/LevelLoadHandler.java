package dungeon;

import dungeon.messages.Message;
import dungeon.messages.EventHandler;
import dungeon.messages.Mailman;
import dungeon.messages.LifecycleEvent;
import dungeon.models.*;

import java.util.Arrays;

public class LevelLoadHandler implements EventHandler {
  private final Mailman mailman;

  public LevelLoadHandler (Mailman mailman) {
    this.mailman = mailman;
  }

  @Override
  public void handleEvent (Message message) {
    if (message == LifecycleEvent.INITIALIZE) {
      World world = new World(
        Arrays.asList(
          new Room(
            "warm-up",
            Arrays.asList(
              new Enemy(new Position(2, 1))
            ),
            Arrays.asList(
              Arrays.asList(new Tile(false), new Tile(false), new Tile(false)),
              Arrays.asList(new Tile(false), new Tile(false), new Tile(false)),
              Arrays.asList(new Tile(false), new Tile(true), new Tile(false))
            )
          )
        ),
        new Player("Link", 1, "warm-up", new Position(0, 0))
      );

      this.mailman.publish(new LevelLoadedEvent(world));
    }
  }

  public static class LevelLoadedEvent implements Message {
    private final World world;

    public LevelLoadedEvent (World world) {
      this.world = world;
    }

    public World getWorld () {
      return this.world;
    }
  }
}
