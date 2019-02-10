package be.jtb.vds.documentmover.utils;

import java.awt.Component;

import javax.swing.JOptionPane;

public class MessageHelper {
	
	public static final int YES_OPTION = JOptionPane.YES_OPTION;
	
	private static MessageHelper instance;
	private Component baseComponent;

	private MessageHelper() {
	}

	public static MessageHelper getInstance() {
		if (null == instance) {
			instance = new MessageHelper();
		}
		return instance;
	}

	public void setDisplayContainer(Component component) {
		this.baseComponent = component;
	}

	public void displayMessage(String message) {
		JOptionPane.showMessageDialog(this.baseComponent, message);
	}

	public int displayConfirm(String message) {
		return JOptionPane.showConfirmDialog(this.baseComponent, message);
	}
}
