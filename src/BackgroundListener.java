import gnu.io.SerialPort;
import gnu.io.SerialPortEvent;
import gnu.io.SerialPortEventListener;

import java.io.IOException;
import java.io.InputStream;
import java.io.PrintWriter;
import java.net.Socket;

public class BackgroundListener extends Thread implements
		SerialPortEventListener {

	private InputStream input;

	private PrintWriter pw;
	private Socket client;
	private SerialPort serialPort;
	private final String TAG = "LISTENER: ";

	// private static final String PORT_NAMES[] = { "COM3" };
	// private static final int DATA_RATE = 9600;
	// private static final int TIME_OUT = 2000;

	public BackgroundListener(SerialPort serialPort, Socket clientSocket) {
		this.serialPort = serialPort;
		this.client = clientSocket;
	}

	public void run() {
		System.out.println(TAG + "before initialization");
		initialize();
		int available = 0;
		byte[] chunk = null;
		String info = "";

		try {
			while (true) {
				available = input.available();
				chunk = new byte[available];
				input.read(chunk, 0, available);
				info = new String(chunk);
				if (info.trim().equals("") || info.equals(" ")) {
				} else {
					handleInfo(info);
				}
			}
		} catch (IOException ioe) {
			ioe.printStackTrace();
		}
	}

	public void handleInfo(String info) {
		if (info.contains("ad")) {// alarm:door
			System.out.println(TAG + "ALARM: " + info.trim());
			pw.println("alarm:door");
		} else if (info.contains("af")) {
			System.out.println(TAG + "ALARM: " + info.trim());
			pw.println("alarm:fire");
		} else if (info.contains("az")) {
			System.out.println(TAG + "ALARM: " + info.trim());
			pw.println("alarm:off");
		} else {// other information, like requested temperature
			System.out.println(TAG + info.trim());
			A2E.infoFromArduino = info.trim();
		}
	}

	public void initialize() {
		try {
			pw = new PrintWriter(client.getOutputStream(), true);
			input = serialPort.getInputStream();
			System.out.println(TAG + "input stream gotten");
			// add event listeners
			serialPort.addEventListener(this);
			serialPort.notifyOnDataAvailable(true);
		} catch (Exception e) {
			e.printStackTrace();
		}

	}

	@Override
	public void serialEvent(SerialPortEvent arg0) {
	}

}