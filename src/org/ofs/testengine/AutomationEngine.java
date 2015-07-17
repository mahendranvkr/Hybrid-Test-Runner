package org.ofs.testengine;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;

import org.ofs.actions.Actions;
import org.ofs.actions.Status;
import org.ofs.source.ConfigProperties;
import org.ofs.source.TestCaseBuilder;
import org.ofs.source.TestSuiteBuilder;

public class AutomationEngine {

	List<HashMap<String, String>> testSuites = null;
	public AutomationEngine() {

	}

	public void startProcess() {
		TestSuiteBuilder testSuiteBuilder = new TestSuiteBuilder();
		testSuites = testSuiteBuilder.getTestSuites();
		Date startSuiteTimeStamp = new Date();

		String RunId;
		SimpleDateFormat formatter;

		formatter = new SimpleDateFormat("yyyy_MM_dd_HH_mm_ss", new Locale(
				"en", "US"));

		RunId = "Run_ID_" + formatter.format(startSuiteTimeStamp);

		// String currentRunTestSuiteName = testSuite.get("Test Suite Name");
		System.out.println("|Test Suite|Start|" + startSuiteTimeStamp);
		System.out.println("Test suite has '" + testSuites.size()
				+ "' Test Case(s)");
		Actions actions = new Actions();
		for (HashMap<String, String> testSuite : testSuites) {
			Date startTestTimeStamp = new Date();
			TestCaseBuilder testCaseBuilder = new TestCaseBuilder(
					ConfigProperties.get("appPath") + "\\test.case\\" + testSuite.get("Test Suite ID") + "\\"
							+ testSuite.get("File Name"));
			List<HashMap<String, String>> testCases = testCaseBuilder
					.getTestCases();
			String testSuiteStatus = "Pass";
			// Mahendran Added
			String currentTestCaseFileName = testSuite.get("File Name");
			System.out.println("|Test Case|Start|" + currentTestCaseFileName
					+ "|" + startTestTimeStamp + "'");
			System.out
					.println("Test Case has '" + testCases.size() + "' Steps");
			for (HashMap<String, String> testCase : testCases) {
				testCase.put("Run ID", RunId);
				String currentRunStepName = testCase.get("Design Step");
				System.out.println("|Test Step|Start|" + currentRunStepName);
				testCase.put("Test Suite Name",
						testSuite.get("Test Suite Name"));
				testCase.put("Test Suite ID", testSuite.get("Test Suite ID"));
				testCase.put("Test Case ID", testCase.get("Test Case ID"));
				testCase.put("Test Case Path", testSuite.get("Location"));
				testCase.put("Test Case Name", testCase.get("Test Case Name"));
				// Mahendran - Printing Test Case Wise Output
				testSuite.put("Test Case ID", testCase.get("Test Case ID"));
				testSuite.put("Test Case Name", testCase.get("Test Case Name"));

				if (testCase.get("Action").contains("Open and Wait")) {
					Status status = actions.openBrowser(testCase);
					if (status.getResult()) {
						testCase.put("Step Execution Status", "Pass");
						testCase.put("Actual Result", status.getMessage());
					} else {
						testCase.put("Step Execution Status", "Fail");
						testCase.putAll(testSuite);
						break;
					}
				} else {
					Status testCaseStatus = actions.executeTestCase(testCase);
					if (testCaseStatus.getResult()) {
						testCase.put("Step Execution Status", "Pass");
						testCase.put("Actual Result",
								testCaseStatus.getMessage());

					} else {
						testSuiteStatus = "Failed";
						// testCase.put("Result", "failed");
						// testCase.put("Step Execution Status",
						// testCaseStatus.getMessage());
						testCase.put("Step Execution Status", "Fail");
						testCase.put("Actual Result",
								testCaseStatus.getMessage());
					}
				}

				testCase.putAll(testSuite);
				System.out.println("|Test Step|End|" + currentRunStepName);

			}
			testSuite.put("Test Execution Status", testSuiteStatus);
			testSuite.put("Run ID", RunId);

			Date endTestTimeStamp = new Date();		

			long diff = endTestTimeStamp.getTime()
					- startTestTimeStamp.getTime();
			long diffSeconds = diff / 1000 % 60;
			long diffMinutes = diff / (60 * 1000) % 60;
			long diffHours = diff / (60 * 60 * 1000);
			//
			String testDuration = diffHours + ":" + diffMinutes + ":" + diffSeconds + "'";
			// Mahendran Added on 10/30
			System.out.println("|Test Case|End|" + currentTestCaseFileName
					+ "|" + endTestTimeStamp + "|" + testDuration);
			
			testSuite.put("Duration", diffHours + ":" + diffMinutes + ":" + diffSeconds );
			testCaseBuilder.saveTestResults(testCases);
			actions.closeBrowser(); // Mahendran added on 10/30 - To close the
			// Browser after every test case gets
			// executed
			
			
		}

		testSuiteBuilder.saveTestResults(testSuites);
		Date endtSuiteTimeStamp = new Date();
		System.out.println("|Test Suite|End|" + endtSuiteTimeStamp);

	}
}
