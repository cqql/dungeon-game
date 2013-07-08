package dungeon.game.messages;

import dungeon.messages.Message;
import dungeon.models.Merchant;

/**
 * Tells the UI to show the trade window for the given merchant.
 */
public class TradeWithMerchant implements Message {
  private final Merchant merchant;

  public TradeWithMerchant (Merchant merchant) {
    this.merchant = merchant;
  }

  public Merchant getMerchant () {
    return this.merchant;
  }
}
