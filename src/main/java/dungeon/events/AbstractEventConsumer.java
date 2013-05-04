package dungeon.events;

import java.util.concurrent.atomic.AtomicBoolean;

/**
 * An abstract event runner.
 *
 * Your job is to override #run() and continually check #isRunning() and stop working if it returns false.
 *
 * This class is thread-safe.
 */
public abstract class AbstractEventConsumer implements EventConsumer {
  private final AtomicBoolean running = new AtomicBoolean(true);

  @Override
  public void shutdown () {
    this.running.set(false);
  }

  /**
   * Should this client still run?
   */
  public boolean isRunning () {
    return this.running.get();
  }

  /**
   * This is intentionally a no-op in case you want to create a consumer that does not actually depend on the received
   * events.
   */
  @Override
  public void onEvent (Event event) {

  }
}
