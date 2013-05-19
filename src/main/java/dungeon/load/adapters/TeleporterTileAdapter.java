package dungeon.load.adapters;

import dungeon.models.TeleporterTile;

import javax.xml.bind.annotation.XmlAttribute;
import javax.xml.bind.annotation.XmlTransient;
import javax.xml.bind.annotation.adapters.XmlAdapter;
import javax.xml.bind.annotation.adapters.XmlJavaTypeAdapter;

public class TeleporterTileAdapter extends TileAdapter {
  @XmlTransient
  public boolean blocking;

  @XmlJavaTypeAdapter(TargetAdapter.class)
  public TeleporterTile.Target target;

  public static class TargetAdapter extends XmlAdapter<TargetAdapter, TeleporterTile.Target> {
    @XmlAttribute
    public String roomId;

    @XmlAttribute
    public int x;

    @XmlAttribute
    public int y;

    @Override
    public TeleporterTile.Target unmarshal (TargetAdapter targetAdapter) throws Exception {
      return null;  //To change body of implemented methods use File | Settings | File Templates.
    }

    @Override
    public TargetAdapter marshal (TeleporterTile.Target target) throws Exception {
      TargetAdapter adapter = new TargetAdapter();
      adapter.roomId = target.getRoomId();
      adapter.x = target.getX();
      adapter.y = target.getY();

      return adapter;
    }
  }
}
