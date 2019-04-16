package org.team5940.pantry.exparimental.command;

import edu.wpi.first.wpilibj.Timer;

/**
 * A command that does nothing but takes a specified amount of time to finish.  Useful for
 * CommandGroups.  Can also be subclassed to make a command with an internal timer.
 */
public class WaitCommand extends SendableCommandBase {
	protected Timer m_timer = new Timer();
	final double m_duration;

	/**
	 * Creates a new WaitCommand.  This command will do nothing, and end after the specified duration.
	 *
	 * @param seconds the time to wait, in seconds
	 */
	public WaitCommand(double seconds) {
		m_duration = seconds;
		setName(m_name + ": " + seconds + " seconds");
	}

	@Override
	public void initialize() {
		m_timer.reset();
		m_timer.start();
	}

	@Override
	public void execute() {

	}

	@Override
	public void end(boolean interrupted) {
		m_timer.stop();
	}

	@Override
	public boolean isFinished() {
		return m_timer.hasPeriodPassed(m_duration);
	}

	@Override
	public boolean runsWhenDisabled() {
		return true;
	}
}
