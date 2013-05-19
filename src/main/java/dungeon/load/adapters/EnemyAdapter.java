package dungeon.load.adapters;

import dungeon.models.Enemy;
import dungeon.models.Position;

import javax.xml.bind.annotation.adapters.XmlAdapter;
import javax.xml.bind.annotation.adapters.XmlJavaTypeAdapter;

public class EnemyAdapter extends XmlAdapter<EnemyAdapter, Enemy> {
  @XmlJavaTypeAdapter(PositionAdapter.class)
  public Position position;

  @Override
  public Enemy unmarshal (EnemyAdapter enemyAdapter) throws Exception {
    return new Enemy(enemyAdapter.position);
  }

  @Override
  public EnemyAdapter marshal (Enemy enemy) throws Exception {
    EnemyAdapter adapter = new EnemyAdapter();
    adapter.position = enemy.getPosition();

    return adapter;
  }
}
