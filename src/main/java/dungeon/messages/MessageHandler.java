package dungeon.messages;

/**
 * Handles messages.
 *
 * WARNING: Every handler runs in it's own thread.
 */
public interface MessageHandler {
  /**
   * The mailbox will pass the messages to this method to be handled.
   */
  public void handleMessage (Message message);
}
