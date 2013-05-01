package dungeon.events;

import dungeon.Log;

import java.util.concurrent.BlockingQueue;
import java.util.concurrent.LinkedBlockingQueue;

/**
 * A wrapper for an EventHandler that manages an event queue and passes the events one after another to the handlers
 * #onEvent() method.
 *
 * This class is thread-safe.
 */
public final class EventQueueConsumer extends AbstractEventConsumer {
  private final EventHandler eventHandler;

  private final BlockingQueue<Event> eventQueue;

  public EventQueueConsumer (EventHandler eventHandler) {
    this.eventHandler = eventHandler;
    eventQueue = new LinkedBlockingQueue<Event>();
  }

  /**
   * Pass the events to the event handler info FIFO-order.
   */
  @Override
  public void run () {
    while (isRunning()) {
      try {
        Event event = eventQueue.take();

        eventHandler.onEvent(event);
      } catch (InterruptedException e) {
        Log.notice("EventQueueConsumer interrupted while running", e);

        shutdown();
      }
    }
  }

  /**
   * Append the event to the event queue.
   */
  public void onEvent (Event event) {
    try {
      eventQueue.put(event);
    } catch (InterruptedException e) {
      Log.notice("EventQueueConsumer interrupted while receiving an event", e);
    }
  }
}
