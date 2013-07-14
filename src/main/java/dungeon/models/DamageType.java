package dungeon.models;

import java.io.Serializable;

public enum DamageType implements Serializable {
  NORMAL {
    @Override
    public boolean isStrongAgainst (DamageType damageType) {
      return false;
    }
  },
  ROCK {
    @Override
    public boolean isStrongAgainst (DamageType damageType) {
      return damageType == SCISSORS;
    }
  },
  PAPER {
    @Override
    public boolean isStrongAgainst (DamageType damageType) {
      return damageType == ROCK;
    }
  },
  SCISSORS {
    @Override
    public boolean isStrongAgainst (DamageType damageType) {
      return damageType == PAPER;
    }
  };

  public abstract boolean isStrongAgainst (DamageType damageType);
}
