package dungeon.pulse;

import dungeon.messages.Message;

import java.io.Serializable;

/**
 * Objects of this class are continually sent by the PulseGenerator.
 *
 * By measuring the time delta between pulses you can implement things that depend on time like a projectile moving
 * through the room.
 */
public class Pulse implements Message, Serializable {

}
