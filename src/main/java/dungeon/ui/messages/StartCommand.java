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
  private final int playerId;

  private final Command command;

  public StartCommand (int playerId, Command command) {
    this.playerId = playerId;
    this.command = command;
  }

  /**
   * Who started the command?
   */
  public int getPlayerId () {
    return playerId;
  }

  public Command getCommand () {
    return this.command;
  }
}
