package dungeon.load.adapters;

import dungeon.models.Position;

import javax.xml.bind.annotation.XmlAttribute;
import javax.xml.bind.annotation.adapters.XmlAdapter;

public class PositionAdapter extends XmlAdapter<PositionAdapter, Position> {
  @XmlAttribute
  public float x;

  @XmlAttribute
  public float y;

  @Override
  public Position unmarshal (PositionAdapter positionAdapter) throws Exception {
    return new Position(positionAdapter.x, positionAdapter.y);
  }

  @Override
  public PositionAdapter marshal (Position position) throws Exception {
    PositionAdapter adapter = new PositionAdapter();
    adapter.x = position.getX();
    adapter.y = position.getY();

    return adapter;
  }
}
