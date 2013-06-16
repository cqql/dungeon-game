package dungeon.models;

import dungeon.game.Transaction;

public enum ItemType {
  HEALTH_POTION("Heiltrank", true, false, 5, 0, 0, "") {
    @Override
    public String getDescription () {
      return "Heilt " + this.getHitPointDelta() + " HP";
    }

    @Override
    public void use (Transaction transaction) {
      transaction.pushAndCommit(new Player.HitpointTransform(this.getHitPointDelta()));
    }
  },
  MANA_POTION("Manatrank", true, false, 0, 5, 0, "") {
    @Override
    public String getDescription () {
      return "Heilt " + this.getManaDelta() + " MP";
    }

    @Override
    public void use (Transaction transaction) {
      transaction.pushAndCommit(new Player.ManaTransform(this.getManaDelta()));
    }
  },
  WEAK_BOW ("Schwacher Bogen", true, true, 0, 0, 3, "weakBow") {
    @Override
    public String getDescription () {
      return this.getWeaponId() + " macht " + this.getDamageDelta() + " Schaden";
    }

    @Override
    public void use (Transaction transaction) {
      transaction.pushAndCommit(new Player.EquipWeaponTransform(this.getWeaponId(), WEAK_BOW, this.getDamageDelta()));
    }
  },
  STRONG_BOW ("Starker Bogen", true, true, 0, 0, 5, "strongBow") {
    @Override
    public String getDescription () {
      return this.getWeaponId() + " macht " + this.getDamageDelta() + " Schaden";
    }

    @Override
    public void use (Transaction transaction) {
      transaction.pushAndCommit(new Player.EquipWeaponTransform(this.getWeaponId(), STRONG_BOW, this.getDamageDelta()));
    }
  };

  private final String name;

  private final boolean useable;

  private final boolean equipable;

  private final int hitPointDelta;

  private final int manaDelta;

  private final int damageDelta;

  private final String weaponId;

  private ItemType (String name, boolean useable, boolean equipable, int hitPointDelta, int manaDelta, int damageDelta, String weaponId) {
    this.name = name;
    this.useable = useable;
    this.equipable = equipable;
    this.hitPointDelta = hitPointDelta;
    this.manaDelta = manaDelta;
    this.damageDelta = damageDelta;
    this.weaponId = weaponId;
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

  public String getWeaponId () {
    return this.weaponId;
  }

  /**
   * Use the item, e.g. apply the appropriate transforms.
   */
  public void use (Transaction transaction) {

  }

  @Override
  public String toString () {
    return "[" + this.name() + " useable=" + this.useable + ", equipable=" + this.equipable + ", hpDelta=" + this.hitPointDelta + ", mpDelta=" + this.manaDelta + ", damage=" + this.damageDelta + "]";
  }
}
