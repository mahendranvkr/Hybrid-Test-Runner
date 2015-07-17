package org.ofs.source;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.ofs.tools.XSLBuilder;

public class TestSuiteBuilder {
	String testBuilderPath = "";

	public TestSuiteBuilder() {
		testBuilderPath = ConfigProperties.get("appPath")+"//test.suite//"
				+ ConfigProperties.get("testSuiteName");
	}

	public List<HashMap<String, String>> getTestSuites() {
		XSLBuilder xslReader = new XSLBuilder(testBuilderPath);

		List<HashMap<String, String>> testSuiteDetails = xslReader
				.convertXSLtoObject("Details");

		List<HashMap<String, String>> testCaseDetails = xslReader
				.convertXSLtoObject("Test Cases");

		for (HashMap<String, String> testCaseDetail : testCaseDetails) {
			testCaseDetail
					.putAll((Map<String, String>) testSuiteDetails.get(0));
		}

		return testCaseDetails;

	}

	public void saveTestResults(List<HashMap<String, String>> testSuites) {
		String[] outputColumns = ConfigProperties.get("TestSuiteColumns").split(",");
		String outputPath =ConfigProperties.get("appPath")+"//test.results//"+testSuites.get(0).get("Test Suite Name")+"-output.xls";
		XSLBuilder xslReader = new  XSLBuilder(outputPath,testSuites,"Test_Case_Result",outputColumns);
		xslReader.convertObjecttoXLS(false);
		
	}

}
