package dungeon.events;

public interface EventListener {
  public void setEventHost (EventHost eventHost);

  public void onEvent (Event event);
}
