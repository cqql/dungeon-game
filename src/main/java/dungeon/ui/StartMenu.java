package dungeon.ui;

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

  private final Listener listener;

  public StartMenu (Listener listener) {
    super(new BorderLayout());

    this.listener = listener;

    this.startButton = new JButton("Start");
    this.quitButton = new JButton("Beenden");

    this.add(this.startButton, BorderLayout.NORTH);
    this.add(this.quitButton, BorderLayout.SOUTH);

    this.startButton.addMouseListener(new MouseInputAdapter() {
      @Override
      public void mouseClicked (MouseEvent e) {
        if (StartMenu.this.listener != null) {
          StartMenu.this.listener.onStart();
        }
      }
    });

    this.quitButton.addMouseListener(new MouseInputAdapter() {
      @Override
      public void mouseClicked (MouseEvent e) {
        if (StartMenu.this.listener != null) {
          StartMenu.this.listener.onQuit();
        }
      }
    });
  }

  public static interface Listener {
    public void onStart ();

    public void onQuit ();
  }
}
