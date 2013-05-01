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
      initialize();
    } else if (event == LifecycleEvent.SHUTDOWN) {
      dispose();
    }
  }

  private void initialize () {
    setName(TITLE);
    setTitle(TITLE);

    setUndecorated(true);
    setExtendedState(Frame.MAXIMIZED_BOTH);
    setResizable(false);

    setDefaultCloseOperation(DO_NOTHING_ON_CLOSE);
    addWindowListener(new WindowAdapter() {
      @Override
      public void windowClosing (WindowEvent e) {
        eventHost.publish(LifecycleEvent.SHUTDOWN);
      }
    });

    setVisible(true);
  }
}
