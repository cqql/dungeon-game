package dungeon.models;

public class TeleporterTile extends Tile {

  public boolean blocking = false;

  public static class Target {
    String RoomID;
    public int x;
    public int y;
  }

  public Target target;

}
