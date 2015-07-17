package org.ofs.actions;

import org.openqa.selenium.By;
import org.openqa.selenium.NoSuchElementException;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.ui.Select;

import com.thoughtworks.selenium.Wait;

import java.util.List;
import java.util.concurrent.TimeUnit;

public class ActionMethods {
	Status status = new Status();
	
	/**
	 * @author MahendranAA
	 * 
	 * This method is used to select a value from any search results
	 * 
	 * @param string
	 * @param category
	 * @return
	 * @throws Exception
	 */
	public void coreSelectSearchResult(List<WebElement> list, String inputValue) throws Exception {
    	try{
    		//List<WebElement> webElements = list ; 
    		String resultLC;
    		String stringLC;
    		for (WebElement result : list){
    			stringLC = inputValue.toLowerCase();
    			resultLC = result.getText().toLowerCase();
    			if (resultLC.contains(stringLC)){
    				this.coreClick(result, "search result");
    			}
    		}
    	} catch (Exception e) {
    		System.out.println("");
    	}
	}
		
	 /**
     * @author Mahendran
     * @Method coreEnterText
     * This Method is used to enter the input value into the specified Text box. 
     * @createdOn 5/6/2014
     */

	public void coreEnterText(WebElement element, String inputValue) {
		try {
			if (element.isEnabled()) {				
				element.clear();			
				element.sendKeys(inputValue);			
				String actualValue = element.getAttribute("value");
				if (inputValue.equals(actualValue)) {
					status.setResult(true);
					status.setMessage("The Expected value '" + inputValue
							+ "' is entered for the Text field ObjectName.");
			
				} else {
	
					status.setResult(false);
					status.setMessage("The Expected value '" + inputValue
							+ "' is entered in the Text field but it does does not match with the actual value '" + actualValue
							+ "' for the field ObjectName");
				} 		
					
			} else {

				status.setResult(false);
				status.setMessage("The Object '" + inputValue
						+ "' is exists but not enabled, hence cannot enter the input value '"
						+ inputValue
						+ "' for the field ObjectName");
			}
				

		} catch (Error e) {

			status.setResult(false);
			status.setMessage(e.toString());
		}

	}
	
	 /**
     * @author Mahendran
     * @Method coreVerifyText
     * This Method is used to verify the text in the text field. 
     * @createdOn 5/6/2014
     */

	public void coreVerifyText(WebElement element, String inputValue) {

		try {
			
			String actualValue = (element.getAttribute("value"));
			if (inputValue.equals(actualValue)) {
				status.setResult(true);
				status.setMessage("The Expected value '"
						+ inputValue
						+ "' is match with the actual value '"
						+ actualValue
						+ "' for the field ObjectName.");

			} else {

				status.setResult(false);
				status.setMessage("The Expected value '"
						+ inputValue
						+ "' does NOT match with the actual value '"
						+ actualValue
						+ "' for the field ObjectName.");

			}

		}

		catch (Error e) {

			status.setResult(false);
			status.setMessage(e.toString());

		}
	}
	
	 /**
     * @author Mahendran
     * @Method coreVerifyDefaultText
     * This Method is used to Verify the Default Text Present in the Text Box or Text Area Box. 
     * @createdOn 5/6/2014
     */

	public void coreVerifyDefaultText(WebElement element, String inputValue	) {

		try {

			String actualValue = element.getAttribute("defaultValue");
			if (inputValue.equals(actualValue)) {

				status.setResult(true);
				status.setMessage("The Expected value '" + inputValue
						+ "' is match with the actual value '" + actualValue
						+ "' for the field ObjectName.");

			} else {
				status.setResult(false);
				status.setMessage("The Expected value '" + inputValue
						+ "' does NOT match with the actual value '"
						+ actualValue + "' for the field ObjectName."); // Report
																		// Message
																		// }
			}

		}

		catch (Error e) {

			status.setResult(false);
			status.setMessage(e.toString());
		}

	}

	/**
	 * 
	 * @author Mahendran 
	 * @Method coreVerifyLabel
	 * This Method is used to Verify the Label of Button/Label or any other Element
	 * @createdOn 5/6/2014
	 */

	public void coreVerifyLabel(WebElement element, String inputValue) {

		try {

			String actualValue = element.getText().trim(); //6/19/15 Mahendran Trim Added
			if (inputValue.equals(actualValue)) {

				status.setResult(true);
				status.setMessage("The Expected value '" + inputValue
						+ "' is match with the actual value '" + actualValue
						+ "' for the field ObjectName"); // Report Message

			} else {

				status.setResult(false);
				status.setMessage("The Expected value '" + inputValue
						+ "' does NOT match with the actual value '"
						+ actualValue + "' for the field 'ObjectName'."); // Report Message 
			}

		}

		catch (Error e) {

			status.setResult(false);
			status.setMessage(e.toString());

		}

	}
	/**
	 * 
	 * @author Mahendran 
	 * @Method coreVerifyLabel
	 * This Method is used to Verify the Label of Button/Label or any other Element
	 * @createdOn 5/6/2014
	 */

	public void coreVerifyButtonLabel(WebElement element, String inputValue) {

		try {

			String actualValue = element.getAttribute("value");
			if (inputValue.equals(actualValue)) {

				status.setResult(true);
				status.setMessage("The Expected value '" + inputValue
						+ "' is match with the actual value '" + actualValue
						+ "' for the field ObjectName"); // Report Message

			} else {

				status.setResult(false);
				status.setMessage("The Expected value '" + inputValue
						+ "' does NOT match with the actual value '"
						+ actualValue + "' for the field 'ObjectName'."); // Report Message 
			}

		}

		catch (Error e) {

			status.setResult(false);
			status.setMessage(e.toString());

		}

	}

	/**
	 * 
	 * @author Mahendran
	 * @Method coreVerifyEnabled
	 * This Method is used to verify the Enabled property for the field
	 * @createdOn 5/6/2014
	 */

	public void coreVerifyEnabled(WebElement element, String inputValue) {

		try {

			boolean actualValue = element.isEnabled();

			boolean expectedValue = convertStringToBoolean(inputValue);

			if (expectedValue == actualValue) {

				status.setResult(true);
				status.setMessage("The Expected value of Enabled property '"
						+ expectedValue + "' is match with the actual value '"
						+ actualValue + "' for the field ObjectName"); // Report Message 

			} else {

				status.setResult(false);
				status.setMessage("The Expected value of Enabled Property '"
						+ expectedValue
						+ "' does NOT match with the actual value '"
						+ actualValue + "' for the field ObjectName"); // Report Message
			}

		}

		catch (Error e) {

			status.setResult(false);
			status.setMessage(e.toString());

		}

	}

	/**
	 * 
	 * @author Mahendran
	 * @Method coreVerifyFocused
	 * This Method is used to verify the Focused properties of the field
	 * @createdOn 5/6/2014
	 */

	public void coreVerifyFocused(WebElement element, String inputValue) {

		try {

			boolean actualValue = element.getAttribute("focused") != null;

			boolean expectedValue = convertStringToBoolean(inputValue);

			if (expectedValue == actualValue) {

				status.setResult(true);
				status.setMessage("The Expected value of Focused property '"
						+ expectedValue + "' is match with the actual value '"
						+ actualValue + "' for the field ObjectName"); // Report Message

			} else {

				status.setResult(false);
				status.setMessage("The Expected value of Focused Property '"
						+ expectedValue
						+ "' does NOT match with the actual value '"
						+ actualValue + "' for the field ObjectName"); // Report Message
			}

		}

		catch (Error e) {

			status.setResult(false);
			status.setMessage(e.toString());

		}

	}

	/**
	 * @author Mahendran
	 * @Method coreVerifyLength
	 *This Method is used to verify the Maximum Length of the Text field
	 * @createdOn 5/6/2014
	 */

	public void coreVerifyLength(WebElement element, String inputValue) {

		try {

			String length = element.getAttribute("maxlength");

			if (inputValue.equals(length)) {

				status.setResult(true);
				status.setMessage("The Expected value of Lenght '" + inputValue
						+ "' is match with the actual value '" + length
						+ "' for the field ObjectName"); // Report Message 

			} else {

				status.setResult(false);
				status.setMessage("The Expected value of Length Property '"
						+ inputValue
						+ "' does NOT match with the actual value '" + length
						+ "' for the field ObjectName"); // Report Message 
			}

		}

		catch (Error e) {

			status.setResult(false);
			status.setMessage(e.toString());

		}

	}

	/**
	 * @author Mahendran
	 * @Method coreVerifyElementPresent
	 * This Method is used to verify the Element is present or Not based on the input value
	 * @createdOn 5/6/2014
	 */

	public void coreVerifyElementPresent(WebElement element, String inputValue) {

		try {

			boolean expectedValue = convertStringToBoolean(inputValue);

			boolean actualValue = element.isDisplayed();

			if (expectedValue == actualValue) {

				status.setResult(true);
				status.setMessage("The Expected value of Element Present '"
						+ expectedValue + "' is match with the actual value '"
						+ actualValue + "' for the field ObjectName"); // Report Message 

			} else {

				status.setResult(false);
				status.setMessage("The Expected value of Element Present '"
						+ expectedValue
						+ "' does NOT match with the actual value '"
						+ actualValue + "' for the field ObjectName"); // Report Message
			}

		}

		catch (Error e) {

			status.setResult(false);
			status.setMessage(e.toString());

		}

	}

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

	public void coreVerifyElementPresentAbort(WebElement element,
			String inputValue) {

		try {

			boolean expectedValue = convertStringToBoolean(inputValue);

			boolean actualValue = (element != null) ? true : false;

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

	}

	/**
	 * 
	 * @author Mahendran
	 * 
	 * @Method ofsCommonSelectValue
	 * 
	 *         This Method is used to Select the Value as per the input value
	 *         from the Drop down Element
	 * 
	 * @createdOn 5/6/2014
	 */

	public void coreSelectValue(WebElement element, String inputValue) {

		try {

			if (element.isEnabled()) { // Checking the Object Enable

				Select dropdown = new Select(element);
				dropdown.selectByVisibleText(inputValue);				
				//Getting selected value to ensure the selected value as per the expected				
				WebElement option = dropdown.getFirstSelectedOption();
				String actualValue = option.getText();

				if (inputValue.equals(actualValue)) {

					status.setResult(true);
					status.setMessage("The Expected value '" + inputValue
							+ "' is selected in the Drop down filed ObjectName."); // Report
																			// Message

				} else {

					status.setResult(false);
					status.setMessage("The Expected value '"
							+ inputValue
							+ "' is selected in the Drop down filed but it does not match with the actual value'"
							+ actualValue + "' for the drop down field ObjectName."); // Report
																			// Message

				}

			} else {

				status.setResult(false);
				status.setMessage("The Drop down field ObjectName "
						+ "' is exist but it is not enabled, hence cannot select the expected value '"
						+ inputValue + "' .");// Report Message

			}

		}

		catch (NoSuchElementException e) {

			status.setResult(false);
			status.setMessage("The Expected value '" + inputValue
					+ "' is not available in the Drop down ' ObjectName"
					+ "', hence cannot select the expected value.");

		}

	}

	/**
	 * 
	 * @author Mahendran
	 * 
	 * @Method ofsCommonVerifySelectedValue
	 * 
	 *         This Method is used to verify the selected value from the drop
	 *         down box
	 * 
	 * @createdOn 5/6/2014
	 */

	public void coreVerifySelectedValue(WebElement element,
			String inputValue) {

		try {

			Select dropdown = new Select(element);

			//dropdown.selectByVisibleText(inputValue);

			WebElement option = dropdown.getFirstSelectedOption();

			String actualValue = option.getText();

			if (inputValue.equals(actualValue)) {

				status.setResult(true);
				status.setMessage("The Expected value '" + inputValue
						+ "' is match with the actual value '" + actualValue
						+ "' for the Drop down filed ObjectName."); // Report
																		// Message

			} else {

				status.setResult(false);
				status.setMessage("The Expected value '" + inputValue
						+ "' does not match with the actual value '"
						+ actualValue + "' for the Drop down filed ObjectName."); // Report
																		// Message

			}

		}

		catch (NoSuchElementException e) {

			status.setResult(false);
			status.setMessage("The Expected value '" + inputValue
					+ "' is not available in the Drop down ' ObjectName"
					+ "', hence cannot verify the expected value.");

		}

	}
	

	/**
	 * 
	 * @author Mahendran
	 * 
	 * @Method ofsCommonSelectRadioButton
	 * 
	 *         This Method is used to Select the specified Radio Button
	 * 
	 * @createdOn 5/6/2014
	 */

	public void coreSelect(WebElement element, String inputValue) {

		try {

			// objectId = By.xpath("//input[@type='radio' and @value='"
			// + inputValue + "']"); // Overriding the Object since the
			// // Radio button is something
			// // different than the other
			// // Elements.
			//
			// if (isElementPresent(objectId)) { // Checking for Object
			// Existence

			if (element.isEnabled()) { // Checking the Object Enable

				element.click();

				if (element.isSelected()) {

					status.setResult(true);
					status.setMessage("The expected Radio Button '"
							+ inputValue
							+ "' is selected from the Radio Group 'ObjectName'.");// Report
																			// Message

				} else {

					status.setResult(false);
					status.setMessage("The expected Radio Button '"
							+ inputValue
							+ "' is available and clicked but it is not selected from the Radio Group ObjectName"
							+ "'. please check whether other Radio button is selected from the Radio button group manually ");// Report
																																// Message

				}

			} else {

				status.setResult(false);
				status.setMessage("The Radio Button 'ObjectName"
						+ "' is exist and it is not enabled, hence cannot select the expected value '"
						+ inputValue + "' .");// Report Message

			}

		}

		catch (NoSuchElementException e) {

			status.setResult(false);
			status.setMessage("The Expected value '"
					+ inputValue
					+ "' is not available in the Radio Group 'ObjectName"
					+ "', hence cannot select the expected value. Please check the 'value' property of the '"
					+ inputValue
					+ "' radio button in the application using FireBug.");

		}

	}

	/**
	 * 
	 * @author Mahendran
	 * 
	 * @Method ofsCommonVerifySelectedRadioButton
	 * 
	 *         This Method is used to verify the specified Radio button either
	 *         selected or Not.
	 * 
	 * @createdOn 5/8/2014
	 */

	public void coreVerifySelected(WebElement element, String inputValue) {

		try {

			if (element.isEnabled()) { // Checking the Object Enable

				 //if (driver.findElement(
				 //By.xpath("//input[@type='radio' and @value='"
				// + inputValue + "']")).isSelected()) {
				if (element.isSelected()) {
					status.setResult(true);
					status.setMessage("The Radio Button '"
							+ inputValue
							+ "' is selected from the Radio Group as expected ObjectName.");// Report
																			// Message

				} else {

					status.setResult(false);
					status.setMessage("The Radio Button '" + inputValue
							+ "' is not selected state from the Radio Group 'ObjectName"
							+ "' which is not expected.");// Report
					// Message

				}

			} else {

				status.setResult(false);
				status.setMessage("The Radio Button 'ObjectName"
						+ "' is exist and it is not enabled, hence cannot verify the Radio button '"
						+ inputValue + "' is selected or Not .");// Report
																	// Message

			}
		}

		catch (NoSuchElementException e) {

			status.setResult(false);
			status.setMessage("The Expected value '"
					+ inputValue
					+ "' is not available in the Radio Group '"
					+ element.getAttribute("ObjectName")
					+ "', hence cannot verify the Radio button '"
					+ inputValue
					+ "' is selected. Please check the 'value' property of the '"
					+ inputValue
					+ "' radio button in the application using FireBug.");

		}

	}

	/**
	 * 
	 * @author Mahendran
	 * 
	 * @Method ofsCommonVerifyNotSelectedRadioButton
	 * 
	 *         This Method is used to verify the specified Radio button either
	 *         selected or Not.
	 * 
	 * @createdOn 5/8/2014
	 */

	public void coreVerifyNotSelected(WebElement element, String inputValue) {

		try {

			if (element.isEnabled()) { // Checking the Object Enable
			
				 //if (!driver.findElement(By.xpath("//input[@type='radio' and @value='" + inputValue + "']")).isSelected()) {
				if (!element.isSelected()) {
					status.setResult(true);
					status.setMessage("The Radio Button '"
							+ inputValue
							+ "' is NOT selected from the Radio Group as expected 'ObjectName'.");// Report
																			// Message

				} else {

					status.setResult(false);
					status.setMessage("The Radio Button '" + inputValue
							+ "' is selected state from the Radio Group 'ObjectName"
							+ "' which is not expected.");// Report
					// Message

				}

			} else {

				status.setResult(false);
				status.setMessage("The Radio Button 'ObjectName"
						+ "' is exist and it is not enabled, hence cannot verify the Radio button '"
						+ inputValue + "' is Not Selected .");// Report
																// Message

			}

		}

		catch (NoSuchElementException e) {

			status.setResult(false);
			status.setMessage("The Expected value '"
					+ inputValue
					+ "' is not available in the Radio Group 'ObjectName"
					+ "', hence cannot verify the Radio button '"
					+ inputValue
					+ "' is Not Selected. Please check the 'value' property of the '"
					+ inputValue
					+ "' radio button in the application using FireBug.");

		}

	}

	/**
	 * 
	 * @author Mahendran
	 * 
	 * @Method ofsCommonButtonClick
	 * 
	 *         This Method is used to Click the specified Button.
	 * 
	 * @createdOn 5/8/2014
	 */

	public void coreClick(WebElement element, String inputValue) {

		try {

			if (element.isEnabled()) { // Checking the Object Enable

				element.click();

				status.setResult(true);
				status.setMessage("The Button ObjectName is exist and Clicked.");// Report Message

			} else {

				status.setResult(false);
				status.setMessage("The Button ObjectName is exist and it is not enabled, hence cannot Click the Button.");// Report
																								// Message

			}

		}

		catch (NoSuchElementException e) {

			status.setResult(false);
			status.setMessage("The Expected Button ObjectName is not available in the application, Please check the Element using Fire Bug");

		}

	}
	/**
	 * 
	 * @author Mahendran
	 * 
	 * @Method coreClickandWait
	 * 
	 *         This Method is used to Click the specified Button.
	 * 
	 * @createdOn 5/8/2014
	 */

	public void coreClickandWait(WebElement element, String inputValue) {

		try {

			if (element.isEnabled()) { // Checking the Object Enable

				element.click();
				if (inputValue.equals("")) {
					inputValue = "100";
				}
				int sleepTime = Integer.parseInt(inputValue);
				try {
				    Thread.sleep(sleepTime);                 //1000 milliseconds is one second.
				} catch(InterruptedException ex) {
				    Thread.currentThread().interrupt();
				}

				status.setResult(true);
				status.setMessage("The Button ObjectName is exist and Clicked.");// Report Message

			} else {

				status.setResult(false);
				status.setMessage("The Button ObjectName is exist and it is not enabled, hence cannot Click the Button.");// Report
																								// Message

			}

		}

		catch (NoSuchElementException e) {

			status.setResult(false);
			status.setMessage("The Expected Button ObjectName is not available in the application, Please check the Element using Fire Bug");
		}

	}

	/**
	 * 
	 * @author Mahendran
	 * 
	 * @Method ofsCommonButtonClickWait
	 * 
	 *         This Method is used to Click the specified Button then Wait for
	 *         specified value.
	 * 
	 * @createdOn 5/8/2014
	 */

	/*public void verifyCommonButtonClickWait(WebElement element, String inputValue) {

		verifyClick(element, inputValue);

		// wait(10);

	} */

	private boolean convertStringToBoolean(String value) {

		String strBoolean = value;

		// Do the String to boolean conversion

		boolean theValue = Boolean.parseBoolean(strBoolean);

		return theValue;

	} 

}
