package be.vds.jtb.fcos;

import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;
import java.io.IOException;

import javax.swing.JFrame;

public class Launch {

	public static void main(String[] args) {
		DocumentMover m = new DocumentMover();
		MoverFrame f = new MoverFrame(m);
		f.addPropertyChangeListener(new PropertyChangeListener() {

			@Override
			public void propertyChange(PropertyChangeEvent evt) {
				if (evt.getPropertyName().equals("windowClosed")) {
					endApplication(0);
				}
			}
		});

		try {
			f.loadDefaultProperties();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		f.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
		f.setSize(600, 600);
		f.setLocationRelativeTo(null);
		f.setVisible(true);
	}
	
	private static void endApplication(int stopCode) {
		System.exit(stopCode);
	}
}
