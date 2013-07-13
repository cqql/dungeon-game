package dungeon.ui.screens;

import dungeon.client.Client;
import dungeon.ui.messages.MenuCommand;

import javax.swing.*;
import javax.swing.event.MouseInputAdapter;
import java.awt.BorderLayout;
import java.awt.event.MouseEvent;

public class WinScreen extends JPanel {
  private final Client client;

  public WinScreen (Client client) {
    super(new BorderLayout());

    this.client = client;

    JButton backButton = new JButton("Zurück");
    backButton.addMouseListener(new MouseInputAdapter() {
      @Override
      public void mouseClicked (MouseEvent e) {
        WinScreen.this.client.send(MenuCommand.SHOW_MENU);
      }
    });

    this.add(new JLabel("Du hast gewonnen"), BorderLayout.CENTER);
    this.add(backButton, BorderLayout.SOUTH);
  }
}
