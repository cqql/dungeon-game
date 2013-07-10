package dungeon.ui.messages;

import dungeon.models.Item;
import dungeon.models.Merchant;

public class BuyCommand extends AbstractPlayerMessage {
  private final Merchant merchant;

  private final Item item;

  public BuyCommand (int playerId, Merchant merchant, Item item) {
    super(playerId);

    this.merchant = merchant;
    this.item = item;
  }

  public Merchant getMerchant () {
    return this.merchant;
  }

  public Item getItem () {
    return this.item;
  }
}
