import lejos.hardware.motor.Motor;
import lejos.robotics.navigation.DifferentialPilot;

/**
 * Trace Sqaure
 * @author Wei Du
 *
 */
public class SquareTacer {
	DifferentialPilot pilot; // The pilot represent the robot
	
	public void drawSquare(float length) {
		for (int i = 0; i < 4; i++) {
			pilot.travel(length);
			pilot.rotate(90);
		}
	}
	
	public static void main(String[] args) {
		SquareTacer sq = new SquareTacer();
		sq.pilot = new DifferentialPilot(2.25f, 5.5f, Motor.C, Motor.B);
		sq.drawSquare(10);
	}
}
