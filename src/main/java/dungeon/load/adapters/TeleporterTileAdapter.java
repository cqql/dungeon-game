package dungeon.load.adapters;

import dungeon.models.TeleporterTile;

import javax.xml.bind.annotation.XmlTransient;

public class TeleporterTileAdapter extends TileAdapter {
  @XmlTransient
  public boolean blocking;

  public TeleporterTile.Target target;
}
