package dungeon.events;

import dungeon.Log;

import java.util.ArrayList;
import java.util.Collection;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.LinkedBlockingQueue;

/**
 * Publishes events to all registered listeners.
 *
 * And every listener runs in it's own thread.
 */
public final class EventHost {
  private ExecutorService executor;

  private Collection<EventClient> clients;

  private BlockingQueue<Event> eventQueue;

  private boolean running;

  public EventHost () {
    this(Executors.newCachedThreadPool());
  }

  public EventHost (ExecutorService executor) {
    this.executor = executor;

    clients = new ArrayList<EventClient>();
    eventQueue = new LinkedBlockingQueue<Event>();

    send(LifeCycleEvents.INITIALIZE);
  }

  /**
   * Register a listener.
   *
   * This may only be called before calling #run().
   *
   * @param listener
   */
  public void addListener (EventListener listener) {
    clients.add(new EventClient(this, listener));
  }

  /**
   * Runs the event host until a LifeCycleEvents.SHUTDOWN event is received or it's thread is interrupted.
   */
  public void run () {
    for (EventClient client : clients) {
      executor.execute(client);
    }

    running = true;

    try {
      while (running) {
        waitForNextEvent();
      }
    } catch (InterruptedException e) {
      Log.notice("The event host has been interrupted", e);
    } finally {
      Log.notice("The event host is shutting down");

      shutdown();
    }
  }

  /**
   * Send an event.
   *
   * This method should be called from the listeners, that want to publish their own events.
   *
   * @param event Event to be published
   * @return true on success, otherwise false
   */
  public boolean send (Event event) {
    if (event == null) {
      return false;
    }

    try {
      eventQueue.put(event);

      return true;
    } catch (InterruptedException e) {
      Log.notice("A thread has been interrupted while sending an event", e);

      return false;
    }
  }

  private void shutdown () {
    executor.shutdown();

    for (EventClient client : clients) {
      client.shutdown();
    }

    executor.shutdownNow();
  }

  private void waitForNextEvent () throws InterruptedException {
    Event event = eventQueue.take();

    handleEvent(event);
  }

  private void handleEvent (Event event) {
    publishEvent(event);

    if (event == LifeCycleEvents.SHUTDOWN) {
      running = false;
    }
  }

  private void publishEvent (Event event) {
    for (EventClient client : clients) {
      client.receive(event);
    }
  }
}
