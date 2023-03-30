package org.firstinspires.ftc.teamcode.main.testing;

import com.acmerobotics.dashboard.telemetry.TelemetryPacket;
import com.qualcomm.hardware.lynx.LynxModule;
import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;
import com.qualcomm.robotcore.eventloop.opmode.TeleOp;
import com.qualcomm.robotcore.hardware.DcMotorEx;

/**
 * This class is primarily used to test if encoders on the motors work properly and report correctly to roadrunner systems
 * <br>To use it, run the OpMode and push the robot around and you will see what would roadrunner sees as encoder positions
 */
@TeleOp(group = "test")
public class WheelMotorEncoderTest extends LinearOpMode {

    private static DcMotorEx leftFront, leftRear, rightFront, rightRear;

    @Override
    public void runOpMode() throws InterruptedException {

        leftFront = hardwareMap.get(DcMotorEx.class, "leftFront");
        leftRear = hardwareMap.get(DcMotorEx.class, "leftRear");
        rightRear = hardwareMap.get(DcMotorEx.class, "rightRear");
        rightFront = hardwareMap.get(DcMotorEx.class, "rightFront");

        //enable BULK READ to mimic how roadrunner reads the values
        for (LynxModule module : hardwareMap.getAll(LynxModule.class)) {
            module.setBulkCachingMode(LynxModule.BulkCachingMode.AUTO);
        }

        waitForStart();
        while (opModeIsActive() && !isStopRequested()) {
            telemetry.addData("Left Front Motor Encoder Position: ", leftFront.getCurrentPosition());
            telemetry.addData("Left Rear Motor Encoder Position: ", leftFront.getCurrentPosition());
            telemetry.addData("Right Front Motor Encoder Position: ", leftFront.getCurrentPosition());
            telemetry.addData("Right Rear Motor Encoder Position: ", leftFront.getCurrentPosition());
            telemetry.update();
        }
    }
}
