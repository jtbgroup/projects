package be.mil.model;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileOutputStream;
import java.io.FileReader;
import java.io.IOException;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.CellStyle;
import org.apache.poi.ss.usermodel.CreationHelper;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.ss.util.WorkbookUtil;

public class IataLogReader {

	private static final SimpleDateFormat sdf = new SimpleDateFormat(
			"yyyy-MM-dd, HH:mm:ss");
	private File logFile;
	private List<Date> dateList = new ArrayList<Date>();
	private List<Integer> hitList = new ArrayList<Integer>();
	private List<Integer> maxList = new ArrayList<Integer>();
	private File excelFile;

	public IataLogReader(File logFile, File excelFile) {
		this.logFile = logFile;
		this.excelFile = excelFile;
	}

	public void readFile() throws IOException {
		dateList.clear();
		hitList.clear();
		maxList.clear();

		BufferedReader reader = new BufferedReader(new FileReader(logFile));

		while (reader.ready()) {
			String line = reader.readLine();
			if (null != line) {
				Pattern p = Pattern
						.compile("(.*) <notification>: New lease assigned.* Total leases: (\\d*) / (\\d*)");
				Matcher m = p.matcher(line);
				if (m.matches()) {
					try {
						Date d = sdf.parse(m.group(1));
						dateList.add(d);
						hitList.add(Integer.parseInt(m.group(2)));
						maxList.add(Integer.parseInt(m.group(3)));
					} catch (ParseException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					}
				}
			}
		}

		reader.close();

		exportToExcel();
	}

	private void exportToExcel() throws IOException {
		if (!excelFile.getParentFile().exists()) {
			excelFile.getParentFile().mkdirs();
		} else if (!excelFile.exists()) {
			excelFile.delete();
		}
		excelFile.createNewFile();

		Workbook wb = new HSSFWorkbook();
		CreationHelper createHelper = wb.getCreationHelper();
		Sheet sheet = wb.createSheet("IATA connections");

		for (int i = 0; i < dateList.size(); i++) {
			Row row = sheet.createRow((short) i);
			Cell dateCell = row.createCell(0);

			CellStyle cellStyle = wb.createCellStyle();
			cellStyle.setDataFormat(createHelper.createDataFormat().getFormat(
					"yyyy-MM-dd HH:mm:ss"));
			dateCell.setCellValue(dateList.get(i));
			dateCell.setCellStyle(cellStyle);

			row.createCell(1).setCellValue(hitList.get(i));
			row.createCell(2).setCellValue(maxList.get(i));
		}

		final String name = "IATA connections";
		wb.setSheetName(0, WorkbookUtil.createSafeSheetName(name));
		FileOutputStream fileOut = new FileOutputStream(excelFile);
		wb.write(fileOut);
		fileOut.close();
		wb.close();
	}

}
