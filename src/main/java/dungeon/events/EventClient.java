package dungeon.events;

import dungeon.Log;

import java.util.concurrent.BlockingQueue;
import java.util.concurrent.LinkedBlockingQueue;
import java.util.concurrent.atomic.AtomicBoolean;

/**
 * A wrapper for an EventListener that manages the event queue and passes the events one after another to the listeners
 * #onEvent() method.
 */
final class EventClient implements Runnable {
  private final EventHost eventHost;

  private final EventListener listener;

  private final BlockingQueue<Event> eventQueue;

  private final AtomicBoolean running;

  public EventClient (EventHost eventHost, EventListener listener) {
    this.eventHost = eventHost;
    this.listener = listener;
    running = new AtomicBoolean();

    eventQueue = new LinkedBlockingQueue<Event>();
  }

  @Override
  public void run () {
    running.set(true);

    listener.setEventHost(eventHost);

    while (running.get()) {
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
   * Ends the client's event loop.
   */
  public void shutdown () {
    running.set(false);
  }

  /**
   * The event host calls this to pass an event to the client.
   *
   * Beware that this method is called from the host's thread.
   */
  public void onEvent (Event event) {
    try {
      eventQueue.put(event);
    } catch (InterruptedException e) {
      Log.notice("Interrupted while receiving an event", e);
    }
  }
}
