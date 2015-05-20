package thymio;

import java.util.ArrayList;
import java.util.List;

public class Thymio {
	private short vleft;
	private short vright;

	public static final double MAXSPEED = 500;
	public static final double SPEEDCOEFF = 2.93;
	public static final double BASE_WIDTH = 95;
	public static final double VROTATION = 100;
	public static final double STRAIGHTON = 150;

	private ThymioClient myClient;

	public Thymio(String host) {
		vleft = vright = 0;

		myClient = new ThymioClient(host);

	}

	public short getVLeft() {
		return vleft;
	}

	public synchronized void setVLeft(short v) {
		ArrayList<Short> data = new ArrayList<Short>();
		this.vleft = v;

		data.add(new Short(v));
		myClient.setVariable("motor.left.target", data);
	}

	public synchronized void setVRight(short v) {
		ArrayList<Short> data = new ArrayList<Short>();
		this.vright = v;

		data.add(new Short(v));
		myClient.setVariable("motor.right.target", data);
	}

	public synchronized void setSpeed(short vright, short vleft) {
		ArrayList<Short> data = new ArrayList<Short>();
		// Fehler bei ThymioServer - Vertauschen notwendig!
		this.vleft = vright;
		this.vright = vleft;

		data.add(new Short(vleft));
		data.add(new Short(vright));

		myClient.setSpeed(data);
	}

	public List<Short> getProxHorizontal() {
		return myClient.getVariable("prox.horizontal");
	}

	public List<Short> getGroundReflected() {
		return myClient.getVariable("prox.ground.reflected");
	}

	public synchronized void stopThymio() {
		setVLeft((short) 0);
		setVRight((short) 0);
	}

	public short getVRight() {
		return vright;
	}
}
