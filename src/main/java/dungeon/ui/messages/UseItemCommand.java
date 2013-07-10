package dungeon.ui.messages;

import dungeon.models.Item;

public class UseItemCommand extends AbstractPlayerMessage {
  private final Item item;

  public UseItemCommand (int playerId, Item item) {
    super(playerId);

    this.item = item;
  }

  public Item getItem () {
    return this.item;
  }
}
