package dungeon.models;

import dungeon.models.messages.Transform;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;

public class Room {
  private final String id;

  private final List<Enemy> enemies;

  private final List<SavePoint> savePoints;

  private final List<Tile> tiles;

  /**
   * Items lying around in the room.
   */
  private final List<Drop> drops;

  /**
   * Projectiles flying through the room.
   */
  private final List<Projectile> projectiles;

  public Room (String id, List<Enemy> enemies, List<SavePoint> savePoints, List<Tile> tiles, List<Drop> drops, List<Projectile> projectiles) {
    this.id = id;
    this.drops = Collections.unmodifiableList(new ArrayList<>(drops));
    this.enemies = Collections.unmodifiableList(new ArrayList<>(enemies));
    this.savePoints = Collections.unmodifiableList(new ArrayList<>(savePoints));
    this.tiles = Collections.unmodifiableList(new ArrayList<>(tiles));
    this.projectiles = Collections.unmodifiableList(new ArrayList<>(projectiles));
  }

  public String getId () {
    return this.id;
  }

  public List<Enemy> getEnemies () {
    return this.enemies;
  }

  public List<SavePoint> getSavePoints () {
    return this.savePoints;
  }

  public List<Tile> getTiles () {
    return this.tiles;
  }

  public List<Drop> getDrops () {
    return this.drops;
  }

  public List<Projectile> getProjectiles () {
    return this.projectiles;
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
    String id = this.id;
    List<Enemy> enemies = this.enemies;
    List<Tile> tiles = this.tiles;
    List<SavePoint> savePoints = this.savePoints;
    List<Drop> drops = this.drops;
    List<Projectile> projectiles = this.projectiles;

    if (transform instanceof RemoveDropTransform) {
      drops = new ArrayList<>();

      for (Drop drop : this.drops) {
        if (drop.getId() != ((RemoveDropTransform)transform).dropId) {
          drops.add(drop);
        }
      }
    }

    return new Room(id, enemies, savePoints, tiles, drops, projectiles);
  }

  public static class RemoveDropTransform implements Transform {
    private final int dropId;

    public RemoveDropTransform (int dropId) {
      this.dropId = dropId;
    }
  }
}
