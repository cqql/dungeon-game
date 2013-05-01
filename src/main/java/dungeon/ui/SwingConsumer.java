package dungeon.ui;

import dungeon.events.Event;
import dungeon.events.EventConsumer;

import javax.swing.*;

/**
 * An event consumer that passes the events to the given frame in the swing event dispatching thread, so that the frame
 * can be manipulated in response to events, which is forbidden from other threads.
 */
public class SwingConsumer implements EventConsumer {
  private final EventedFrame frame;

  public SwingConsumer (EventedFrame frame) {
    this.frame = frame;
  }

  @Override
  public void shutdown () {
    SwingUtilities.invokeLater(new Runnable() {
      @Override
      public void run () {
        frame.dispose();
      }
    });
  }

  /**
   * Sends events to the swing EDT.
   */
  @Override
  public void onEvent (final Event event) {
    SwingUtilities.invokeLater(new Runnable() {
      @Override
      public void run () {
        frame.handleEvent(event);
      }
    });
  }

  /**
   * This is a no-op, because all interaction with the frame has to be done in the swing event dispatching thread.
   */
  @Override
  public void run () {

  }
}
