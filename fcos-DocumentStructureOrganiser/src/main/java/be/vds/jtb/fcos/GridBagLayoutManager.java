package be.vds.jtb.fcos;

import java.awt.Component;
import java.awt.GridBagConstraints;

import javax.swing.JComponent;

public class GridBagLayoutManager {

	public static void addComponent(JComponent parentComponent,
			Component component, GridBagConstraints constraint, int gridx,
			int gridy, int gridwidth, int gridheight, double weightx, double weighty,
			int fill, int anchor) {
		constraint.gridx = gridx;
		constraint.gridy = gridy;
		constraint.gridwidth = gridwidth;
		constraint.gridheight = gridheight;
		constraint.weightx = weightx;
		constraint.weighty = weighty;
		constraint.fill = fill;
		constraint.anchor = anchor;
		parentComponent.add(component, constraint);
	}

}
