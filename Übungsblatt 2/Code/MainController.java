package main;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;

import thymio.Thymio;

public class MainController {

	/**
	 * TODO: Hier die IP-Adresse des Raspberries eingeben!
	 */
	private static final String IP = "192.168.43.170";
	// Tutor-AJ: 192.168.43.100
	// Tutor-TK: 172.20.10.13
	private static final String DELIMITER = ";";
	private static final String NEW_LINE = "\n";

	public Thymio myThymio;

	public static void main(String[] args) {

		MainController mc = new MainController(IP);
		mc.run();

	}

	public MainController(String host) {
		myThymio = new Thymio(host);
	}

	public void run() {
		// Datei umbenennen
		File csv = new File("ecke.csv");
		FileWriter fw;
		try {
			fw = new FileWriter(csv);
			fw.write("s0;s1;s2;s3;s4" + NEW_LINE);

			for (int i = 0; i < 750; i++) {
				ArrayList<Short> sensorArray = (ArrayList<Short>) myThymio
						.getProxHorizontal();
				System.out.println(sensorArray);
				fw.append(sensorArray.get(0) + DELIMITER + sensorArray.get(1)
						+ DELIMITER + sensorArray.get(2) + DELIMITER
						+ sensorArray.get(3) + DELIMITER + sensorArray.get(4)
						+ DELIMITER + NEW_LINE);
			}
			fw.flush();
		} catch (IOException e) {
		}

	}

}
