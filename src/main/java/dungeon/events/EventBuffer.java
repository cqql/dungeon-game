package dungeon.events;

import dungeon.Log;

import java.util.concurrent.BlockingQueue;
import java.util.concurrent.LinkedBlockingQueue;

/**
 * A wrapper for an EventListener that manages an event queue and passes the events one after another to the listeners
 * #onEvent() method.
 */
public final class EventBuffer extends AbstractEventConsumer {
  private final EventListener listener;

  private final BlockingQueue<Event> eventQueue;

  public EventBuffer (EventHost eventHost, EventListener listener) {
    super(eventHost);

    this.listener = listener;
    eventQueue = new LinkedBlockingQueue<Event>();
  }

  @Override
  public void run () {
    listener.setEventHost(getEventHost());

    while (isRunning()) {
      try {
        Event event = eventQueue.take();

        listener.onEvent(event);
      } catch (InterruptedException e) {
        Log.notice("Event client interrupted while running", e);

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
      Log.notice("Interrupted while receiving an event", e);
    }
  }
}
