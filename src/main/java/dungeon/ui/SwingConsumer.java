package dungeon.ui;

import dungeon.events.Event;
import dungeon.events.EventConsumer;
import dungeon.events.EventHandler;

import javax.swing.*;

/**
 * An event consumer that passes the events to the given event handler in the swing event dispatching thread (EDT), so
 * that the event handler can be manipulated in response to events, which is forbidden from other threads.
 */
public class SwingConsumer implements EventConsumer {
  private final EventHandler eventHandler;

  public SwingConsumer (EventHandler frame) {
    this.eventHandler = frame;
  }

  /**
   * The event handler is responsible for disposing itself upon the LifecycleEvent.SHUTDOWN event.
   */
  @Override
  public void shutdown () {

  }

  /**
   * Sends events to the EDT.
   */
  @Override
  public void onEvent (final Event event) {
    SwingUtilities.invokeLater(new Runnable() {
      @Override
      public void run () {
        SwingConsumer.this.eventHandler.handleEvent(event);
      }
    });
  }

  /**
   * This is a no-op, because all event handling will be done on the EDT.
   */
  @Override
  public void run () {

  }
}
