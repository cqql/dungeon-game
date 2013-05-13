package dungeon.models;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class Room {
  private final String id;

  private final List<Enemy> enemies;

  private final List<List<Tile>> tiles;

  public Room (String id, List<Enemy> enemies, List<List<Tile>> tiles) {
    this.id = id;
    this.enemies = Collections.unmodifiableList(new ArrayList<>(enemies));
    this.tiles = Collections.unmodifiableList(new ArrayList<>(tiles));
  }

  public String getId () {
    return id;
  }

  public List<Enemy> getEnemies () {
    return enemies;
  }

  public List<List<Tile>> getTiles () {
    return tiles;
  }

  public int getSize () {
    if (tiles.size() == 0) {
      return 0;
    } else {
      return tiles.get(0).size();
    }
  }
}
