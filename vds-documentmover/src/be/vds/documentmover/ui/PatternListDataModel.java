package be.vds.documentmover.ui;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import javax.swing.AbstractListModel;
import javax.swing.DefaultListModel;

public class PatternListDataModel extends DefaultListModel {

	// private List<String> data;

	public void setPatterns(List<String> patterns) {
		if (patterns != null) {
			Collections.sort(patterns);
			for (String string : patterns) {
				addElement(string);
			}
		}
	}

	//
	// public List<String> getPatterns() {
	// return data;
	// }
	//
	// @Override
	// public Object getElementAt(int index) {
	// if(null == data){
	// return null;
	// }
	// return data.get(index);
	// }
	//
	// @Override
	// public int getSize() {
	// return data == null ? 0 : data.size();
	// }
	//
	// public void addValue(String value) {
	// if(data == null){
	// data = new ArrayList<String>();
	// }
	// data.add(value);
	// Collections.sort(data);
	// // int index = data.indexOf(value);
	// // fireIntervalAdded(null, index, index);
	// fire
	// }
	//
	// public void removeValue(String value) {
	// int index = data.indexOf(value);
	// if (-1 > index) {
	// data.remove(value);
	// // fireIntervalRemoved(null, index, index);
	// }
	//
	// }

	public List<String> getPatterns() {
		List<String> l = new ArrayList<String>();
		for (int i = 0; i < getSize(); i++) {
			l.add((String) get(i));
		}
		return l;
	}

}
