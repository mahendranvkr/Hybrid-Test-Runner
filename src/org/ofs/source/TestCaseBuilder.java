package org.ofs.source;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.ofs.tools.XSLBuilder;

public class TestCaseBuilder {
String testCasePath="";
	public TestCaseBuilder(String url) {
		testCasePath =url;
	}
	

	public List<HashMap<String, String>> getTestCases() {
		XSLBuilder xslReader = new  XSLBuilder(testCasePath);
		List<HashMap<String, String>> manualTestCases = xslReader.convertXSLtoObject("Manual");
		List<HashMap<String, String>> automationTestCases = xslReader.convertXSLtoObject("Automation");
		List<HashMap<String, String>> testCaseInfo = xslReader.convertXSLtoObject("Details"); //Mahendran
		
		
		for(HashMap<String, String> automationTestCase : automationTestCases){
			
			for(HashMap<String, String> manualTestCase : manualTestCases){
				if(automationTestCase.get("Manual Step ID").equalsIgnoreCase(manualTestCase.get("Step ID"))){
					automationTestCase.putAll(manualTestCase);
					automationTestCase.putAll((Map<String, String>) testCaseInfo.get(0));//mahendran added
				}
			}
		}
		
		return automationTestCases;
	}


	public void saveTestResults(List<HashMap<String, String>> testCases) {
		String[] outputColumns = ConfigProperties.get("TestCaseColumns").split(",");
		String outputPath =ConfigProperties.get("appPath")+"//test.results//"+testCases.get(0).get("Test Suite Name")+"-output.xls";
		XSLBuilder xslReader = new  XSLBuilder(outputPath,testCases,"Test_Steps_Result",outputColumns);
		xslReader.convertObjecttoXLS(false);
		
	}

}


