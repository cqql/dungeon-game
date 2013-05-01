package dungeon.events;

/**
 * This is the abstract base class for things that do not consume events but only produce them.
 *
 * The tasks left to you is implementing #run(). Be aware that you have to check #isRunning() regularly and stop if it's
 * false.
 */
public abstract class EventSource extends AbstractEventConsumer {
  public EventSource (EventHost eventHost) {
    super(eventHost);
  }

  public void publish (Event event) {
    getEventHost().publish(event);
  }
}
