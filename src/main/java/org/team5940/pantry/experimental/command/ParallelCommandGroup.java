package org.team5940.pantry.experimental.command;

import java.util.Collections;
import java.util.HashMap;
import java.util.Map;
import java.util.Set;

import edu.wpi.first.wpilibj.command.IllegalUseOfCommandException;

/**
 * A CommandGroup that runs a set of commands in parallel, ending when the last command ends.
 *
 * <p>As a rule, CommandGroups require the union of the requirements of their component commands.
 */
public class ParallelCommandGroup extends CommandGroupBase {

	//maps commands in this group to whether they are still running
	private final Map<Command, Boolean> m_commands = new HashMap<>();
	private boolean m_runWhenDisabled = true;

	/**
	 * Creates a new ParallelCommandGroup.  The given commands will be executed simultaneously.
	 * The command group will finish when the last command finishes.  If the CommandGroup is
	 * interrupted, only the commands that are still running will be interrupted.
	 *
	 * @param commands the commands to include in this group.
	 */
	public ParallelCommandGroup(Command... commands) {
		addCommands(commands);
	}

	@Override
	public void addCommands(Command... commands) {
		if (!Collections.disjoint(Set.of(commands), getGroupedCommands())) {
			throw new IllegalUseOfCommandException("Commands cannot be added to multiple CommandGroups");
		}

		registerGroupedCommands(commands);

		for (Command command : commands) {
			if (!Collections.disjoint(command.getRequirements(), m_requirements)) {
				throw new IllegalUseOfCommandException("Multiple commands in a parallel group cannot"
						+ "require the same subsystems");
			}
			m_commands.put(command, true);
			m_requirements.addAll(command.getRequirements());
			m_runWhenDisabled &= command.runsWhenDisabled();
		}
	}

	@Override
	public void initialize() {
		for (Map.Entry<Command, Boolean> commandRunning : m_commands.entrySet()) {
			commandRunning.getKey().initialize();
			commandRunning.setValue(true);
		}
	}

	@Override
	public void execute() {
		for (Map.Entry<Command, Boolean> commandRunning : m_commands.entrySet()) {
			if (!commandRunning.getValue()) {
				continue;
			}
			commandRunning.getKey().execute();
			if (commandRunning.getKey().isFinished()) {
				commandRunning.getKey().end();
				commandRunning.setValue(false);
			}
		}
	}

	@Override
	public void interrupted() {
		for (Map.Entry<Command, Boolean> commandRunning : m_commands.entrySet()) {
			if (commandRunning.getValue()) {
				commandRunning.getKey().interrupted();
			}
		}
	}

	@Override
	public boolean isFinished() {
		return !m_commands.values().contains(true);
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
