package dungeon.events;

import java.util.concurrent.atomic.AtomicBoolean;

public abstract class AbstractEventConsumer implements EventConsumer {
  private final AtomicBoolean running = new AtomicBoolean(true);

  /**
   * You should stop.
   */
  @Override
  public void shutdown () {
    running.set(false);
  }

  /**
   * Should this client still run?
   */
  public boolean isRunning () {
    return running.get();
  }

  /**
   * This is intentionally a NOP in case you want to create a consumer that does not actually depend on the received
   * events.
   */
  @Override
  public void onEvent (Event event) {

  }
}
