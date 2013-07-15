package dungeon.load.adapters;

import dungeon.models.*;

import javax.xml.bind.annotation.XmlAttribute;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.adapters.XmlAdapter;
import javax.xml.bind.annotation.adapters.XmlJavaTypeAdapter;
import java.util.ArrayList;
import java.util.List;

public class PlayerAdapter extends XmlAdapter<PlayerAdapter, Player> {
  @XmlAttribute
  public int id;

  @XmlAttribute
  public String name;

  @XmlAttribute
  public int lives;

  @XmlAttribute
  public int hitPoints;

  @XmlAttribute
  public int maxHitPoints;

  @XmlAttribute
  public int money;

  @XmlAttribute
  public int mana;

  @XmlAttribute
  public int maxMana;

  @XmlElement(name = "item")
  @XmlJavaTypeAdapter(ItemAdapter.class)
  public List<Item> items = new ArrayList<>();

  @XmlElement(name = "quest")
  @XmlJavaTypeAdapter(QuestAdapter.class)
  public List<Quest> quests = new ArrayList<>();

  @XmlAttribute
  public String levelId;

  @XmlAttribute
  public String roomId;

  @XmlAttribute
  public int weaponId;

  @XmlJavaTypeAdapter(PositionAdapter.class)
  public Position position;

  @XmlAttribute
  public String viewingDirection;

  @XmlAttribute
  public String savePointRoomId;

  @XmlJavaTypeAdapter(PositionAdapter.class)
  public Position savePointPosition;

  @Override
  public Player unmarshal (PlayerAdapter adapter) throws Exception {
    return new Player(adapter.id, adapter.name, adapter.lives, adapter.hitPoints, adapter.maxHitPoints, adapter.money, adapter.mana, adapter.maxMana, adapter.items, adapter.quests, adapter.levelId, adapter.roomId, adapter.weaponId, adapter.position, Direction.valueOf(adapter.viewingDirection), adapter.savePointRoomId, adapter.savePointPosition);
  }

  @Override
  public PlayerAdapter marshal (Player player) throws Exception {
    PlayerAdapter adapter = new PlayerAdapter();
    adapter.id = player.getId();
    adapter.name = player.getName();
    adapter.lives = player.getLives();
    adapter.hitPoints = player.getHitPoints();
    adapter.maxHitPoints = player.getMaxHitPoints();
    adapter.money = player.getMoney();
    adapter.mana = player.getMana();
    adapter.maxMana = player.getMaxMana();
    adapter.items = player.getItems();
    adapter.quests = player.getQuests();
    adapter.levelId = player.getLevelId();
    adapter.roomId = player.getRoomId();
    adapter.weaponId = player.getWeaponId();
    adapter.position = player.getPosition();
    adapter.viewingDirection = player.getViewingDirection().toString();
    adapter.savePointRoomId = player.getSavePointRoomId();
    adapter.savePointPosition = player.getSavePointPosition();

    return adapter;
  }
}
