package org.firstinspires.ftc.teamcode.main.auto;

import com.acmerobotics.dashboard.config.Config;
import com.acmerobotics.roadrunner.geometry.Pose2d;
import com.acmerobotics.roadrunner.geometry.Vector2d;
import com.acmerobotics.roadrunner.trajectory.Trajectory;
import com.qualcomm.robotcore.eventloop.opmode.Autonomous;
import com.qualcomm.robotcore.eventloop.opmode.OpMode;

import org.firstinspires.ftc.teamcode.roadrunner.drive.SampleMecanumDrive;
import org.firstinspires.ftc.teamcode.util.general.misc.GeneralConstants;

@Config
@Autonomous(group = GeneralConstants.SAMPLE_OPMODE)
public class SimpleMecanumPath extends OpMode {

    private SampleMecanumDrive drive;
    public static double DISTANCE = 10;

    @Override
    public void init() {
        drive = new SampleMecanumDrive(hardwareMap);
    }

    @Override
    public void start(){ //this method gets called once just before loop()
        Trajectory traj1 = drive.trajectoryBuilder(new Pose2d()) //make a triangle trajectory with heading changes
                .lineTo(new Vector2d(DISTANCE, DISTANCE)) //drive diagonally to (10, 10)
                .lineToLinearHeading(new Pose2d(DISTANCE, 0, GeneralConstants.DEG2RAD * 180d)) //drive down to (10, 0) while rotating 180 degrees
                .lineToLinearHeading(new Pose2d(0, 0, 0)) //drive back to origin and align heading with 0 while doing so
                .build();

        drive.setPoseEstimate(new Pose2d()); //necessary
        drive.followTrajectoryAsync(traj1); //non-blocking version of followTrajectory()
    }

    @Override
    public void loop() {

    }
}
