package dungeon.game;

import dungeon.models.World;
import dungeon.models.messages.Transform;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

/**
 * Allows you to commit a bunch of transforms to a world.
 *
 * When any one of those transforms fails, the transaction will be reset.
 */
public class Transaction {
  private World world;

  /**
   * The committed transforms.
   */
  private final List<Transform> transforms = new ArrayList<>();

  /**
   * The transforms currently in transaction.
   */
  private final List<Transform> pendingTransforms = new ArrayList<>();

  public Transaction (World world) {
    this.world = world;
  }

  /**
   * A shorthand for committing a single transform.
   */
  public void pushAndCommit (Transform transform) {
    this.world = this.world.apply(transform);

    this.transforms.add(transform);
  }

  /**
   * Returns all committed transforms.
   */
  public List<Transform> getTransforms () {
    return Collections.unmodifiableList(transforms);
  }

  /**
   * Returns the state of the world after applying all committed transforms.
   */
  public World getWorld () {
    return world;
  }
}
