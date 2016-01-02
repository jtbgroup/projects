package be.vds.jtb.fcos;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class DocumentIdGenerator {

	private static String parentFolder = "structured/";
	private static String basePath = "Z:/documents/"+parentFolder;
	// private static String basePath =
	// "Z:/documents/99 - Archives/37 FCOS/DEML/";
	private static String destFile = "C:/Users/vanderslyen.g/Desktop/";
	private int fileCounter, folderCounter = 0;

	public static void main(String[] args) {
		DocumentIdGenerator l = new DocumentIdGenerator();
		l.generateIds();
	}

	public void generateIds() {
		File file = new File(basePath);

		List<DocumentFile> files = listFilesAndFolders(file, false);
		List<DocumentFile> folders = new ArrayList<DocumentFile>();
		System.out.println("building lists...");
		for (DocumentFile documentFile : files) {
			if (documentFile.isDirectory()) {
				folders.add(documentFile);
			}
		}
		files.removeAll(folders);

		Collections.sort(folders);
		try {
			System.out.println("building SQL files...");
			buildSQL(folders, files);
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	private void buildSQL(List<DocumentFile> folders, List<DocumentFile> files)
			throws IOException {
		StringBuilder sb = null;
		File catF = new File(destFile + "/categories.sql");
		FileWriter catw = new FileWriter(catF);
		catw.write("DELETE FROM jml_phocadownload_categories;\r\n");
		for (DocumentFile documentFile : folders) {
			sb = new StringBuilder();
			sb.append("INSERT INTO jml_phocadownload_categories ");
			sb.append("(id, parent_id, title, alias, published, access, uploaduserid, accessuserid, deleteuserid, date) VALUES ");
			sb.append("(");
			sb.append(documentFile.getId()).append(", ");
			DocumentFile parent = getFolderForPath(folders, documentFile);
			sb.append(null == parent ? 0 : parent.getId()).append(", '");
			sb.append(formatString(documentFile.getTitle())).append("', '");
			sb.append(documentFile.getAlias()).append("', ");
			sb.append("1, 2, -2, -1, -2, now());");
			System.out.println(sb.toString());
			sb.append("\r\n");
			catw.write(sb.toString());
		}
		catw.flush();
		catw.close();
		System.out.println("wrote cat sql");

		File fileF = new File(destFile + "/files.sql");
		FileWriter filew = new FileWriter(fileF);
		filew.write("DELETE FROM jml_phocadownload;\r\n");
		for (DocumentFile documentFile : files) {
			sb = new StringBuilder();
			sb.append("INSERT INTO jml_phocadownload ");
			sb.append("(id, catid, title, alias, filename, access, published, approved, description, features, changelog, notes, date) VALUES ");
			sb.append("(");
			sb.append(documentFile.getId()).append(", ");

			DocumentFile parent = getFolderForPath(folders, documentFile);
			sb.append(null == parent ? 0 : parent.getId()).append(", '");

			sb.append(formatString(documentFile.getTitle())).append("', '");
			sb.append(documentFile.getAlias()).append("', '");
			sb.append(parentFolder).append(formatString(getPathWithoutRoot(documentFile))).append("', ");
			sb.append("2,1,1,'','','','',now());");
			System.out.println(sb.toString());
			sb.append("\r\n");
			filew.write(sb.toString());
		}
		filew.flush();
		filew.close();
		System.out.println("wrote files sql");
	}

	private String formatString(String string) {
		return string.replace("'", "''");
	}

	private DocumentFile getFolderForPath(List<DocumentFile> folderList,
			DocumentFile file) {
		for (DocumentFile documentFile : folderList) {
			if (documentFile.getAbsolutePath().equals(
					file.getParentAbsolutePath())) {
				return documentFile;
			}
		}
		return null;
	}

	private String getParentPathWithoutRoot(DocumentFile documentFile) {
		String fileN = documentFile.getName();
		String absP = getPathWithoutRoot(documentFile);
		absP = absP.substring(0, absP.length() - fileN.length());
		return absP;
	}
	
	private String getPathWithoutRoot(DocumentFile documentFile) {
		int length = basePath.length();
		String absP = documentFile.getAbsolutePath();
		absP = absP.replaceAll("\\\\", "/");
		absP = absP.substring(length);
		return absP;
	}

	private List<DocumentFile> listFilesAndFolders(File file,
			boolean includeCurrent) {
		List<DocumentFile> list = new ArrayList<DocumentFile>();

		if (!file.isDirectory()) {
			list.add(new DocumentFile(file, generateFileId()));
			System.out.println("read " + file.getAbsolutePath());
		} else {
			if (includeCurrent) {
				list.add(new DocumentFile(file, generateFolderId()));
				System.out.println("read " + file.getAbsolutePath());
			}
			for (File child : file.listFiles()) {
				List<DocumentFile> children = listFilesAndFolders(child, true);
				list.addAll(children);
			}
		}
		return list;

	}

	private int generateFileId() {
		fileCounter = fileCounter + 1;
		return fileCounter;
	}

	private int generateFolderId() {
		folderCounter = folderCounter + 1;
		return folderCounter;
	}
}
