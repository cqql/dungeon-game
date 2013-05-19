package dungeon.load;

import dungeon.load.messages.LevelLoadedEvent;
import dungeon.messages.LifecycleEvent;
import dungeon.messages.Mailman;
import dungeon.messages.Message;
import dungeon.messages.MessageHandler;
import dungeon.models.*;

import java.util.Arrays;

public class LevelLoadHandler implements MessageHandler {
  private final Mailman mailman;

  public LevelLoadHandler (Mailman mailman) {
    this.mailman = mailman;
  }

  @Override
  public void handleMessage (Message message) {
    if (message == LifecycleEvent.INITIALIZE) {
      World world = new World(
        Arrays.asList(
          new Room(
            "warm-up",
            Arrays.asList(
              new Enemy(new Position(2, 1))
            ),
            Arrays.asList(
              new Tile(false, new Position(0, 0)), new Tile(false, new Position(1, 0)), new Tile(false, new Position(2, 0)),
              new Tile(false, new Position(0, 1)), new Tile(false, new Position(1, 1)), new Tile(false, new Position(2, 1)),
              new Tile(false, new Position(0, 2)), new Tile(true, new Position(1, 2)), new TeleporterTile(new Position(2, 2), new TeleporterTile.Target("warm-up", 0, 0))
            )
          )
        ),
        new Player("Link", 1, "warm-up", new Position(0, 0))
      );

      this.mailman.send(new LevelLoadedEvent(world));
    }
  }
}
