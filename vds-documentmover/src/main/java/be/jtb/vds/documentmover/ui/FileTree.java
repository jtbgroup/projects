package be.jtb.vds.documentmover.ui;

import java.awt.BorderLayout;
import java.awt.Dimension;
import java.io.File;
import java.util.Collections;
import java.util.Vector;
import java.util.logging.Logger;

import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTree;
import javax.swing.event.TreeSelectionEvent;
import javax.swing.event.TreeSelectionListener;
import javax.swing.tree.DefaultMutableTreeNode;
import javax.swing.tree.DefaultTreeCellRenderer;

public class FileTree extends JPanel {
	private static final Logger LOGGER = Logger.getLogger(FileTree.class.getName());
	
public FileTree(File dir) {
    setLayout(new BorderLayout());
    JTree tree = new JTree(addNodes(null, dir));
    tree.addTreeSelectionListener(new TreeSelectionListener() {
        public void valueChanged(TreeSelectionEvent e) {
            DefaultMutableTreeNode node = (DefaultMutableTreeNode) e.getPath().getLastPathComponent();
           LOGGER.finest("You selected " + node);
        }
    });
    DefaultTreeCellRenderer renderer = (DefaultTreeCellRenderer) tree.getCellRenderer();
    renderer.setLeafIcon(null);
    renderer.setClosedIcon(null);
    renderer.setOpenIcon(null);
    JScrollPane scrollpane = new JScrollPane();
    scrollpane.getViewport().add(tree);
    add(BorderLayout.CENTER, scrollpane);
}

private DefaultMutableTreeNode addNodes(DefaultMutableTreeNode curTop, File dir) {
    String curPath = dir.getPath();
    DefaultMutableTreeNode curDir = new DefaultMutableTreeNode(curPath);
    if (curTop != null) {
        curTop.add(curDir);
    }
    Vector<String> ol = new Vector<String>();
    String[] tmp = dir.list();
    for (int i = 0; i < tmp.length; i++) {
        ol.addElement(tmp[i]);
    }
    Collections.sort(ol, String.CASE_INSENSITIVE_ORDER);
    File f;
    Vector<Object> files = new Vector<Object>();
    for (int i = 0; i < ol.size(); i++) {
        String thisObject = ol.elementAt(i);
        String newPath;
        if (curPath.equals(".")) {
            newPath = thisObject;
        } else {
            newPath = curPath + File.separator + thisObject;
        }
        if ((f = new File(newPath)).isDirectory()) {
            addNodes(curDir, f);
        } else {
            files.addElement(thisObject);
        }
    }
    for (int fnum = 0; fnum < files.size(); fnum++) {
        curDir.add(new DefaultMutableTreeNode(files.elementAt(fnum)));
    }
    return curDir;
}



}