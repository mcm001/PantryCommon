package org.team5940.pantry.command;

import java.util.function.Supplier;

import org.ghrobotics.lib.debug.LiveDashboard;
import org.ghrobotics.lib.localization.Localization;
import org.ghrobotics.lib.mathematics.twodim.control.TrajectoryTracker;
import org.ghrobotics.lib.mathematics.twodim.geometry.Pose2d;
import org.ghrobotics.lib.mathematics.twodim.geometry.Pose2dWithCurvature;
import org.ghrobotics.lib.mathematics.twodim.trajectory.types.TimedEntry;
import org.ghrobotics.lib.mathematics.twodim.trajectory.types.TimedTrajectory;
import org.ghrobotics.lib.mathematics.twodim.trajectory.types.TrajectorySamplePoint;
import org.ghrobotics.lib.mathematics.units.Length;
import org.ghrobotics.lib.mathematics.units.TimeUnitsKt;
import org.ghrobotics.lib.subsystems.drive.TrajectoryTrackerDriveBase;
import org.ghrobotics.lib.subsystems.drive.TrajectoryTrackerOutput;
import org.team5940.pantry.experimental.command.SendableCommandBase;
import org.team5940.pantry.experimental.command.Subsystem;

import edu.wpi.first.wpilibj.Notifier;
import edu.wpi.first.wpilibj.Timer;

// @SuppressWarnings({"WeakerAccess", "unused"})
public class TrajectoryTrackerCommand extends SendableCommandBase {
	private TrajectoryTracker trajectoryTracker;
	private Supplier<TimedTrajectory<Pose2dWithCurvature>> trajectorySource;
	private boolean reset;
	private TrajectoryTrackerOutput output;
	// TODO make sure that this fabled namespace collision doesn't happen on Shuffleboard 
	Length mDesiredLeft;
	Length mDesiredRight;
	double mCurrentLeft;
	double mCurrentRight;

	// private NetworkTableEntry refVelEntry = Shuffleboard.getTab("Auto").getLayout("List", "Pathing info").add("Reference Velocity", 0).getEntry();
	// private NetworkTableEntry currentVelEntry = Shuffleboard.getTab("Auto").getLayout("List", "Pathing info").add("Current Velocity", 0).getEntry();

	Notifier mUpdateNotifier;
	private Supplier<TrajectoryTracker> trajectoryTrackerGetter;
    private TrajectoryTrackerDriveBase driveBase;
    private Supplier<Localization> localization;

	// public TrajectoryTrackerCommand(TrajectoryTrackerDriveBase driveBase, Supplier<TimedTrajectory<Pose2dWithCurvature>> trajectorySource, Subsystem toRequire) {
		// this(driveBase, trajectorySource, false, toRequire);
	// }

	// public TrajectoryTrackerCommand(TrajectoryTrackerDriveBase driveBase, Supplier<TimedTrajectory<Pose2dWithCurvature>> trajectorySource, boolean reset, Subsystem toRequire) {
		// this(driveBase, () -> {Robot.drivetrain.getTrajectoryTracker();}, trajectorySource, reset, toRequire);
	// }

    public TrajectoryTrackerCommand(TrajectoryTrackerDriveBase driveBase, Supplier<TrajectoryTracker> trajectoryTracker, Supplier<TimedTrajectory<Pose2dWithCurvature>> trajectorySource, Supplier<Localization> localization, boolean reset, Subsystem toRequire) {
		addRequirements(toRequire);
		this.driveBase = driveBase;
		this.trajectoryTrackerGetter = trajectoryTracker;
		this.trajectorySource = trajectorySource;
        this.reset = reset;
        this.localization = localization;
	}

	@Override
	public void initialize() {

		this.trajectoryTracker = trajectoryTrackerGetter.get();

		LiveDashboard.INSTANCE.setFollowingPath(false);

		trajectoryTracker.reset(this.trajectorySource.get());

		if (reset == true) {
			localization.get().reset(trajectorySource.get().getFirstState().getState().getPose());
		}

		// Logger.log("desired linear, real linear");

		LiveDashboard.INSTANCE.setFollowingPath(true);

		mUpdateNotifier = new Notifier(() -> {
			output = trajectoryTracker.nextState(driveBase.getRobotPosition(), TimeUnitsKt.getSecond(Timer.getFPGATimestamp()));

			TrajectorySamplePoint<TimedEntry<Pose2dWithCurvature>> referencePoint = trajectoryTracker.getReferencePoint();
			if (referencePoint != null) {
				Pose2d referencePose = referencePoint.getState().getState().getPose();

				LiveDashboard.INSTANCE.setPathX(referencePose.getTranslation().getX().getFeet());
				LiveDashboard.INSTANCE.setPathY(referencePose.getTranslation().getY().getFeet());
				LiveDashboard.INSTANCE.setPathHeading(referencePose.getRotation().getRadian());

			}

            driveBase.setOutput(output);

        });
        
		mUpdateNotifier.startPeriodic(0.01);
	}

	@Override
	public void execute() {

	}

	@Override
	public void end(boolean interrupted) {
		mUpdateNotifier.stop();
		driveBase.setOutput(new TrajectoryTrackerOutput(0, 0, 0, 0));
		LiveDashboard.INSTANCE.setFollowingPath(false);
	}

	@Override
	public boolean isFinished() {
		return trajectoryTracker.isFinished();
	}

	public TimedTrajectory<Pose2dWithCurvature> getTrajectory() {
		return this.trajectorySource.get();
	}

	// Translation2d pose = Translation2dKt.


}
