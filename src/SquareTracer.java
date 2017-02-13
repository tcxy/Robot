import lejos.hardware.motor.Motor;
import lejos.robotics.navigation.DifferentialPilot;

/**
 * Trace Square
 * @author Wei Du
 *
 */
public class SquareTacer {
	DifferentialPilot pilot; // The pilot represent the robot
	
	public void drawSquare(float length) {
		for (int i = 0; i < 4; i++) {
			pilot.travel(length);
			pilot.rotate(180);
			//pilot.steer(turnRate, angle, immediateReturn);
			//pilot.rotateLeft();
		}
	}
	
	public static void main(String[] args) {
		SquareTacer sq = new SquareTacer();
		sq.pilot = new DifferentialPilot(1.95f, 3.62f, Motor.C, Motor.B);
		sq.drawSquare(12);
	}
}
