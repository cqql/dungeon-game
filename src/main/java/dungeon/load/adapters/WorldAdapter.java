package dungeon.load.adapters;

import dungeon.models.Player;
import dungeon.models.Room;
import dungeon.models.World;

import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;
import javax.xml.bind.annotation.adapters.XmlAdapter;
import javax.xml.bind.annotation.adapters.XmlJavaTypeAdapter;
import java.util.List;

@XmlRootElement(name = "world")
public class WorldAdapter extends XmlAdapter<WorldAdapter, World> {
  @XmlElement(name = "room")
  @XmlJavaTypeAdapter(RoomAdapter.class)
  public List<Room> rooms;

  @XmlJavaTypeAdapter(PlayerAdapter.class)
  public Player player;

  @Override
  public World unmarshal (WorldAdapter worldAdapter) throws Exception {
    return new World(worldAdapter.rooms, worldAdapter.player);
  }

  @Override
  public WorldAdapter marshal (World world) {
    WorldAdapter adapter = new WorldAdapter();
    adapter.rooms = world.getRooms();
    adapter.player = world.getPlayer();

    return adapter;
  }
}
