package dungeon.ui;

import dungeon.messages.LifecycleEvent;
import dungeon.messages.Mailman;
import dungeon.messages.Message;
import dungeon.messages.MessageHandler;

import javax.swing.*;
import java.awt.Frame;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;

/**
 * The main frame for the application.
 *
 * It handles showing the different screens of the game.
 */
public class MainFrame extends JFrame implements MessageHandler {
  public static final String TITLE = "DUNGEON GAME";

  private final Mailman mailman;

  private final UiManager uiManager;

  public MainFrame (Mailman mailman, UiManager uiManager) {
    this.mailman = mailman;
    this.uiManager = uiManager;
  }

  @Override
  public void handleMessage (Message message) {
    if (message == LifecycleEvent.INITIALIZE) {
      this.initialize();
    } else if (message == LifecycleEvent.SHUTDOWN) {
      this.dispose();
    }
  }

  private void initialize () {
    this.setName(TITLE);
    this.setTitle(TITLE);

    this.setUndecorated(true);
    this.setExtendedState(Frame.MAXIMIZED_BOTH);
    this.setResizable(false);

    this.setDefaultCloseOperation(DO_NOTHING_ON_CLOSE);
    this.addWindowListener(new WindowAdapter() {
      @Override
      public void windowClosing (WindowEvent e) {
        MainFrame.this.mailman.send(LifecycleEvent.SHUTDOWN);
      }
    });

    this.add(this.uiManager);

    this.setVisible(true);
  }
}
