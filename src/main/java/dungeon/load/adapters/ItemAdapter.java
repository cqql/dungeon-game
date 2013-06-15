package dungeon.load.adapters;

import dungeon.models.Item;

import javax.xml.bind.annotation.adapters.XmlAdapter;

public class ItemAdapter extends XmlAdapter<ItemAdapter, Item> {
  @Override
  public Item unmarshal (ItemAdapter itemAdapter) throws Exception {
    return new Item();
  }

  @Override
  public ItemAdapter marshal (Item item) throws Exception {
    ItemAdapter adapter = new ItemAdapter();

    return adapter;
  }
}
