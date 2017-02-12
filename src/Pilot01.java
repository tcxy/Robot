import lejos.hardware.motor.Motor;
import lejos.hardware.motor.NXTRegulatedMotor;
import lejos.robotics.navigation.DifferentialPilot;
import lejos.utility.Delay;

public class Pilot01 {
	
	NXTRegulatedMotor right = Motor.B;
	NXTRegulatedMotor left = Motor.C;
	
	public static void main(String[] args) {
		Pilot01 pl = new Pilot01();
		pl.left.setSpeed(100);
		pl.right.setSpeed(100);
		Delay.msDelay(60);
		pl.left.setSpeed(50);
		pl.right.setSpeed(-50);
		Delay.msDelay(60);
		pl.left.setSpeed(100);
		pl.right.setSpeed(100);
		Delay.msDelay(60);
	}
	
}
