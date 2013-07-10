package dungeon.ui.messages;

import dungeon.messages.Message;

/**
 * This message is sent when the ends a command like releasing a key.
 *
 * @see StartCommand
 */
public class EndCommand implements Message {
  private final int playerId;

  private final Command command;

  public EndCommand (int playerId, Command command) {
    this.playerId = playerId;
    this.command = command;
  }

  /**
   * Who ended the command?
   */
  public int getPlayerId () {
    return playerId;
  }

  public Command getCommand () {
    return this.command;
  }
}
