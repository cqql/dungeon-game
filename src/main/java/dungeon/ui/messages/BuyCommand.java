package dungeon.ui.messages;

import dungeon.messages.Message;
import dungeon.models.Item;
import dungeon.models.Merchant;

public class BuyCommand implements Message {
  private final Merchant merchant;

  private final Item item;

  public BuyCommand (Merchant merchant, Item item) {
    this.merchant = merchant;
    this.item = item;
  }

  public Merchant getMerchant () {
    return merchant;
  }

  public Item getItem () {
    return item;
  }
}
