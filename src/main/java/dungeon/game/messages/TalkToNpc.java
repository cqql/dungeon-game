package dungeon.game.messages;

import dungeon.models.NPC;
import dungeon.models.Player;

/**
 * Tell the UI to show the dialog from the NPC.
 */
public class TalkToNpc implements ClientCommand {
  private final int playerId;

  private final NPC npc;

  public TalkToNpc (Player player, NPC npc) {
    this.playerId = player.getId();
    this.npc = npc;
  }

  public int getPlayerId () {
    return this.playerId;
  }

  public NPC getNpc () {
    return this.npc;
  }
}
