package dungeon.load.adapters;

import dungeon.models.Enemy;
import dungeon.models.Room;
import dungeon.models.Tile;

import javax.xml.bind.annotation.XmlAttribute;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlElements;
import javax.xml.bind.annotation.adapters.XmlAdapter;
import javax.xml.bind.annotation.adapters.XmlJavaTypeAdapter;
import java.util.List;

public class RoomAdapter extends XmlAdapter<RoomAdapter, Room> {
  @XmlAttribute
  public String id;

  @XmlElement(name = "enemy")
  @XmlJavaTypeAdapter(EnemyAdapter.class)
  public List<Enemy> enemies;

  @XmlElements({
    @XmlElement(name = "teleporter", type = TeleporterTileAdapter.class),
    @XmlElement(name = "win-tile", type = VictoryTileAdapter.class),
    @XmlElement(name = "tile", type = TileAdapter.class)
  })
  @XmlJavaTypeAdapter(TileAdapter.class)
  public List<Tile> tiles;

  @Override
  public Room unmarshal (RoomAdapter adapter) throws Exception {
    return new Room(adapter.id, adapter.enemies, adapter.tiles);
  }

  @Override
  public RoomAdapter marshal (Room room) throws Exception {
    RoomAdapter adapter = new RoomAdapter();
    adapter.id = room.getId();
    adapter.enemies = room.getEnemies();
    adapter.tiles = room.getTiles();

    return adapter;
  }
}
