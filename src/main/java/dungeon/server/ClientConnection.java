package dungeon.server;

import dungeon.game.LogicHandler;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.Socket;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 * Handles the connection to one client.
 */
public class ClientConnection implements Runnable {
  private static final Logger LOGGER = Logger.getLogger(ClientConnection.class.getName());

  private final Socket socket;

  private final ObjectInputStream inputStream;

  private final ObjectOutputStream outputStream;

  private final LogicHandler logicHandler;

  public ClientConnection (Socket socket, LogicHandler logicHandler) throws IOException {
    this.socket = socket;
    this.logicHandler = logicHandler;

    try {
      this.outputStream = new ObjectOutputStream(socket.getOutputStream());
      this.inputStream = new ObjectInputStream(socket.getInputStream());
    } catch (IOException e) {
      LOGGER.log(Level.WARNING, "Could not create in/out streams", e);

      throw e;
    }
  }

  @Override
  public void run () {
    LOGGER.info("Send world object");
    this.send(this.logicHandler.getWorld());
  }

  private void send (Object object) {
    try {
      this.outputStream.writeObject(object);
    } catch (IOException e) {
      LOGGER.log(Level.WARNING, "Could not send object " + object, e);
    }
  }
}
