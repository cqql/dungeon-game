package dungeon.ui.messages;

import dungeon.messages.Message;
import dungeon.models.Item;

public class UseItemCommand implements Message {
  private final Item item;

  public UseItemCommand (Item item) {
    this.item = item;
  }

  public Item getItem () {
    return this.item;
  }
}
