package dungeon.ui;

import javax.swing.*;
import javax.swing.event.MouseInputAdapter;
import java.awt.BorderLayout;
import java.awt.event.MouseEvent;

public class WinScreen extends JPanel {
  private final Listener listener;

  public WinScreen (Listener listener) {
    super(new BorderLayout());

    this.listener = listener;

    JButton backButton = new JButton("Zurück");
    backButton.addMouseListener(new MouseInputAdapter() {
      @Override
      public void mouseClicked (MouseEvent e) {
        if (WinScreen.this.listener != null) {
          WinScreen.this.listener.onBackButton();
        }
      }
    });

    this.add(new JLabel("Du hast gewonnen"), BorderLayout.CENTER);
    this.add(backButton, BorderLayout.SOUTH);
  }

  public static interface Listener {
    public void onBackButton ();
  }
}
