package dungeon.ui;

import dungeon.messages.Message;
import dungeon.messages.Mailbox;
import dungeon.messages.MessageHandler;

import javax.swing.*;

/**
 * An event consumer that passes the messages to the given event handler in the swing event dispatching thread (EDT), so
 * that the event handler can be manipulated in response to messages, which is forbidden from other threads.
 */
public class SwingMailbox implements Mailbox {
  private final MessageHandler messageHandler;

  public SwingMailbox (MessageHandler frame) {
    this.messageHandler = frame;
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
  public void putMessage (final Message message) {
    SwingUtilities.invokeLater(new Runnable() {
      @Override
      public void run () {
        SwingMailbox.this.messageHandler.handleMessage(message);
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
