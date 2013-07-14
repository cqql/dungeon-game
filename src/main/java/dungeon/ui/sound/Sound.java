package dungeon.ui.sound;

import javax.sound.sampled.*;
import java.io.IOException;
import java.io.InputStream;

/**
 * A sound that can be played/looped.
 */
class Sound {
  private final long size;

  private final AudioFormat format;

  private final DataLine.Info info;

  private final byte[] data;

  public Sound (String resource) throws IOException, UnsupportedAudioFileException {
    InputStream fileStream = this.getClass().getClassLoader().getResourceAsStream(resource);
    AudioInputStream audioStream = AudioSystem.getAudioInputStream(fileStream);

    this.format = audioStream.getFormat();
    this.size = this.format.getFrameSize() * audioStream.getFrameLength();
    this.info = new DataLine.Info(Clip.class, audioStream.getFormat());
    this.data = new byte[(int)this.size];

    audioStream.read(this.data, 0, (int)this.size);
  }

  public void play () {
    try {
      Clip clip = (Clip)AudioSystem.getLine(this.info);
      clip.open(this.format, this.data, 0, (int)this.size);
      clip.start();
    } catch (LineUnavailableException e) {
      e.printStackTrace();  //To change body of catch statement use File | Settings | File Templates.
    }
  }
}
