package dungeon.ui;

import dungeon.load.messages.LevelLoadedEvent;
import dungeon.messages.Mailman;
import dungeon.messages.Message;
import dungeon.messages.MessageHandler;
import dungeon.models.Player;
import dungeon.models.Room;
import dungeon.models.World;
import dungeon.models.messages.Transform;
import dungeon.ui.messages.MenuCommand;

import java.util.concurrent.atomic.AtomicReference;

/**
 * A client to the game server.
 *
 * This handles the communication with the server, holds a local version of the world and the ID of the player.
 */
public class Client implements MessageHandler {
  private final Mailman mailman;

  private final AtomicReference<Integer> playerId = new AtomicReference<>();

  private final AtomicReference<World> world = new AtomicReference<>();

  public Client (Mailman mailman) {
    this.mailman = mailman;
  }

  @Override
  public void handleMessage (Message message) {
    if (message instanceof Transform) {
      this.world.set(this.world.get().apply((Transform)message));
    } else if (message instanceof LevelLoadedEvent) {
      this.world.set(((LevelLoadedEvent)message).getWorld());
    }
  }

  public void send (Message message) {
    this.mailman.send(message);
  }

  public int getPlayerId () {
    return this.playerId.get();
  }

  /**
   * @return The player object in the current {@link #world} belonging to this client
   */
  public Player getPlayer () {
    return this.world.get().getPlayer(this.getPlayerId());
  }

  /**
   * @return The room that this client is currently in
   */
  public Room getCurrentRoom () {
    Player player = this.getPlayer();

    if (player == null) {
      return null;
    } else {
      return this.world.get().getCurrentRoom(player);
    }
  }

  /**
   * Adds the player to the world and starts the game.
   */
  public void startGame () {
    Player player = new Player("Link");

    this.playerId.set(player.getId());
    this.send(new World.AddPlayerTransform(player));
    this.send(MenuCommand.START_GAME);
  }
}
