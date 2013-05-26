package dungeon.ui.screens;

import dungeon.messages.Mailman;
import dungeon.ui.messages.MenuCommand;

import javax.swing.*;
import javax.swing.event.MouseInputAdapter;
import java.awt.BorderLayout;
import java.awt.event.MouseEvent;

public class WinScreen extends JPanel {
  private final Mailman mailman;

  public WinScreen (Mailman mailman) {
    super(new BorderLayout());
    this.mailman = mailman;

    JButton backButton = new JButton("Zurück");
    backButton.addMouseListener(new MouseInputAdapter() {
      @Override
      public void mouseClicked (MouseEvent e) {
        if (WinScreen.this.mailman != null) {
          WinScreen.this.mailman.send(MenuCommand.SHOW_MENU);
        }
      }
    });

    this.add(new JLabel("Du hast gewonnen"), BorderLayout.CENTER);
    this.add(backButton, BorderLayout.SOUTH);
  }
}
