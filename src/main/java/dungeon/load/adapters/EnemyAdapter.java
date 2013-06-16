package dungeon.load.adapters;

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

  @XmlJavaTypeAdapter(PositionAdapter.class)
  public Position position;

  @XmlAttribute
  public String moveStrategy;

  @Override
  public Enemy unmarshal (EnemyAdapter adapter) throws Exception {
    return new Enemy(adapter.id, adapter.hitPoints, adapter.position, Enemy.MoveStrategy.valueOf(adapter.moveStrategy));
  }

  @Override
  public EnemyAdapter marshal (Enemy enemy) throws Exception {
    EnemyAdapter adapter = new EnemyAdapter();
    adapter.id = enemy.getId();
    adapter.hitPoints = enemy.getHitPoints();
    adapter.position = enemy.getPosition();
    adapter.moveStrategy = enemy.getMoveStrategy().toString();

    return adapter;
  }
}
