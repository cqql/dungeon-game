package dungeon.ui.screens;

import dungeon.messages.LifecycleEvent;
import dungeon.ui.Client;

import javax.swing.*;
import javax.swing.event.MouseInputAdapter;
import java.awt.GridLayout;
import java.awt.event.MouseEvent;

/**
 * The start menu that shows things like a start and quit button.
 */
public class StartMenu extends JPanel {
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
        try {
          StartMenu.this.client.startServer(6077);
        } catch (Client.ServerStartException e1) {
          JOptionPane.showMessageDialog(null, "Spiel konnte nicht gestartet werden");
        } catch (Client.ConnectException e1) {
          JOptionPane.showMessageDialog(null, "Spiel konnte nicht gestartet werden");
        }
      }
    });

    this.joinNetworkButton.addMouseListener(new MouseInputAdapter() {
      @Override
      public void mouseClicked (MouseEvent e) {
        try {
          StartMenu.this.client.connect("localhost", 6077);
        } catch (Client.ConnectException e1) {
          JOptionPane.showMessageDialog(null, "Es konnte keine Verbindung hergestellt werden");
        }
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
