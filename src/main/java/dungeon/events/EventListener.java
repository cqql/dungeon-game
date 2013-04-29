package dungeon.events;

/**
 * Listen for events.
 *
 * Most of the time you will want to implement AbstractEventListener.
 *
 * WARNING: Every listener runs in it's own thread. This means that you should not manipulate the event objects.
 * If you publish your own events and they have data attached, make the data objects immutable.
 *
 * @see AbstractEventListener
 */
public interface EventListener {
  /**
   * Sets the event host, so that listeners can publish their own events.
   */
  public void setEventHost (EventHost eventHost);

  /**
   * This method will be called every time the event host publishes an event.
   */
  public void onEvent (Event event);
}
