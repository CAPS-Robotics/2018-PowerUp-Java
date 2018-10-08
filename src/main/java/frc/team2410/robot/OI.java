package frc.team2410.robot;

import edu.wpi.first.wpilibj.Joystick;
import edu.wpi.first.wpilibj.XboxController;

public class OI {
	boolean[] canPress = new boolean[12];
	boolean[] canPress1 = new boolean[12];
	public Joystick joy1;
	XboxController buttonPad;
	
	public OI() {
		for (int i = 0; i < canPress.length; i++) {
			canPress[i] = true;
		}
		joy1 = new Joystick(0);
		buttonPad = new XboxController(1);
	}
	
	void pollButtons() {
		if (joy1.getRawButton(2)) {
			Robot.drivetrain.returnWheelsToZero();
		}
		
		if (joy1.getRawButton(1)) {
			if (canPress[0]) {
				Robot.arm.toggleIntake();
			}
			canPress[0] = false;
		}
		else {
			canPress[0] = true;
		}
		
		if (buttonPad.getRawButton(11)) {
			if (canPress1[10]) {
				Robot.arm.toggleKick();
			}
			canPress1[10] = false;
		} else {
			canPress1[10] = true;
		}
		
		if (buttonPad.getRawButton(4)) {
			if (canPress1[3]) {
				Robot.drivetrain.shift();
			}
			canPress1[3] = false;
		} else {
			canPress1[3] = true;
		}
		
		if(joy1.getRawButton(6)) {
			Robot.gyro.resetHeading(0);
		}
		
		if (this.getStick() == 0) {
			if (Math.abs(Robot.arm.getPosition() - Robot.arm.targetPos) < 1){
				Robot.arm.armMotor.set(0);
			}
		} else {
			Robot.arm.armMotor.set(-this.getStick());
			Robot.arm.targetPos = Robot.arm.getPosition();
		}
		if (buttonPad.getRawButton(1)) {
			if(canPress1[0]) {
				Robot.vision.setCamera(1-Robot.vision.camera);
				canPress1[0] = false;
			}
		} else {
			canPress1[0] = true;
		}
		
		//Scale
		if (buttonPad.getPOV(0) == 0) {
			Robot.arm.moveTo(70);
		}
		
		//Intake
		if (buttonPad.getPOV(0) == 180) {
			Robot.arm.moveTo(0);
		}
		
		//Switch
		if (buttonPad.getPOV(0) == 90) {
			Robot.arm.moveTo(28);
		}
		
		//Reset
		if (buttonPad.getPOV(0) == 270) {
			Robot.arm.setPosition(0);
			Robot.arm.targetPos = 0;
		}
	}
	
	public double getX() {
		return this.applyDeadzone(joy1.getRawAxis(0), 0.15, 1);
	}
	
	public double getY() {
		return this.applyDeadzone(-joy1.getRawAxis(1), 0.15, 1);
	}
	
	public double getTwist() {
		return this.applyDeadzone(joy1.getRawAxis(2), 0.70, 1) / 2;
	}
	
	public double getSlider() {
		return joy1.getRawAxis(3);
	}
	
	double getAnalogY(int stickNum){
		return this.applyDeadzone(buttonPad.getRawAxis(stickNum * 2 + 1), 0.50, 1);
	}
	
	double applyDeadzone(double val, double deadzone, double maxval) {
		if (Math.abs(val) <= deadzone) {
			return 0;
		}
		
		double sign = val / Math.abs(val);
		val = sign * maxval * (Math.abs(val) - deadzone) / (maxval - deadzone);
		return val;
	}
	
	public double getStick() {
		return this.applyDeadzone(buttonPad.getRawAxis(3), 0.50, 1);
	}
}
