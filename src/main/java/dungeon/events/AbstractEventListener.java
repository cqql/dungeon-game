package dungeon.events;

public abstract class AbstractEventListener implements EventListener {
  private EventHost eventHost;

  public EventHost getEventHost () {
    return eventHost;
  }

  public void setEventHost (EventHost eventHost) {
    this.eventHost = eventHost;
  }
}
