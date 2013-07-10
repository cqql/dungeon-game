package dungeon.ui.screens;

import dungeon.messages.LifecycleEvent;
import dungeon.ui.Client;

import javax.swing.*;
import javax.swing.event.MouseInputAdapter;
import java.awt.BorderLayout;
import java.awt.event.MouseEvent;

/**
 * The start menu that shows things like a start and quit button.
 */
public class StartMenu extends JPanel {
  private final JButton startButton;

  private final JButton quitButton;

  private final Client client;

  public StartMenu (Client client) {
    super(new BorderLayout());

    this.client = client;

    this.startButton = new JButton("Start");
    this.quitButton = new JButton("Beenden");

    this.add(this.startButton, BorderLayout.NORTH);
    this.add(this.quitButton, BorderLayout.SOUTH);

    this.startButton.addMouseListener(new MouseInputAdapter() {
      @Override
      public void mouseClicked (MouseEvent e) {
        StartMenu.this.client.startGame();
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
