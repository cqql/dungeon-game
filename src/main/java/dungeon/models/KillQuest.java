package dungeon.models;

import dungeon.game.Transaction;
import dungeon.models.messages.Transform;

/**
 * A quest, that is resolved, when all enemies in a room are dead.
 */
public class KillQuest extends Quest {
  private final String roomId;

  public KillQuest (int id, String name, String text, boolean done, String roomId) {
    super(id, name, text, done);

    this.roomId = roomId;
  }

  public String getRoomId () {
    return roomId;
  }

  @Override
  public void giveReward (Transaction transaction, Player player) {
    transaction.pushAndCommit(new Player.MaxHitPointTransform(player, 1));
    transaction.pushAndCommit(new Player.MaxManaTransform(player, 2));
  }

  @Override
  public boolean isSolved (World world) {
    Room room = world.getRoom(this.roomId);

    return room.getEnemies().size() == 0;
  }

  @Override
  public Quest apply (Transform transform) {
    if (transform instanceof SolveTransform && this.getId() == ((SolveTransform)transform).getId()) {
      return new KillQuest(this.getId(), this.getName(), this.getText(), true, this.roomId);
    } else {
      return this;
    }
  }
}
