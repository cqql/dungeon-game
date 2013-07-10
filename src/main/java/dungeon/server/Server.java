package dungeon.server;

import dungeon.game.LogicHandler;
import dungeon.load.LevelLoadHandler;
import dungeon.messages.Mailman;
import dungeon.pulse.PulseGenerator;

import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.atomic.AtomicBoolean;
import java.util.logging.Level;
import java.util.logging.Logger;

public class Server implements Runnable {
  private static final Logger LOGGER = Logger.getLogger(Server.class.getName());

  private final Thread thread = new Thread(this);

  private final Mailman mailman = new Mailman();

  private final int port;

  private final ExecutorService connections = Executors.newCachedThreadPool();

  private final AtomicBoolean running = new AtomicBoolean();

  public Server (int port) {
    this.port = port;

    this.mailman.addMailbox(new PulseGenerator(this.mailman));
    this.mailman.addHandler(new LevelLoadHandler(this.mailman));
    this.mailman.addHandler(new LogicHandler(this.mailman));

    this.thread.setDaemon(true);
  }

  public void run () {
    this.running.set(true);

    ServerSocket serverSocket;

    LOGGER.info("Start server on port " + this.port);

    try {
      serverSocket = new ServerSocket(this.port);
    } catch (IOException e) {
      LOGGER.log(Level.WARNING, "Could not bind port " + this.port, e);
      return;
    }

    while (this.running.get()) {
      Socket connection;

      try {
        connection = serverSocket.accept();

        LOGGER.info("Connection from " + connection.getRemoteSocketAddress());
      } catch (IOException e) {
        LOGGER.log(Level.WARNING, "Failed while connecting", e);
        continue;
      }

      try {
        this.connections.execute(new ClientConnection(connection));
      } catch (IOException e) {
        LOGGER.log(Level.WARNING, "Could not setup connection", e);
      }
    }
  }

  /**
   * Start the server in it's own thread.
   */
  public void start () {
    this.thread.start();
  }
}
