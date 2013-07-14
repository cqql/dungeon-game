package dungeon.load.adapters;

import dungeon.models.DamageType;
import dungeon.models.Enemy;
import dungeon.models.Position;

import javax.xml.bind.annotation.XmlAttribute;
import javax.xml.bind.annotation.adapters.XmlAdapter;
import javax.xml.bind.annotation.adapters.XmlJavaTypeAdapter;

public class EnemyAdapter extends XmlAdapter<EnemyAdapter, Enemy> {
  @XmlAttribute
  public int id;

  @XmlAttribute
  public int hitPoints;

  @XmlAttribute
  public String type;

  @XmlAttribute
  public int speed;

  @XmlJavaTypeAdapter(PositionAdapter.class)
  public Position position;

  @XmlAttribute
  public String moveStrategy;

  @XmlAttribute
  public String onDeath;

  @Override
  public Enemy unmarshal (EnemyAdapter adapter) throws Exception {
    return new Enemy(adapter.id, adapter.hitPoints, DamageType.valueOf(adapter.type), adapter.speed, adapter.position, Enemy.MoveStrategy.valueOf(adapter.moveStrategy), adapter.onDeath);
  }

  @Override
  public EnemyAdapter marshal (Enemy enemy) throws Exception {
    EnemyAdapter adapter = new EnemyAdapter();
    adapter.id = enemy.getId();
    adapter.hitPoints = enemy.getHitPoints();
    adapter.type = enemy.getType().name();
    adapter.speed = enemy.getSpeed();
    adapter.position = enemy.getPosition();
    adapter.moveStrategy = enemy.getMoveStrategy().toString();
    adapter.onDeath = enemy.getOnDeath();

    return adapter;
  }
}
