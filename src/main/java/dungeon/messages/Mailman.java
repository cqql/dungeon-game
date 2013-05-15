package dungeon.messages;

import dungeon.Log;

import java.util.ArrayList;
import java.util.Collection;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.LinkedBlockingQueue;
import java.util.concurrent.atomic.AtomicBoolean;

/**
 * Publishes messages to attached event consumers.
 *
 * Be aware of the fact that every event consumer will be running in it's own thread.
 */
public final class Mailman {
  private final ExecutorService executor = Executors.newCachedThreadPool();

  private final Collection<Mailbox> mailboxes = new ArrayList<>();

  private final BlockingQueue<Message> messageQueue = new LinkedBlockingQueue<>();

  private final AtomicBoolean running = new AtomicBoolean(true);

  public Mailman() {
    this.publish(LifecycleEvent.INITIALIZE);
  }

  /**
   * Add an event handler.
   *
   * This may only be called before calling #run().
   */
  public void addHandler (MessageHandler messageHandler) {
    this.mailboxes.add(new EventQueueConsumer(messageHandler));
  }

  public void addConsumer (Mailbox mailbox) {
    this.mailboxes.add(mailbox);
  }

  /**
   * Runs the event host until a LifecycleEvent.SHUTDOWN event is received or it's thread is interrupted.
   */
  public void run () {
    for (Mailbox mailbox : this.mailboxes) {
      this.executor.execute(mailbox);
    }

    try {
      while (this.running.get()) {
        this.waitForNextEvent();
      }
    } catch (InterruptedException e) {
      Log.notice("The event host has been interrupted", e);
    } finally {
      Log.notice("The event host is shutting down");

      this.shutdown();
    }
  }

  /**
   * Publish an message.
   *
   * This method should be called from the handlers, that want to publish their own messages.
   *
   * @param message Message to be published
   * @return true on success, otherwise false
   */
  public boolean publish (Message message) {
    if (message == null) {
      return false;
    }

    try {
      this.messageQueue.put(message);

      return true;
    } catch (InterruptedException e) {
      Log.notice("A thread has been interrupted while sending an message", e);

      return false;
    }
  }

  /**
   * Shuts down the host, the executor and all mailboxes.
   *
   * This does not call executor.shutdownNow, because this will force mailboxes to end and will have bad side effects.
   */
  private void shutdown () {
    this.running.set(false);

    this.executor.shutdown();

    for (Mailbox mailbox : this.mailboxes) {
      mailbox.shutdown();
    }
  }

  private void waitForNextEvent () throws InterruptedException {
    Message message = this.messageQueue.take();

    this.handleEvent(message);
  }

  private void handleEvent (Message message) {
    this.publishEvent(message);

    if (message == LifecycleEvent.SHUTDOWN) {
      this.shutdown();
    }
  }

  private void publishEvent (Message message) {
    for (Mailbox mailbox : this.mailboxes) {
      mailbox.onEvent(message);
    }
  }
}
