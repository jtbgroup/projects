package be.vds.jtb.nasbackup;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;

import junit.framework.TestCase;

public class BackupEngineTest extends TestCase {

	private File originalSrcFolder;
	private File destFolder;

	@Override
	protected void setUp() throws Exception {
		originalSrcFolder = new File(
				System.getProperty("user.home") + File.separatorChar + "temp" + File.separatorChar + "BackUpEngine");
		File srcFolder = originalSrcFolder;
		destFolder = new File(
				System.getProperty("user.home") + File.separatorChar + "temp" + File.separatorChar + "BackUpEngineCopy");
		srcFolder.mkdirs();
		for (int i = 0; i < 50; i++) {
			String fileName = "test_" + i + ".txt";
			if (i > 0 && (i % 10) == 0) {
				srcFolder = new File(srcFolder.getAbsolutePath() + File.separatorChar + "folder_" + (i / 10));
				srcFolder.mkdirs();
			}
			File f = new File(srcFolder.getAbsolutePath() + File.separatorChar + fileName);
			f.createNewFile();
			FileWriter w = new FileWriter(f);
			w.write("this is just a test for file n° " + i);
			w.flush();
			w.close();
		}
	}

	@Override
	protected void tearDown() throws Exception {
		deleteFolder(originalSrcFolder);
	}

	public void testRunEngine() throws IOException {
		BackupEngine engine = new BackupEngine();
		engine.setSrcFolder(originalSrcFolder);
		assertEquals(54, engine.countAllFiles(originalSrcFolder));
		engine.setDestFolder(destFolder);
		engine.runEngine();
	}

	private void deleteFolder(File parentFolder) {
		for (File file : parentFolder.listFiles()) {
			if (!file.isDirectory()) {
				file.delete();
			} else {
				deleteFolder(file);
			}
		}
		parentFolder.delete();
	}

}
