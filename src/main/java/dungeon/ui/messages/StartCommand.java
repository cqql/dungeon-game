package dungeon.ui.messages;

/**
 * This message means that the player starts a command.
 *
 * For example this is sent with the appropriate command when the player presses a key like W to move up.
 *
 * @see EndCommand
 */
public class StartCommand extends AbstractPlayerMessage {
  private final Command command;

  public StartCommand (int playerId, Command command) {
    super(playerId);

    this.command = command;
  }

  public Command getCommand () {
    return this.command;
  }
}
