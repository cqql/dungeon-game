package dungeon.load.adapters;

import dungeon.models.Level;
import dungeon.models.Room;

import javax.xml.bind.annotation.XmlAttribute;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.adapters.XmlAdapter;
import javax.xml.bind.annotation.adapters.XmlJavaTypeAdapter;
import java.util.List;

public class LevelAdapter extends XmlAdapter<LevelAdapter, Level> {
  @XmlAttribute
  public String id;

  @XmlElement(name = "room")
  @XmlJavaTypeAdapter(RoomAdapter.class)
  public List<Room> rooms;

  @Override
  public Level unmarshal (LevelAdapter adapter) throws Exception {
    return new Level(adapter.id, adapter.rooms);
  }

  @Override
  public LevelAdapter marshal (Level level) throws Exception {
    LevelAdapter adapter = new LevelAdapter();
    adapter.id = level.getId();
    adapter.rooms = level.getRooms();

    return adapter;
  }
}
