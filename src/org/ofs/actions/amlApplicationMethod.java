package org.ofs.actions;

import org.ofs.source.ConfigProperties;
import org.ofs.tools.XSLBuilder;
import org.openqa.selenium.By;
import org.openqa.selenium.NoSuchElementException;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.ui.Select;

import com.thoughtworks.selenium.Wait;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.concurrent.TimeUnit;

public class amlApplicationMethod {
	Status status = new Status();

	/**
	 * @author Mahendran
	 * @Method amlMenuSelectMenu This Method is used to select the Menu/Sub Menu
	 *         by specified Menu items
	 * @createdOn 6/15/2015
	 */

	public void amlMenuSelectMenu(WebDriver driver, String inputValue,
			String objectType, String inputSelector, String runID,
			String attachments) {
		// Splitting up the Input Menu Items
		List<String> inputMenuItems = Arrays.asList(inputValue.split(";"));
		int inputMenuCount;
		inputMenuCount = inputMenuItems.size();
		// System.out.println("No. of input Menu Items : "+inputMenuCount);
		String eachInputMenu;
		if (inputMenuCount != 0) {
			int childMenuContainerCounter = 1;
			String menuItemsFoundSoFar = "";
			boolean childMenuItemFound = false;
			for (int i = 0; i <= inputMenuCount - 1; i++) {
				eachInputMenu = inputMenuItems.get(i).trim();
				try {
					// <MAIN MENU - START>
					if (i == 0) {
						//to wait untill the Main Menu is displayed
						try {
							Thread.sleep(100);
						} catch (InterruptedException e) {
							// TODO Auto-generated catch block
							e.printStackTrace();
						}
						childMenuItemFound = true; // This is just for Printing
													// the Result
						String inputSelectorMainRoot = inputSelector
								+ "/div[2]";
						WebElement element = driver.findElement(By
								.xpath(inputSelectorMainRoot));
						String mainMenuPresent = element.getAttribute("style")
								.toString().trim();
						// Checking the Main Menu Visibility
						if (mainMenuPresent.contains("visibility: visible")) {
							String inputSelectorMainMenuContainer = inputSelectorMainRoot
									+ "/div";
							// Getting WebElements for the Main Menu Container
							List<WebElement> mainMenuItemsContainer;
							mainMenuItemsContainer = driver.findElements(By
									.xpath(inputSelectorMainMenuContainer));
							String eachMenuItem;
							int mainMenuCount;
							// Get Count of Main Menu
							mainMenuCount = mainMenuItemsContainer.size();
							int mainMenuCounter = 1;
							// Printing the Total number of Main Menu displayed
							// in the Application
							// System.out.println("Total number of Main Menu displayed : "+mainMenuCount);
							String inputSelectorMainMenu;
							WebElement element1;
							// Iterating the Main Menu Counter
							boolean mainMenuFound = false;
							for (WebElement result : mainMenuItemsContainer) {
								inputSelectorMainMenu = inputSelectorMainMenuContainer
										+ "["
										+ mainMenuCounter
										+ "]/table/tbody/tr/td";
								// Getting actual Main Menu
								element1 = driver.findElement(By
										.xpath(inputSelectorMainMenu));
								eachMenuItem = element1.getText().trim();
								// System.out.println("Main Menu <"+mainMenuCounter+"> is '"+eachMenuItem+"'.");
								// compare input and actual MAIN MENU
								if (eachInputMenu.equals(eachMenuItem)) {
									mainMenuFound = true;
									menuItemsFoundSoFar = eachMenuItem;
									result.click(); // Clicking the Main Menu//
									break;
								}
								mainMenuCounter++; // Just for Printing the Main
													// Menu one by one
							} // End of for (WebElement result :
								// mainMenuItemsContainer) {
							if (!mainMenuFound) {
								status.setResult(false);
								status.setMessage("The AML Main Menu is displayed but the Main Menu '"
										+ eachInputMenu
										+ "' is not found, Hence cannot select the Menu '"
										+ inputValue
										+ "', Please check the application Menu or Input Data.");
								break;
							}
						} else {
							status.setResult(false);
							status.setMessage("The AML Main Menu is not displayed or Menu is not Visible since cannot navigate to Menu, '"
									+ inputValue
									+ "' Please check the application Menu.");
							break;
						} // End of if
							// (mainMenuPresent.contains("visibility: visible"))
							// {
							// <MAIN MENU - END>
						// <Child Menu STARTS here>
					} else if (i > 0) {
						// All Child Menu items will start from the DIV 3, so
						// iterating the DIV from 3 and getting the Visible
						// Child Item
						String inputSelecotorChildMenuContainer;
						inputSelecotorChildMenuContainer = inputSelector
								+ "/div";
						List<WebElement> childMenuItemsContainer;
						childMenuItemsContainer = driver.findElements(By
								.xpath(inputSelecotorChildMenuContainer));
						int childMenuContainerCount = 1;

						for (WebElement result : childMenuItemsContainer) {
							childMenuItemFound = false;
							// First 2 DIV not specific to Child Menu so
							// iterating from 3rd DIV
							if (childMenuContainerCount >= 3
									&& childMenuContainerCount > childMenuContainerCounter) {
								String mainMenuPresent = result
										.getAttribute("style");
								if (mainMenuPresent
										.contains("visibility: visible")) {
									String inputSelectorChildMenu = inputSelecotorChildMenuContainer
											+ "["
											+ childMenuContainerCount
											+ "]/div";
									List<WebElement> childMenuItems;
									childMenuItems = driver.findElements(By
											.xpath(inputSelectorChildMenu));
									String eachChildMenuItem;
									for (WebElement result1 : childMenuItems) {
										eachChildMenuItem = result1.getText()
												.toString().trim();
										if (eachInputMenu
												.equals(eachChildMenuItem)) {
											menuItemsFoundSoFar = menuItemsFoundSoFar
													+ ";" + eachChildMenuItem;
											childMenuContainerCounter = childMenuContainerCount;
											childMenuItemFound = true;
											result1.click();
											break;
										} // End of if
											// (eachInputMenu.equals(eachChildMenuItem))
											// {

									}// End of for (WebElement result1 :
										// childMenuItems) {
									if (childMenuItemFound) {
										// To Check the Final Menu Item is
										// Clicked
										if (inputMenuCount == (i + 1)) {
											status.setResult(true);
											status.setMessage("The Menu '"
													+ inputValue
													+ "' is available and clicked. ");
										}
										break;
									} else {
										status.setResult(false);
										status.setMessage("The Child Menu '"
												+ eachInputMenu
												+ "' is not found under the Menu '"
												+ menuItemsFoundSoFar
												+ "' Hence cannont select the Menu '"
												+ inputValue
												+ "', please check the application Menu.");
										break;
									}

								} else {
									status.setResult(false);
									status.setMessage("It seems like there is no Child Menu displayed underneeth the Menu '"
											+ menuItemsFoundSoFar
											+ "', Hence cannot select the Menu '"
											+ inputValue
											+ "' please check the application Menu.");
								} // End of for (WebElement result :
									// childMenuItemsContainer) {
							} // End of if (childMenuContainerCount>=3 &&
								// childMenuContainerCount>childMenuContainerCounter)
								// {
							childMenuContainerCount++;
						} // End of for (WebElement result :
							// childMenuItemsContainer) {
						// Child Menu END>
					}// End of if(i==0) {
				} catch (Error e) {
					status.setResult(false);
					status.setMessage(e.toString());
				}
				if (!childMenuItemFound) {
					// If any Child Menu is not found then Exist the entire loop
					break;
				}

			} // End of for (int i=0 ;i<=inputMenuCount-1; i++) {
		} else {
			status.setResult(false);
			status.setMessage("The Input Main Menu is empty, please check the input value.");
		} // End of if (inputMenuCount!=0) {
	} // End of amlSelectMenu(WebDriver driver, String inputValue, String
		// objectType, String inputSelector) {

	/**
	 * @author Mahendran
	 * @Method amlComplexGridSelectRows This Method is used to select the Grid
	 *         Row (Radio button)
	 * @createdOn 6/15/2015
	 */

	public void amlComplexGridSelectRows(WebDriver driver, String inputValue,
			String objectType, String inputSelector, String runID,
			String attachments) {
		// Splitting up the Input Menu Items
		List<String> inputMenuItems = Arrays.asList(inputValue.split(";"));
		int inputMenuCount;
		inputMenuCount = inputMenuItems.size();
		// System.out.println("No. of input Menu Items : "+inputMenuCount);
		String eachInputRow;
		// Checking whether the input value is null or not
		if (inputMenuCount != 0) {
			// Iterating the Input Value since this Grid support Multi row
			// select (using Check Box)
			String rowFound = "";
			String rowNotFound = "";
			boolean allRowsFound = true;
			boolean noDataFound = false;
			for (int i = 1; i <= inputMenuCount; i++) {
				eachInputRow = inputMenuItems.get(i - 1).trim();
				try {
					String inputSelectorGridRow = inputSelector + "/tbody/tr";
					// Getting Total Rows in the Grid
					List<WebElement> gridRowsCotainer;
					gridRowsCotainer = driver.findElements(By
							.xpath(inputSelectorGridRow));
					int gridRowCount = gridRowsCotainer.size();
					//To handle the Grid has Not data and display text as "No Data Found"					
					WebElement noDatElement = null;
					String actualValue = "";
					if (gridRowCount==1) {
						String inputSelectorNoData = inputSelector + "/tbody/tr[1]";
						noDatElement = driver.findElement(By.xpath(inputSelectorNoData));
						if (!noDatElement.getAttribute("class").contains("row-value")) {
							noDataFound = true;
							break;
						}
					}
					// System.out.println("Total No. Rows present in the Grid is : '"+gridRowCount+"'.");
					int gridRowCounter = 1;
					boolean itemFound = false;
					int columnIndex[] = amlComplexGridColumnIndex(driver,"Customer Name");
					System.out.println("Column Index '"+columnIndex[0]+"' and "+columnIndex[1]+".");
					for (WebElement result : gridRowsCotainer) {
						// In this AML Grid Type there will be an unique
						// identifier (Unique Column, ex., "Customer Name" in
						// Alert Table"),
						// So Row selection login will be based on the unique
						// value.
						String inputSelectorUniqueColumn = inputSelectorGridRow
								+ "[" + gridRowCounter
								+ "]/td["+columnIndex[0]+"]/table/tbody/tr/td/a/u";
						WebElement gridRowUniqueElement;
						try {
							gridRowUniqueElement = driver.findElement(By
									.xpath(inputSelectorUniqueColumn));
						} catch (NoSuchElementException e) {
							 inputSelectorUniqueColumn = inputSelectorGridRow
									+ "[" + gridRowCounter
									+ "]/td["+columnIndex[0]+"]";
							 gridRowUniqueElement = driver.findElement(By
										.xpath(inputSelectorUniqueColumn));
						}
						
						String eachUniqueValue = gridRowUniqueElement.getText()
								.toString().trim();
						// System.out.println("Grid Row<"+gridRowCounter+"> : "+eachUniqueValue+".");
						// Compare the Input Data with actual Unique value to
						// select the row
						if (eachInputRow.equals(eachUniqueValue)) {
							itemFound = true;
							rowFound = rowFound + ";" + eachInputRow;
							// To select the Row Check box
							int columnCount = columnIndex[1]-1;
							String inputSelectorGridRowCheckBox;
							inputSelectorGridRowCheckBox = inputSelectorGridRow
									+ "["
									+ gridRowCounter
									+ "]/td["+columnCount+"]/input[@type='checkbox']";
							WebElement gridRowCheckBoxElement;
							try {
								gridRowCheckBoxElement = driver
										.findElement(By
												.xpath(inputSelectorGridRowCheckBox));
							} catch (NoSuchElementException e) {
								inputSelectorGridRowCheckBox = inputSelectorGridRow
										+ "["
										+ gridRowCounter
										+ "]/td["+columnCount+"]/input[@type='radio']";
								gridRowCheckBoxElement = driver
										.findElement(By
												.xpath(inputSelectorGridRowCheckBox));
							}
							
							boolean checkBoxSelected = gridRowCheckBoxElement
									.isSelected();
							if (!checkBoxSelected) {
								gridRowCheckBoxElement.click();
							} // End of if (!checkBoxSelected) {
							break;
						} // End of if (eachInputMenu.equals(eachUniqueValue)) {
						gridRowCounter++; // increasing the count for every Row
					} // End of for (WebElement result : gridRowsCotainer) {
					if (!itemFound) {
						rowNotFound = rowNotFound + ";" + eachInputRow;
						allRowsFound = false;
					}
				} catch (Error e) {
					status.setResult(false);
					status.setMessage(e.toString());
				}

			} // ENd of for (int i=0 ;i<=inputMenuCount-1; i++) {
				// Setting up the Final Result
			if (noDataFound) {
				status.setResult(false);
				status.setMessage("The grid has no data hence cannot slect the row '"+inputValue+"', please check the application.");
			}else if (allRowsFound) {
				status.setResult(true);
				status.setMessage("The Rows are found and selected, {"
						+ rowFound + "}.");
			} else {
				status.setResult(false);
				status.setMessage("It seems like one or more rows are not found, hence cannon select all the Row, {Selected Rows : "
						+ rowFound
						+ "} {Rows Not Found : "
						+ rowNotFound
						+ "}.");
			}

		} else {
			status.setResult(false);
			status.setMessage("The Input Main Menu is empty, please check the input value.");
		} // End of if (inputMenuCount!=0) {
	} // END of public void amlGridSelectRow(WebDriver driver, String
		// inputValue, String objectType, String inputSelector) {

	/**
	 * @author Mahendran
	 * @Method amlComplexValidateGrid This Method is used to select the Grid Row
	 *         (Radio button)
	 * @createdOn 6/17/2015
	 */
	public void amlComplexGridValidateGrid(WebDriver driver, String inputValue,
			String objectType, String inputSelector, String runID,
			String attachments) {
		// This List to collect all the Row data and create output sheet.
		List<HashMap<String, String>> gridData = new ArrayList<HashMap<String, String>>();
		try {

			// TO Get the Grid Column Headers
			String inputSelectorColumn;
			List<WebElement> columnList;
			// For this type of AML Grid has the Same Column Element
			inputSelectorColumn = "//tr[@class='row-label th-bc']/th";
			columnList = driver.findElements(By.xpath(inputSelectorColumn));
			// For Column Header
			int columnSize = columnList.size();
			String[] ColumnHeader;
			boolean columnTable = false; // To Handle the Inherit column Name
											// since if the Column Header is td
											// then no inherit column applicable
			// For some Grid (ex., CTR1 the Column Header is under the td
			// instead of th so this additional steps to handle that scenario
			if (columnSize == 0) {
				inputSelectorColumn = "//tr[@class='row-label th-bc']/td[@class='wrap-td']";
				columnList = driver.findElements(By.xpath(inputSelectorColumn));
				columnSize = columnList.size();
				columnTable = true;
				columnSize = columnSize + 1;// To get the Last column Value
											// (Radio Button)
				ColumnHeader = new String[columnSize];
			} else {
				ColumnHeader = new String[columnSize - 1];
			}
			int columnCounter = 1;
			String columnName;
			for (WebElement list : columnList) {
				// To get the Inherit Column Header
				if (columnTable) {
					// To get the Last column Value (Radio Button)
					if (columnCounter == columnSize - 1) {
						ColumnHeader[columnCounter] = "";
					}
					columnName = list.getText().toString().trim();
					// System.out.println("Column Header <"+columnCounter+"> : "+columnName);
					if (columnName != null) {
						ColumnHeader[columnCounter - 1] = columnName;
					}
				} else {
					String ignoreColumnHeader = list.getAttribute("width")
							.toString().trim();
					int ignoreColumnHeaderIdentifier = Integer
							.parseInt(ignoreColumnHeader.replace("%", ""));
					if ((ignoreColumnHeaderIdentifier > 40)) {
						String inputSelectorIgnoreColumn = inputSelectorColumn
								+ "[" + columnCounter + "]/table";
						List<WebElement> ignoreColumnHeaderElements;
						ignoreColumnHeaderElements = driver.findElements(By
								.xpath(inputSelectorIgnoreColumn));
						if ((ignoreColumnHeaderElements.size() > 0)) {
							columnCounter--;
						} else {
							columnName = list.getText().toString().trim();
							// System.out.println("Column Header <"+columnCounter+"> : "+columnName);
							if (columnName != null) {
								ColumnHeader[columnCounter - 1] = columnName;
							}
						} // if ((ignoreColumnHeaderElements.size()>0)) {
					} else {
						columnName = list.getText().toString().trim();
						// System.out.println("Column Header <"+columnCounter+"> : "+columnName);
						if (columnName != null) {
							ColumnHeader[columnCounter - 1] = columnName;
						}
					} // End of if (!(ignoreColumnHeaderIdentifier > 40)) {
				} // End of if (columnTable) {
				columnCounter++;
			} // End of for (WebElement list : columnList) {

			// TO Get Grid Row
			String inputSelectorGridRow = inputSelector + "/tbody/tr";
			// Getting Total Rows in the Grid
			List<WebElement> gridRowsCotainer;
			gridRowsCotainer = driver.findElements(By
					.xpath(inputSelectorGridRow));			
			int gridRowCount = gridRowsCotainer.size();
			// System.out.println("Total No. Rows present in the Grid is : '"+gridRowCount+"'.");
			//To handle the Grid has Not data and display text as "No Data Found"
			boolean noDataFound = false;
			WebElement noDatElement = null;
			String actualValue = "";
			if (gridRowCount==1) {
				String inputSelectorNoData = inputSelector + "/tbody/tr[1]";
				noDatElement = driver.findElement(By.xpath(inputSelectorNoData));
				if (!noDatElement.getAttribute("class").contains("row-value")) {
					noDataFound = true;
				}
			}
			if (noDataFound) {				
				actualValue = noDatElement.getText().toString().trim();
				//To Check the Expected text match with the actual
				if(inputValue.equals(actualValue)) {
					status.setResult(true);
					status.setMessage("The Grid has no data and the expected Text message '"+inputValue+"' is matched with the actual value '"+actualValue+"' for the grid ObjectName");
				} else {
					status.setResult(false);
					status.setMessage("The Grid has no data but the expected Text message '"+inputValue+"' does Not match with the actual value '"+actualValue+"' for the grid ObjectName");
				}
			} else {
				int gridRowCounter = 1;
				String celldata;
				for (WebElement result : gridRowsCotainer) {
					HashMap<String, String> rowdata = new HashMap<String, String>();
					// System.out.println("ROW  <<"+gridRowCounter+">>");
					String inputSelectorGridColumn = inputSelectorGridRow + "["
							+ gridRowCounter + "]/td";
					// Getting Total Rows in the Grid
					List<WebElement> gridRowsColumnCotainer;
					gridRowsColumnCotainer = driver.findElements(By
							.xpath(inputSelectorGridColumn));
					int gridRowColumnCount = gridRowsColumnCotainer.size();
					// System.out.println("Total No. Root Column present in the Grid is : '"+gridRowColumnCount+"'.");
					int gridRowColumnCounter = 1;
					int gridRowInheritColumnCounter = 0;
					int gridRowInheritColumnCount = 0;
					for (WebElement result1 : gridRowsColumnCotainer) {
						// To Get the Inherit columns
						// This valign only property is differs from other column
						// String inheritColumn =
						// result1.getAttribute("valign").toString().trim();
						try {
							String inheritColumn = result1.getAttribute("valign")
									.toString().trim();
							String inputSelectorGridInheritColumn = inputSelectorGridColumn
									+ "["
									+ gridRowColumnCounter
									+ "]/table/tbody/tr/td";
							// Getting Total Rows in the Grid
							List<WebElement> gridRowsInheritColumnCotainer;
							gridRowsInheritColumnCotainer = driver.findElements(By
									.xpath(inputSelectorGridInheritColumn));
							gridRowInheritColumnCount = gridRowsInheritColumnCotainer
									.size();
							// System.out.println("Total No. Root Column present in the Grid is : '"+gridRowColumnCount+"'.");
							// gridRowInheritColumnCounter=gridRowColumnCounter;
							// Iterating the Inherit Columns
							for (WebElement result2 : gridRowsInheritColumnCotainer) {
								celldata = result2.getText().toString().trim();
								// System.out.println("COLUMN INHERIT  <<"+((gridRowColumnCounter+gridRowInheritColumnCounter)-1)+">>  : "+celldata);
								rowdata.put(
										ColumnHeader[(gridRowColumnCounter + gridRowInheritColumnCounter) - 1],
										celldata); // writing Cell data into Array
								gridRowInheritColumnCounter++;
							}
						} catch (NullPointerException e) {
							// To Get the Check Box or Radio controls selections
							celldata = result1.getText().toString().trim();
							int tempCount;
							if (columnTable) {
								tempCount = gridRowColumnCounter;
							} else {
								tempCount = gridRowColumnCounter + 1;
							}

							if ((tempCount) == (gridRowColumnCount)) {
								try {
									celldata = "";
									String inputSelectorLastColumn;
									inputSelectorLastColumn = inputSelectorGridColumn
											+ "[" + (tempCount) + "]/input";
									List<WebElement> lastColumnElements;
									lastColumnElements = driver.findElements(By
											.xpath(inputSelectorLastColumn));
									for (WebElement result3 : lastColumnElements) {
										if (result3.isDisplayed()) {
											String typeOfControl = result3
													.getAttribute("type")
													.toString().trim();
											boolean isSelected = false;
											if (typeOfControl.equals("checkbox")
													|| (typeOfControl
															.equals("radio"))) {
												celldata = "Unchecked";
												isSelected = result3.isSelected();
												if (isSelected) {
													celldata = "Checked";
												}
											} // End of if
												// (typeOfControl.equals("checked"))
												// {
											break;
										} // if (result3.isDisplayed()) {
									} // ENd of for (WebElement result3 :
										// lastColumnElements ) {
										// Checking whether Check Box or Radio
										// Button

								} catch (NoSuchElementException exception) {
									status.setResult(false);
									status.setMessage(exception.toString());
								}
							} // End of if(gridRowColumnCount==gridRowColumnCounter)
								// {
								// System.out.println("COLUMN ROOT  <<"+((gridRowColumnCounter+gridRowInheritColumnCounter)-1)+">>  : "+celldata);
								// if (columnTable) {
							// rowdata.put(
							// ColumnHeader[(gridRowColumnCounter +
							// gridRowInheritColumnCounter)], celldata); // writing
							// Cell data into Array
							// } else {
							rowdata.put(
									ColumnHeader[(gridRowColumnCounter + gridRowInheritColumnCounter) - 1],
									celldata); // writing Cell data into Array
							// }

							gridRowColumnCounter++;
						}
					} // End of for (WebElement result1 : gridRowsColumnCotainer) {
					if (rowdata != null)
						gridData.add(rowdata); // Adding entire Row data into Array
					gridRowCounter++;
				} // End of for (WebElement result : gridRowsCotainer) {

				// Compare the expected and actual grid data
				String outputPath = ConfigProperties.get("appPath")
						+ "//test.results//" + runID + "//OutputFiles//"
						+ inputValue.replace(".xls", "_Output.xls");
				XSLBuilder xslBuilder = new XSLBuilder(outputPath, gridData,
						"Report Result Grid", ColumnHeader);
				boolean success = xslBuilder.constructTargetXLS(attachments + "//"
						+ inputValue);
				// Setting up the Final Result
				if (success) {
					status.setResult(true);
					status.setMessage("Input sheet matched with the actual data.");
				} else {
					status.setResult(false);
					status.setMessage("Input sheet is Not matched with the actual data., please check the Output sheet '"
							+ outputPath + ".");
				}
			} //End of 	if (gridRowCount==1) {
			

		} catch (Error e) {
			status.setResult(false);
			status.setMessage(e.toString());
		}
	} // END of public void amlComplexValidateGrid(WebDriver driver, String
		// inputValue, String objectType, String inputSelector) {

	/**
	 * @author Mahendran
	 * @Method amlComplexGridClickColumnHeader This Method is used to select the
	 *         Grid Row (Radio button)
	 * @createdOn 6/17/2015
	 */
	public void amlComplexGridClickColumnHeader(WebDriver driver,
			String inputValue, String objectType, String inputSelector,
			String runID, String attachments) {
		try {
			boolean columnTable = false;
			// TO Get the Grid Column Headers
			String inputSelectorColumn;
			List<WebElement> columnList;
			// For this type of AML Grid has the Same Column Element
			inputSelectorColumn = "//tr[@class='row-label th-bc']/th";
			columnList = driver.findElements(By.xpath(inputSelectorColumn));
			// For Column Header
			int columnSize = columnList.size();
			boolean columnFound = false;
			boolean sortableColumn = false;
			// To Handle the Inherit column Name since if the Column Header is
			// td then no inherit column applicable
			// For some Grid (ex., CTR1 the Column Header is under the td
			// instead of th so this additional steps to handle that scenario
			if (columnSize == 0) {
				columnTable = true;
				inputSelectorColumn = "//tr[@class='row-label th-bc']/td[@class='wrap-td']";
				columnList = driver.findElements(By.xpath(inputSelectorColumn));
				columnSize = columnList.size();
			}
			int columnCounter = 1;
			String columnName = "";
			if (columnTable) {
				for (WebElement list : columnList) {
					// To get the Inherit Column Header

					columnName = list.getText().toString().trim();
//					System.out.println("Column Name : " + columnName);
					// System.out.println("Column Header <"+columnCounter+"> : "+columnName);
					if (inputValue.equals(columnName)) {
						columnFound = true;
						String inputSelectorSortElements = inputSelectorColumn
								+ "[" + columnCounter + "]/a";
						List<WebElement> sortElements = driver.findElements(By
								.xpath(inputSelectorSortElements));
						int sortableCount = sortElements.size();
						if (sortableCount > 0) {
							String inputSelectorSortElement = inputSelectorColumn
									+ "[" + columnCounter + "]/a[1]";
							WebElement sortElement = driver.findElement(By
									.xpath(inputSelectorSortElement));
							sortElement.click();
							sortableColumn = true;
						}
						break;
					} // End of if (inputValue.equals(columnName)) {

					columnCounter++;
				} // End of for (WebElement list : columnList) {
			} else {

				String inputSelectorIgnoreColumn = "";
				for (WebElement list : columnList) {
					// // To get the Inherit Column Header

					int validColumnCounter = 1;
					String ignoreColumnHeader = list.getAttribute("width")
							.toString().trim();
					int ignoreColumnHeaderIdentifier = Integer
							.parseInt(ignoreColumnHeader.replace("%", ""));
					if ((ignoreColumnHeaderIdentifier > 40)) {
						inputSelectorIgnoreColumn = inputSelectorColumn + "["
								+ columnCounter + "]/table/tbody/tr/th";
						List<WebElement> ignoreColumnHeaderElements;
						ignoreColumnHeaderElements = driver.findElements(By
								.xpath(inputSelectorIgnoreColumn));
						if ((ignoreColumnHeaderElements.size() > 0)) {
							List<WebElement> inheritColumnElements = driver
									.findElements(By
											.xpath(inputSelectorIgnoreColumn));

							for (WebElement list1 : inheritColumnElements) {
								columnName = list1.getText().toString().trim();

								if (inputValue.equals(columnName)) {
									columnFound = true;

									break;
								}
								validColumnCounter++;
							} // End of for (WebElement list1 : columnList) {

						} // if ((ignoreColumnHeaderElements.size()>0)) {
					} // End of if ((ignoreColumnHeaderIdentifier > 40)) {
					if (columnFound) {
						String inputSelectorSortElements = inputSelectorIgnoreColumn
								+ "[" + validColumnCounter + "]/a";
						List<WebElement> sortElements = driver.findElements(By
								.xpath(inputSelectorSortElements));
						int sortableCount = sortElements.size();
						if (sortableCount > 0) {
							String inputSelectorSortElement = inputSelectorIgnoreColumn
									+ "[" + validColumnCounter + "]/a[1]";
							WebElement sortElement = driver.findElement(By
									.xpath(inputSelectorSortElement));
							sortElement.click();
							sortableColumn = true;
						}

						break;
					} else {
						columnName = list.getText().toString().trim();
						if (inputValue.equals(columnName)) {
							columnFound = true;
							String inputSelectorSortElements = inputSelectorColumn
									+ "[" + columnCounter + "]/a";
							List<WebElement> sortElements = driver
									.findElements(By
											.xpath(inputSelectorSortElements));
							int sortableCount = sortElements.size();
							if (sortableCount > 0) {
								String inputSelectorSortElement = inputSelectorColumn
										+ "[" + columnCounter + "]/a[1]";
								WebElement sortElement = driver.findElement(By
										.xpath(inputSelectorSortElement));
								sortElement.click();
								sortableColumn = true;
							}

							break;
						} // End of if (inputValue.equals(columnName)) {
					} // End of if (columnFound && inheritColumn) {
					columnCounter++;
				} // End of for (WebElement list : columnList) {
			} // End of if(columnTable) {
				// Setting up the Final Result
			if (columnFound) {
				if (sortableColumn) {
					status.setResult(true);
					status.setMessage("The Column Name '" + inputValue
							+ "' is found and Clicked.");
				} else {
					status.setResult(false);
					status.setMessage("The Column Name '"
							+ inputValue
							+ "' is found and which is not Sortable Column Header, hence cannot click the column header, please check the application.");
				}
			} else {
				status.setResult(false);
				status.setMessage("The Column Name '"
						+ inputValue
						+ "' is Not found and hence cannot click the column header, please check the application.");
			}
		} catch (Error e) {
			status.setResult(false);
			status.setMessage(e.toString());
		}

	} // End of public void amlComplexGridClickColumnHeader
	
	/**
	 * @author Mahendran
	 * @Method amlComplexGridColumnIndex This method is used to get the Column Index to Select the AML Complex Grid Row
	 * NOTE - This method is internal to use inside the Method amlComplexGridClickColumnHeader
	 * @createdOn 6/17/2015
	 */
	public int[] amlComplexGridColumnIndex(WebDriver driver, String inputValue) {
		int columnIndex = 0;
		int[] columnReturn = new int[2];
		int columnSize = 0;
		try {
			boolean columnTable = false;
			// TO Get the Grid Column Headers
			String inputSelectorColumn;
			List<WebElement> columnList;
			// For this type of AML Grid has the Same Column Element
			inputSelectorColumn = "//tr[@class='row-label th-bc']/th";
			columnList = driver.findElements(By.xpath(inputSelectorColumn));
			// For Column Header
			columnSize = columnList.size();			
			boolean columnFound = false;
			boolean sortableColumn = false;
			// To Handle the Inherit column Name since if the Column Header is
			// td then no inherit column applicable
			// For some Grid (ex., CTR1 the Column Header is under the td
			// instead of th so this additional steps to handle that scenario
			if (columnSize == 0) {
				columnTable = true;
				inputSelectorColumn = "//tr[@class='row-label th-bc']/td";
				columnList = driver.findElements(By.xpath(inputSelectorColumn));
				columnSize = columnList.size();
				columnSize--; //Its taking another td from the footer page, to ignore that the count is reduced
			} else {
				columnSize =columnSize - 3; //Due to Inherit Column, reducing the count 3
			}
			int columnCounter = 1;
			
			String columnName = "";
			if (columnTable) {
				for (WebElement list : columnList) {
					// To get the Inherit Column Header

					columnName = list.getText().toString().trim();
//					System.out.println("Column Name : " + columnName);
					// System.out.println("Column Header <"+columnCounter+"> : "+columnName);
					if (inputValue.equals(columnName)) {
						columnFound = true;	
						columnIndex = columnCounter;
						break;
					} // End of if (inputValue.equals(columnName)) {

					columnCounter++;
				} // End of for (WebElement list : columnList) {
			} else {

				String inputSelectorIgnoreColumn = "";
				for (WebElement list : columnList) {
					// // To get the Inherit Column Header

					int validColumnCounter = 1;
					String ignoreColumnHeader = list.getAttribute("width")
							.toString().trim();
					int ignoreColumnHeaderIdentifier = Integer
							.parseInt(ignoreColumnHeader.replace("%", ""));
					if ((ignoreColumnHeaderIdentifier > 40)) {
						inputSelectorIgnoreColumn = inputSelectorColumn + "["
								+ columnCounter + "]/table/tbody/tr/th";
						List<WebElement> ignoreColumnHeaderElements;
						ignoreColumnHeaderElements = driver.findElements(By
								.xpath(inputSelectorIgnoreColumn));
						if ((ignoreColumnHeaderElements.size() > 0)) {
							List<WebElement> inheritColumnElements = driver
									.findElements(By
											.xpath(inputSelectorIgnoreColumn));

							for (WebElement list1 : inheritColumnElements) {
								columnName = list1.getText().toString().trim();

								if (inputValue.equals(columnName)) {
									columnFound = true;
									columnIndex = columnCounter + validColumnCounter;
									break;
								}
								validColumnCounter++;
							} // End of for (WebElement list1 : columnList) {
							

						} // if ((ignoreColumnHeaderElements.size()>0)) {
					} // End of if ((ignoreColumnHeaderIdentifier > 40)) {
					if (columnFound) {						
						columnIndex = columnCounter;
						break;
					} else {
						columnName = list.getText().toString().trim();
						if (inputValue.equals(columnName)) {
							columnFound = true;
							columnIndex = columnCounter;

							break;
						} // End of if (inputValue.equals(columnName)) {
					} // End of if (columnFound && inheritColumn) {
					columnCounter++;
				} // End of for (WebElement list : columnList) {
			} // End of if(columnTable) {
				// Setting up the Final Result
			if (columnFound) {
				columnReturn[0] = columnIndex;
				columnReturn[1] = columnSize;
				return columnReturn;
			} else {
				columnIndex = 0;
				columnReturn[0] = columnIndex;
				columnReturn[1] = columnSize;
				return columnReturn;
			}
		} catch (Error e) {
			columnReturn[0] = columnIndex;
			columnReturn[1] = columnSize;
			return columnReturn;
		}

	} // End of public void amlComplexGridClickColumnHeader
} // End of Class