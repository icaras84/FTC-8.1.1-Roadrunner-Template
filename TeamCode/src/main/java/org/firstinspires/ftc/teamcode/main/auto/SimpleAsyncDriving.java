package org.firstinspires.ftc.teamcode.main.auto;

import com.acmerobotics.dashboard.config.Config;
import com.acmerobotics.roadrunner.geometry.Pose2d;
import com.acmerobotics.roadrunner.geometry.Vector2d;
import com.acmerobotics.roadrunner.trajectory.Trajectory;
import com.qualcomm.robotcore.eventloop.opmode.Autonomous;
import com.qualcomm.robotcore.eventloop.opmode.OpMode;

import org.firstinspires.ftc.teamcode.roadrunner.drive.SampleMecanumDrive;
import org.firstinspires.ftc.teamcode.util.general.misc.GeneralConstants;
import org.firstinspires.ftc.teamcode.util.statemachine.Flag;
import org.firstinspires.ftc.teamcode.util.statemachine.State;

/*
This OpMode is to demonstrate the ability of the state machine to drive the robot while also doing other things
such as printing telemetry, manipulating a servo/motor, or even queuing new states
 */
@Config
@Autonomous(group = GeneralConstants.SAMPLE_OPMODE)
public class SimpleAsyncDriving extends OpMode {

    //bad practice for inner classes as it clutters, but this is suppose to be specific to this example
    private static class DriveCmd implements State{

        public static SampleMecanumDrive drive;
        private Trajectory internalTrajectory;

        public DriveCmd(Trajectory trajectory){
            this.internalTrajectory = trajectory;
        }

        @Override
        public void init() {
            drive.followTrajectoryAsync(internalTrajectory);
        }

        @Override
        public void run() {
            drive.update();
        }

        @Override
        public void end() {}

        @Override
        public boolean isFinished() {
            return !drive.isBusy();
        }
    }

    private State.Sequence masterStateSequence;
    private State.Sequence driveSequence;
    private State.Sequence mainSequence;

    private SampleMecanumDrive drive;

    public static double DISTANCE = 10;
    public static double WAIT_TIME_SEC = 1;

    @Override
    public void init() {

        drive = new SampleMecanumDrive(hardwareMap);
        drive.setPoseEstimate(new Pose2d());
        DriveCmd.drive = drive;
        Flag<Flag.Basic> driveSyncFlag = new Flag<>(Flag.Basic.OFF);
        Flag<Flag.Basic> synced = new Flag<>(Flag.Basic.ON);

        mainSequence = (new State.Sequence()).addAll(
                new State() {
                    @Override
                    public void init() {
                        telemetry.addData("Drive Sequence Ended: ", "False");
                    }

                    @Override
                    public void run() {

                    }

                    @Override
                    public void end() {

                    }

                    @Override
                    public boolean isFinished() {
                        return true;
                    }
                },
                new State.WaitFor<>(driveSyncFlag, synced),
                new State() {
                    @Override
                    public void init() {
                        telemetry.addData("Drive Sequence Ended: ", "True");
                    }

                    @Override
                    public void run() {

                    }

                    @Override
                    public void end() {

                    }

                    @Override
                    public boolean isFinished() {
                        return false;
                    }
                }
        );


        driveSequence = (new State.Sequence()).addAll(
                new DriveCmd(
                        drive.trajectoryBuilder(drive.getPoseEstimate())
                                .lineToSplineHeading(new Pose2d(0, DISTANCE, GeneralConstants.DEG2RAD * 90d))
                                .lineTo(new Vector2d(0, 0))
                                .build()
                ),
                new State.Wait(WAIT_TIME_SEC * GeneralConstants.SEC2MS), //CAN be simplified to one trajectory, but for this instance: no
                new DriveCmd(
                        drive.trajectoryBuilder(drive.getPoseEstimate())
                                .lineToSplineHeading(new Pose2d(0, DISTANCE, GeneralConstants.DEG2RAD * 0d))
                                .lineTo(new Vector2d(0, 0))
                                .build()
                ),
                new State() {
                    @Override
                    public void init() {
                        driveSyncFlag.setCurrentState(Flag.Basic.ON);
                    }

                    @Override
                    public void run() {

                    }

                    @Override
                    public void end() {

                    }

                    @Override
                    public boolean isFinished() {
                        return false;
                    }
                });

        masterStateSequence = new State.Sequence();
        masterStateSequence.add(new State.AsyncGroup(mainSequence, driveSequence));
    }

    @Override
    public void loop() {
        masterStateSequence.run();
    }

    @Override
    public void stop(){
        masterStateSequence.end();
    }
}
