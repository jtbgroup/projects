package be.jtb.vds.documentmover.ui;

import java.awt.BorderLayout;
import java.awt.Component;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.event.ActionEvent;
import java.io.File;
import java.io.IOException;

import javax.swing.AbstractAction;
import javax.swing.Box;
import javax.swing.JButton;
import javax.swing.JDialog;
import javax.swing.JFileChooser;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;

import be.jtb.vds.documentmover.ui.event.EventManager;
import be.jtb.vds.documentmover.ui.event.EventProducer;
import be.jtb.vds.documentmover.utils.ConfigurationHelper;

public class PreferenceDialog extends JDialog {

	

	private PreferencesPanel preferencesPanel;

	private PreferenceDialog() {
		initComponent();
	}

	private void initComponent() {
		JPanel panel = new JPanel(new BorderLayout());
		
		 preferencesPanel = new PreferencesPanel();
		preferencesPanel.loadValues();
		panel.add(preferencesPanel, BorderLayout.CENTER);
		
		panel.add(createButtonsPanel(), BorderLayout.SOUTH);
		
		this.getContentPane().add(panel);
	}


		private Component createButtonsPanel() {
			JButton cancelButton = new JButton(new AbstractAction("cancel") {

				@Override
				public void actionPerformed(ActionEvent arg0) {
					PreferenceDialog.this.dispose();
				}
			});

			JButton saveButton = new JButton(new AbstractAction("save") {

				@Override
				public void actionPerformed(ActionEvent arg0) {
					try {
						preferencesPanel.saveConfiguration();
					} catch (IOException e) {
						JOptionPane.showMessageDialog(
								PreferenceDialog.this,
								"Error while saving the configuration:/r/n"
										+ e.getMessage());
					} finally {
						PreferenceDialog.this.dispose();
					}
				}
			});

			JPanel p = new JPanel(new FlowLayout(FlowLayout.RIGHT));
			p.add(cancelButton);
			p.add(saveButton);
			return p;
	}

	public static int showDialog() {
		PreferenceDialog dlg = new PreferenceDialog();
		dlg.setModal(true);
		dlg.setSize(800, 400);
		dlg.setLocationRelativeTo(null);
		dlg.setVisible(true);
		return 0;
	}
}
