package dungeon.load.messages;

import dungeon.messages.Message;
import dungeon.models.World;

/**
 * This is sent by the LevelLoadHandler to publish the loaded World.
 */
public class LevelLoadedEvent implements Message {
  private final World world;

  public LevelLoadedEvent (World world) {
    this.world = world;
  }

  public World getWorld () {
    return this.world;
  }
}
