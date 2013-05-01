package dungeon.events;

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
