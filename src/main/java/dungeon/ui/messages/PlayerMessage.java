package dungeon.ui.messages;

import dungeon.messages.Message;

import java.io.Serializable;

/**
 * Any message, that is sent in response to input by a player, e.g. "buy the item".
 */
public interface PlayerMessage extends Message, Serializable {
  /**
   * Which player issued the message/command?
   */
  public int getPlayerId ();
}
