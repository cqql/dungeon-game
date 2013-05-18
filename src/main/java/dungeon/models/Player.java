package dungeon.models;

import dungeon.models.messages.Transform;

public class Player {
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

  public Player apply (Transform transform) {
    if (transform instanceof MoveTransform) {
      MoveTransform move = (MoveTransform)transform;

      return new Player(this.name, this.hitPoints, this.roomId, new Position(this.position.getX() + move.xDelta, this.position.getY() + move.yDelta));
    } else if (transform instanceof HitpointTransform) {
      HitpointTransform hpTransform = (HitpointTransform)transform;

      return new Player(this.name, this.hitPoints + hpTransform.delta, this.roomId, this.position);
    } else {
      return this;
    }
  }

  public static class MoveTransform implements Transform {
    private final float xDelta;

    private final float yDelta;

    public MoveTransform (float xDelta, float yDelta) {
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
}
