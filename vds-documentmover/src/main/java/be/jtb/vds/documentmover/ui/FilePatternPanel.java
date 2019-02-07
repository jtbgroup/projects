package be.jtb.vds.documentmover.ui;

import java.awt.BorderLayout;
import java.awt.Component;
import java.awt.event.ActionEvent;
import java.util.ArrayList;
import java.util.List;

import javax.swing.AbstractAction;
import javax.swing.BoxLayout;
import javax.swing.DefaultListModel;
import javax.swing.JButton;
import javax.swing.JList;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.ListSelectionModel;
import javax.swing.event.ListSelectionEvent;
import javax.swing.event.ListSelectionListener;

public class FilePatternPanel extends JPanel {

	private PatternListDataModel patternListDataModel;
	private JList patternList;
	private JButton removeButton;

	public FilePatternPanel() {
		initializeComponent();
	}

	public void initializeComponent() {
		this.setLayout(new BorderLayout());
		this.add(createList(), BorderLayout.CENTER);
		this.add(createButtonsPanel(), BorderLayout.EAST);
	}

	private Component createList() {
		patternListDataModel = new PatternListDataModel();

		patternList = new JList(patternListDataModel);
		patternList.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
		patternList.addListSelectionListener(new ListSelectionListener() {

			@Override
			public void valueChanged(ListSelectionEvent arg0) {
				removeButton.setEnabled(patternList.getSelectedIndex() > -1);
			}
		});

		JScrollPane scroll = new JScrollPane(patternList);
		return scroll;
	}

	public void setFilePatterns(List<String> filePatterns) {
		patternListDataModel.setPatterns(filePatterns);
	}

	private Component createButtonsPanel() {
		JButton addButton = new JButton(new AbstractAction("+") {

			@Override
			public void actionPerformed(ActionEvent arg0) {
				PatternEditDialog dlg = new PatternEditDialog();
				int i = dlg.showDialog();
				if (i == PatternEditDialog.SAVE) {
					patternListDataModel.addElement(dlg.getValue());
				}
			}
		});

		removeButton = new JButton(new AbstractAction("-") {

			@Override
			public void actionPerformed(ActionEvent arg0) {
				patternListDataModel.removeElement(patternList
						.getSelectedValue());

			}
		});
		removeButton.setEnabled(false);

		JPanel p = new JPanel();
		BoxLayout bl = new BoxLayout(p, BoxLayout.PAGE_AXIS);
		p.setLayout(bl);
		p.add(addButton);
		p.add(removeButton);

		return p;
	}

	public List<String> getFilePatterns() {
		return patternListDataModel.getPatterns();
	}
}
