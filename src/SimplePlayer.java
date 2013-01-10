import java.awt.BorderLayout;
import java.awt.Component;
import java.io.IOException;
import java.net.URL;

import javax.media.CannotRealizeException;
import javax.media.Manager;
import javax.media.NoPlayerException;
import javax.media.Player;
import javax.swing.JFrame;

public class SimplePlayer extends JFrame {
	private final static long serialVersionUID = 15L;
	private Player player;

	public SimplePlayer(String title, URL url) {
		super(title);
		Manager.setHint(Manager.LIGHTWEIGHT_RENDERER, true);
		try {
			createPlayerAndShowComponents(url);
		}

		catch (CannotRealizeException ex) {
			ex.printStackTrace();
		} catch (NoPlayerException ex) {
			ex.printStackTrace();
		} catch (IOException ex) {
			ex.printStackTrace();
		}
	}

	private void createPlayerAndShowComponents(URL url) throws IOException,
			NoPlayerException, CannotRealizeException {
		player = Manager.createRealizedPlayer(url);

		Component comp;
		if ((comp = player.getVisualComponent()) != null)
			add(comp, BorderLayout.CENTER);
		if ((comp = player.getControlPanelComponent()) != null)
			add(comp, BorderLayout.SOUTH);

	}

	public void pausePlayer() {
		player.stop();

	}

	public void startPlayer() {
		this.setVisible(true);
		player.start();

	}

	public void closePlayer() {
		pausePlayer();
		this.setVisible(false);

	}
}