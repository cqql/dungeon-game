package dungeon.load;

import dungeon.load.adapters.WorldAdapter;
import dungeon.load.messages.LevelLoadedEvent;
import dungeon.messages.Mailman;
import dungeon.messages.Message;
import dungeon.messages.MessageHandler;
import dungeon.models.World;
import dungeon.ui.messages.MenuCommand;

import javax.xml.bind.JAXB;
import java.util.logging.Level;
import java.util.logging.Logger;

public class LevelLoadHandler implements MessageHandler {
  private static final Logger logger = Logger.getLogger(LevelLoadHandler.class.getName());

  private final Mailman mailman;

  public LevelLoadHandler (Mailman mailman) {
    this.mailman = mailman;
  }

  @Override
  public void handleMessage (Message message) {
    if (message == MenuCommand.START_GAME) {
      WorldAdapter worldAdapter = new WorldAdapter();

      try {
        World world = worldAdapter.unmarshal(JAXB.unmarshal(getClass().getClassLoader().getResourceAsStream("world.xml"), WorldAdapter.class));

        this.mailman.send(new LevelLoadedEvent(world));
      } catch (Exception e) {
        logger.log(Level.SEVERE, "Loading the world failed", e);
      }
    }
  }
}
