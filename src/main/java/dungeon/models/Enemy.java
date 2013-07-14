package dungeon.models;

import dungeon.game.Transaction;
import dungeon.models.messages.Transform;
import dungeon.util.Vector;

import java.awt.geom.Rectangle2D;
import java.io.Serializable;
import java.util.List;
import java.util.Random;

public class Enemy implements Spatial, Identifiable, Serializable {
  public static final int SIZE = 1000;

  private final int id;

  private final int hitPoints;

  /**
   * How much damage the enemy inflicts on the player.
   */
  private final int strength = 1;

  private final int speed;

  /**
   * The enemy's position in the room.
   */
  private final Position position;

  private final MoveStrategy moveStrategy;

  private final String onDeath;

  public Enemy (int id, int hitPoints, int speed, Position position, MoveStrategy moveStrategy, String onDeath) {
    this.id = id;
    this.hitPoints = hitPoints;
    this.speed = speed;
    this.position = position;
    this.moveStrategy = moveStrategy;
    this.onDeath = onDeath;
  }

  public int getId () {
    return this.id;
  }

  public int getHitPoints () {
    return this.hitPoints;
  }

  public int getStrength () {
    return this.strength;
  }

  public int getSpeed () {
    return this.speed;
  }

  public Position getPosition () {
    return this.position;
  }

  public MoveStrategy getMoveStrategy () {
    return this.moveStrategy;
  }

  /**
   * What should happen when the enemy dies?
   *
   * null => nothing
   * "VICTORY" => victory
   * "level-id#room-id" => advance to level "level-id" and room "room-id"
   */
  public String getOnDeath () {
    return this.onDeath;
  }

  public Enemy apply (Transform transform) {
    if (transform instanceof EnemyTransform) {
      return ((EnemyTransform)transform).apply(this);
    } else {
      return this;
    }
  }

  @Override
  public Rectangle2D space () {
    return new Rectangle2D.Float(this.position.getX(), this.position.getY(), SIZE, SIZE);
  }

  @Override
  public Position getCenter () {
    return new Position(this.position.getVector().plus(new Vector(SIZE / 2, SIZE / 2)));
  }

  @Override
  public boolean equals (Object o) {
    if (this == o) {
      return true;
    }

    if (o == null || getClass() != o.getClass()) {
      return false;
    }

    Enemy enemy = (Enemy)o;

    if (this.id != enemy.id) {
      return false;
    }

    return true;
  }

  @Override
  public int hashCode () {
    return this.id;
  }

  private static class EnemyTransform implements Transform {
    private final Enemy enemy;

    private EnemyTransform (Enemy enemy) {
      this.enemy = enemy;
    }

    public Enemy apply (Enemy enemy) {
      if (this.enemy.equals(enemy)) {
        return new Enemy(
          this.id(enemy),
          this.hitPoints(enemy),
          this.speed(enemy),
          this.position(enemy),
          this.moveStrategy(enemy),
          this.onDeath(enemy)
        );
      } else {
        return enemy;
      }
    }

    protected int id (Enemy enemy) {
      return enemy.id;
    }

    protected int hitPoints (Enemy enemy) {
      return enemy.hitPoints;
    }

    protected int strength (Enemy enemy) {
      return enemy.strength;
    }

    protected int speed (Enemy enemy) {
      return enemy.speed;
    }

    protected Position position (Enemy enemy) {
      return enemy.position;
    }

    protected MoveStrategy moveStrategy (Enemy enemy) {
      return enemy.moveStrategy;
    }

    protected String onDeath (Enemy enemy) {
      return enemy.onDeath;
    }
  }

  public static class HitPointTransform extends EnemyTransform {
    private final int delta;

    public HitPointTransform (Enemy enemy, int delta) {
      super(enemy);

      this.delta = delta;
    }

    @Override
    protected int hitPoints (Enemy enemy) {
      return Math.max(0, enemy.hitPoints + this.delta);
    }
  }

  public static class MoveTransform extends EnemyTransform {
    private final Vector delta;

    public MoveTransform (Enemy enemy, Vector delta) {
      super(enemy);

      this.delta = delta;
    }

    @Override
    protected Position position (Enemy enemy) {
      return new Position(enemy.getPosition().getVector().plus(this.delta));
    }
  }

  public static class TeleportTransform extends EnemyTransform {
    private final Position position;

    public TeleportTransform (Enemy enemy, Position position) {
      super(enemy);

      this.position = position;
    }

    @Override
    protected Position position (Enemy enemy) {
      return this.position;
    }
  }

  public static enum MoveStrategy {
    NORMAL {
      @Override
      public void move (Transaction transaction, Room room, Enemy enemy, double delta) {
        Vector enemyToPlayer = this.getShortestDistance(enemy, transaction.getWorld().getPlayersInRoom(room));

        if (enemyToPlayer.length() < 5000 && !enemyToPlayer.isZero()) {
          transaction.pushAndCommit(new Enemy.MoveTransform(enemy, enemyToPlayer.normalize().times(enemy.getSpeed() * delta)));
        }
      }
    },
    TELEPORT {
      @Override
      public void move (Transaction transaction, Room room, Enemy enemy, double delta) {
        Player targetPlayer = this.getClosestPlayer(enemy, transaction.getWorld().getPlayersInRoom(room));
        Vector enemyToPlayer = targetPlayer.getPosition().getVector().minus(enemy.getPosition().getVector());

        if (enemyToPlayer.length() < 500 || RANDOM.nextInt(300) == 0) {
          Vector position = new Vector(RANDOM.nextFloat() * room.getXSize(), RANDOM.nextFloat() * room.getYSize());
          position = position.times(0.5 + RANDOM.nextFloat() / 2);

          transaction.pushAndCommit(new TeleportTransform(enemy, new Position(position)));
        } else {
          transaction.pushAndCommit(new Enemy.MoveTransform(enemy, enemyToPlayer.normalize().times(enemy.getSpeed() * delta)));
        }
      }
    };

    private static final Random RANDOM = new Random();

    /**
     * Move the enemy.
     *
     * @param room  The room in which the enemy is
     * @param enemy The enemy to move
     * @param delta How long to move
     */
    public abstract void move (Transaction transaction, Room room, Enemy enemy, double delta);

    /**
     * Finds the {@link Player}, that is closest to {@code enemy}.
     */
    Player getClosestPlayer (Enemy enemy, List<Player> players) {
      Player closest = players.get(0);
      double shortestDistance = closest.getPosition().getVector().minus(enemy.getPosition().getVector()).length();

      for (Player player : players) {
        double distance = player.getPosition().getVector().minus(enemy.getPosition().getVector()).length();

        if (distance < shortestDistance) {
          shortestDistance = distance;
          closest = player;
        }
      }

      return closest;
    }

    /**
     * Finds the shortest distance between {@code enemy} and any of {@code players}.
     *
     * @return A vector pointing from {@code enemy} to the {@link Player}, that is closest to it
     */
    Vector getShortestDistance (Enemy enemy, List<Player> players) {
      Player closest = this.getClosestPlayer(enemy, players);

      return closest.getPosition().getVector().minus(enemy.getPosition().getVector());
    }
  }
}
