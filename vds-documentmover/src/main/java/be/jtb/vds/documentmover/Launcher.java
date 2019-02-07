package be.jtb.vds.documentmover;

import java.io.IOException;

import javax.swing.SwingUtilities;
import javax.swing.UIManager;
import javax.swing.UIManager.LookAndFeelInfo;

import org.apache.log4j.BasicConfigurator;
import org.apache.log4j.Logger;

import be.jtb.vds.documentmover.ui.MoverFrame;

public class Launcher implements ApplicationManager {
	private static final Logger LOGGER = Logger.getLogger(Launcher.class);

	public static void main(String[] args) {
		Launcher l = new Launcher();
		l.runApplication();
	}

	private MoverFrame frame;

	private void setLAF() {
		try {
			for (LookAndFeelInfo info : UIManager.getInstalledLookAndFeels()) {
				if ("Nimbus".equals(info.getName())) {
					UIManager.setLookAndFeel(info.getClassName());
					break;
				}
			}
		} catch (Exception e) {
			LOGGER.error(e.getMessage());
		}
	}

	private void runApplication() {
		setLAF();
		initLogger();
		loadConfiguration();
		launchFrame();

	}

	private void initLogger() {
		BasicConfigurator.configure();
	}

	private void loadConfiguration() {
		try {
			ConfigurationHelper.getInstance().loadConfig();
		} catch (IOException e) {
			LOGGER.error(e.getMessage());
		}
	}

	private void launchFrame() {
		LOGGER.debug("Launch frame");
		frame = new MoverFrame(this);
		SwingUtilities.invokeLater(new Runnable() {

			@Override
			public void run() {
//				frame.setExtendedState(JFrame.MAXIMIZED_BOTH); 
				frame.setSize(1300, 800);
				frame.setLocationRelativeTo(null);
				frame.setVisible(true);
			}
		});
		
	}

	@Override
	public void closeApplication() {
		LOGGER.info("Closing...");
		frame.dispose();
		System.exit(0);
	}
}
