package helper;

import java.util.ArrayList;

public class DataCalc {

	private static final String SEP = ",";
	private static final String NEWLINE = "\n";

	private static final double SIN_ANGLE_SIDE = 0.766044443119;
	private static final double COS_ANGLE_SIDE = 0.642787609687;
	private static final double SIN_ANGLE_MIDDLE = 0.939692620786;
	private static final double COS_ANGLE_MIDDLE = 0.342020143326;
	private static final double SIN_ANGLE = 1.0;
	private static final double COS_ANGLE = 0.0;

	private Writer wRaw;
	private Writer wDisc;
	private String id;

	private int[] sensors;
	private double[] sensors_cm;
	private ArrayList<Integer> sensorLeft;
	private ArrayList<Integer> sensorRight;

	private double dist = 0;
	private double angle = 0;

	private static final double INFINITY_SENSOR = -1;

	public DataCalc(Writer wRaw, Writer wDisc) {
		this.wRaw = wRaw;
		this.wDisc = wDisc;

		sensors = new int[5];
		sensors_cm = new double[5];
		sensorLeft = new ArrayList<Integer>();
		sensorRight = new ArrayList<Integer>();

	}

	public void run(String line, String id) {
		this.id = id;
		String[] split = line.split(",");
		sensors[0] = Integer.parseInt(split[0]);
		sensors[1] = Integer.parseInt(split[1]);
		sensors[2] = Integer.parseInt(split[2]);
		sensors[3] = Integer.parseInt(split[3]);
		sensors[4] = Integer.parseInt(split[4]);

		calculateCentimeter(sensors);

		String dataLeft = calculateData(sensorLeft, 0);

		String dataRight = calculateData(sensorRight, 1);

		discreteValues(id, dataLeft, dataRight);
		wRaw.writeLine(id + SEP + dataLeft + SEP + dataRight + NEWLINE);

		sensorLeft.clear();
		sensorRight.clear();

	}

	/**
	 * TODO: Discrete Angles & Distances!
	 * 
	 * @param id
	 *            : ECKE or KANTE or SPITZE
	 * @param dataLeft
	 *            : 18,4.84913160170322
	 * @param dataRight
	 *            58,9.615813981493954
	 */
	private void discreteValues(String id, String dataLeft, String dataRight) {
		String discAngleLeft = "discrete Angle Left";
		String discAngleRight = "discrete Angle Right";
		String discDistLeft = "discrete Dist Left";
		String discDistRight = "discrete Dist Righ";

		// Berechnung Abstand
		double distLeft = Double.parseDouble(dataLeft.split(",")[1]);
		double distRight = Double.parseDouble(dataRight.split(",")[1]);
		int angleLeft = Integer.parseInt(dataLeft.split(",")[0]);
		int angleRight = Integer.parseInt(dataRight.split(",")[0]);

		if (distLeft <= distRight) {
			discDistLeft = String.valueOf(distLeft / distRight);
			discDistRight = "1";
		} else {
			discDistLeft = "1";
			discDistRight = String.valueOf(distRight / distLeft);
		}

		// Berechnung Winkel
		Double[] eckeLeftQuantile = { 2.0, 3.0, 3.0, 3.0, 3.0 };
		Double[] eckeRightQuantile = { 77.00, 80.00, 81.00, 82.00, 84.00 };
		Double[] kanteLeftQuantile = { 82.0, 82.0, 82.0, 82.0, 82.0 };
		Double[] kanteRightQuantile = { 82.0, 82.0, 82.0, 82.0, 82.0 };
		Double[] frontalLeftQuantile = { 49.00, 49.00, 49.00, 50.00, 50.00 };
		Double[] frontalRightQuantile = { 45.00, 46.00, 46.00, 46.00, 46.00 };

		switch (id) {
		case "ECKE":
			for (int i = 0; i < eckeLeftQuantile.length - 1; i++) {
				int value = i + 1;
				if (angleLeft >= eckeLeftQuantile[i]
						&& angleLeft <= eckeLeftQuantile[(value)]) {
					discAngleLeft = String.valueOf(value);
					break;
				}
			}
			for (int i = 0; i < eckeRightQuantile.length - 1; i++) {
				int value = i + 1;
				if (angleRight >= eckeRightQuantile[i]
						&& angleRight <= eckeRightQuantile[(value)]) {
					discAngleRight = String.valueOf(value);
					break;
				}
			}
			break;

		case "KANTE":
			for (int i = 0; i < kanteLeftQuantile.length - 1; i++) {
				int value = i + 1;
				if (angleLeft >= kanteLeftQuantile[i]
						&& angleLeft <= kanteLeftQuantile[(value)]) {
					discAngleLeft = String.valueOf(value);
					break;
				}
			}
			for (int i = 0; i < kanteRightQuantile.length - 1; i++) {
				int value = i + 1;
				if (angleRight >= kanteRightQuantile[i]
						&& angleRight <= kanteRightQuantile[(value)]) {
					discAngleRight = String.valueOf(value);
					break;
				}
			}
			break;

		case "FRONTAL":
			for (int i = 0; i < frontalLeftQuantile.length - 1; i++) {
				int value = i + 1;
				if (angleLeft >= frontalLeftQuantile[i]
						&& angleLeft <= frontalLeftQuantile[(value)]) {
					discAngleLeft = String.valueOf(value);
					break;
				}
			}
			for (int i = 0; i < frontalRightQuantile.length - 1; i++) {
				int value = i + 1;
				if (angleRight >= frontalRightQuantile[i]
						&& angleRight <= frontalRightQuantile[(value)]) {
					discAngleRight = String.valueOf(value);
					break;
				}
			}
			break;
		}

		String discLeft = discAngleLeft + SEP + discDistLeft;
		String discRight = discAngleRight + SEP + discDistRight;
		wDisc.writeLine(id + SEP + discLeft + SEP + discRight + NEWLINE);
	}

	private String calculateData(ArrayList<Integer> sensorSide, int side) {
		int k = 0;
		int h = 0;
		if (side == 0) {
			k = 0;
			h = 1;
		} else if (side == 1) {
			h = 1;
			k = 2;
		}

		if (sensorSide.size() == 0) {
			dist = Double.POSITIVE_INFINITY;
			angle = Double.NaN;
		} else if (sensorSide.size() == 1) {
			dist = sensors_cm[sensorSide.get(0)];
			angle = getLot(sensorSide.get(0));
		} else if (sensorSide.size() > 2) {
			if (sensors_cm[sensorSide.get(h)] != INFINITY_SENSOR
					&& sensors_cm[sensorSide.get(k)] != INFINITY_SENSOR) {
				Double[] vector = calculateLine(sensors_cm[sensorSide.get(h)],
						sensors_cm[sensorSide.get(k)], sensorSide.get(h),
						sensorSide.get(k));
				angle = 180 - Math.round(calculateAngle(vector));
			}

			dist = Math.min(sensors_cm[sensorSide.get(h)],
					sensors_cm[sensorSide.get(k)]);

		}

		String res = (int) angle + SEP + dist;
		return res;
	}

	private double getLot(int value) {
		switch (value) {
		case 0:
			return (50);
		case 1:
			return (70);
		case 2:
			return (90);
		case 3:
			return (70);
		case 4:
			return (50);
		}
		return 0;
	}

	private Double[] calculateLine(double sensor_a, double sensor_b, int first,
			int second) {
		Double[] v1 = { 0.0, 0.0 };

		double sensor_a_x = getCoord(first, sensor_a, "x");
		double sensor_a_y = getCoord(first, sensor_a, "y");
		double sensor_b_x = getCoord(second, sensor_b, "x");
		double sensor_b_y = getCoord(second, sensor_b, "y");

		v1[0] = sensor_a_x - sensor_b_x;
		v1[1] = sensor_a_y - sensor_b_y;

		return v1;

	}

	private double calculateAngle(Double[] v1) {
		Double[] v_unit = { 0.0, 1.0 };
		double zähler = (v1[0] * v_unit[0]) + (v1[1] * v_unit[1]);
		double nenner = Math.sqrt((Math.pow(v1[0], 2) + Math.pow(v1[1], 2))
				* (Math.pow(v_unit[0], 2) + Math.pow(v_unit[1], 2)));
		double temp = Math.abs(zähler / nenner);
		double ang = 180 - Math.toDegrees(Math.acos(temp));
		return ang;
	}

	private double getCoord(int value, double sensorValue, String key) {
		switch (value) {
		case 0:
			if (key == "x")
				return ((-1 * COS_ANGLE_SIDE) * sensorValue);
			if (key == "y")
				return (SIN_ANGLE_SIDE * sensorValue);
			break;
		case 1:
			if (key == "x")
				return ((-1 * COS_ANGLE_MIDDLE) * sensorValue);
			if (key == "y")
				return (SIN_ANGLE_MIDDLE * sensorValue);
			break;
		case 2:
			if (key == "x")
				return (COS_ANGLE * sensorValue);
			if (key == "y")
				return (SIN_ANGLE * sensorValue);
			break;
		case 3:
			if (key == "x")
				return (COS_ANGLE_MIDDLE * sensorValue);
			if (key == "y")
				return (SIN_ANGLE_MIDDLE * sensorValue);
			break;
		case 4:
			if (key == "x")
				return (COS_ANGLE_SIDE * sensorValue);
			if (key == "y")
				return (SIN_ANGLE_SIDE * sensorValue);
			break;
		}
		return -1;
	}

	private void calculateCentimeter(int[] sensors2) {
		for (int i = 0; i < 5; i++) {
			if (sensors[i] > 1200) {
				sensors_cm[i] = ((-1.829e-01)
						+ (Math.pow(sensors[i], 1) * 2.833e-02)
						+ (Math.pow(sensors[i], 2) * -1.935e-05)
						+ (Math.pow(sensors[i], 3) * 4.701e-09) + (Math.pow(
						sensors[i], 4) * -3.937e-13));
			} else {
				sensors_cm[i] = INFINITY_SENSOR;
			}

			if (i < 3 && sensors_cm[i] != INFINITY_SENSOR) {
				sensorLeft.add(i);
			}
			if (i >= 2 && sensors_cm[i] != INFINITY_SENSOR) {
				sensorRight.add(i);
			}
		}

	}

}
