package dungeon.load.adapters;

import dungeon.models.Drop;
import dungeon.models.Item;
import dungeon.models.Position;

import javax.xml.bind.annotation.XmlAttribute;
import javax.xml.bind.annotation.adapters.XmlAdapter;
import javax.xml.bind.annotation.adapters.XmlJavaTypeAdapter;

public class DropAdapter extends XmlAdapter<DropAdapter, Drop> {
  @XmlJavaTypeAdapter(PositionAdapter.class)
  public Position position;

  @XmlJavaTypeAdapter(ItemAdapter.class)
  public Item item;

  @XmlAttribute
  public int money;

  @Override
  public Drop unmarshal (DropAdapter dropAdapter) throws Exception {
    return new Drop(dropAdapter.position, dropAdapter.item, dropAdapter.money);
  }

  @Override
  public DropAdapter marshal (Drop drop) throws Exception {
    DropAdapter adapter = new DropAdapter();
    adapter.position = drop.getPosition();
    adapter.item = drop.getItem();
    adapter.money = drop.getMoney();

    return adapter;
  }
}
