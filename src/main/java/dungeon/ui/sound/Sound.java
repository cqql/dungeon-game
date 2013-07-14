package dungeon.ui.sound;

import javax.sound.sampled.*;
import java.io.IOException;
import java.io.InputStream;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 * A sound that can be played/looped.
 */
class Sound {
  private static final Logger LOGGER = Logger.getLogger(Sound.class.getName());

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

    audioStream.close();
    fileStream.close();
  }

  public void play () {
    try {
      Clip clip = (Clip)AudioSystem.getLine(this.info);
      clip.open(this.format, this.data, 0, (int)this.size);
      clip.addLineListener(new CloseAfterPlayListener());
      clip.start();
    } catch (LineUnavailableException e) {
      LOGGER.log(Level.INFO, "Could allocate line. Probably to many open sounds.", e);
    }
  }

  /**
   * Closes the line after playing to redeem resources.
   */
  private static class CloseAfterPlayListener implements LineListener {
    @Override
    public void update (LineEvent lineEvent) {
      if (lineEvent.getType() == LineEvent.Type.STOP) {
        lineEvent.getLine().close();
      }
    }
  }
}
