package be.vds.jtb.nasbackup;

import java.util.Date;

import be.vds.wizard.WizardPanelDescriptor;


public class SourceFolderPanelDescriptor extends WizardPanelDescriptor {
    
    public static final String IDENTIFIER = "SOURCE FOLDER PANEL";
    
    public SourceFolderPanelDescriptor() {
        super(IDENTIFIER, new SourceFolderPanel());
    }
    
    public Object getNextPanelDescriptor() {
        return TestPanel2Descriptor.IDENTIFIER;
    }
    
    public Object getBackPanelDescriptor() {
        return null;
    }  
    
    public void doBeforeNext() {
    	
    	getWizard().getModel().getDataMap().put("test1", new Date());
    }
    
}
