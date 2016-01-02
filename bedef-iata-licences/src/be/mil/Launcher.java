package be.mil;

import java.io.File;
import java.io.IOException;

import javax.swing.SwingUtilities;
import javax.swing.UIManager;
import javax.swing.UIManager.LookAndFeelInfo;

import org.apache.log4j.BasicConfigurator;
import org.apache.log4j.Logger;

import be.mil.model.IataLogReader;



public class Launcher {
	
	private static final Logger LOGGER = Logger.getLogger(Launcher.class);
	private static final String pathLogFile = "/home/gautier/Documents/tfs-log.txt";
	private static final String pathExcelFile = "/home/gautier/Documents/iata.xlsx";

	
	public static void main(String[] args) {
		Launcher l = new Launcher();
		l.runApplication();
	}



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
		//loadConfiguration();
		launchFrame();

	}

	private void initLogger() {
		BasicConfigurator.configure();
	}	

	

	private void launchFrame() {
		LOGGER.debug("Launch frame");
		IataLogReader r = new IataLogReader(new File(pathLogFile), new File(pathExcelFile));
		try {
			r.readFile();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
//		frame = new MoverFrame(this);
//		SwingUtilities.invokeLater(new Runnable() {
//
//			@Override
//			public void run() {
////				frame.setExtendedState(JFrame.MAXIMIZED_BOTH); 
//				frame.setSize(1000, 600);
//				frame.setLocationRelativeTo(null);
//				frame.setVisible(true);
//			}
//		});
		
	}


}
