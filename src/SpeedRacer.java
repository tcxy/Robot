import lejos.hardware.BrickFinder;
import lejos.hardware.ev3.EV3;
import lejos.hardware.lcd.TextLCD;
import lejos.hardware.motor.Motor;
import lejos.hardware.port.SensorPort;
import lejos.hardware.sensor.EV3ColorSensor;
import lejos.hardware.sensor.SensorMode;
import lejos.robotics.navigation.MovePilot;
import lejos.robotics.RegulatedMotor;

public class SpeedRacer {
	
	private static SensorMode color;
	private static double lastColorValue = 0.0605f;
	private static RegulatedMotor leftMotor;
	private static RegulatedMotor rightMotor;
	private static int normalSpeed;
	private static final int MAX = 950;
	private static final int MIN = 650;
    final static EV3 ev3 = (EV3) BrickFinder.getLocal();
    private static TextLCD lcd = ev3.getTextLCD();
	
	private static final float AVERAGECOLOR = 0.0605f;
	private static final float DIFFERENCE = 0.1f;
	
	public static float average( float[] values ) {
		float sum = 0;
		for( float f : values ) { sum += f; }
		return sum / values.length;
	}
	
	public static void main(String [] args) {
		leftMotor = Motor.B;
		rightMotor = Motor.C;
		
		
		
	    EV3ColorSensor colorSensor = new EV3ColorSensor( SensorPort.S2 );
	    color = colorSensor.getRGBMode();
	    
		
	    long startTime = System.currentTimeMillis();
	    long duration;
	    

//	    leftMotor.setSpeed((int) (normalSpeed / 1.5));
//	    rightMotor.setSpeed((int) (normalSpeed / 1.5));
	    
	    int count = 0;
	    normalSpeed = MAX;
	    
	    do {
	    	
	    	if (count > 10) {
	    	    leftMotor.forward();
	    	    rightMotor.forward();
	    	}
	        duration = System.currentTimeMillis() - startTime;
	        
	        setSpeed();
	        
	        // TAKING READINGS FROM THE LIGHT SENSOR
	        if (getColorValue() - AVERAGECOLOR < 0) {
	        	lcd.drawString("turn left", 0, 0);
	        	turnLeft();
	        } else if (getColorValue() - AVERAGECOLOR > 0) {
	        	lcd.drawString("turn right", 0, 0);
	        	turnRight();
	        } else {
	        	lcd.drawString("forward", 0, 0);
	        	leftMotor.setSpeed(normalSpeed);
	        	rightMotor.setSpeed(normalSpeed);
	        }
	        
	        lcd.drawString("left speed" + leftMotor.getSpeed(), 0, 2);
	        lcd.drawString("right speed" + rightMotor.getSpeed(), 0, 3);
	        lcd.drawString(String.format("percentage: %f", percentageOfOffset()), 0, 4);

	        count++;
	    } while (duration < 60000);
	    
	    leftMotor.stop();
	    rightMotor.stop();
	}
	// 650{0.02,0.05,0.08,0.12,0.2,0.5}
	// 650{0.02,0.05,0.08,0.12,0.18,0.25,0.5}
	private static void turnLeft() {
		if (percentageOfOffset() < 0.02) {
			leftMotor.setSpeed((int) (normalSpeed));
		} else if (percentageOfOffset() < 0.05) {
			leftMotor.setSpeed((int) (normalSpeed / 1.05)); 
		} else if (percentageOfOffset() < 0.08) {
			leftMotor.setSpeed((int) (normalSpeed / 1.1)); 
		} else if (percentageOfOffset() < 0.12) {
			leftMotor.setSpeed((int) (normalSpeed / 1.2));
		} else if (percentageOfOffset() < 0.14) {
			leftMotor.setSpeed((int) (normalSpeed / 1.3));
		} else if (percentageOfOffset() < 0.2) {
			leftMotor.setSpeed((int) (normalSpeed / 1.55));
		} else if (percentageOfOffset() < 0.3) {
			leftMotor.setSpeed((int) (normalSpeed / 1.8));
		} else if (percentageOfOffset() < 0.4) {
			leftMotor.setSpeed((int) (normalSpeed / (percentageOfOffset() * 4.5)));
		} else if (percentageOfOffset() < 0.5) {
			leftMotor.setSpeed((int) (normalSpeed / (percentageOfOffset() * 6)));
		} else {
			leftMotor.setSpeed((int) (normalSpeed / (percentageOfOffset() * 8)));
		}
//		if (percentageOfOffset() < 0.3) {
//			leftMotor.setSpeed((int) (normalSpeed / (percentageOfOffset() * 3)));
//		} else {
//			leftMotor.setSpeed((int) (normalSpeed / (percentageOfOffset() * 8)));
//		}
		
		rightMotor.setSpeed(normalSpeed);
	}
	
	private static void turnRight() {
		if (percentageOfOffset() < 0.02) {
			rightMotor.setSpeed((int) (normalSpeed));
		} else if (percentageOfOffset() < 0.05) {
			rightMotor.setSpeed((int) (normalSpeed / 1.05)); 
		} else if (percentageOfOffset() < 0.08) {
			rightMotor.setSpeed((int) (normalSpeed / 1.1)); 
		} else if (percentageOfOffset() < 0.12) {
			rightMotor.setSpeed((int) (normalSpeed / 1.15));
		} else if (percentageOfOffset() < 0.2) { 
			rightMotor.setSpeed((int) (normalSpeed / 1.25));
		}else if (percentageOfOffset() < 0.4) {
			rightMotor.setSpeed((int) (normalSpeed / 1.3));
		} else if (percentageOfOffset() < 0.5) {
			rightMotor.setSpeed((int) (normalSpeed / 1.4));
		} else {
			rightMotor.setSpeed((int) (normalSpeed / 1.6));
		}
		leftMotor.setSpeed(normalSpeed);
	}
	
	private static void setSpeed() {
//		if (percentageOfOffset() < 0.12) {
//			normalSpeed = NORMAL;
//		} else if (percentageOfOffset() < 0.2) {
//			normalSpeed = (int) (NORMAL * 0.9);
//		} else if (percentageOfOffset() < 0.4) {
//			normalSpeed = (int) (NORMAL * 0.8);
//		} else if (percentageOfOffset() < 0.5) {
//			normalSpeed = (int) (NORMAL * 0.75);
//		} else {
//			normalSpeed = (int) (NORMAL * 0.7);
//		}
		
		normalSpeed = (int) (MAX - (1 - percentageOfOffset()) * (MAX - MIN));
	}
	
	private static double percentageOfOffset() {
		return Math.abs(getColorValue() - AVERAGECOLOR) / DIFFERENCE;
	}
	
	private static float getColorValue() {
		float[] colorSample = new float[color.sampleSize()];
		color.fetchSample(colorSample, 0);
		lcd.drawString("" + average(colorSample), 0, 1);
		return average(colorSample);
	}
	
}


class Pilot {
	MovePilot pilot;
	
	private static final double DISTANCE = 15;
	
	Pilot () {
		pilot = new MovePilot(49.6f, 137f, Motor.C, Motor.B);
	}
	
	public void goThrough() {
		pilot.travel(DISTANCE);
	}
	
	public void goAndTurn() {
		pilot.travelArc(300, DISTANCE, true);
	}
}
