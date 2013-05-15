package dungeon.messages;

/**
 * A mailbox should accept messages through #putMessage that will then be handled in it's #run method.
 *
 * WARNING: #shutdown() and #putMessage() will be called from the mailman's thread whereas #run() is running in it's own
 * thread. Therefore you have to care about synchronization.
 */
public interface Mailbox extends Runnable {
  /**
   * You should stop.
   */
  public void shutdown ();

  /**
   * The message host calls this to put a message in the mailbox.
   *
   * Beware that this method is called from the mailman's thread.
   */
  public void putMessage (Message message);
}
