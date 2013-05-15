package dungeon.messages;

/**
 * Lifecycle events of the mailman.
 */
public enum LifecycleEvent implements Message {
  /**
   * This will always be the first message, that is published.
   *
   * You should to initialization work if you receive this event.
   */
  INITIALIZE,

  /**
   * This will always be the last message, that is published.
   *
   * You should shutdown your mailbox's and handler's activities (e.g. persist data) when you receive this.
   */
  SHUTDOWN;
}
