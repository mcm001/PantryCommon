package org.team5940.pantry.exparimental.command;

import java.util.Set;

/**
 * Schedules the given commands when this command is initialized, and ends when all the commands are
 * no longer scheduled.  Useful for forking off from CommandGroups.  If this command is interrupted,
 * it will cancel all of the commands.
 */
public class ProxyScheduleCommand extends SendableCommandBase {
	private final Set<Command> m_toSchedule;
	private boolean m_finished;

	/**
	 * Creates a new ProxyScheduleCommand that schedules the given commands when initialized,
	 * and ends when they are all no longer scheduled.
	 *
	 * @param toSchedule the commands to schedule
	 */
	public ProxyScheduleCommand(Command... toSchedule) {
		m_toSchedule = Set.of(toSchedule);
	}

	@Override
	public void initialize() {
		m_finished = false;
		for (Command command : m_toSchedule) {
			command.schedule();
		}
	}

	@Override
	public void end(boolean interrupted) {
		if (interrupted) {
			for (Command command : m_toSchedule) {
				command.cancel();
			}
		}
	}

	@Override
	public void execute() {
		for (Command command : m_toSchedule) {
			m_finished &= !command.isScheduled();
		}
	}

	@Override
	public boolean isFinished() {
		return m_finished;
	}
}
