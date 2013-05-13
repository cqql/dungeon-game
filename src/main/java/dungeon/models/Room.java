package dungeon.models;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class Room {
  public final String id;

  public final List<Enemy> enemies;

  public final List<List<Tile>> tiles;

  public Room (String id, List<Enemy> enemies, List<List<Tile>> tiles) {
    this.id = id;
    this.enemies = Collections.unmodifiableList(new ArrayList<>(enemies));
    this.tiles = Collections.unmodifiableList(new ArrayList<>(tiles));
  }
}
