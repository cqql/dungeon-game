package dungeon.messages;

/**
 * Lifecycle messages of the event host.
 */
public enum LifecycleEvent implements Message {
  /**
   * This will always be the first event, that is published.
   *
   * You should to initialization work if you receive this event.
   */
  INITIALIZE,

  /**
   * This will always be the last event, that is published.
   *
   * You should shutdown your listeners activities (e.g. persist data) when you receive this.
   */
  SHUTDOWN;
}
