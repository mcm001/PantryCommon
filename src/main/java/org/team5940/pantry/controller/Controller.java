/*----------------------------------------------------------------------------*/
/* Copyright (c) 2018 FIRST. All Rights Reserved.                             */
/* Open Source Software - may be modified and shared by FRC teams. The code   */
/* must be accompanied by the FIRST BSD license file in the root directory of */
/* the project.                                                               */
/*----------------------------------------------------------------------------*/

package edu.wpi.first.wpilibj.experimental.controller;

/**
 * Base class for Controllers.
 *
 * <p>Common base class for controllers. Controllers run control loops, the most common are PID
 * controllers and their variants, but this includes anything that is controlling an actuator in a
 * separate thread.
 */
public abstract class Controller {
	private final double m_period;

	public Controller(double period) {
		m_period = period;
	}

	public double getPeriod() {
		return m_period;
	}

	/**
	 * Calculate the output based on the last updated reference and measurement values.
	 *
	 * @return The controller output.
	 */
	public abstract double calculate();

	/**
	 * Calculate the output based on the provided reference and measurement values.
	 *
	 * @param reference   The value of the reference.
	 * @param measurement The value of the process variable.
	 * @return The controller output.
	 */
	public abstract double calculate(double reference, double measurement);
}
