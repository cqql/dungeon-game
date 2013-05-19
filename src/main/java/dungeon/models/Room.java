package dungeon.models;

import dungeon.models.messages.Transform;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class Room {
  private final String id;

  private final List<Enemy> enemies;

  private final List<Tile> tiles;

  public Room (String id, List<Enemy> enemies, List<Tile> tiles) {
    this.id = id;
    this.enemies = Collections.unmodifiableList(new ArrayList<>(enemies));
    this.tiles = Collections.unmodifiableList(new ArrayList<>(tiles));
  }

  public String getId () {
    return this.id;
  }

  public List<Enemy> getEnemies () {
    return this.enemies;
  }

  public List<Tile> getTiles () {
    return tiles;
  }

  public int getSize () {
    if (this.tiles.size() == 0) {
      return 0;
    } else {
      return 3;
      //return this.tiles.get(0).size();
    }
  }

  public Room apply (Transform transform) {
    return this;
  }
}
