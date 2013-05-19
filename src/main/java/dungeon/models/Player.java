package dungeon.models;

import dungeon.models.messages.Transform;

import java.awt.geom.Rectangle2D;

public class Player {
  public static final int SIZE = 1000;

  private final String name;

  private final int hitPoints;

  private final String roomId;

  private final Position position;

  public Player (String name, int hitPoints, String roomId, Position position) {
    this.name = name;
    this.hitPoints = hitPoints;
    this.roomId = roomId;
    this.position = position;
  }

  public String getName () {
    return this.name;
  }

  public int getHitPoints () {
    return this.hitPoints;
  }

  public String getRoomId () {
    return this.roomId;
  }

  public Position getPosition () {
    return this.position;
  }

  /**
   * Checks if the player touches the enemy #enemy.
   */
  public boolean touches (Enemy enemy) {
    Rectangle2D enemySpace = new Rectangle2D.Float(enemy.getPosition().getX(), enemy.getPosition().getY(), Enemy.SIZE, Enemy.SIZE);

    return this.playerSpace().intersects(enemySpace);
  }

  /**
   * Checks if the player touches the 1x1 tile at Position (#x, #y).
   */
  public boolean touches (Tile tile) {
    Rectangle2D tileSpace = new Rectangle2D.Float(tile.getPosition().getX(), tile.getPosition().getY(), Tile.SIZE, Tile.SIZE);

    return this.playerSpace().intersects(tileSpace);
  }

  /**
   * Returns a rectangle that represents the space occupied by the player.
   */
  private Rectangle2D playerSpace () {
    return new Rectangle2D.Float(this.getPosition().getX(), this.getPosition().getY(), Player.SIZE, Player.SIZE);
  }

  public Player apply (Transform transform) {
    if (transform instanceof MoveTransform) {
      MoveTransform move = (MoveTransform)transform;

      return new Player(this.name, this.hitPoints, this.roomId, new Position(this.position.getX() + move.xDelta, this.position.getY() + move.yDelta));
    } else if (transform instanceof HitpointTransform) {
      HitpointTransform hpTransform = (HitpointTransform)transform;

      return new Player(this.name, this.hitPoints + hpTransform.delta, this.roomId, this.position);
    } else if (transform instanceof TeleportTransform) {
      TeleportTransform teleportTransform = (TeleportTransform)transform;

      return new Player(this.name, this.hitPoints, teleportTransform.roomId, new Position(teleportTransform.x, teleportTransform.y));
    } else {
      return this;
    }
  }

  public static class MoveTransform implements Transform {
    private final int xDelta;

    private final int yDelta;

    public MoveTransform (int xDelta, int yDelta) {
      this.xDelta = xDelta;
      this.yDelta = yDelta;
    }
  }

  public static class HitpointTransform implements Transform {
    private final int delta;

    public HitpointTransform (int delta) {
      this.delta = delta;
    }
  }

  public static class TeleportTransform implements Transform {
    private final String roomId;

    private final int x;

    private final int y;

    public TeleportTransform (String roomId, int x, int y) {
      this.roomId = roomId;
      this.x = x;
      this.y = y;
    }
  }
}
