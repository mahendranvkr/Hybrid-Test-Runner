package org.ofs.actions;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.NoSuchElementException;
import java.util.Set;
import java.util.concurrent.TimeUnit;

import org.ofs.tools.*;
import org.ofs.source.ConfigProperties;
import org.openqa.selenium.Alert;
import org.openqa.selenium.By;
import org.openqa.selenium.Dimension;
import org.openqa.selenium.NoAlertPresentException;
import org.openqa.selenium.NotFoundException;
import org.openqa.selenium.Platform;
import org.openqa.selenium.UnhandledAlertException;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebDriverException;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.remote.DesiredCapabilities;
import org.openqa.selenium.remote.RemoteWebDriver;
import org.openqa.selenium.remote.UnreachableBrowserException;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.Select;
import org.openqa.selenium.support.ui.WebDriverWait;
import org.openqa.selenium.JavascriptExecutor;

public class Actions {
	WebElement webElement; // Mahendran added since using this element since its
							// used by several methods
	WebDriver driver;
	String resultCode = "";
	String resultMsg = "";
	String stackTrace = "";
	Boolean stepContinue = true;
	String attachments = "";
	String runID = "";
	Status status = new Status(true, "");
	List<HashMap<String, String>> ObjectRepository;

	public Actions() {
		// read propperties file
		// read repo object file
		XSLBuilder xslReader = new XSLBuilder(ConfigProperties.get("appPath")
				+ "//objct.repository//objectRepository.xls");
		ObjectRepository = xslReader.convertXSLtoObject("Object Repository");

	}

	String getAction(String ObjectName) {
		String action = "";

		return action;
	}

	/**
	 * @author Mahendran.R
	 * 
	 *         This method is used to open on the specified URL on the specified
	 *         browser/operating system using the selenium grid
	 * @return
	 * @createdOn 5/6/2014
	 */
	public Status openBrowser(HashMap<String, String> testcase) {// throws
																	// MalformedURLException
																	// {
		String operatingSystem = (ConfigProperties.get("operatingSystem")
				.trim());
		String browser = (ConfigProperties.get("browser").trim());
		String nodeUrl = "";
		if (operatingSystem.equals("windows")) {
			nodeUrl = ConfigProperties.get("nodeUrl" + browser);
		}
		String inputValue = (String) testcase.get("Input Value");
		try {

			switch (operatingSystem) {

			case "windows":

				switch (browser) {

				case "chrome":

					DesiredCapabilities capWinCrome = DesiredCapabilities
							.chrome();

					capWinCrome.setBrowserName(browser);

					capWinCrome.setPlatform(Platform.WINDOWS);

					driver = new RemoteWebDriver(new URL(nodeUrl), capWinCrome);

					break;

				case "firefox":

					DesiredCapabilities capWinff = DesiredCapabilities
							.firefox();

					capWinff.setBrowserName(browser);

					capWinff.setPlatform(Platform.WINDOWS);

					driver = new RemoteWebDriver(new URL(nodeUrl), capWinff);

					break;

				case "internetexplorer":

					DesiredCapabilities capWinIE = DesiredCapabilities
							.internetExplorer();

					capWinIE.setBrowserName(browser);

					capWinIE.setPlatform(Platform.WINDOWS);

					driver = new RemoteWebDriver(new URL(nodeUrl), capWinIE);

					break;

				case "safari":

					DesiredCapabilities capWinSafari = DesiredCapabilities
							.safari();

					capWinSafari.setBrowserName(browser);

					capWinSafari.setPlatform(Platform.WINDOWS);

					driver = new RemoteWebDriver(new URL(nodeUrl), capWinSafari);

					break;

				}

				break;

			case "mac":

				switch (browser) {

				case "safari":

					DesiredCapabilities capMacSafari = DesiredCapabilities
							.safari();

					capMacSafari.setBrowserName(browser);

					capMacSafari.setPlatform(Platform.MAC);

					driver = new RemoteWebDriver(new URL(nodeUrl), capMacSafari);

					break;

				}

				break;

			}

			String currentApp = (ConfigProperties.get("curentRunApp").trim());
			String nodeURL = ConfigProperties.get(currentApp).trim();
			if (currentApp.equals(inputValue)) {
				driver.get(nodeURL + "/");
				driver.manage().timeouts().implicitlyWait(10, TimeUnit.SECONDS);

				driver.manage().window().maximize();

				resultCode = "Pass";
				status.setResult(true);
				status.setMessage("The Base URL '" + inputValue
						+ "' is successfully launched on <'" + browser
						+ "> Browser, <" + operatingSystem
						+ "> and the Node is '" + nodeUrl + ".");
				// <<<<<SPECIFIC TO AML STARTS HERE>>>>>>
				// Mahendran added thos code on 6/11/2015
				// This is specific to AML application to handle the pop up
				// window
				if (currentApp.equals("aml")) {
					String windowTitle = "Base60AML";
					Set<String> windows = driver.getWindowHandles();
					for (String window : windows) {
						if (!driver.getTitle().contains(windowTitle)) {
							driver.switchTo().window(window);
							System.out.println("Window Titile :"
									+ driver.getTitle().toString());
						} else {
							try {
								Thread.sleep(500);
							} catch (InterruptedException e) {
								// TODO Auto-generated catch block
								e.printStackTrace();
							}
						}

					}
				}

				// <<<<<SPECIFIC TO AML ENDS HERE>>>>>>
			} else {
				System.out
						.println("Current application URL in the Config file '"
								+ ConfigProperties.get("curentRunApp")
								+ "' Does NOT match with the input Value '"
								+ inputValue
								+ "' , please check the config file and test case");
				status.setResult(false);
				status.setMessage("Current application URL in the Config file '"
						+ ConfigProperties.get("curentRunApp")
						+ "' Does NOT match with the input Value '"
						+ inputValue
						+ "' , please check the config file and test case");

			}

		} catch (MalformedURLException e) {
			System.out.println("[ERROR] : It seems like the node URL has not yet set in the config file for the browser '"+browser+"', please check the node URL in config file.");
			status.setResult(false);
			stackTrace = stackTrace + e.toString();
			stepContinue = false;

		} catch (WebDriverException e) {

			status.setResult(false);
			status.setMessage("It seems like the Selenium Grid's Hub and Node are not started, please check the Selenium Server Hub and Node(s)");
			System.out.println("[ERROR] : It seems like the Selenium Grid's Hub and Node are not started, please check the Selenium Server Hub and Node(s)");
			stackTrace = stackTrace + e.toString();
			stepContinue = false;			
		}

		if (!stepContinue) {
			System.out.println(stackTrace);
		}
		return status;
	}

	// Mahendran Added on 10/30
	// this method is used to close the browser after every test case gets
	// executed

	public void closeBrowser() {
		try {			
			driver.quit();
		} catch (WebDriverException e) {
			status.setResult(false);
			stackTrace = stackTrace + e.toString();
			stepContinue = false;
		}

	}

	By getTestObject(String objectName) {
		By objectId = null;
		for (HashMap<String, String> row : ObjectRepository) {
			if (row.get("Object Name").equals(objectName)) {
				String LocatingBy = row.get("Locating By");
				String selector = row.get("Element");

				if (LocatingBy.contains("CSS Selector")) {
					objectId = By.cssSelector(selector);
				} else if (LocatingBy.contains("by Name")) {
					objectId = By.name(selector);
				} else if (LocatingBy.contains("by ID")) {
					objectId = By.id(selector);
				} else if (LocatingBy.contains("by Xpath")) {
					objectId = By.xpath(selector);
				}
				return objectId;
			}
		}

		// TODO Auto-generated method stub
		return null;
	}

	/*
	 * To Get the Selector Name for WebElements
	 */

	String getTestSelector(String objectName) {

		for (HashMap<String, String> row : ObjectRepository) {
			if (row.get("Object Name").equals(objectName)) {
				String selector = row.get("Element");
				return selector;
			}
		}

		// TODO Auto-generated method stub
		return null;
	}

	public Status executeTestCase(HashMap<String, String> testCase) {
		String action = testCase.get("Action").trim();
		String testOutputPath = ConfigProperties.get("appPath")
				+ "//test.results";
		String objectType = testCase.get("Object Type").trim(); // Mahendran
																// Added
		String objectName = testCase.get("Object Name").trim();
		String inputValue = testCase.get("Input Value").trim();
		By element = getTestObject(objectName);
		runID = testCase.get("Run ID"); // to Pass the Run ID to any
										// Function
		attachments = ConfigProperties.get("appPath") + "//test.case//"
				+ testCase.get("Test Suite ID") + "//attachments//"
				+ testCase.get("Test Case ID");

		ActionMethods actions = new ActionMethods();
		amlApplicationMethod amlActions = new amlApplicationMethod(); // 6/12/2015
																		// :
																		// Mahendran
																		// :
																		// Added
																		// this
																		// for
																		// AML
		Class c = actions.getClass();
		Class amlClass = amlActions.getClass();// 6/12/2015 : Mahendran : Added
												// this for AML
		boolean alertPresent = false; // 6/22/15 - Mahendran added this to
										// handle the alert dialog
		String alertMessage = "";
		if (element != null) {
			boolean objectFound = false;
			webElement = null;
			// parameter type is null

			try {
				webElement = driver.findElement(element);
				if (webElement != null) {
					objectFound = true;
				}
			} catch (NoSuchElementException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
				status.setResult(false);
				status.setMessage("The Object '"
						+ objectName
						+ "' does not exist <NoSuchElement>, hence cannot perform the'"
						+ action
						+ "', please check application's object or check the Object's element in the Object repository.");
			} catch (NotFoundException e) {
				// TODO Auto-generated catch block
				// e.printStackTrace();
				status.setResult(false);
				status.setMessage("The Object '"
						+ objectName
						+ "' does not exist<NotFound>, hence cannot perform the'"
						+ action
						+ "', please check application's object or check the Object's element in the Object repository.");
			} catch (UnhandledAlertException e) {
				// To Handle unhandled Alert dialog
				alertPresent = true;
				try {
					driver.switchTo().alert();
					Alert alert = driver.switchTo().alert();
					alertMessage = alert.getText();
					status.setResult(false);
					status.setMessage("Unexpected Alert dialog is displayed '"
							+ alertMessage
							+ "', Hybrid Test Runner dismissed the Alert. please check the Results for more details.");
					alert.dismiss();
				} // try
				catch (NoAlertPresentException Ex) {
					status.setResult(false);
					status.setMessage("Unexpected Alert dialog is displayed '"
							+ alertMessage
							+ "', Hybrid Test Runner dismissed the Alert. please check the Results for more details.");
				} // catch

			}

			// parameter type is null
			if (objectFound) {
				String methodName;

				if (objectType.toLowerCase().trim().startsWith("aml")) {
					Class[] cArg = new Class[6];
					cArg[0] = WebDriver.class;
					cArg[1] = String.class; // inputValue
					cArg[2] = String.class; // Object Type
					cArg[3] = String.class; // Input Selector String
					cArg[4] = String.class; // runID
					cArg[5] = String.class; // attachment

					methodName = getMethodName(action, objectType);
					try {
						Method m = amlClass.getMethod(methodName, cArg);
						String inputSelector = getTestSelector(objectName);
						m.invoke(amlActions, driver, inputValue, objectType,
								inputSelector, runID, attachments);
					} catch (IllegalAccessException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					} catch (IllegalArgumentException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					} catch (InvocationTargetException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					} catch (NoSuchMethodException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					} catch (SecurityException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
						// 6/22/15 Mahendran added this to handle the un handled
						// Alert dialog
					}
				} else {
					Class[] cArg = new Class[2];
					cArg[0] = WebElement.class;
					cArg[1] = String.class;
					methodName = getMethodName(action, objectType);
					try {
						Method m = c.getMethod(methodName, cArg);
						m.invoke(actions, webElement, inputValue);
					} catch (IllegalAccessException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					} catch (IllegalArgumentException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					} catch (InvocationTargetException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					} catch (NoSuchMethodException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					} catch (SecurityException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
						// To Handle the Unexpected Alert Screen
					}
				}

			} else {
				if (action.equals("Verify Element Present")) {
					coreVerifyElementPresent(objectFound, inputValue);
				} else if (action.equals("Verify Element Present Abort")) {
					coreVerifyElementPresentAbort(objectFound, inputValue);
				} else if (alertPresent) {
					status.setResult(false);
					status.setMessage("Unexpected Alert dialog is displayed '"
							+ alertMessage
							+ "', Hybrid Testt Runner dismissed the Alert. please check the Results for more details.");
				} else {
					status.setResult(false);
					status.setMessage("The Object '"
							+ objectName
							+ "' does not exist, hence cannot perform the'"
							+ action
							+ "', please check application's object or check the Object's element in the Object repository.");
				}
			}
			// 6/22/2015 Mahendran added this block to handle the Alert dialogs
		} else if (objectType.equals("Alert")) {
			// To Call the appropriate Alert core fundtions
			if (action.equals("Verify Alert Present")) {
				coreVerifyAlertPresent(inputValue);
			} else if (action.equals("Verify Alert Text")) {
				coreVerifyAlertText(inputValue);
			} else if (action.equals("Accept Alert")) {
				coreAlertAccept(inputValue);
			} else if (action.equals("Dismiss Alert")) {
				coreAlertDismiss(inputValue);
			} else {
				status.setResult(false);
				status.setMessage("Un supported Action '" + action
						+ "' for the Object Type '" + objectType
						+ "', please contact your automation engineer.");

			}
		} else {
			status.setResult(false);
			status.setMessage("The Object '"
					+ objectName
					+ "' returns as null from Object Repository, hence cannot perform the'"
					+ action
					+ "', please check the Object and it's element in the Object repository, Note : Object is invalid so cannot take screen shot.");
		}
		if (!status.getResult()) {

			String FileName = testCase.get("Test Case ID") + "_"
					+ testCase.get("Step ID");
			String FileAbsolutePath = testOutputPath + "//"
					+ testCase.get("Run ID") + "//Screenshots//" + FileName
					+ ".jpg";
			testCase.put("Error Screen Output", FileAbsolutePath);
			// 6/22/15 Mahendran Added this to handle the Alert Screen
			// Alert dialog will be closed when we take the screen shot,
			// So When any ALert dialog pops up then skipping the screen shot.
			 try 
			    { 
			        driver.switchTo().alert(); 
			        System.out.println("Alert Dialog is displayed since screen shot cannot be taken, please contact the Automation Engineer for more details"); 
			    }   // try 
			    catch (NoAlertPresentException Ex) 
			    { 
			    	ScreenShot.getScreenshot(webElement, driver, FileAbsolutePath); 
			    }   // catch

		}
		status.setMessage(status.getMessage().replace("ObjectName", "'"+objectName+"'"));
		return status;
	}

	private String getMethodName(String action, String objectType) {
		String methodName = "";
		if (objectType.toLowerCase().trim().startsWith("aml")) {

			methodName = objectType.replace(" ", "").trim()
					+ action.replace(" ", "").trim();
			methodName = methodName.replaceAll("AML", "aml");
		} else {

			methodName = "core" + action.replace(" ", "").trim();
		}
		return methodName;
	}

	/**
	 * @author Mahendran
	 * @Method coreVerifyElementPresent This Method is used to verify the
	 *         Element is present or Not based on the input value
	 * @createdOn 5/6/2014
	 */

	public void coreVerifyElementPresent(boolean objectFound, String inputValue) {

		try {

			boolean expectedValue = convertStringToBoolean(inputValue);

			boolean actualValue = objectFound;

			if (expectedValue == actualValue) {

				status.setResult(true);
				status.setMessage("The Expected value of Element Present '"
						+ expectedValue + "' is match with the actual value '"
						+ actualValue + "' for the field ObjectName"); // Report
																		// Message

			} else {

				status.setResult(false);
				status.setMessage("The Expected value of Element Present '"
						+ expectedValue
						+ "' does NOT match with the actual value '"
						+ actualValue + "' for the field ObjectName"); // Report
																		// Message
			}

		}

		catch (Error e) {

			status.setResult(false);
			status.setMessage(e.toString());

		}

	}// VerifyObjectPresent

	/**
	 * @author Mahendran
	 * @Method coreVerifyAlertPresent This Method is used to verify the Element
	 *         is present or Not based on the input value
	 * @createdOn 6/22/2015
	 */

	public void coreVerifyAlertPresent(String inputValue) {

		try {

			boolean expectedValue = convertStringToBoolean(inputValue);
			boolean actualValue = false;
			// To
			WebDriverWait wait = new WebDriverWait(driver, 10 /*
															 * timeout in
															 * seconds
															 */);
			if (ExpectedConditions.alertIsPresent() != null) {
				actualValue = true;

			}
			if (expectedValue == actualValue) {

				status.setResult(true);
				status.setMessage("The Expected value of Alert Present '"
						+ expectedValue + "' is match with the actual value '"
						+ actualValue + "' for the Alert Dialog"); // Report
																	// Message

			} else {

				status.setResult(false);
				status.setMessage("The Expected value of Alert Present '"
						+ expectedValue
						+ "' does NOT match with the actual value '"
						+ actualValue + "' for the Alert Dialog"); // Report
																	// Message
			}

		}

		catch (Error e) {

			status.setResult(false);
			status.setMessage(e.toString());

		}

	}// VerifyObjectPresent

	/**
	 * @author Mahendran
	 * @Method coreAlertAccept This Method is used to accept the Alert Dialog
	 * @createdOn 6/22/2015
	 */

	public void coreAlertAccept(String inputValue) {

		try {

			WebDriverWait wait = new WebDriverWait(driver, 10 /*
															 * timeout in
															 * seconds
															 */);
			if (ExpectedConditions.alertIsPresent() != null) {
				Alert alert = driver.switchTo().alert();
				alert.accept();
				status.setResult(true);
				status.setMessage("The Alert dialog is displayed and accepted the Alert dialog"); // Report
			} else {
				status.setResult(false);
				status.setMessage("It seems like the Alert Dialog is not displayed hence cannot accept the Alert, please check the application"); // Report
				// Message
			}

		}

		catch (Error e) {

			status.setResult(false);
			status.setMessage(e.toString());

		}

	}// VerifyObjectPresent

	/**
	 * @author Mahendran
	 * @Method coreAlertDismiss This Method is used to Dismiss the Alert Dialog
	 * @createdOn 6/22/2015
	 */

	public void coreAlertDismiss(String inputValue) {

		try {

			WebDriverWait wait = new WebDriverWait(driver, 10 /*
															 * timeout in
															 * seconds
															 */);
			if (ExpectedConditions.alertIsPresent() != null) {
				Alert alert = driver.switchTo().alert();
				alert.dismiss();
				status.setResult(true);
				status.setMessage("The Alert dialog is displayed and Dismissed the Alert dialog"); // Report
			} else {
				status.setResult(false);
				status.setMessage("It seems like the Alert Dialog is not displayed hence cannot dismiss the Alert, please check the application"); // Report
				// Message
			}

		}

		catch (Error e) {

			status.setResult(false);
			status.setMessage(e.toString());

		}

	}// VerifyObjectPresent

	/**
	 * @author Mahendran
	 * @Method coreVerifyAlertText This Method is used to verify the Text
	 *         displayed in the Alert dialog
	 * @createdOn 6/22/2015
	 */

	public void coreVerifyAlertText(String expectedValue) {

		try {
			String actualValue = "";

			WebDriverWait wait = new WebDriverWait(driver, 10);
			if (ExpectedConditions.alertIsPresent() != null) {
				Alert alert = driver.switchTo().alert();
				actualValue = alert.getText().trim();
				if (expectedValue.equals(actualValue)) {

					status.setResult(true);
					status.setMessage("The Expected Alert Message '"
							+ expectedValue
							+ "' is match with the actual Alert Message '"
							+ actualValue + "'.");// Report
					// Message

				} else {

					status.setResult(false);
					status.setMessage("The Expected Alert Message '"
							+ expectedValue
							+ "' does Not match with the actual Alert Message '"
							+ actualValue + "'.");// Report // Report
					// Message
				}
			} else {

				status.setResult(false);
				status.setMessage("It seems like there is no Alert dialog dislayed, hence cannot verify the Alert Text, please check the application");
			}

		}

		catch (Error e) {

			status.setResult(false);
			status.setMessage(e.toString());

		}

	}// VerifyObjectPresent

	/**
	 * 
	 * @author Mahendran
	 * 
	 * @Method ofsVerifyElementPresentAbort
	 * 
	 *         This Method is used to verify the Element is present or Not based
	 *         on the input value and Set the stepContinue boolean variable as
	 *         False IF Expected and Actual Does Not match
	 * 
	 * @createdOn 5/6/2014
	 */

	public void coreVerifyElementPresentAbort(boolean objectFound,
			String inputValue) {

		try {

			boolean expectedValue = convertStringToBoolean(inputValue);
			boolean actualValue = objectFound;

			if (expectedValue == actualValue) {

				status.setResult(true);
				status.setMessage("The Expected value of Element Present Abort '"
						+ expectedValue
						+ "' is match with the actual value '"
						+ actualValue
						+ "' for the field ObjectName"
						+ "'., hence continue the remaining steps in the Test Case as expected"); // Report
																									// Message
																									// }

			} else {

				status.setResult(false);
				status.setMessage("The Expected value of Element Present Abort '"
						+ expectedValue
						+ "' does NOT match with the actual value '"
						+ actualValue
						+ "' for the field ObjectName"
						+ "'., hence ignoring the remaining steps in the Test Case as expected"); // Report
																									// Message
																									// }

			}

		}

		catch (Error e) {

			status.setResult(false);
			status.setMessage(e.toString());

		}

	}// Verify Present Abort

	/**
	 * @author Mahendran
	 * @Method convertStringToBoolean This Method is used to verify the Element
	 *         is present or Not based on the input value
	 * @createdOn 5/6/2014
	 */
	private boolean convertStringToBoolean(String value) {

		String strBoolean = value;

		// Do the String to boolean conversion

		boolean theValue = Boolean.parseBoolean(strBoolean);

		return theValue;

	} // convertStringToBoolean

}
