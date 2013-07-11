package dungeon.server;

import dungeon.game.LogicHandler;
import dungeon.load.WorldLoader;
import dungeon.messages.Mailman;
import dungeon.messages.Message;
import dungeon.messages.MessageHandler;
import dungeon.models.World;
import dungeon.models.messages.Transform;
import dungeon.pulse.PulseGenerator;

import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.atomic.AtomicBoolean;
import java.util.logging.Level;
import java.util.logging.Logger;

public class Server implements Runnable {
  private static final Logger LOGGER = Logger.getLogger(Server.class.getName());

  private final Thread thread = new Thread(this);

  private final Mailman mailman = new Mailman();

  private final WorldLoader worldLoader = new WorldLoader();

  private final LogicHandler logicHandler;

  private final int port;

  private final List<ClientConnection> connections = new ArrayList<>();

  private final ExecutorService connectionExecutor = Executors.newCachedThreadPool();

  private final AtomicBoolean running = new AtomicBoolean();

  public Server (int port) throws Exception {
    this.port = port;

    World world;

    try {
      world = this.worldLoader.load();
    } catch (Exception e) {
      LOGGER.log(Level.WARNING, "Loading the world failed", e);

      throw e;
    }

    this.logicHandler = new LogicHandler(this.mailman, world);

    this.mailman.addMailbox(new PulseGenerator(this.mailman));
    this.mailman.addHandler(this.logicHandler);

    this.mailman.addHandler(new MessageHandler() {
      @Override
      public void handleMessage (Message message) {
        if (message instanceof Transform) {
          for (ClientConnection connection : Server.this.connections) {
            connection.send(message);
          }
        }
      }
    });

    this.thread.setDaemon(true);
  }

  public void run () {
    this.running.set(true);

    LOGGER.info("Start the event system");
    Thread eventThread = new Thread(this.mailman);
    eventThread.setDaemon(true);
    eventThread.start();

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
        ClientConnection clientConnection = new ClientConnection(connection, this.mailman, this.logicHandler);

        this.connections.add(clientConnection);
        this.connectionExecutor.execute(clientConnection);
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
