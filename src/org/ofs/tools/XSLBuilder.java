package org.ofs.tools;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;

import jxl.Cell;
import jxl.Sheet;
import jxl.Workbook;
import jxl.WorkbookSettings;
import jxl.format.Colour;
import jxl.format.UnderlineStyle;
import jxl.read.biff.BiffException;
import jxl.write.Alignment;
import jxl.write.Border;
import jxl.write.BorderLineStyle;
import jxl.write.Label;
import jxl.write.VerticalAlignment;
import jxl.write.WritableCellFormat;
import jxl.write.WritableFont;
import jxl.write.WritableHyperlink;
import jxl.write.WritableSheet;
import jxl.write.WritableWorkbook;
import jxl.write.WriteException;

public class XSLBuilder {
	File file;
	List<HashMap<String, String>> testObject = null;
	String[] ColumnsNames = null;
	private String SheetName;

	public XSLBuilder(String fileName) {
		file = new File(fileName);

	}

	public XSLBuilder(String filePath, List<HashMap<String, String>> testCases) {
		file = new File(filePath);
		testObject = testCases;
	}

	public XSLBuilder(String filePath, List<HashMap<String, String>> testCases,
			String sheetName, String[] ColumnsNames) {
		file = new File(filePath);
		testObject = testCases;
		this.ColumnsNames = ColumnsNames;
		this.SheetName = sheetName;
	}

	public XSLBuilder(String filePath, List<HashMap<String, String>> testCases,
			String sheetName) {
		file = new File(filePath);
		testObject = testCases;
		this.SheetName = sheetName;
	}

	public List<HashMap<String, String>> convertXSLtoObject(String sheetName) {
		Workbook workbook;
		List<HashMap<String, String>> xsldata = new ArrayList<HashMap<String, String>>();
		try {
			workbook = Workbook.getWorkbook(file);
			Sheet sheet = workbook.getSheet(sheetName);
			int rowCount = sheet.getRows();
			int columnCount = sheet.getColumns();
			Cell[] KeyMap = sheet.getRow(0);

			for (int i = 1; i < rowCount; i++) {
				Cell[] rowData = sheet.getRow(i);
				HashMap<String, String> rowObject = new HashMap<String, String>();
				if (rowData.length == 0
						|| rowData[0].getContents().length() == 0)
					break;
				for (int j = 0; j < columnCount; j++) {
					rowObject.put(KeyMap[j].getContents(),
							rowData[j].getContents());
				}
				xsldata.add(rowObject);

			}
			workbook.close();
		} catch (BiffException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

		return xsldata;
	}

	public void convertObjecttoXLS(boolean create) {

		try {
			WorkbookSettings ws = new WorkbookSettings();

			ws.setLocale(new Locale("en", "EN"));
			WritableWorkbook workbook;
			if (!file.exists() || create) {
				if (new File(file.getParent()).mkdirs()) {
					workbook = Workbook.createWorkbook(file, ws);
				} else {
					workbook = Workbook.createWorkbook(file, ws);
				}
			} else {
				Workbook oldWorkbook = Workbook.getWorkbook(file);
				workbook = Workbook.createWorkbook(file, oldWorkbook);
			}
			// Create a blank sheet
			WritableSheet sheet;
			if (workbook.getSheet(this.SheetName) == null) {
				sheet = workbook.createSheet(this.SheetName,
						workbook.getNumberOfSheets());
			} else {
				sheet = workbook.getSheet(this.SheetName);
			}
			// WritableSheet sheet = workbook.createSheet(this.SheetName,
			// workbook.getNumberOfSheets());
			if (ColumnsNames == null) {
				java.util.Set<String> set = testObject.get(0).keySet();
				int i = 0;
				ColumnsNames = new String[set.size()];
				for (String column : set) {
					ColumnsNames[i++] = column;

				}
			}
			int j = 0;
			for (String column : ColumnsNames) {

				// System.out.println(column+ ": " + testCase.get(column));

				try {
					Label label = new Label(j, 0, column.trim(), getCellFormat(
							Colour.BLACK, Colour.AQUA, true, 14));

					sheet.addCell(label);
					sheet.setColumnView(j, 30);

				} catch (WriteException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
				j++;
			}
			int existingsheetSize = sheet.getRows();
			for (int i = 0; i < testObject.size(); i++) {
				HashMap<String, String> testCase = testObject.get(i);
				j = 0;
				for (String column : ColumnsNames) {

					Label label;
					// System.out.println(column+ ": " + testCase.get(column));
					if (column.trim().equalsIgnoreCase("Step Execution Status")
							|| column.trim().equalsIgnoreCase(
									"Test Execution Status")) {

						jxl.format.Colour status = Colour.BLACK;

						if (testCase.get(column.trim()) != null
								&& ((String) testCase.get(column.trim()))
										.equals("Pass")) {

							status = Colour.GREEN;

						} else {

							status = Colour.RED;

						}

						label = new Label(j, i + existingsheetSize,

						testCase.get(column.trim()), getCellFormat(status,
								Colour.WHITE, true, 12));

					} else {

						WritableCellFormat cellFormat = new WritableCellFormat();

						String cellValue = testCase.get(column.trim());

						// cellFormat.setBackground(colour, pattern);

						if (column.trim().equals("Error Screen Output")
								&& cellValue != null && cellValue.length() > 0) {
							WritableFont cellFont = new WritableFont(
									WritableFont.TIMES, 13,
									WritableFont.NO_BOLD, false,
									UnderlineStyle.SINGLE, Colour.BLUE);
							cellFormat = new WritableCellFormat(cellFont);

							WritableHyperlink hl = new WritableHyperlink(j, i
									+ existingsheetSize,

							new File(cellValue), cellValue);

							sheet.addHyperlink(hl);
						}

						cellFormat.setBorder(Border.ALL, BorderLineStyle.THIN,
								Colour.BLACK);
						cellFormat.setWrap(true);

						cellFormat.setAlignment(Alignment.LEFT);

						cellFormat.setVerticalAlignment(VerticalAlignment.TOP);

						label = new Label(j, i + existingsheetSize, cellValue,
								cellFormat);

					}

					try {
						sheet.addCell(label);
					} catch (WriteException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					}
					j++;
				}

			}
			// System.out.println("No of Columns: " + sheet.getColumns());
			// System.out.println("No of Rows: " + sheet.getRows());
			workbook.write();
			workbook.close();

		} catch (IOException | WriteException e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		} catch (BiffException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	private static WritableCellFormat getCellFormat(Colour cellColor,
			Colour cellBackground, boolean setBaground, int fontSize)
			throws WriteException {

		WritableFont cellFont = new WritableFont(WritableFont.TIMES, fontSize,
				WritableFont.NO_BOLD, false, UnderlineStyle.NO_UNDERLINE,
				cellColor);
		WritableCellFormat cellFormat = new WritableCellFormat(cellFont);
		if (setBaground)
			cellFormat.setBackground(cellBackground, jxl.format.Pattern.SOLID);

		cellFormat.setBorder(Border.ALL, BorderLineStyle.THIN, Colour.BLACK);

		cellFormat.setWrap(true);

		return cellFormat;

	}

	public static boolean compare(String source, String target) {

		boolean status = true;

		File sourceFile = new File(source);

		File targetFile = new File(target);

		try {

			Workbook workbook = Workbook.getWorkbook(sourceFile);

			Sheet sourceSheet = workbook.getSheet(0);

			workbook = Workbook.getWorkbook(targetFile);

			WorkbookSettings ws = new WorkbookSettings();

			ws.setLocale(new Locale("en", "EN"));

			WritableWorkbook writableWorkbook = Workbook.createWorkbook(

			new File(target), workbook, ws);

			WritableSheet targetSheet = writableWorkbook.getSheet(0);

			int sourceRowCount = sourceSheet.getRows();

			int targetRowCount = targetSheet.getRows();

			int sourceColumnCount = sourceSheet.getColumns();

			int targetColumnCount = targetSheet.getColumns();

			if (sourceRowCount == targetRowCount

			&& sourceColumnCount == targetColumnCount) {

				for (int i = 1; i < targetRowCount; i++) {

					for (int j = 0; j < sourceColumnCount; j++) {

						Cell sourceCell = sourceSheet.getCell(j, i);

						Cell targetCell = targetSheet.getCell(j, i);

						String sourceCellValue = sourceCell.getContents();

						String targetCellValue = targetCell.getContents();

						if (sourceCellValue == null) {

							if (targetCellValue == null) {

							} else {

								Label label = new Label(j, i, sourceCellValue,

								getCellFormat(Colour.RED, Colour.WHITE,

								true, 12));

								targetSheet.addCell(label);

								status = false;

							}

						} else if (targetCellValue != null) {

							if (sourceCellValue.trim().equals(

							targetCellValue.trim())) {

							} else {

								status = false;

							}

						} else {
							status = false;
						}

					}

				}

			} else {
				status = false;
			}

			writableWorkbook.write();

			writableWorkbook.close();

		} catch (BiffException e) {

			// TODO Auto-generated catch block

			e.printStackTrace();

		} catch (IOException e) {

			// TODO Auto-generated catch block

			e.printStackTrace();

		} catch (WriteException e) {

			// TODO Auto-generated catch block

			e.printStackTrace();

		}

		return status;

	}

	public static boolean compareGrids(String source, String target) {

		boolean status = true;

		File sourceFile = new File(source);

		File targetFile = new File(target);

		try {

			Workbook workbook = Workbook.getWorkbook(sourceFile);

			Sheet sourceSheet = workbook.getSheet(0);

			workbook = Workbook.getWorkbook(targetFile);

			WorkbookSettings ws = new WorkbookSettings();

			ws.setLocale(new Locale("en", "EN"));

			WritableWorkbook writableWorkbook = Workbook.createWorkbook(

			new File(target), workbook, ws);

			WritableSheet targetSheet = writableWorkbook.getSheet(0);

			int sourceRowCount = sourceSheet.getRows();

			int targetRowCount = targetSheet.getRows();

			int sourceColumnCount = sourceSheet.getColumns();

			int targetColumnCount = targetSheet.getColumns();

			if (sourceRowCount == targetRowCount

			&& sourceColumnCount == targetColumnCount) {

				for (int i = 1; i < targetRowCount; i++) {

					for (int j = 0; j < sourceColumnCount; j++) {

						Cell sourceCell = sourceSheet.getCell(j, i);

						Cell targetCell = targetSheet.getCell(j, i);

						String sourceCellValue = sourceCell.getContents();

						String targetCellValue = targetCell.getContents();

						if (sourceCellValue == null) {

							if (targetCellValue == null) {

							} else {

								Label label = new Label(j, i, sourceCellValue,

								getCellFormat(Colour.RED, Colour.WHITE,

								true, 12));

								targetSheet.addCell(label);

								status = false;

							}

						} else if (targetCellValue != null) {

							if (sourceCellValue.trim().equals(

							targetCellValue.trim())) {

							} else {

								status = false;

							}

						} else {
							status = false;
						}

					}

				}

			} else {
				status = false;
			}

			writableWorkbook.write();

			writableWorkbook.close();

		} catch (BiffException e) {

			// TODO Auto-generated catch block

			e.printStackTrace();

		} catch (IOException e) {

			// TODO Auto-generated catch block

			e.printStackTrace();

		} catch (WriteException e) {

			// TODO Auto-generated catch block

			e.printStackTrace();

		}

		return status;

	}

	public boolean constructTargetXLS(String source) {

		boolean success = true;

		File sourceFile = new File(source);

		try {
			WorkbookSettings ws = new WorkbookSettings();

			Workbook sourceWorkbook = Workbook.getWorkbook(sourceFile);

			Sheet sourceSheet = sourceWorkbook.getSheet(0);

			ws.setLocale(new Locale("en", "EN"));
			WritableWorkbook workbook;
			if (!file.exists()) {
				if (new File(file.getParent()).mkdirs()) {
					workbook = Workbook.createWorkbook(file, ws);
				} else {
					workbook = Workbook.createWorkbook(file, ws);
				}
			} else {
				Workbook oldWorkbook = Workbook.getWorkbook(file);
				workbook = Workbook.createWorkbook(file, oldWorkbook);
			}
			// Create a blank sheet
			WritableSheet sheet;
			if (workbook.getSheet(this.SheetName) == null) {
				sheet = workbook.createSheet(this.SheetName,
						workbook.getNumberOfSheets());
			} else {
				sheet = workbook.getSheet(this.SheetName);
			}
			// WritableSheet sheet = workbook.createSheet(this.SheetName,
			// workbook.getNumberOfSheets());
			if (ColumnsNames == null) {
				java.util.Set<String> set = testObject.get(4).keySet();
				int i = 0;
				ColumnsNames = new String[set.size()];
				for (String column : set) {
					ColumnsNames[i++] = column;

				}
			}
			// int j = 0;
			// for (String column : ColumnsNames) {
			//
			// // System.out.println(column+ ": " + testCase.get(column));
			//
			// try {
			// Label label = new Label(j, 0, column.trim(), getCellFormat(
			// Colour.BLACK, Colour.AQUA, true, 14));
			//
			// sheet.addCell(label);
			// sheet.setColumnView(j, 30);
			//
			// } catch (WriteException e) {
			// // TODO Auto-generated catch block
			// e.printStackTrace();
			// }
			// j++;
			// }
			int existingsheetSize = sheet.getRows();

			int sourceRowCount = sourceSheet.getRows();

			int sourceColumnCount = sourceSheet.getColumns();

			int rows = (sourceRowCount > testObject.size()) ? sourceRowCount
					: testObject.size();

			for (int i = 0; i < rows; i++) {
				WritableCellFormat cellFormat = new WritableCellFormat();

				if (i == 0) {
					int newrow = 0;
					for (String column : ColumnsNames) {

						// System.out.println(column+ ": " +
						// testCase.get(column));

						try {
							Label label = new Label(newrow, 0, column.trim(),
									getCellFormat(Colour.BLACK, Colour.AQUA,
											true, 14));

							sheet.addCell(label);
							sheet.setColumnView(newrow, 30);

						} catch (WriteException e) {
							// TODO Auto-generated catch block
							e.printStackTrace();
						}

						try {
							Label label = new Label(newrow + sourceColumnCount
									+ 2, 0, column.trim(), getCellFormat(
									Colour.BLACK, Colour.AQUA, true, 14));
							sheet.setColumnView(newrow + sourceColumnCount + 2,
									30);
							sheet.addCell(label);

						} catch (WriteException e) {
							// TODO Auto-generated catch block
							e.printStackTrace();
						}

						newrow++;
					}

				} else {

					int j = 0;
					for (String column : ColumnsNames) {
						String sourceCellValue = "";
						HashMap<String, String> testCase = (i <= testObject
								.size()) ? testObject.get(i - 1) : null;
						try {
							Cell sourceCell = sourceSheet.getCell(j, i);
							sourceCellValue = sourceCell.getContents();
							Label sourcelabel = new Label(j, i, sourceCellValue);
							try {
								sheet.addCell(sourcelabel);
							} catch (WriteException e) {
								// TODO Auto-generated catch block
								success = false;
								e.printStackTrace();
							}
						} catch (ArrayIndexOutOfBoundsException e) {
							success = false;
						}

						Label label;

						String objectValue = (testCase != null) ? testCase
								.get(column) : "";
						Colour status = Colour.BLACK;
						if (objectValue == null) {
							if (sourceCellValue != null
									|| sourceCellValue.length() != 0) {
								success = false;
								status = Colour.RED;
							}

						} else if (sourceCellValue != null
								&& !objectValue.equals(sourceCellValue)) {
							success = false;
							status = Colour.RED;

						}

						label = new Label(j + sourceColumnCount + 2, i,
								objectValue, getCellFormat(status,
										Colour.WHITE, true, 12));

						// System.out.println(column+ ": " +
						// testCase.get(column));
						// if (column.trim().equalsIgnoreCase(
						// "Step Execution Status")
						// || column.trim().equalsIgnoreCase(
						// "Test Execution Status")) {
						//
						// jxl.format.Colour status = Colour.BLACK;
						//
						// if (testCase.get(column.trim()) != null
						// && ((String) testCase.get(column.trim()))
						// .equals("Pass")) {
						//
						// status = Colour.GREEN;
						//
						// } else {
						//
						// status = Colour.RED;
						//
						// }
						//
						// label = new Label(j, i + existingsheetSize,
						//
						// testCase.get(column.trim()), getCellFormat(status,
						// Colour.WHITE, true, 12));
						//
						// } else {
						//
						// WritableCellFormat cellFormat = new
						// WritableCellFormat();
						//
						// String cellValue = testCase.get(column.trim());
						//
						// // cellFormat.setBackground(colour, pattern);
						//
						// if (column.trim().equals("Error Screen Output")
						// && cellValue != null
						// && cellValue.length() > 0) {
						// WritableFont cellFont = new WritableFont(
						// WritableFont.TIMES, 13,
						// WritableFont.NO_BOLD, false,
						// UnderlineStyle.SINGLE, Colour.BLUE);
						// cellFormat = new WritableCellFormat(cellFont);
						//
						// WritableHyperlink hl = new WritableHyperlink(j,
						// i + existingsheetSize,
						//
						// new File(cellValue), cellValue);
						//
						// sheet.addHyperlink(hl);
						// }
						//
						// cellFormat.setBorder(Border.ALL,
						// BorderLineStyle.THIN, Colour.BLACK);
						// cellFormat.setWrap(true);
						//
						// cellFormat.setAlignment(Alignment.LEFT);
						//
						// cellFormat
						// .setVerticalAlignment(VerticalAlignment.TOP);
						//
						// label = new Label(j, i + existingsheetSize,
						// cellValue, cellFormat);
						//
						// }

						try {
							sheet.addCell(label);
						} catch (WriteException e) {
							// TODO Auto-generated catch block
							success = false;
							e.printStackTrace();
						}
						j++;
					}

				}

			}
			// System.out.println("No of Columns: " + sheet.getColumns());
			// System.out.println("No of Rows: " + sheet.getRows());
			workbook.write();
			workbook.close();

		} catch (IOException | WriteException e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		} catch (BiffException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return success;
	}

}
