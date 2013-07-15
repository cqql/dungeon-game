package dungeon.load.adapters;

import dungeon.models.NPC;
import dungeon.models.Position;
import dungeon.models.Quest;

import javax.xml.bind.annotation.XmlAttribute;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.adapters.XmlAdapter;
import javax.xml.bind.annotation.adapters.XmlJavaTypeAdapter;

public class NPCAdapter extends XmlAdapter<NPCAdapter, NPC> {
  @XmlAttribute
  public int id;

  @XmlJavaTypeAdapter(PositionAdapter.class)
  public Position position;

  @XmlAttribute
  public String name;

  @XmlAttribute
  public String saying;

  @XmlElement
  @XmlJavaTypeAdapter(QuestAdapter.class)
  public Quest quest;

  @Override
  public NPC unmarshal (NPCAdapter adapter) throws Exception {
    return new NPC(adapter.id, adapter.position, adapter.name, adapter.saying, adapter.quest);
  }

  @Override
  public NPCAdapter marshal (NPC npc) throws Exception {
    NPCAdapter adapter = new NPCAdapter();
    adapter.id = npc.getId();
    adapter.position = npc.getPosition();
    adapter.name = npc.getName();
    adapter.saying = npc.getSaying();
    adapter.quest = npc.getQuest();

    return adapter;
  }
}
