package org.ofs.tools;

import java.io.File;
import java.io.IOException;

import org.apache.commons.io.FileUtils;
import org.ofs.source.ConfigProperties;
import org.openqa.selenium.JavascriptExecutor;
import org.openqa.selenium.OutputType;
import org.openqa.selenium.StaleElementReferenceException;
import org.openqa.selenium.TakesScreenshot;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebDriverException;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.remote.Augmenter;

public class ScreenShot {

	public static void getScreenshot(WebElement field, WebDriver driver,
			String outputFile) {
//		String testOutputPath = ConfigProperties.get("testOutputPath");
		try {

			if (field != null) {

				JavascriptExecutor js = (JavascriptExecutor) driver;

				js.executeScript(
						"arguments[0].setAttribute('style', arguments[1]);",
						field, "color: red; border: 4px solid red;");

				// Capture entire page screenshot and then store it to
				// destination drive

				// File screenshot = ((TakesScreenshot) driver)
				// .getScreenshotAs(OutputType.FILE);

				WebDriver augmentedDriver = new Augmenter().augment(driver);
				File screenshot = ((TakesScreenshot) augmentedDriver)
						.getScreenshotAs(OutputType.FILE);

				FileUtils.copyFile(screenshot, new File(outputFile), true);

				js.executeScript(
						"arguments[0].setAttribute('style', arguments[1]);",
						field, ""); // Removing the Border after taking the
									// screen shot..

				// System.out
				// .print("Screenshot is captured...");

			} else {
				
								 

				// File screenshot = ((TakesScreenshot) driver)
				// .getScreenshotAs(OutputType.FILE);
				
				
				driver = new Augmenter().augment(driver);
				File screenshot = ((TakesScreenshot) driver)
						.getScreenshotAs(OutputType.FILE);

				FileUtils.copyFile(screenshot, new File(outputFile), true);

				// System.out.print("Object does not Present hence cannot mark the field in the screen shot, however application Screenshot is captured...");

			}

		} catch (IOException e) {

			System.out.println(e.toString());
	
		} catch (WebDriverException exception) {
			driver = new Augmenter().augment(driver);
			File screenshot = ((TakesScreenshot) driver)
					.getScreenshotAs(OutputType.FILE);

			try {
				FileUtils.copyFile(screenshot, new File(outputFile), true);
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			
		}

	}

}
