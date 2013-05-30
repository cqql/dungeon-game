package dungeon.pulse;

import dungeon.messages.AbstractMailbox;
import dungeon.messages.Mailman;

/**
 * Generates a new pulse every so and so milliseconds and sends it to the mailman.
 *
 * @see Pulse
 */
public class PulseGenerator extends AbstractMailbox {
  /**
   * How long should the interval be between every pulse?
   */
  private static final int INTERVAL = 10;

  private final Mailman mailman;

  public PulseGenerator (Mailman mailman) {
    this.mailman = mailman;
  }

  @Override
  public void run () {
    while (this.isRunning()) {
      this.mailman.send(new Pulse());

      try {
        Thread.sleep(INTERVAL);
      } catch (InterruptedException e) {
        this.shutdown();
      }
    }
  }
}
