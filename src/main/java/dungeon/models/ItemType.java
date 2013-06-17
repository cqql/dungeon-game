package dungeon.models;

import dungeon.game.Transaction;

public enum ItemType {
  HEALTH_POTION("Heiltrank", true, false, 5, 0, 0) {
    @Override
    public String getDescription () {
      return "Heilt " + this.getHitPointDelta() + " HP";
    }

    @Override
    public void use (Transaction transaction) {
      transaction.pushAndCommit(new Player.HitpointTransform(this.getHitPointDelta()));
    }
  },
  MANA_POTION("Manatrank", true, false, 0, 5, 0) {
    @Override
    public String getDescription () {
      return "Heilt " + this.getManaDelta() + " MP";
    }

    @Override
    public void use (Transaction transaction) {
      transaction.pushAndCommit(new Player.ManaTransform(this.getManaDelta()));
    }
  },
  WEAK_BOW ("Schwacher Bogen", false, true, 0, 0, 3) {
    @Override
    public String getDescription () {
      return "Macht " + this.getDamageDelta() + " Schaden";
    }

    @Override
    public void equip (Transaction transaction) {
      transaction.pushAndCommit(new Player.EquipWeaponTransform(1));
    }
  },
  STRONG_BOW ("Starker Bogen", false, true, 0, 0, 5) {
    @Override
    public String getDescription () {
      return "Macht " + this.getDamageDelta() + " Schaden";
    }

    @Override
    public void equip (Transaction transaction) {
      transaction.pushAndCommit(new Player.EquipWeaponTransform(2));
    }
  };

  private final String name;

  private final boolean useable;

  private final boolean equipable;

  private final int hitPointDelta;

  private final int manaDelta;

  private final int damageDelta;


  private ItemType (String name, boolean useable, boolean equipable, int hitPointDelta, int manaDelta, int damageDelta) {
    this.name = name;
    this.useable = useable;
    this.equipable = equipable;
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
   */
  public void use (Transaction transaction) {

  }

  /**
   * Equip the item, e.g. apply the appropriate transforms.
   */
  public void equip (Transaction transaction) {

  }

  @Override
  public String toString () {
    return "[" + this.name() + " useable=" + this.useable + ", equipable=" + this.equipable + ", hpDelta=" + this.hitPointDelta + ", mpDelta=" + this.manaDelta + ", damage=" + this.damageDelta + "]";
  }
}
