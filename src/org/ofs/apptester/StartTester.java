package org.ofs.apptester;

import org.ofs.testengine.AutomationEngine;
import org.ofs.source.ConfigProperties;

public class StartTester {

	/**
	 * @param args
	 */
	public static void main(String[] args) {
		ConfigProperties testProperties = new ConfigProperties();
		AutomationEngine automationEngine = new AutomationEngine();
		automationEngine.startProcess();

	}

}
