package dungeon.load.adapters;

import dungeon.util.Vector;

import javax.xml.bind.annotation.adapters.XmlAdapter;

public class VectorAdapter extends XmlAdapter<VectorAdapter, Vector> {
  public double x;

  public double y;

  @Override
  public Vector unmarshal (VectorAdapter adapter) throws Exception {
    return new Vector(adapter.x, adapter.y);
  }

  @Override
  public VectorAdapter marshal (Vector vector) throws Exception {
    VectorAdapter adapter = new VectorAdapter();
    adapter.x = vector.getX();
    adapter.y = vector.getY();

    return adapter;
  }
}
