package dungeon.messages;

import dungeon.Log;

import java.util.concurrent.BlockingQueue;
import java.util.concurrent.LinkedBlockingQueue;
import java.util.concurrent.TimeUnit;

/**
 * A wrapper for an MessageHandler that manages an event queue and passes the messages one after another to the handlers
 * #handleMessage() method.
 *
 * This class is thread-safe.
 */
public final class QueueMailbox extends AbstractMailbox {
  private static final int WAIT_TIME = 10;

  private final MessageHandler messageHandler;

  private final BlockingQueue<Message> messageQueue;

  public QueueMailbox(MessageHandler messageHandler) {
    this.messageHandler = messageHandler;
    this.messageQueue = new LinkedBlockingQueue<>();
  }

  /**
   * Pass the messages to the event handler info FIFO-order.
   */
  @Override
  public void run () {
    while (isRunning()) {
      try {
        // Wait 10 milliseconds at most to prevent dead lock
        Message message = this.messageQueue.poll(WAIT_TIME, TimeUnit.MILLISECONDS);

        if (message != null) {
          this.messageHandler.handleMessage(message);
        }
      } catch (InterruptedException e) {
        Log.notice("QueueMailbox interrupted while running", e);

        this.shutdown();
      }
    }
  }

  /**
   * Append the message to the message queue.
   */
  public void putMessage (Message message) {
    try {
      this.messageQueue.put(message);
    } catch (InterruptedException e) {
      Log.notice("QueueMailbox interrupted while receiving an message", e);
    }
  }
}
