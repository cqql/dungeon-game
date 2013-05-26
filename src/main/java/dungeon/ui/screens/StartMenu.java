package dungeon.ui.screens;

import dungeon.messages.LifecycleEvent;
import dungeon.messages.Mailman;
import dungeon.ui.messages.MenuCommand;

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

  private final Mailman mailman;

  public StartMenu (Mailman mailman) {
    super(new BorderLayout());

    this.mailman = mailman;

    this.startButton = new JButton("Start");
    this.quitButton = new JButton("Beenden");

    this.add(this.startButton, BorderLayout.NORTH);
    this.add(this.quitButton, BorderLayout.SOUTH);

    this.startButton.addMouseListener(new MouseInputAdapter() {
      @Override
      public void mouseClicked (MouseEvent e) {
        if (StartMenu.this.mailman != null) {
          StartMenu.this.mailman.send(MenuCommand.START_GAME);
        }
      }
    });

    this.quitButton.addMouseListener(new MouseInputAdapter() {
      @Override
      public void mouseClicked (MouseEvent e) {
        if (StartMenu.this.mailman != null) {
          StartMenu.this.mailman.send(LifecycleEvent.SHUTDOWN);
        }
      }
    });
  }
}
