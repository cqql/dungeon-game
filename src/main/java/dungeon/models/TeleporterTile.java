package dungeon.models;

public class TeleporterTile extends Tile {

  public boolean blocking = false;

  public static class Target {
    String roomID;
    public int x;
    public int y;
  }

  public Target target;

}
