package dungeon.load.adapters;

import dungeon.models.Item;
import dungeon.models.Merchant;
import dungeon.models.Position;

import javax.xml.bind.annotation.XmlAttribute;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.adapters.XmlAdapter;
import javax.xml.bind.annotation.adapters.XmlJavaTypeAdapter;
import java.util.ArrayList;
import java.util.List;

public class MerchantAdapter extends XmlAdapter<MerchantAdapter, Merchant> {
  @XmlAttribute
  public int id;

  @XmlJavaTypeAdapter(PositionAdapter.class)
  public Position position;

  @XmlAttribute
  public int money;

  @XmlElement(name = "item")
  @XmlJavaTypeAdapter(ItemAdapter.class)
  public List<Item> items = new ArrayList<>();

  @Override
  public Merchant unmarshal (MerchantAdapter adapter) throws Exception {
    return new Merchant(adapter.id, adapter.position, adapter.money, adapter.items);
  }

  @Override
  public MerchantAdapter marshal (Merchant merchant) throws Exception {
    MerchantAdapter adapter = new MerchantAdapter();
    adapter.id = merchant.getId();
    adapter.position = merchant.getPosition();
    adapter.money = merchant.getMoney();
    adapter.items = merchant.getItems();

    return adapter;
  }
}
