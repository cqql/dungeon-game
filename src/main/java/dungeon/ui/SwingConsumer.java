package dungeon.ui;

import dungeon.messages.Message;
import dungeon.messages.Mailbox;
import dungeon.messages.EventHandler;

import javax.swing.*;

/**
 * An event consumer that passes the messages to the given event handler in the swing event dispatching thread (EDT), so
 * that the event handler can be manipulated in response to messages, which is forbidden from other threads.
 */
public class SwingConsumer implements Mailbox {
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
   * Sends messages to the EDT.
   */
  @Override
  public void onEvent (final Message message) {
    SwingUtilities.invokeLater(new Runnable() {
      @Override
      public void run () {
        SwingConsumer.this.eventHandler.handleEvent(message);
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
