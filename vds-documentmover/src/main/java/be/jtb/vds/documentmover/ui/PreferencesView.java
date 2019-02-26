package be.jtb.vds.documentmover.ui;

import java.awt.BorderLayout;
import java.awt.Component;
import java.awt.FlowLayout;
import java.awt.event.ActionEvent;
import java.io.IOException;

import javax.swing.AbstractAction;
import javax.swing.JButton;
import javax.swing.JOptionPane;
import javax.swing.JPanel;

public class PreferencesView extends View{

	private PreferencesPanel preferencesPanel;

	public PreferencesView(String identifier, String name) {
		super(identifier, name);
		initializeComponents();
	}

	private void initializeComponents() {
		this.setLayout(new BorderLayout());
		
	 preferencesPanel = new PreferencesPanel();
		preferencesPanel.loadValues();
		this.add(preferencesPanel, BorderLayout.CENTER);
		
		this.add(createButtonsPanel(), BorderLayout.SOUTH);
	}
	
	private Component createButtonsPanel() {

		JButton saveButton = new JButton(new AbstractAction("save") {

			@Override
			public void actionPerformed(ActionEvent arg0) {
				try {
					preferencesPanel.saveConfiguration();
				} catch (IOException e) {
					JOptionPane.showMessageDialog(
							null,
							"Error while saving the configuration:/r/n"
									+ e.getMessage());
				}
			}
		});

		JPanel p = new JPanel(new FlowLayout(FlowLayout.RIGHT));
		p.add(saveButton);
		return p;
}

}
