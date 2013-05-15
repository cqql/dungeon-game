package dungeon.messages;

/**
 * Handles messages.
 *
 * WARNING: Every handler runs in it's own thread. This means that you should not manipulate the event objects.
 * If you send your own messages and they have data attached, make the data objects immutable.
 */
public interface MessageHandler {
  /**
   * This method will be called every time the message host publishes an message.
   */
  public void handleEvent (Message message);
}
