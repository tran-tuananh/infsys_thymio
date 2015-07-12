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
	private static final String IP = "192.168.43.170"; // Tutor-AJ: //
														// 192.168.43.100
	private static final String DELIMITER = ",";
	private static final String NEW_LINE = "\n";// Tutor-TK:
												// 172.20.10.13
	public Thymio myThymio;

	public MainController(String host) {
		myThymio = new Thymio(host);
	}

	public void run() {
		// Datei umbenennen
		File csv = new File("kante.csv");
		FileWriter fw;
		try {
			fw = new FileWriter(csv);
			// fw.write("Sensor,Value"+NEW_LINE); //Head for long format
			fw.write("s0,s1,s2,s3,s4" + NEW_LINE); // Head for wide format

			for (int i = 0; i < 750; i++) {
				ArrayList<Short> sensorArray = (ArrayList<Short>) myThymio
						.getProxHorizontal();
				System.out.println(sensorArray);
				for (int j = 0; j < sensorArray.size() - 2; j++) {
					//Values in long format
					// fw.append("s"+j+DELIMITER+sensorArray.get(j)+NEW_LINE);
					
					//Values in wide format
					if (j == sensorArray.size() - 3) {
						fw.append(sensorArray.get(j) + NEW_LINE);
					} else {
						fw.append(sensorArray.get(j) + DELIMITER);
					}
				}
			}
			fw.flush();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

	}

	public static void main(String[] args) {

		MainController mc = new MainController(IP);
		mc.run();

	}

}
