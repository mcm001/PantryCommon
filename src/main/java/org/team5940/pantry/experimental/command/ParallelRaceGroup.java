package org.team5940.pantry.experimental.command;

import java.util.Collections;
import java.util.HashSet;
import java.util.Set;

import edu.wpi.first.wpilibj.command.IllegalUseOfCommandException;

/**
 * A CommandGroup that runs a set of commands in parallel, ending when any one of the commands ends
 * and interrupting all the others.
 *
 * <p>As a rule, CommandGroups require the union of the requirements of their component commands.
 */
public class ParallelRaceGroup extends CommandGroupBase {

	private final Set<Command> m_commands = new HashSet<>();
	private boolean m_runWhenDisabled = true;
	private boolean m_finished = true;

	/**
	 * Creates a new ParallelCommandRace.  The given commands will be executed simultaneously, and
	 * will "race to the finish" - the first command to finish ends the entire command, with all other
	 * commands being interrupted.
	 *
	 * @param commands the commands to include in this group.
	 */
	public ParallelRaceGroup(Command... commands) {
		addCommands(commands);
	}

	@Override
	public void addCommands(Command... commands) {
		if (!Collections.disjoint(Set.of(commands), getGroupedCommands())) {
			throw new IllegalUseOfCommandException("Commands cannot be added to multiple CommandGroups");
		}

		if (!m_finished) {
			throw new IllegalUseOfCommandException(
					"Commands cannot be added to a CommandGroup while the group is running");
		}

		registerGroupedCommands(commands);

		for (Command command : commands) {
			if (!Collections.disjoint(command.getRequirements(), m_requirements)) {
				throw new IllegalUseOfCommandException("Multiple commands in a parallel group cannot"
						+ " require the same subsystems");
			}
			m_commands.add(command);
			m_requirements.addAll(command.getRequirements());
			m_runWhenDisabled &= command.runsWhenDisabled();
		}
	}

	@Override
	public void initialize() {
		m_finished = false;
		for (Command command : m_commands) {
			command.initialize();
		}
	}

	@Override
	public void execute() {
		for (Command command : m_commands) {
			command.execute();
			if (command.isFinished()) {
				m_finished = true;
				command.end(false);
			}
		}
	}

	@Override
	public void end(boolean interrupted) {
		for (Command command : m_commands) {
			if (!command.isFinished()) {
				command.end(true);
			}
		}
	}

	@Override
	public boolean isFinished() {
		return m_finished;
	}

	@Override
	public Set<Subsystem> getRequirements() {
		return m_requirements;
	}

	@Override
	public boolean runsWhenDisabled() {
		return m_runWhenDisabled;
	}
}
