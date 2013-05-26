package dungeon.ui.screens;

import dungeon.messages.Mailman;
import dungeon.ui.messages.MenuCommand;

import javax.swing.*;
import javax.swing.event.MouseInputAdapter;
import java.awt.BorderLayout;
import java.awt.event.MouseEvent;

public class DefeatScreen extends JPanel {
  private final Mailman mailman;

  public DefeatScreen (Mailman mailman) {
    super(new BorderLayout());

    this.mailman = mailman;

    JButton backButton = new JButton("Zurück");
    backButton.addMouseListener(new MouseInputAdapter() {
      @Override
      public void mouseClicked (MouseEvent e) {
        if (DefeatScreen.this.mailman != null) {
          DefeatScreen.this.mailman.send(MenuCommand.SHOW_MENU);
        }
      }
    });

    this.add(new JLabel("Du hast verloren"), BorderLayout.CENTER);
    this.add(backButton, BorderLayout.SOUTH);
  }
}
