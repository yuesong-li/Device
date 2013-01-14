import gnu.io.CommPortIdentifier;
import gnu.io.SerialPort;
import gnu.io.SerialPortEvent;
import gnu.io.SerialPortEventListener;

import java.awt.BorderLayout;
import java.awt.Frame;
import java.io.BufferedReader;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.PrintWriter;
import java.net.Socket;
import java.net.URL;
import java.util.Enumeration;
import java.util.Scanner;

import javax.sound.sampled.AudioInputStream;
import javax.sound.sampled.AudioSystem;
import javax.sound.sampled.Clip;
import javax.sound.sampled.LineUnavailableException;
import javax.sound.sampled.UnsupportedAudioFileException;
import javax.swing.ImageIcon;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JScrollPane;

public class A2E implements SerialPortEventListener {

	static SerialPort serialPort;
	/**
	 * The port we're normally going to use.
	 */
	private static final String PORT_NAMES[] = { "COM3" };
	/**
	 * Buffered input stream from the port
	 */
	private InputStream input;
	/**
	 * The output stream to the port
	 */
	private OutputStream output;
	/**
	 * Milliseconds to block while waiting for port open
	 */
	private static final int TIME_OUT = 2000;

	//
	public static String infoFromArduino;

	String deviceStates = "lightIn:off,lightOut:off,fan:off,heaterRoom:off,heaterLoft:off,tempRoom:15,tempLoft:15,bath:off,wash:off,media:off,coffee:off,alarm:off";
	static int portNumber = 7777;
	static String serverAddr = "194.47.32.216";
	static Socket client = null;
	private static String TAG = "A2E: ";

	BackgroundListener listener = null;

	OutputStream clientOut;
	PrintWriter pw;
	InputStream clientIn;
	BufferedReader br;
	SimplePlayer playerFrame = null;

	private static final int DATA_RATE = 9600;
	Scanner sc = new Scanner(System.in);

	public String read() throws IOException {
		int available = input.available();
		byte chunk[] = new byte[available];
		input.read(chunk, 0, available);
		String temp = new String(chunk);
		System.out.println("Info from arduino device: " + temp);
		return temp;
	}

	@SuppressWarnings("rawtypes")
	public void initialize() {

		CommPortIdentifier portId = null;
		Enumeration portEnum = CommPortIdentifier.getPortIdentifiers();

		// iterate through, looking for the port
		while (portEnum.hasMoreElements()) {
			CommPortIdentifier currPortId = (CommPortIdentifier) portEnum
					.nextElement();
			for (String portName : PORT_NAMES) {
				if (currPortId.getName().equals(portName)) {
					portId = currPortId;
					break;
				}
			}
		}

		if (portId == null) {
			System.out.println("Could not find COM port.");
			return;
		}

		try {
			// open serial port, and use class name for the appName.
			serialPort = (SerialPort) portId.open(this.getClass().getName(),
					TIME_OUT);

			// set port parameters
			serialPort.setSerialPortParams(DATA_RATE, SerialPort.DATABITS_8,
					SerialPort.STOPBITS_1, SerialPort.PARITY_NONE);

			// open the streams
			// input = serialPort.getInputStream();
			output = serialPort.getOutputStream();

			// add event listeners
			// serialPort.addEventListener(this);
			// serialPort.notifyOnDataAvailable(true);
		} catch (Exception e) {
			System.err.println(e.toString());
		}

	}

	/**
	 * This should be called when you stop using the port. This will prevent
	 * port locking on platforms like Linux.
	 */
	public synchronized void close() {
		if (serialPort != null) {
			serialPort.removeEventListener();
			serialPort.close();
		}
	}

	/**
	 * Handle an event on the serial port. Read the data and print it.
	 */
	public synchronized void serialEvent(SerialPortEvent oEvent) {

		if (oEvent.getEventType() == SerialPortEvent.DATA_AVAILABLE) {
		}
	}

	// Media Player
	@SuppressWarnings("deprecation")
	public void openFile() {
		// File chooser
		/*
		 * JFileChooser chooser = new JFileChooser(); int option =
		 * chooser.showOpenDialog(null); if (option ==
		 * JFileChooser.APPROVE_OPTION)
		 */
		{
			// Hardcoded file
			File video = new File(
					"C:\\Users\\Server\\Desktop\\simulated_device\\pirates.avi");
			try {
				URL url = video.toURL();
				createAndShowGui(url);
			} catch (Exception e) {
				e.printStackTrace();
			}
		}
	}

	public void commandsAndGui() throws UnsupportedAudioFileException,
			IOException, InterruptedException, LineUnavailableException {
		openFile();
		JFrame frame = new JFrame("Project GUI");
		frame.setExtendedState(Frame.MAXIMIZED_BOTH);
		frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

		// Coffee Machine
		// Original
		ImageIcon loadCoffee = new ImageIcon(
				"C:\\Users\\Server\\Desktop\\simulated_device\\original.jpg");
		JLabel coffeeImage = new JLabel(loadCoffee);
		JScrollPane defaultCoffeImage = new JScrollPane(coffeeImage);

		// Working
		ImageIcon loadCoffeeWorking = new ImageIcon(
				"C:\\Users\\Server\\Desktop\\simulated_device\\working.gif");
		JLabel coffeeImageWorking = new JLabel(loadCoffeeWorking);
		JScrollPane workingCoffeeImage = new JScrollPane(coffeeImageWorking);
		File workingcoffeeSound = new File(
				"C:\\Users\\Server\\Desktop\\simulated_device\\make.WAV");
		AudioInputStream coffeeWorkingSound = AudioSystem
				.getAudioInputStream(workingcoffeeSound);

		// Done
		ImageIcon loadCoffeeDone = new ImageIcon(
				"C:\\Users\\Server\\Desktop\\simulated_device\\done.jpg");
		JLabel coffeeImageDone = new JLabel(loadCoffeeDone);
		JScrollPane doneCoffeeImage = new JScrollPane(coffeeImageDone);
		File doneCoffeeSound = new File(
				"C:\\Users\\Server\\Desktop\\simulated_device\\done.WAV");
		AudioInputStream coffeeDoneSound = AudioSystem
				.getAudioInputStream(doneCoffeeSound);

		// Bathtub
		// Off (Default)
		ImageIcon loadBathtub = new ImageIcon(
				"C:\\Users\\Server\\Desktop\\simulated_device\\off.png");
		JLabel bathtubImage = new JLabel(loadBathtub);
		JScrollPane bathtubEmpty = new JScrollPane(bathtubImage);
		// On (Full)
		ImageIcon loadBathtubFull = new ImageIcon(
				"C:\\Users\\Server\\Desktop\\simulated_device\\on.png");
		JLabel bathtubFullImage = new JLabel(loadBathtubFull);
		JScrollPane bathtubFull = new JScrollPane(bathtubFullImage);

		// Washing Machine
		// Wash Ready
		ImageIcon loadWashingMachine = new ImageIcon(
				"C:\\Users\\Server\\Desktop\\simulated_device\\ready.gif");
		JLabel washingMachineImage = new JLabel(loadWashingMachine);
		JScrollPane washingMachineReady = new JScrollPane(washingMachineImage);
		// Washing
		ImageIcon loadWashingMachineWashing = new ImageIcon(
				"C:\\Users\\Server\\Desktop\\simulated_device\\wash.gif");
		JLabel imageWashingMachineWashing = new JLabel(
				loadWashingMachineWashing);
		JScrollPane washingMachineWashing = new JScrollPane(
				imageWashingMachineWashing);
		File loadWashingMachineWashingSound = new File(
				"C:\\Users\\Server\\Desktop\\simulated_device\\wash.wav");
		AudioInputStream washingSound = AudioSystem
				.getAudioInputStream(loadWashingMachineWashingSound);
		// Dry
		ImageIcon loadWashingMachineDry = new ImageIcon(
				"C:\\Users\\Server\\Desktop\\simulated_device\\dry.gif");
		JLabel imageWashingMachineDry = new JLabel(loadWashingMachineDry);
		JScrollPane washingMachineDry = new JScrollPane(imageWashingMachineDry);
		File loadWashingMachineDrySound = new File(
				"C:\\Users\\Server\\Desktop\\simulated_device\\dry.wav");
		AudioInputStream drySound = AudioSystem
				.getAudioInputStream(loadWashingMachineDrySound);

		// Default GUI images
		frame.add(washingMachineReady, BorderLayout.LINE_START);
		frame.add(bathtubEmpty, BorderLayout.CENTER);
		frame.add(defaultCoffeImage, BorderLayout.LINE_END);
		frame.setVisible(true);

		clientOut = client.getOutputStream();
		pw = new PrintWriter(clientOut, true);
		clientIn = client.getInputStream();
		br = new BufferedReader(new InputStreamReader(clientIn));
		pw.println(deviceStates);

		// create the background thread listening to alarms

		while (true) {
			if (br.ready()) {
				String command = br.readLine();
				System.out.println("A2E: Received command :" + command);

				// out light
				if (command.equals("lightOut:on")) {

					output.write(1);
					output.flush();
					pw.println("lightOut:on");
				}
				if (command.equals("lightOut:off")) {
					output.write(2);
					output.flush();
					pw.println("lightOut:off");
				}
				// room light
				if (command.equals("lightIn:on")) {
					output.write(3);
					output.flush();
					pw.println("lightIn:on");
				}
				if (command.equals("lightIn:off")) {
					output.write(4);
					output.flush();
					pw.println("lightIn:off");
				}

				// room heat
				if (command.equals("heaterRoom:on")) {
					output.write(7);
					output.flush();
					pw.println("heaterRoom:on");
				}
				if (command.equals("heaterRoom:off")) {

					output.write(8);
					output.flush();
					pw.println("heaterRoom:off");
				}
				// loft heat
				if (command.equals("heaterLoft:on")) {

					output.write(9);
					output.flush();
					pw.println("heaterLoft:on");
				}
				if (command.equals("heaterLoft:off")) {

					output.write(10);
					output.flush();
					pw.println("heaterLoft:off");
				}

				// fan
				if (command.equals("fan:on")) {

					output.write(17);
					output.flush();
					pw.println("fan:on");
				}
				if (command.equals("fan:off")) {

					output.write(18);
					output.flush();
					pw.println("fan:off");
				}

				// room temp
				// if (command.equals("tempRoom")) {
				// output.write(21);
				// output.flush();
				// // String rTemp;
				// long startTime = System.currentTimeMillis();
				// while ((System.currentTimeMillis() - startTime) / 1000 < 0.3)
				// {
				// }
				// pw.println("tempRoom:" + infoFromArduino.trim());
				// }
				// // loft temp
				// if (command.equals("tempLoft")) {
				// output.write(22);
				// output.flush();
				// long startTime = System.currentTimeMillis();
				// while ((System.currentTimeMillis() - startTime) / 1000 < 0.3)
				// {
				// }
				// pw.println("tempLoft:" + infoFromArduino.trim());
				// }
				
				if (command.equals("temp")) {
					output.write(21);
					output.flush();
					// String rTemp;
					long startTime = System.currentTimeMillis();
					while ((System.currentTimeMillis() - startTime) / 1000 < 0.3) {
					}
					String temp = "tempRoom:" + infoFromArduino.trim();
					//pw.println("tempRoom:" + infoFromArduino.trim());
					
					output.write(22);
					output.flush();
					startTime = System.currentTimeMillis();
					while ((System.currentTimeMillis() - startTime) / 1000 < 0.3) {
					}
					
					temp += ",tempLoft:" + infoFromArduino.trim();
					pw.println(temp);					
				}
				// coffee machine

				if (command.equalsIgnoreCase("wash:on")) {
					Clip clip = AudioSystem.getClip();
					clip.stop();
					clip.setFramePosition(0);
					frame.add(washingMachineWashing, BorderLayout.LINE_START);
					washingMachineWashing.setVisible(true);
					frame.setVisible(true);
					washingMachineReady.setVisible(false);
					washingMachineDry.setVisible(false);

					clip.open(washingSound);
					clip.start();
					pw.println("wash:on");

				}
				if (command.equalsIgnoreCase("wash:off")) {

					frame.add(washingMachineReady, BorderLayout.LINE_START);
					washingMachineReady.setVisible(true);
					frame.setVisible(true);
					washingMachineWashing.setVisible(false);
					washingMachineDry.setVisible(false);
					pw.println("wash:off");
				}
				if (command.equalsIgnoreCase("wash:dry")) {

					Clip clip = AudioSystem.getClip();
					clip.stop();
					clip.setFramePosition(0);
					frame.add(washingMachineDry, BorderLayout.LINE_START);
					washingMachineDry.setVisible(true);
					frame.setVisible(true);
					washingMachineReady.setVisible(false);
					washingMachineWashing.setVisible(false);

					clip.open(drySound);
					clip.start();

				}
				if (command.equalsIgnoreCase("bath:on")) {
					pw.println("bath:on");
					output.write(11);
					output.flush();

					frame.add(bathtubFull, BorderLayout.CENTER);
					Thread.sleep(2000);
					bathtubFull.setVisible(true);
					bathtubEmpty.setVisible(false);
					frame.setVisible(true);
					output.write(12);
					output.flush();
					output.write(13);
					output.flush();

				}
				if (command.equalsIgnoreCase("bath:off")) {
					pw.println("bath:off");
					frame.add(bathtubEmpty, BorderLayout.CENTER);
					bathtubEmpty.setVisible(true);
					bathtubFull.setVisible(false);
					frame.setVisible(true);
					output.write(14);
					output.flush();
					pw.println("wash:dry");
				}
				if (command.equalsIgnoreCase("coffee:on")) {

					frame.add(workingCoffeeImage, BorderLayout.LINE_END);
					Clip clip = AudioSystem.getClip();
					clip.stop();
					clip.setFramePosition(0);
					clip.open(coffeeWorkingSound);
					clip.start();
					pw.println("coffee:on");
					Thread.sleep(2000);

					workingCoffeeImage.setVisible(true);
					defaultCoffeImage.setVisible(false);
					Clip clip2 = AudioSystem.getClip();
					clip2.flush();
					clip2.open(coffeeDoneSound);
					clip2.start();
					frame.setVisible(true);
					Thread.sleep(2000);

					frame.add(doneCoffeeImage, BorderLayout.LINE_END);
					doneCoffeeImage.setVisible(true);
					workingCoffeeImage.setVisible(false);
					defaultCoffeImage.setVisible(false);
					frame.setVisible(true);

				}
				if (command.equalsIgnoreCase("coffee:off")) {
					pw.println("coffee:off");
					frame.add(defaultCoffeImage, BorderLayout.LINE_END);
					defaultCoffeImage.setVisible(true);
					workingCoffeeImage.setVisible(false);
					doneCoffeeImage.setVisible(false);
					frame.setVisible(true);
				}
				if (command.equalsIgnoreCase("media:pause")) {
					pw.println("media:pause");
					playerFrame.pausePlayer();
				}
				if (command.equalsIgnoreCase("media:on")) {
					pw.println("media:on");
					playerFrame.startPlayer();

				}
				if (command.equalsIgnoreCase("media:off")) {
					pw.println("media:off");
					playerFrame.closePlayer();

				}
				// output.write(21);
				// output.flush();
				// // String rTemp;
				// long startTime = System.currentTimeMillis();
				// while ((System.currentTimeMillis() - startTime) / 1000 < 0.3)
				// {
				// }
				// pw.println("tempRoom:" + infoFromArduino.trim());
				//
				// output.write(22);
				// output.flush();
				// startTime = System.currentTimeMillis();
				// while ((System.currentTimeMillis() - startTime) / 1000 < 0.3)
				// {
				// }
				// pw.println("tempLoft:" + infoFromArduino.trim());

			} else {
				if (listener == null) {
					listener = new BackgroundListener(serialPort, client);
					listener.start();
				}

			}
		}
	}

	@SuppressWarnings("static-access")
	public void createAndShowGui(URL url) throws IOException {
		playerFrame = new SimplePlayer("Simple Media Player", url);
		playerFrame.setDefaultCloseOperation(playerFrame.EXIT_ON_CLOSE);
		playerFrame.setSize(400, 300);
		playerFrame.setVisible(true);
	}

	public static void main(String[] args) throws Exception {
		try {
			client = new Socket(serverAddr, portNumber);
			client.setKeepAlive(true);
			client.setSoTimeout(0);
			System.out.println(TAG + "socket kept alive: "
					+ client.getKeepAlive());
			System.out.println("Client-socket is created "
					+ client.getRemoteSocketAddress());
		} catch (IOException e) {
			System.out.println();
			e.printStackTrace();
		}
		A2E main = new A2E();
		main.initialize();

		// // create the background thread listening to alarms
		// BackgroundListener listener = new BackgroundListener(serialPort);
		// listener.run();

		// main.read();
		main.commandsAndGui();

	}
}