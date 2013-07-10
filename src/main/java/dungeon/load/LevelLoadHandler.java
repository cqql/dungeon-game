package dungeon.load;

import dungeon.load.adapters.WorldAdapter;
import dungeon.load.messages.LevelLoadedEvent;
import dungeon.messages.LifecycleEvent;
import dungeon.messages.Mailman;
import dungeon.messages.Message;
import dungeon.messages.MessageHandler;
import dungeon.models.World;

import javax.xml.bind.JAXB;
import java.util.logging.Level;
import java.util.logging.Logger;

public class LevelLoadHandler implements MessageHandler {
  private static final Logger LOGGER = Logger.getLogger(LevelLoadHandler.class.getName());

  private final Mailman mailman;

  public LevelLoadHandler (Mailman mailman) {
    this.mailman = mailman;
  }

  @Override
  public void handleMessage (Message message) {
    if (message == LifecycleEvent.INITIALIZE) {
      WorldAdapter worldAdapter = new WorldAdapter();

      try {
        World world = worldAdapter.unmarshal(JAXB.unmarshal(getClass().getClassLoader().getResourceAsStream("world.xml"), WorldAdapter.class));

        this.mailman.send(new LevelLoadedEvent(world));
      } catch (Exception e) {
        LOGGER.log(Level.SEVERE, "Loading the world failed", e);
      }
    }
  }
}
