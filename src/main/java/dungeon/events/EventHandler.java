package dungeon.events;

/**
 * Handles events.
 *
 * WARNING: Every handler runs in it's own thread. This means that you should not manipulate the event objects.
 * If you publish your own events and they have data attached, make the data objects immutable.
 */
public interface EventHandler {
  /**
   * This method will be called every time the event host publishes an event.
   */
  public void onEvent (Event event);
}
