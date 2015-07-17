package org.ofs.browers;

import java.net.MalformedURLException;
import java.net.URL;

import org.openqa.selenium.Platform;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.remote.DesiredCapabilities;
import org.openqa.selenium.remote.RemoteWebDriver;

public class Firefox implements browser {
	DesiredCapabilities capWinff;

	public Firefox(String browser) {
		DesiredCapabilities capWinff = DesiredCapabilities.firefox();

		capWinff.setBrowserName(browser.toString());

		capWinff.setPlatform(Platform.WINDOWS);

	}

	@Override
	public WebDriver getDriver(String url) {
		WebDriver driver = null;
		try {
			driver = new RemoteWebDriver(new URL(url), capWinff);
		} catch (MalformedURLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

		return driver;
	}

}
