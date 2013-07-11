package dungeon.game.messages;

import dungeon.messages.Message;
import dungeon.models.Player;

import java.io.Serializable;

public class PlayerJoinCommand implements Message, Serializable {
  private final Player player;

  public PlayerJoinCommand (Player player) {
    this.player = player;
  }

  public Player getPlayer () {
    return player;
  }
}
