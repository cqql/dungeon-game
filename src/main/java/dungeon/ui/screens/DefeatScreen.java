package dungeon.ui.screens;

import dungeon.ui.Client;
import dungeon.ui.messages.MenuCommand;

import javax.swing.*;
import javax.swing.event.MouseInputAdapter;
import java.awt.BorderLayout;
import java.awt.event.MouseEvent;

public class DefeatScreen extends JPanel {
  private final Client client;

  public DefeatScreen (Client client) {
    super(new BorderLayout());

    this.client = client;

    JButton backButton = new JButton("Zurück");
    backButton.addMouseListener(new MouseInputAdapter() {
      @Override
      public void mouseClicked (MouseEvent e) {
        DefeatScreen.this.client.send(MenuCommand.SHOW_MENU);
      }
    });

    this.add(new JLabel("Du hast verloren"), BorderLayout.CENTER);
    this.add(backButton, BorderLayout.SOUTH);
  }
}
