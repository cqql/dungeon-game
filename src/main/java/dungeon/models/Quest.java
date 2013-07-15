package dungeon.models;

import dungeon.game.Transaction;
import dungeon.models.messages.Transform;

import java.io.Serializable;

public class Quest implements Serializable {
  private final int id;

  private final String text;

  /**
   * Has the player done this?
   */
  private final boolean done;

  public Quest (int id, String text, boolean done) {
    this.id = id;
    this.text = text;
    this.done = done;
  }

  public int getId () {
    return id;
  }

  public String getText () {
    return text;
  }

  public boolean isDone () {
    return done;
  }

  /**
   * Checks if the quest is solved in the given {@code world}.
   */
  public boolean isSolved (World world) {
    return false;
  }

  /**
   * Gives the reward to {@code player}.
   */
  public void giveReward (Transaction transaction, Player player) {

  }

  public Quest apply (Transform transform) {
    return this;
  }
}
