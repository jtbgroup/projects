package be.vds.documentmover.ui;

import java.awt.BorderLayout;
import java.awt.Component;
import java.awt.FlowLayout;
import java.awt.event.ActionEvent;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;

import javax.swing.AbstractAction;
import javax.swing.JButton;
import javax.swing.JDialog;
import javax.swing.JPanel;
import javax.swing.JTextField;

public class PatternEditDialog extends JDialog {
	public static final int ERROR = -1;
	public static final int CANCEL = 0;
	public static final int SAVE = 1;

	private int action;
	private JTextField patternTf;

	public PatternEditDialog() {
		initializeComponent();
	}

	private void initializeComponent() {
		this.setModal(true);
		this.setDefaultCloseOperation(JDialog.DISPOSE_ON_CLOSE);

		this.setAlwaysOnTop(true);

		this.addKeyListener(new KeyAdapter() {
			@Override
			public void keyReleased(KeyEvent e) {
				if (e.getKeyCode() == KeyEvent.VK_ESCAPE)
					dispose();
			}
		});

		JPanel contentPane = new JPanel(new BorderLayout());
		contentPane.add(createCentralPane(), BorderLayout.CENTER);
		contentPane.add(createButtonsPane(), BorderLayout.SOUTH);

		this.getContentPane().add(contentPane);

	}

	private Component createCentralPane() {
		patternTf = new JTextField();
		
		 JPanel p = new JPanel(new BorderLayout());
		 p.add(patternTf, BorderLayout.CENTER);
		 return p;
	}

	private Component createButtonsPane() {
		JButton cancelButton = new JButton(new AbstractAction("Cancel") {

			@Override
			public void actionPerformed(ActionEvent arg0) {
				action = CANCEL;
				dispose();
			}
		});
		JButton saveButton = new JButton(new AbstractAction("Save") {

			@Override
			public void actionPerformed(ActionEvent arg0) {
				action = SAVE;
				dispose();
			}
		});

		JPanel p = new JPanel(new FlowLayout(FlowLayout.RIGHT));
		p.add(cancelButton);
		p.add(saveButton);
		return p;
	}

	public int showDialog() {
		this.pack();
		this.setLocationRelativeTo(null);
		this.setVisible(true);
		return action;
	}

	public String getValue() {
		return patternTf.getText();
	}

}
