package dungeon.game.messages;

import dungeon.messages.Message;

import java.io.Serializable;

/**
 * A command from the server for the client.
 */
public interface ClientCommand extends Message, Serializable {

}
