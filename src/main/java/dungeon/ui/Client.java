package dungeon.ui;

import dungeon.game.messages.PlayerJoinCommand;
import dungeon.messages.Mailman;
import dungeon.messages.Message;
import dungeon.messages.MessageHandler;
import dungeon.models.Player;
import dungeon.models.Room;
import dungeon.models.World;
import dungeon.models.messages.Transform;
import dungeon.ui.messages.MenuCommand;
import dungeon.ui.messages.PlayerMessage;

import java.io.IOException;
import java.util.concurrent.atomic.AtomicReference;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 * A client to the game server.
 *
 * This handles the communication with the server, holds a local version of the world and the ID of the player.
 */
public class Client implements MessageHandler {
  private static final Logger LOGGER = Logger.getLogger(Client.class.getName());

  private final Mailman mailman;

  private final AtomicReference<Integer> playerId = new AtomicReference<>();

  private final AtomicReference<World> world = new AtomicReference<>();

  private ServerConnection serverConnection;

  public Client (Mailman mailman) {
    this.mailman = mailman;
  }

  @Override
  public void handleMessage (Message message) {
    if (message instanceof Transform) {
      this.world.set(this.world.get().apply((Transform)message));
    } else if (message instanceof PlayerMessage
      && ((PlayerMessage) message).getPlayerId() == this.playerId.get()
      && this.serverConnection != null) {
      try {
        this.serverConnection.write(message);
      } catch (IOException e) {
        LOGGER.log(Level.WARNING, "Could not send message to server", e);
      }
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

  public void connect (String host, int port) {
    try {
      this.serverConnection = new ServerConnection(host, port);
    } catch (IOException e) {
      LOGGER.log(Level.WARNING, "Could not connect", e);
    }

    LOGGER.info("Synchronize world");

    try {
      this.world.set((World)this.serverConnection.read());
    } catch (IOException e) {
      LOGGER.log(Level.WARNING, "IOError", e);
    } catch (ClassNotFoundException e) {
      LOGGER.log(Level.WARNING, "Class not found", e);
    }

    LOGGER.info("Join player");
    Player player = new Player("Link");
    this.playerId.set(player.getId());

    try {
      this.serverConnection.write(new PlayerJoinCommand(player));
    } catch (IOException e) {
      LOGGER.log(Level.WARNING, "Could not join", e);
    }

    this.send(MenuCommand.START_GAME);
  }
}
