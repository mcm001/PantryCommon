package org.team5940.pantry.experimental.command;

import edu.wpi.first.wpilibj.Timer;

/**
 * A command that does nothing but ends after a specified match time.  Useful for CommandGroups.
 */
public class WaitUntilCommand extends SendableCommandBase {

	private final double m_time;

	/**
	 * Creates a new WaitUntilCommand.  This command will do nothing, and will end when the current
	 * match time exceeds the specified time.
	 *
	 * @param seconds the match time at which to end the command.
	 */
	public WaitUntilCommand(double seconds) {
		m_time = seconds;
		setName(m_name + ": " + seconds + " seconds");
	}

	@Override
	public boolean isFinished() {
		return Timer.getMatchTime() - m_time >= 0;
	}
}
