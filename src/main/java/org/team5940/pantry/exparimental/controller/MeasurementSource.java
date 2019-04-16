/*----------------------------------------------------------------------------*/
/* Copyright (c) 2018 FIRST. All Rights Reserved.                             */
/* Open Source Software - may be modified and shared by FRC teams. The code   */
/* must be accompanied by the FIRST BSD license file in the root directory of */
/* the project.                                                               */
/*----------------------------------------------------------------------------*/

package org.team5940.pantry.exparimental.controller;

/**
 * This interface is a generic measurement source for controllers.
 */
@FunctionalInterface
public interface MeasurementSource {
	double getMeasurement();
}
