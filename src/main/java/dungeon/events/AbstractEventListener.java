package dungeon.events;

/**
 * An abstract implementation of EventListener.
 *
 * All that's left to you is implementing #onEvent().
 */
public abstract class AbstractEventListener implements EventListener {
  private EventHost eventHost;

  public EventHost getEventHost () {
    return eventHost;
  }

  public void setEventHost (EventHost eventHost) {
    this.eventHost = eventHost;
  }

  /**
   * Shortcut to publish an event.
   */
  public void publish (Event event) {
    eventHost.publish(event);
  }
}
