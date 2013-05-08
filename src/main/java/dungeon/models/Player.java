package dungeon.models;

public class Player {
  public static class Position {
    public float x;
    public float y;
    public String roomId;
  }

  public Position position;

  public String name;

  public int hitPoints = 1;
}