package dungeon.messages;

import java.util.concurrent.atomic.AtomicBoolean;

/**
 * An abstract mailbox.
 *
 * Your job is to override #run() and continually check #isRunning() and stop working if it returns false.
 *
 * This class is thread-safe.
 */
public abstract class AbstractMailbox implements Mailbox {
  private final AtomicBoolean running = new AtomicBoolean(true);

  @Override
  public void shutdown () {
    this.running.set(false);
  }

  /**
   * Should this mailbox still run?
   */
  public boolean isRunning () {
    return this.running.get();
  }

  /**
   * This is intentionally a no-op in case you want to create a mailbox that does not actually depend on the received
   * messages.
   */
  @Override
  public void putMessage (Message message) {

  }
}
