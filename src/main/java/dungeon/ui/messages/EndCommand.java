package dungeon.ui.messages;

/**
 * This message is sent when the ends a command like releasing a key.
 *
 * @see StartCommand
 */
public class EndCommand extends AbstractPlayerMessage {
  private final Command command;

  public EndCommand (int playerId, Command command) {
    super(playerId);

    this.command = command;
  }

  public Command getCommand () {
    return this.command;
  }
}
