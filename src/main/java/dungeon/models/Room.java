package dungeon.models;

import dungeon.models.messages.Transform;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
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
    return this.tiles;
  }

  /**
   * Returns all blocking tiles in this room.
   */
  public List<Tile> getWalls () {
    List<Tile> walls = new ArrayList<>();

    for (Tile tile : this.tiles) {
      if (tile.isBlocking()) {
        walls.add(tile);
      }
    }

    return walls;
  }

  /**
   * Returns all victory tiles in this room.
   */
  public List<VictoryTile> getVictoryTiles () {
    List<VictoryTile> tiles = new ArrayList<>();

    for (Tile tile : this.tiles) {
      if (tile instanceof VictoryTile) {
        tiles.add((VictoryTile)tile);
      }
    }

    return tiles;
  }

  /**
   * Returns all teleporters in this room.
   */
  public List<TeleporterTile> getTeleporters () {
    List<TeleporterTile> teleporters = new ArrayList<>();

    for (Tile tile : this.tiles) {
      if (tile instanceof TeleporterTile) {
        teleporters.add((TeleporterTile)tile);
      }
    }

    return teleporters;
  }

  /**
   * Returns the size along the X-axis whereas the size is the longest span occupied by tiles.
   */
  public int getXSize () {
    if (this.tiles.isEmpty()) {
      return 0;
    } else {
      Tile tile = Collections.max(this.tiles, new Comparator<Tile>() {
        @Override
        public int compare (Tile a, Tile b) {
          return a.getPosition().getX() - b.getPosition().getX();
        }
      });

      return tile.getPosition().getX() + Tile.SIZE;
    }
  }

  /**
   * Returns the size along the Y-axis whereas the size is the longest span occupied by tiles.
   */
  public int getYSize () {
    if (this.tiles.isEmpty()) {
      return 0;
    } else {
      Tile tile = Collections.max(this.tiles, new Comparator<Tile>() {
        @Override
        public int compare (Tile a, Tile b) {
          return a.getPosition().getY() - b.getPosition().getY();
        }
      });

      return tile.getPosition().getY() + Tile.SIZE;
    }
  }

  public Room apply (Transform transform) {
    return this;
  }
}
