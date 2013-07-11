package dungeon.game;

import dungeon.messages.Message;
import dungeon.models.World;
import dungeon.models.messages.IdentityTransform;
import dungeon.models.messages.Transform;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

/**
 * A transaction of messages.
 *
 * A transaction groups messages, so that they can be atomically committed or rolled back. If the messages are
 * transforms, they are applied to the given world object. During the transaction you can access the current state of
 * the world through {@link #getWorld()}. When any one of those transforms fails, the transaction will be rolled back
 * to the last time you committed.
 *
 * Example
 *
 * push(x)
 * push(y)
 * commit() # <-- Committed State 1
 * push(a)
 * push(b)
 * commit()
 *
 * When applying the b transform in the second commit call, an exception is thrown and the transaction will be rolled
 * back to state 1. E.g. after commit #2 {@link #getWorld()} will return the same object it did after commit #1.
 */
public class Transaction {
  private World world;

  /**
   * The committed messages.
   */
  private final List<Message> messages = new ArrayList<>();

  /**
   * The transforms currently in transaction.
   */
  private final List<Message> pendingMessages = new ArrayList<>();

  public Transaction (World world) {
    this.world = world;
  }

  /**
   * Adds a message to the current transaction.
   */
  public void push (Message message) {
    if (message instanceof IdentityTransform) {
      return;
    }

    this.pendingMessages.add(message);
  }

  /**
   * Commits the currently pending transforms.
   *
   * The world object is only updated, when all pending messages succeed.
   */
  public void commit () {
    World world = this.world;

    try {
      for (Message message : this.pendingMessages) {
        if (message instanceof Transform) {
          world = world.apply((Transform) message);
        }
      }

      this.messages.addAll(this.pendingMessages);

      this.world = world;
    } finally {
      this.pendingMessages.clear();
    }
  }

  /**
   * A shorthand for committing a single message.
   */
  public void pushAndCommit (Message message) {
    this.push(message);

    this.commit();
  }

  /**
   * Rollback to the last commit, e.g. dismiss all pending messages.
   */
  public void rollback () {
    this.pendingMessages.clear();
  }

  /**
   * Returns all committed messages.
   */
  public List<Message> getMessages () {
    return Collections.unmodifiableList(this.messages);
  }

  /**
   * Returns the state of the world after applying all committed messages.
   */
  public World getWorld () {
    return this.world;
  }
}
