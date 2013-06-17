package dungeon.ui.messages;

import dungeon.messages.Message;
import dungeon.models.Item;

public class EquipWeaponCommand implements Message {
  private final Item item;

  public EquipWeaponCommand (Item item) {
    this.item = item;
  }

  public Item getWeapon () {
    return this.item;
  }
}
