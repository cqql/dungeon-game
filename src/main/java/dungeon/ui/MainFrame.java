package dungeon.ui;

import dungeon.events.Event;
import dungeon.events.EventHandler;
import dungeon.events.EventHost;
import dungeon.events.LifecycleEvent;

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
public class MainFrame extends JFrame implements EventHandler {
  public static final String TITLE = "DUNGEON GAME";

  private final EventHost eventHost;

  public MainFrame (EventHost eventHost) {
    this.eventHost = eventHost;
  }

  @Override
  public void handleEvent (Event event) {
    if (event == LifecycleEvent.INITIALIZE) {
      this.initialize();
    } else if (event == LifecycleEvent.SHUTDOWN) {
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
        MainFrame.this.eventHost.publish(LifecycleEvent.SHUTDOWN);
      }
    });

    this.setVisible(true);
  }
}
