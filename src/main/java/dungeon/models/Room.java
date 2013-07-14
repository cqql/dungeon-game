package dungeon.models;

import dungeon.models.messages.Transform;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;

public class Room implements Serializable {
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

  private final List<NPC> npcs;

  private final List<Merchant> merchants;

  public Room (String id, List<Enemy> enemies, List<SavePoint> savePoints, List<Tile> tiles, List<Drop> drops, List<Projectile> projectiles, List<NPC> npcs, List<Merchant> merchants) {
    this.id = id;
    this.npcs = npcs;
    this.merchants = Collections.unmodifiableList(new ArrayList<>(merchants));
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

  public List<NPC> getNpcs () {
    return this.npcs;
  }

  public List<Merchant> getMerchants () {
    return this.merchants;
  }

  /**
   * Returns the current version of the merchant, if it exists.
   */
  public Merchant findMerchant (Merchant searched) {
    for (Merchant merchant : this.merchants) {
      if (searched.equals(merchant)) {
        return merchant;
      }
    }

    return null;
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
    if (transform instanceof RoomTransform) {
      return ((RoomTransform)transform).apply(this);
    } else {
      List<Enemy> enemies = new ArrayList<>();
      List<Projectile> projectiles = new ArrayList<>();
      List<Merchant> merchants = new ArrayList<>();

      for (Projectile projectile : this.projectiles) {
        projectiles.add(projectile.apply(transform));
      }

      for (Enemy enemy : this.enemies) {
        enemies.add(enemy.apply(transform));
      }

      for (Merchant merchant : this.merchants) {
        merchants.add(merchant.apply(transform));
      }

      return new Room(this.id, enemies, this.savePoints, this.tiles, this.drops, projectiles, this.npcs, merchants);
    }
  }

  private static class RoomTransform implements Transform {
    private final String roomId;

    public RoomTransform (String roomId) {
      this.roomId = roomId;
    }

    public Room apply (Room room) {
      if (this.roomId.equals(room.getId())) {
        return new Room(
          this.id(room),
          this.enemies(room),
          this.savePoints(room),
          this.tiles(room),
          this.drops(room),
          this.projectiles(room),
          this.npcs(room),
          this.merchants(room)
        );
      } else {
        return room;
      }
    }

    protected String id (Room room) {
      return room.id;
    }

    protected List<Enemy> enemies (Room room) {
      return room.enemies;
    }

    protected List<SavePoint> savePoints (Room room) {
      return room.savePoints;
    }

    protected List<Tile> tiles (Room room) {
      return room.tiles;
    }

    protected List<Drop> drops (Room room) {
      return room.drops;
    }

    protected List<Projectile> projectiles (Room room) {
      return room.projectiles;
    }

    protected List<NPC> npcs (Room room) {
      return room.npcs;
    }

    protected List<Merchant> merchants (Room room) {
      return room.merchants;
    }
  }

  public static class AddDropTransform extends RoomTransform {
    private final Drop drop;

    public AddDropTransform (String roomId, Drop drop) {
      super(roomId);

      this.drop = drop;
    }

    @Override
    protected List<Drop> drops (Room room) {
      List<Drop> drops = new ArrayList<>(room.drops);
      drops.add(this.drop);
      return drops;
    }
  }

  public static class RemoveDropTransform extends RoomTransform {
    private final Drop drop;

    public RemoveDropTransform (Room room, Drop drop) {
      super(room.getId());

      this.drop = drop;
    }

    @Override
    protected List<Drop> drops (Room room) {
      List<Drop> drops = new ArrayList<>(room.drops);
      drops.remove(this.drop);
      return drops;
    }
  }

  public static class AddProjectileTransform extends RoomTransform {
    private final Projectile projectile;

    public AddProjectileTransform (String roomId, Projectile projectile) {
      super(roomId);

      this.projectile = projectile;
    }

    @Override
    protected List<Projectile> projectiles (Room room) {
      List<Projectile> projectiles = new ArrayList<>(room.projectiles);
      projectiles.add(this.projectile);
      return projectiles;
    }
  }

  public static class RemoveProjectileTransform extends RoomTransform {
    private final Projectile projectile;

    public RemoveProjectileTransform (String roomId, Projectile projectile) {
      super(roomId);

      this.projectile = projectile;
    }

    @Override
    protected List<Projectile> projectiles (Room room) {
      List<Projectile> projectiles = new ArrayList<>(room.projectiles);
      projectiles.remove(this.projectile);
      return projectiles;
    }
  }

  public static class RemoveEnemyTransform extends RoomTransform {
    private final Enemy enemy;

    public RemoveEnemyTransform (Room room, Enemy enemy) {
      super(room.getId());

      this.enemy = enemy;
    }

    @Override
    protected List<Enemy> enemies (Room room) {
      List<Enemy> enemies = new ArrayList<>(room.enemies);
      enemies.remove(this.enemy);
      return enemies;
    }
  }
}
