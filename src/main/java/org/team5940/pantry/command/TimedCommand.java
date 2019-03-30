package org.team5940.pantry.command;

import org.team5940.pantry.experimental.command.Command;
import org.team5940.pantry.experimental.command.SendableCommandBase;

import edu.wpi.first.wpilibj.Timer;

public class TimedCommand extends SendableCommandBase {

    protected final Command m_command;
    private double time;
    private double startTime = -1;

	/**
	 * Creates a new PerpetualCommand.  Will run another command in perpetuity, ignoring that
	 * command's end conditions, unless this command itself is interrupted.
	 *
	 * @param command the command to run perpetually
	 */
	public TimedCommand(double time, Command command) {
        m_command = command;
        this.time = time;
		m_requirements.addAll(command.getRequirements());
	}

	@Override
	public void initialize() {
        startTime = Timer.getFPGATimestamp();
		m_command.initialize();
	}

	@Override
	public void execute() {
		m_command.execute();
	}

    @Override
    public boolean isFinished() {
        return startTime > -1 && (Timer.getFPGATimestamp() - startTime) > time;
    }

	@Override
	public void end(boolean interrupted) {
		m_command.end(interrupted);
	}

}