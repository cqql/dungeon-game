package dungeon.load.adapters;

import dungeon.models.Position;
import dungeon.models.SavePoint;

import javax.xml.bind.annotation.adapters.XmlAdapter;
import javax.xml.bind.annotation.adapters.XmlJavaTypeAdapter;

public class SavePointAdapter extends XmlAdapter<SavePointAdapter, SavePoint> {
  @XmlJavaTypeAdapter(PositionAdapter.class)
  public Position position;

  @Override
  public SavePoint unmarshal (SavePointAdapter adapter) throws Exception {
    return new SavePoint(adapter.position);
  }

  @Override
  public SavePointAdapter marshal (SavePoint savePoint) throws Exception {
    SavePointAdapter adapter = new SavePointAdapter();
    adapter.position = savePoint.getPosition();

    return adapter;
  }
}
