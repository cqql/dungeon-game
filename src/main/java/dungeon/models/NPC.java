package dungeon.models;

public class NPC {
  public static final int SIZE = 1000;

  private final int id;

  private final Position position;

  private final String name;

  private final String saying;

  public NPC (int id, Position position, String name, String saying) {
    this.id = id;
    this.position = position;
    this.name = name;
    this.saying = saying;
  }

  public int getId () {
    return this.id;
  }

  public Position getPosition () {
    return this.position;
  }

  public String getName () {
    return this.name;
  }

  public String getSaying () {
    return this.saying;
  }
}
