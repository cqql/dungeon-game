package dungeon.messages;

import dungeon.Log;

import java.util.concurrent.BlockingQueue;
import java.util.concurrent.LinkedBlockingQueue;
import java.util.concurrent.TimeUnit;

/**
 * A wrapper for an EventHandler that manages an event queue and passes the messages one after another to the handlers
 * #handleEvent() method.
 *
 * This class is thread-safe.
 */
public final class EventQueueConsumer extends AbstractMailbox {
  private final EventHandler eventHandler;

  private final BlockingQueue<Message> messageQueue;

  public EventQueueConsumer (EventHandler eventHandler) {
    this.eventHandler = eventHandler;
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
        Message message = this.messageQueue.poll(10, TimeUnit.MILLISECONDS);

        if (message != null) {
          this.eventHandler.handleEvent(message);
        }
      } catch (InterruptedException e) {
        Log.notice("EventQueueConsumer interrupted while running", e);

        this.shutdown();
      }
    }
  }

  /**
   * Append the message to the message queue.
   */
  public void onEvent (Message message) {
    try {
      this.messageQueue.put(message);
    } catch (InterruptedException e) {
      Log.notice("EventQueueConsumer interrupted while receiving an message", e);
    }
  }
}
