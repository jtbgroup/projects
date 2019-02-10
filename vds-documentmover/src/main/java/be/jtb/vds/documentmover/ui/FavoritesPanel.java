package be.jtb.vds.documentmover.ui;

import java.awt.BorderLayout;
import java.awt.Component;
import java.awt.event.ActionEvent;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Vector;

import javax.swing.AbstractAction;
import javax.swing.BoxLayout;
import javax.swing.DefaultListSelectionModel;
import javax.swing.JButton;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.event.ListSelectionEvent;
import javax.swing.event.ListSelectionListener;
import javax.swing.table.DefaultTableModel;

public class FavoritesPanel extends JPanel {

//	private PatternListDataModel patternListDataModel;
	private JTable favoritesTable;
	private JButton removeButton;
	private DefaultTableModel favoritesTableModel;

	public FavoritesPanel() {
		initializeComponent();
	}

	public void initializeComponent() {
		this.setLayout(new BorderLayout());
		this.add(createTable(), BorderLayout.CENTER);
		this.add(createButtonsPanel(), BorderLayout.EAST);
	}

	private Component createTable() {
		favoritesTableModel = new DefaultTableModel();
		favoritesTableModel.setColumnIdentifiers(new String[] { "Name", "Path" });
		favoritesTable = new JTable(favoritesTableModel);
		favoritesTable.setSelectionMode(DefaultListSelectionModel.SINGLE_SELECTION);
		favoritesTable.getSelectionModel().addListSelectionListener(new ListSelectionListener() {

			@Override
			public void valueChanged(ListSelectionEvent e) {
				removeButton.setEnabled(favoritesTable.getSelectedRow() > -1);
			}
		});

		JScrollPane scroll = new JScrollPane(favoritesTable);
		return scroll;
	}

	public void setFavorites(List<Favorite> favorites) {
//		favoritesTable.removeAll();
//		favoritesTableModel.ad
		for (Favorite favorite : favorites) {
			favoritesTableModel.addRow(new String[] { favorite.getName(), favorite.getPath() });
		}
		favoritesTable.setModel(favoritesTableModel);
//		favoritesTable.repaint();
	}

	private Component createButtonsPanel() {
		JButton addButton = new JButton(new AbstractAction("+") {

			@Override
			public void actionPerformed(ActionEvent arg0) {
				FavoriteDialog dlg = new FavoriteDialog();
				int i = dlg.showDialog();
				if (i == PatternEditDialog.SAVE) {
					favoritesTableModel.addRow(new String[] { dlg.getFavoriteName(), dlg.getFavoriteFolder() });
				}
			}
		});

		removeButton = new JButton(new AbstractAction("-") {

			@Override
			public void actionPerformed(ActionEvent arg0) {
				favoritesTableModel.removeRow(favoritesTable.getSelectedRow());
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

	public List<Favorite> getFavorites() {
		List<Favorite> favorites = new ArrayList<Favorite>();
		int rows = favoritesTableModel.getRowCount();
		for (int i = 0; i < rows; i++) {
			favorites.add(new Favorite((String) favoritesTableModel.getValueAt(i, 0), (String) favoritesTableModel.getValueAt(i, 1)));
		}

		return favorites;
	}

}
