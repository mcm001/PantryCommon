package org.team5940.pantry.exparimental.command;

/**
 * A base for subsystems that handles registration in the constructor, and provides a more intuitive
 * method for setting the default command.
 */
public class SubsystemBase implements Subsystem {

	private Command m_defaultCommand;

	public SubsystemBase() {
		CommandScheduler.getInstance().registerSubsystem(this);
	}

	/**
	 * Sets the default command for this subsystem.
	 *
	 * @param defaultCommand the default command to use
	 */
	@Override
	public void setDefaultCommand(Command defaultCommand) {
		m_defaultCommand = defaultCommand;
	}

	@Override
	public Command getDefaultCommand() {
		return m_defaultCommand;
	}
}
