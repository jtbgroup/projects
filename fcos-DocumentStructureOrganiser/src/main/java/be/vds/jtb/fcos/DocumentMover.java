package be.vds.jtb.fcos;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

public class DocumentMover {
	private List<MovingFile> movingFiles;

	private File useruploadPath;
	private File structuredPath;
	private File catList;
	private File fileList;

	public void setUseruploadPath(File useruploadPath) {
		this.useruploadPath = useruploadPath;
	}

	public void setStructuredPath(File structuredPath) {
		this.structuredPath = structuredPath;
	}

	public void setCatList(File catList) {
		this.catList = catList;
	}

	public void setFileList(File fileList) {
		this.fileList = fileList;
	}

	public void generateFilesToMoveList() throws IOException {

		List<PhocaCategory> cats = buildCategoriesFromCSV(catList);
		List<PhocaFile> files = buildFilesFromCSV(cats, fileList);
		List<MovingFile> userFiles = buildUserFiles(useruploadPath, files,
				structuredPath);

		this.movingFiles = userFiles;
	}

	private List<PhocaCategory> buildCategoriesFromCSV(File catList)
			throws IOException {
		List<PhocaCategory> cats = new ArrayList<PhocaCategory>();
		BufferedReader r = new BufferedReader(new FileReader(catList));
		while (r.ready()) {
			String s = r.readLine();
			s = new String(s.getBytes(), "UTF-8");
			String[] parts = s.split(";");
			PhocaCategory pc = new PhocaCategory();
			pc.setId(Integer.valueOf(parts[0]));
			pc.setParentId(Integer.valueOf(parts[1]));
			pc.setTitle(parts[2]);
			cats.add(pc);
		}
		return cats;
	}

	private List<PhocaFile> buildFilesFromCSV(List<PhocaCategory> cats,
			File files) throws IOException {
		List<PhocaFile> result = new ArrayList<PhocaFile>();
		BufferedReader r = new BufferedReader(new FileReader(files));

		while (r.ready()) {
			String s = r.readLine();
			s = new String(s.getBytes(), "UTF-8");
			String[] parts = s.split(";");
			PhocaFile pf = new PhocaFile();
			pf.setId(Integer.parseInt(parts[0]));
			pf.setFileName(parts[1]);
			pf.setCategoryName(buildCatName(cats, Integer.parseInt(parts[2])));
			result.add(pf);
		}
		return result;
	}

	private String buildCatName(List<PhocaCategory> cats, int id) {
		String str = "";
		PhocaCategory cat = getCategory(cats, id);
		while (cat != null) {
			str = cat.getTitle() + "/" + str;
			cat = getCategory(cats, cat.getParentId());
		}
		return str;
	}

	private PhocaCategory getCategory(List<PhocaCategory> cats, int id) {
		for (PhocaCategory phocaCategory : cats) {
			if (phocaCategory.getId() == id)
				return phocaCategory;
		}
		return null;
	}

	private List<MovingFile> buildUserFiles(File parent,
			List<PhocaFile> phocaFiles, File structuredPath) {
		List<MovingFile> result = new ArrayList<MovingFile>();
		for (File file : parent.listFiles()) {
			if (file.isDirectory()) {
				result.addAll(buildUserFiles(file, phocaFiles, structuredPath));
			} else {
				PhocaFile phocaFile = getMatchingPhocaFile(file, phocaFiles);
				if (null != phocaFile) {
					MovingFile mf = new MovingFile();

					mf.setCurrentFile(file);
					mf.setCurrentFileName(phocaFile.getFileName());

					String filename = phocaFile.getFileName().substring(
							phocaFile.getFileName().lastIndexOf("/") + 1);
					mf.setTargetFileName("structured/"
							+ phocaFile.getCategoryName() + filename);
					mf.setTargetFile(new File(structuredPath.getParent()
							+ File.separatorChar + mf.getTargetFileName()));

					mf.setCurrentId(phocaFile.getId());
					result.add(mf);
				}
			}
		}
		return result;
	}

	private PhocaFile getMatchingPhocaFile(File file, List<PhocaFile> phocaFiles) {
		for (PhocaFile phocaFile : phocaFiles) {
			String fileName = phocaFile.getFileName().replaceAll("\\\\", "/");
			String absP = file.getAbsolutePath().replaceAll("\\\\", "/");
			if (absP.endsWith(fileName)) {
				return phocaFile;
			}
		}
		return null;
	}

	public void moveFiles() throws IOException {
		StringBuilder sb = new StringBuilder();
		String dateStr = new SimpleDateFormat("yyyyMMddHHmmss")
				.format(new Date());
		File tempFolder = new File(useruploadPath.getParent()
				+ File.separatorChar + "tmp" + dateStr);
		tempFolder.mkdirs();
		File tempFile = null;
		File targetFile = null;
		for (MovingFile movF : movingFiles) {
			// Back up files
			tempFile = new File(tempFolder.getAbsolutePath()
					+ File.separatorChar + movF.getTargetFile().getName());
			FileUtilities.copyFile(movF.getCurrentFile(), tempFile);

			// move to target
			targetFile = movF.getTargetFile();
			if (!targetFile.getParentFile().exists()) {
				targetFile.getParentFile().mkdirs();
			}
			FileUtilities.copyFile(movF.getCurrentFile(), targetFile);

			// delete original
			movF.getCurrentFile().delete();

			// append SQL instruction
			sb.append("UPDATE jml_phocadownload SET filename = \"")
					.append(movF.getTargetFileName()).append("\" WHERE id = ")
					.append(movF.getCurrentId()).append(";\r\n");
		}

		File sqlF = new File(tempFolder.getAbsolutePath() + File.separatorChar
				+ "update.sql");
		sqlF.createNewFile();
		FileWriter fw = new FileWriter(sqlF);
		fw.write(sb.toString());
		fw.flush();
		fw.close();
	}

	public List<MovingFile> getMovingFiles() {
		return movingFiles;
	}

	public File getStructuredPath() {
		return structuredPath;
	}

	public File getUseruploadPath() {
		return useruploadPath;
	}

	public File getCatList() {
		return catList;
	}

	public File getFileList() {
		return fileList;
	}
}
