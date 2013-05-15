package dungeon.messages;

/**
 * Consume a stream of messages.
 *
 * WARNING: #shutdown() and #onEvent() will be called from the event host's thread whereas #run() is running in it's own
 * thread. Therefore you have to care about synchronization.
 */
public interface Mailbox extends Runnable {
  /**
   * You should stop.
   */
  public void shutdown ();

  /**
   * The message host calls this to pass an message to the client.
   *
   * Beware that this method is called from the host's thread.
   */
  public void onEvent (Message message);
}
