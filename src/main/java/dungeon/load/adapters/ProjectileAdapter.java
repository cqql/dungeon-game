package dungeon.load.adapters;

import dungeon.models.Position;
import dungeon.models.Projectile;
import dungeon.util.Vector;

import javax.xml.bind.annotation.XmlAttribute;
import javax.xml.bind.annotation.adapters.XmlAdapter;
import javax.xml.bind.annotation.adapters.XmlJavaTypeAdapter;

public class ProjectileAdapter extends XmlAdapter<ProjectileAdapter, Projectile> {
  @XmlAttribute
  public int id;

  @XmlJavaTypeAdapter(PositionAdapter.class)
  public Position position;

  public Vector direction;

  @XmlAttribute
  public int damage;

  @Override
  public Projectile unmarshal (ProjectileAdapter adapter) throws Exception {
    return new Projectile(adapter.id, null, adapter.position, adapter.direction, adapter.damage);
  }

  @Override
  public ProjectileAdapter marshal (Projectile projectile) throws Exception {
    ProjectileAdapter adapter = new ProjectileAdapter();
    adapter.id = projectile.getId();
    adapter.position = projectile.getPosition();
    adapter.direction = projectile.getVelocity();
    adapter.damage = projectile.getDamage();

    return adapter;
  }
}
