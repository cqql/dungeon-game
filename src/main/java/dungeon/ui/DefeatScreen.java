package dungeon.ui;

import javax.swing.*;
import javax.swing.event.MouseInputAdapter;
import java.awt.BorderLayout;
import java.awt.event.MouseEvent;

public class DefeatScreen extends JPanel {
  private final Listener listener;

  public DefeatScreen (Listener listener) {
    super(new BorderLayout());

    this.listener = listener;

    JButton backButton = new JButton("Zurück");
    backButton.addMouseListener(new MouseInputAdapter() {
      @Override
      public void mouseClicked (MouseEvent e) {
        if (DefeatScreen.this.listener != null) {
          DefeatScreen.this.listener.onBackButton();
        }
      }
    });

    this.add(new JLabel("Du hast verloren"), BorderLayout.CENTER);
    this.add(backButton, BorderLayout.SOUTH);
  }

  public static interface Listener {
    public void onBackButton ();
  }
}
