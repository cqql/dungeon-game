package dungeon.load.adapters;

import dungeon.models.Item;

import javax.xml.bind.annotation.XmlAttribute;
import javax.xml.bind.annotation.adapters.XmlAdapter;

public class ItemAdapter extends XmlAdapter<ItemAdapter, Item> {
  @XmlAttribute
  public int id;

  @Override
  public Item unmarshal (ItemAdapter itemAdapter) throws Exception {
    return new Item(itemAdapter.id);
  }

  @Override
  public ItemAdapter marshal (Item item) throws Exception {
    ItemAdapter adapter = new ItemAdapter();
    adapter.id = item.getId();

    return adapter;
  }
}
