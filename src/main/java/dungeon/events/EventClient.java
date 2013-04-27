package dungeon.events;

import dungeon.Log;

import java.util.concurrent.BlockingQueue;
import java.util.concurrent.LinkedBlockingQueue;
import java.util.concurrent.atomic.AtomicBoolean;

final class EventClient implements Runnable {
  private EventHost eventHost;

  private EventListener listener;

  private BlockingQueue<Event> eventQueue;

  private AtomicBoolean running;

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

  public void shutdown () {
    running.set(false);
  }

  public void receive (Event event) {
    try {
      eventQueue.put(event);
    } catch (InterruptedException e) {
      Log.notice("Interrupted while receiving an event", e);
    }
  }
}
