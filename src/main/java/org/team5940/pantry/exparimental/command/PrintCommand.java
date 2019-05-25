package org.team5940.pantry.exparimental.command;

/**
 * A command that prints a string when initialized.
 */
public class PrintCommand extends InstantCommand {
	/**
	 * Creates a new a PrintCommand.
	 *
	 * @param message the message to print
	 */
	public PrintCommand(String message) {
		super(() -> System.out.println(message));
	}

	@Override
	public boolean runsWhenDisabled() {
		return true;
	}
}
