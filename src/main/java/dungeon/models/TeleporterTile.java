package dungeon.models;

public class TeleporterTile extends Tile {
  public final Target target;

  public TeleporterTile (boolean blocking, Target target) {
    super(blocking);

    this.target = target;
  }

  public static class Target {
    public final String roomId;

    public final int x;

    public final int y;

    public Target (String roomId, int x, int y) {
      this.roomId = roomId;
      this.x = x;
      this.y = y;
    }
  }
}
