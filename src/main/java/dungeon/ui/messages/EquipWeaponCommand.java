package dungeon.ui.messages;

import dungeon.messages.Message;
import dungeon.models.Item;
import dungeon.models.Player;

public class EquipWeaponCommand implements Message {
  private final Item item;

  public equipWeaponCommand (Item item) {
    this.item = Player.EquipWeaponTransform(item.getId());
  }

  public int getWeaponId () {
    return this.item.getId();
  }
}
