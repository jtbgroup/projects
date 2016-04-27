package be.vds.jtb.nasbackup;

import be.vds.test.TestPanel1Descriptor;
import be.vds.test.TestPanel2Descriptor;
import be.vds.test.TestPanel3Descriptor;
import be.vds.wizard.Wizard;
import be.vds.wizard.WizardModel;
import be.vds.wizard.WizardPanelDescriptor;

/**
 * Hello world!
 *
 */
public class App {
	public static void main(String[] args) {
		Wizard wizard = createWizard();
		int ret = wizard.showModalDialog();
	}

	private static Wizard createWizard() {
		Wizard wizard = new Wizard();
		
		
		WizardPanelDescriptor descriptor1 = new SourceFolderPanelDescriptor();
		wizard.registerWizardPanel(SourceFolderPanelDescriptor.IDENTIFIER, descriptor1);
		
		
//		WizardPanelDescriptor descriptor1 = new TestPanel1Descriptor();
//		wizard.registerWizardPanel(TestPanel1Descriptor.IDENTIFIER, descriptor1);
//
//		WizardPanelDescriptor descriptor2 = new TestPanel2Descriptor();
//		wizard.registerWizardPanel(TestPanel2Descriptor.IDENTIFIER, descriptor2);
//
//		WizardPanelDescriptor descriptor3 = new TestPanel3Descriptor();
//		wizard.registerWizardPanel(TestPanel3Descriptor.IDENTIFIER, descriptor3);

		wizard.setCurrentPanel(TestPanel1Descriptor.IDENTIFIER);

		return wizard;
	}
}
