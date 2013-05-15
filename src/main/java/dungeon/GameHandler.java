package dungeon;

import dungeon.messages.Event;
import dungeon.messages.EventHandler;
import dungeon.messages.Mailman;
import dungeon.models.Player;
import dungeon.models.World;
import dungeon.ui.events.MoveCommand;

/**
 * Hier wird die eigentliche Logik des Spiels durchgeführt.
 */
public class GameHandler implements EventHandler {
  private final Mailman mailman;

  private World world;

  public GameHandler (Mailman mailman) {
    this.mailman = mailman;
  }

  @Override
  public void handleEvent (Event event) {
    if (event instanceof LevelLoadHandler.LevelLoadedEvent) {
      this.world = ((LevelLoadHandler.LevelLoadedEvent)event).getWorld();
    } else if (event instanceof MoveCommand) {
      move((MoveCommand)event);
    }
  }

  private void move (MoveCommand command) {
    Player.MoveTransform transform;

    switch (command) {
      case UP:
        transform = new Player.MoveTransform(0, -0.1f);
        break;
      case DOWN:
        transform = new Player.MoveTransform(0, 0.1f);
        break;
      case LEFT:
        transform = new Player.MoveTransform(-0.1f, 0);
        break;
      case RIGHT:
        transform = new Player.MoveTransform(0.1f, 0);
        break;
      default:
        return;
    }

    this.world = this.world.apply(transform);

    mailman.publish(transform);
  }
}
