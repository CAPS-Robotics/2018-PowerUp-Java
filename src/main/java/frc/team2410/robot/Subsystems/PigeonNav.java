package frc.team2410.robot.Subsystems;

import edu.wpi.first.wpilibj.PIDSource;
import edu.wpi.first.wpilibj.PIDSourceType;
import frc.team2410.robot.RobotMap;
import com.ctre.phoenix.motorcontrol.can.TalonSRX;
import com.ctre.phoenix.sensors.PigeonIMU;

public class PigeonNav implements PIDSource
{
	private PigeonIMU gyro;
	private double[] ypr;
	private int offset;

	public PigeonNav() {
		this.gyro = new PigeonIMU(new TalonSRX(RobotMap.PIGEON_IMU_SRX));
		this.ypr = new double[3];
		this.resetHeading(0);
	}

	public double pidGet() {
		return this.getHeading();
	}

	//completely useless in every way but PIDSource is bad
	public void setPIDSourceType(PIDSourceType pidSource) {}

	public double getHeading() {
		double angle = (((this.gyro.getFusedHeading() - offset) % 360.0); // Wraps angle between 0-360
		angle = (angle + 360.0) % 360.0; // Changes negative values to equivalent postive values (ex. -90 -> 270 degrees)
		return angle <= 180 ? angle : angle - 360;  // Changes >180 Degrees to Neg Equivalent (ex. 270 -> -90) and Returns it
	}

	public double getAngularRate() {
		this.gyro.getRawGyro(this.ypr);
		return ypr[0];
	}

	public void resetHeading(int head) {
		this.gyro.setFusedHeading(0, 20);
		this.offset = head;
	}

	public PIDSourceType getPIDSourceType() {
		return PIDSourceType.kDisplacement;
	}
}
