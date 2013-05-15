package dungeon.ui;

import dungeon.messages.Message;
import dungeon.messages.MessageHandler;
import dungeon.messages.Mailman;
import dungeon.messages.LifecycleEvent;

import javax.swing.*;
import java.awt.*;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;

/**
 * The main frame for the application.
 *
 * handleEvent() will be called on the swing EDT. This means that any methods called by handleEvent() are generally
 * save to manipulate the frame.
 */
public class MainFrame extends JFrame implements MessageHandler {
  public static final String TITLE = "DUNGEON GAME";

  private final Mailman mailman;

  public MainFrame (Mailman mailman) {
    this.mailman = mailman;
  }

  @Override
  public void handleEvent (Message message) {
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
        MainFrame.this.mailman.publish(LifecycleEvent.SHUTDOWN);
      }
    });

    this.setVisible(true);
  }
}
