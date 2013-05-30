package dungeon.ui.messages;

import dungeon.messages.Message;

/**
 * This message means that the player starts a command.
 *
 * For example this is sent with the appropriate command when the player presses a key like W to move up.
 *
 * @see EndCommand
 */
public class StartCommand implements Message {
  private final Command command;

  public StartCommand (Command command) {
    this.command = command;
  }

  public Command getCommand () {
    return command;
  }
}
