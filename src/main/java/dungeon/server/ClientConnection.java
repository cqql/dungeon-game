package dungeon.server;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.Socket;
import java.util.logging.Level;
import java.util.logging.Logger;

public class ClientConnection implements Runnable {
  private static final Logger LOGGER = Logger.getLogger(ClientConnection.class.getName());

  private final Socket socket;

  private final ObjectInputStream inputStream;

  private final ObjectOutputStream outputStream;

  public ClientConnection (Socket socket) throws IOException {
    this.socket = socket;

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

  }
}
