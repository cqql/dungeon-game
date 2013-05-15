package dungeon.messages;

/**
 * Consume a stream of messages.
 *
 * WARNING: #shutdown() and #onEvent() will be called from the event host's thread whereas #run() is running in it's own
 * thread. Therefore you have to care about synchronization.
 */
public interface EventConsumer extends Runnable {
  /**
   * You should stop.
   */
  public void shutdown ();

  /**
   * The event host calls this to pass an event to the client.
   *
   * Beware that this method is called from the host's thread.
   */
  public void onEvent (Event event);
}
