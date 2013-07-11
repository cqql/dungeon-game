package dungeon.ui.screens;

import dungeon.messages.LifecycleEvent;
import dungeon.server.Server;
import dungeon.ui.Client;

import javax.swing.*;
import javax.swing.event.MouseInputAdapter;
import java.awt.GridLayout;
import java.awt.event.MouseEvent;
import java.util.logging.Logger;

/**
 * The start menu that shows things like a start and quit button.
 */
public class StartMenu extends JPanel {
  private static final Logger LOGGER = Logger.getLogger(StartMenu.class.getName());

  private final JButton startButton;

  private final JButton startNetworkButton;

  private final JButton joinNetworkButton;

  private final JButton quitButton;

  private final Client client;

  public StartMenu (Client client) {
    super(new GridLayout(4, 1));

    this.client = client;

    this.startButton = new JButton("Lokales Spiel starten");
    this.startNetworkButton = new JButton("Netzwerkspiel starten");
    this.joinNetworkButton = new JButton("Netzwerkspiel beitreten");
    this.quitButton = new JButton("Beenden");

    this.add(this.startButton);
    this.add(this.startNetworkButton);
    this.add(this.joinNetworkButton);
    this.add(this.quitButton);

    this.startButton.addMouseListener(new MouseInputAdapter() {
      @Override
      public void mouseClicked (MouseEvent e) {
        Server server = null;

        try {
          server = new Server(6077);
        } catch (Exception ex) {
          LOGGER.warning("Could not start server");
          return;
        }

        server.start();

        try {
          Thread.sleep(500);
        } catch (InterruptedException e1) {
          // Ignore
        }

        StartMenu.this.client.connect("localhost", 6077);
      }
    });

    this.joinNetworkButton.addMouseListener(new MouseInputAdapter() {
      @Override
      public void mouseClicked (MouseEvent e) {
        StartMenu.this.client.connect("localhost", 6077);
      }
    });

    this.quitButton.addMouseListener(new MouseInputAdapter() {
      @Override
      public void mouseClicked (MouseEvent e) {
        StartMenu.this.client.send(LifecycleEvent.SHUTDOWN);
      }
    });
  }
}
