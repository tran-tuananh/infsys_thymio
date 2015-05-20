package main;

import thymio.Thymio;

public class MainController {

	/**
	 * TODO: Hier die IP-Adresse des Raspberries eingeben!
	 */
	private static final String IP = "192.168.43.113"; // Tutor-AJ:
														// 192.168.43.100
														// Tutor-TK:
														// 172.20.10.13
	public Thymio myThymio;

	public MainController(String host) {
		myThymio = new Thymio(host);
	}

	public void run() {

		/*
		 * TODO: Implementieren Sie hier die Aufgaben. Sie können für das
		 * zweite Aufgabenblatt entweder beide Aufgaben hier implementieren und
		 * für das Auführen jeweils ein Programm "auskommentieren"
		 */

		/*
		 * Aufgabe 1 (Abstand halten!)
		 */

		boolean isRunning = true;
		short turnSpeed = 250;
		short cruiseSpeed = 200;

		// Objekte nah am Sensor -> hoher Wert, Objekte weiter weg -> kleiner
		// Wert
		// (~St�rke der Reflektion)
		int maxFrontSensorValue = 2000;
		int minLeftSensorValue = 1300;
		int maxLeftSensorValue = 1700;

		myThymio.setSpeed(cruiseSpeed, cruiseSpeed);
		while (isRunning) {
			int sensorOuterLeftValue = myThymio.getProxHorizontal().get(0);
			int sensorMidFrontValue = myThymio.getProxHorizontal().get(2);
			boolean frontIsFree = sensorMidFrontValue < maxFrontSensorValue;

			if (sensorOuterLeftValue < minLeftSensorValue && frontIsFree) {
				myThymio.setVRight(turnSpeed);
			} else if (sensorOuterLeftValue > maxLeftSensorValue && frontIsFree) {
				myThymio.setVLeft(turnSpeed);
			} else if (frontIsFree) {
				myThymio.setSpeed(cruiseSpeed, cruiseSpeed);
			} else {
				myThymio.stopThymio();
				isRunning = false;
			}
		}

		// Aufgabe 2 (Cruise Control)
	}

	public static void main(String[] args) {

		MainController mc = new MainController(IP);
		mc.run();

	}

}
