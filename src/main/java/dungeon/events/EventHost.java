package dungeon.events;

import dungeon.Log;

import java.util.ArrayList;
import java.util.Collection;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.LinkedBlockingQueue;

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

  public void addListener (EventListener listener) {
    clients.add(new EventClient(this, listener));
  }

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
