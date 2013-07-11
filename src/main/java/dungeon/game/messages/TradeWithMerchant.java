package dungeon.game.messages;

import dungeon.models.Merchant;
import dungeon.models.Player;

/**
 * Tells the UI to show the trade window for the given merchant.
 */
public class TradeWithMerchant implements ClientCommand {
  private final int playerId;

  private final Merchant merchant;

  public TradeWithMerchant (Player player, Merchant merchant) {
    this.playerId = player.getId();
    this.merchant = merchant;
  }

  public int getPlayerId () {
    return playerId;
  }

  public Merchant getMerchant () {
    return this.merchant;
  }
}
