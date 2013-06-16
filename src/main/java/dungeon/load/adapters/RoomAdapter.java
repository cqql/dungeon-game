package dungeon.load.adapters;

import dungeon.models.*;

import javax.xml.bind.annotation.XmlAttribute;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlElements;
import javax.xml.bind.annotation.adapters.XmlAdapter;
import javax.xml.bind.annotation.adapters.XmlJavaTypeAdapter;
import java.util.ArrayList;
import java.util.List;

public class RoomAdapter extends XmlAdapter<RoomAdapter, Room> {
  @XmlAttribute
  public String id;

  @XmlElement(name = "enemy")
  @XmlJavaTypeAdapter(EnemyAdapter.class)
  public List<Enemy> enemies = new ArrayList<>();

  @XmlElement(name = "save-point")
  @XmlJavaTypeAdapter(SavePointAdapter.class)
  public List<SavePoint> savePoints = new ArrayList<>();

  @XmlElements({
    @XmlElement(name = "teleporter", type = TeleporterTileAdapter.class),
    @XmlElement(name = "victory-tile", type = VictoryTileAdapter.class),
    @XmlElement(name = "tile", type = TileAdapter.class)
  })
  @XmlJavaTypeAdapter(TileAdapter.class)
  public List<Tile> tiles = new ArrayList<>();

  @XmlElement(name = "drop")
  @XmlJavaTypeAdapter(DropAdapter.class)
  public List<Drop> drops = new ArrayList<>();

  @XmlElement(name = "projectile")
  @XmlJavaTypeAdapter(ProjectileAdapter.class)
  public List<Projectile> projectiles = new ArrayList<>();

  @Override
  public Room unmarshal (RoomAdapter adapter) throws Exception {
    return new Room(adapter.id, adapter.enemies, adapter.savePoints, adapter.tiles, adapter.drops, adapter.projectiles);
  }

  @Override
  public RoomAdapter marshal (Room room) throws Exception {
    RoomAdapter adapter = new RoomAdapter();
    adapter.id = room.getId();
    adapter.enemies = room.getEnemies();
    adapter.savePoints = room.getSavePoints();
    adapter.tiles = room.getTiles();
    adapter.drops = room.getDrops();
    adapter.projectiles = room.getProjectiles();

    return adapter;
  }
}
