package dungeon.game.messages;

import dungeon.messages.Message;
import dungeon.models.NPC;

/**
 * Tell the UI to show the dialog from the NPC.
 */
public class TalkToNpc implements Message {
  private final NPC npc;

  public TalkToNpc (NPC npc) {
    this.npc = npc;
  }

  public NPC getNpc () {
    return this.npc;
  }
}
