package dungeon.models;

import dungeon.game.Transaction;
import dungeon.models.messages.Transform;

import java.io.Serializable;

public class Quest implements Serializable {
  private final int id;

  private final String name;

  private final String text;

  /**
   * Has the player done this?
   */
  private final boolean done;

  public Quest (int id, String name, String text, boolean done) {
    this.id = id;
    this.name = name;
    this.text = text;
    this.done = done;
  }

  public int getId () {
    return this.id;
  }

  public String getName () {
    return this.name;
  }

  public String getText () {
    return this.text;
  }

  public boolean isDone () {
    return this.done;
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
    if (transform instanceof SolveTransform && this.id == ((SolveTransform)transform).id) {
      return new Quest(this.id, this.name, this.text, true);
    } else {
      return this;
    }
  }

  public static class SolveTransform implements Transform {
    private final int id;

    public SolveTransform (int id) {
      this.id = id;
    }

    public int getId () {
      return this.id;
    }
  }
}
