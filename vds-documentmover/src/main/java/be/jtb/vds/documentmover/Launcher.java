package be.jtb.vds.documentmover;

import java.io.File;
import java.io.IOException;

import javax.swing.SwingUtilities;
import javax.swing.UIManager;
import javax.swing.UIManager.LookAndFeelInfo;

import org.apache.log4j.BasicConfigurator;
import org.apache.log4j.Logger;

import be.jtb.vds.documentmover.ui.EventManager;
import be.jtb.vds.documentmover.ui.EventProducer;
import be.jtb.vds.documentmover.ui.MoverFrame;
import be.jtb.vds.documentmover.utils.ConfigurationHelper;
import be.jtb.vds.documentmover.utils.MessageHelper;

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

	private void intializeConfigValues() {
		EventProducer producer = new EventProducer() {
			
			@Override
			public String getName() {
				return "launcher";
			}
			
			@Override
			public String getIdentifier() {
				return "launcher";
			}
		};

		EventManager.getInstance().notifyPreferencesChanged(producer);
		EventManager.getInstance().notifySourceFileSelected(producer,
				new File(ConfigurationHelper.getInstance().getSourceFolder()));
		EventManager.getInstance().notifyDestinationFileSelected(producer,
				new File(ConfigurationHelper.getInstance().getDestinationFolder()));
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
				frame.setSize(1000, 600);
				frame.setLocationRelativeTo(null);
				intializeConfigValues();
				MessageHelper.getInstance().setDisplayContainer(frame);
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
