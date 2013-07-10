package dungeon.ui.messages;

import dungeon.models.Item;

public class EquipWeaponCommand extends AbstractPlayerMessage {
  private final Item item;

  public EquipWeaponCommand (int playerId, Item item) {
    super(playerId);

    this.item = item;
  }

  public Item getWeapon () {
    return this.item;
  }
}
