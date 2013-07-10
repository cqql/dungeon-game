package dungeon.models;

import dungeon.game.Transaction;

import java.io.Serializable;

public enum ItemType implements Serializable {
  HEALTH_POTION("Heiltrank", true, false, 5, 5, 0, 0) {
    @Override
    public String getDescription () {
      return "Heilt " + this.getHitPointDelta() + " HP";
    }

    @Override
    public void use (Transaction transaction, Player player) {
      transaction.pushAndCommit(new Player.HitpointTransform(player, this.getHitPointDelta()));
    }
  },
  MANA_POTION("Manatrank", true, false, 5, 0, 5, 0) {
    @Override
    public String getDescription () {
      return "Heilt " + this.getManaDelta() + " MP";
    }

    @Override
    public void use (Transaction transaction, Player player) {
      transaction.pushAndCommit(new Player.ManaTransform(player, this.getManaDelta()));
    }
  },
  WEAK_BOW ("Schwacher Bogen", false, true, 10, 0, 0, 3) {
    @Override
    public String getDescription () {
      return "Macht " + this.getDamageDelta() + " Schaden";
    }
  },
  STRONG_BOW ("Starker Bogen", false, true, 20, 0, 0, 5) {
    @Override
    public String getDescription () {
      return "Macht " + this.getDamageDelta() + " Schaden";
    }
  };

  private final String name;

  private final boolean useable;

  private final boolean equipable;

  private final int value;

  private final int hitPointDelta;

  private final int manaDelta;

  private final int damageDelta;

  private ItemType (String name, boolean useable, boolean equipable, int value, int hitPointDelta, int manaDelta, int damageDelta) {
    this.name = name;
    this.useable = useable;
    this.equipable = equipable;
    this.value = value;
    this.hitPointDelta = hitPointDelta;
    this.manaDelta = manaDelta;
    this.damageDelta = damageDelta;
  }

  public String getName () {
    return this.name;
  }

  public abstract String getDescription ();

  public boolean isUseable () {
    return this.useable;
  }

  public boolean isEquipable () {
    return this.equipable;
  }

  public int getValue () {
    return this.value;
  }

  public int getHitPointDelta () {
    return this.hitPointDelta;
  }

  public int getManaDelta () {
    return this.manaDelta;
  }

  public int getDamageDelta () {
    return this.damageDelta;
  }

  /**
   * Use the item, e.g. apply the appropriate transforms.
   *
   * @param transaction The current transaction on which the item should operate
   * @param player The player who is using the item
   */
  public void use (Transaction transaction, Player player) {

  }

  @Override
  public String toString () {
    return "[" + this.name() + " useable=" + this.useable + ", equipable=" + this.equipable + ", hpDelta=" + this.hitPointDelta + ", mpDelta=" + this.manaDelta + ", damage=" + this.damageDelta + "]";
  }
}
