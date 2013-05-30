package dungeon.ui.messages;

import dungeon.messages.Message;

/**
 * This message is sent when the ends a command like releasing a key.
 *
 * @see StartCommand
 */
public class EndCommand implements Message {
  private final Command command;

  public EndCommand (Command command) {
    this.command = command;
  }

  public Command getCommand () {
    return command;
  }
}
