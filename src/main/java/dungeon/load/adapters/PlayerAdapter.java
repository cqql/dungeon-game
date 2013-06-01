package dungeon.load.adapters;

import dungeon.models.Player;
import dungeon.models.Position;

import javax.xml.bind.annotation.XmlAttribute;
import javax.xml.bind.annotation.adapters.XmlAdapter;
import javax.xml.bind.annotation.adapters.XmlJavaTypeAdapter;

public class PlayerAdapter extends XmlAdapter<PlayerAdapter, Player> {
  @XmlAttribute
  public String name;

  @XmlAttribute
  public int lives;

  @XmlAttribute
  public int hitPoints;

  @XmlAttribute
  public int maxHitPoints;

  @XmlAttribute
  public String levelId;

  @XmlAttribute
  public String roomId;

  @XmlJavaTypeAdapter(PositionAdapter.class)
  public Position position;

  @XmlAttribute
  public String savePointRoomId;

  @XmlJavaTypeAdapter(PositionAdapter.class)
  public Position savePointPosition;

  @Override
  public Player unmarshal (PlayerAdapter adapter) throws Exception {
    return new Player(adapter.name, adapter.lives, adapter.hitPoints, adapter.maxHitPoints, adapter.levelId, adapter.roomId, adapter.position, adapter.savePointRoomId, adapter.savePointPosition);
  }

  @Override
  public PlayerAdapter marshal (Player player) throws Exception {
    PlayerAdapter adapter = new PlayerAdapter();
    adapter.name = player.getName();
    adapter.lives = player.getLives();
    adapter.hitPoints = player.getHitPoints();
    adapter.maxHitPoints = player.getMaxHitPoints();
    adapter.levelId = player.getLevelId();
    adapter.roomId = player.getRoomId();
    adapter.position = player.getPosition();
    adapter.savePointRoomId = player.getSavePointRoomId();
    adapter.savePointPosition = player.getSavePointPosition();

    return adapter;
  }
}
