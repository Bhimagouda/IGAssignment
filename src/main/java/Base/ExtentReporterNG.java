package Base;

import com.relevantcodes.extentreports.ExtentReports;
import com.relevantcodes.extentreports.ExtentTest;
import com.relevantcodes.extentreports.LogStatus;
import org.testng.*;
import org.testng.xml.XmlSuite;

import java.io.File;
import java.util.*;

public class ExtentReporterNG implements IReporter {
    private ExtentReports extent;
    public ExtentTest test;
    static String testReportName;


    public void generateReport(List<XmlSuite> xmlSuites, List<ISuite> suites, String outputDirectory) {
        testReportName = "TestReport_"+Base.getCurrentTime()+".html";
        extent = new ExtentReports(outputDirectory + File.separator + testReportName, false);
        for (ISuite suite : suites) {

            Map<String, ISuiteResult> result = suite.getResults();
            for (ISuiteResult r : result.values()) {
                ITestContext context = r.getTestContext();
                buildTestNodes(context.getPassedTests(), LogStatus.PASS);
                buildTestNodes(context.getFailedTests(), LogStatus.FAIL);
                buildTestNodes(context.getSkippedTests(), LogStatus.SKIP);
            }
        }
        extent.flush();
        extent.close();
    }

    public void buildTestNodes(IResultMap tests, LogStatus status) {
        if (tests.size() > 0) {
            List<ITestResult> resultList = new LinkedList<ITestResult>(tests.getAllResults());
            class ResultComparator implements Comparator<ITestResult> {
                public int compare(ITestResult r1, ITestResult r2) {
                    return getTime(r1.getStartMillis()).compareTo(getTime(r2.getStartMillis()));
                }
            }
            Collections.sort(resultList, new ResultComparator());

            for (ITestResult result : resultList) {
                test = extent.startTest(result.getMethod().getMethodName());
                test.getTest().setDescription(result.getMethod().getDescription());
                test.getTest().setStartedTime(getTime(result.getStartMillis()));
                test.getTest().setEndedTime(getTime(result.getEndMillis()));
                for (String message : Reporter.getOutput(result)) {    //This code picks the log from Reporter object.**
                    test.log(LogStatus.INFO, message);
                }
                for (String group : result.getMethod().getGroups())
                    test.assignCategory(group);
                String message = "Test " + status.toString().toLowerCase() + "ed";
                if (result.getThrowable() != null)
                    message = result.getThrowable().getClass() + ": " + result.getThrowable().getMessage();
                test.log(status, message);
                extent.endTest(test);
            }
        }
    }

    private Date getTime(long millis) {
        Calendar calendar = Calendar.getInstance();
        calendar.setTimeInMillis(millis);
        return calendar.getTime();
    }
}
