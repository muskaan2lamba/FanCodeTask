package com.task.fancode;

import com.aventstack.extentreports.ExtentReports;
import com.aventstack.extentreports.ExtentTest;
import com.aventstack.extentreports.Status;
import com.aventstack.extentreports.markuputils.ExtentColor;
import com.aventstack.extentreports.markuputils.MarkupHelper;
import com.aventstack.extentreports.reporter.ExtentSparkReporter;
import com.task.constants.Constants;
import com.task.constants.StatusCodeEnum;
import com.task.endpoints.Endpoints;
import com.task.model.Todo;
import com.task.model.User;
import com.task.utils.Helper;
import com.task.utils.ResponseArrayUtils;
import kong.unirest.HttpResponse;
import kong.unirest.Unirest;

import org.testng.Assert;
import org.testng.ITestResult;
import org.testng.annotations.AfterMethod;
import org.testng.annotations.AfterTest;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.BeforeTest;
import org.testng.annotations.Test;

public class FancodeTask {
    ExtentReports extent = new ExtentReports();
    ExtentSparkReporter spark = new ExtentSparkReporter("extent-report.html");
    public ExtentTest testLogger;

    @BeforeTest
    public void setupReport() {
        extent.attachReporter(spark);
    }
    
    @BeforeClass
    public void setUp() {
        Unirest.config().defaultBaseUrl(Constants.BASE_URI);
    }

    @Test
    public void validateUserTaskCompletion() {
        double minimumCompletionPercentage = 50;
        String queryParamKey = "userId";
        
        ExtentTest test = extent.createTest("Validate Users with Task Completion > 50%");
        testLogger = test;
        HttpResponse<String> userResponse = Unirest.get(Endpoints.USERS).asString();
        Assert.assertEquals(userResponse.getStatus(), StatusCodeEnum.Code_200.getCode(), "Failed to fetch users");
        test.pass("Successfully fetched user data from endpoint");

        test.log(Status.INFO, "Parsing user data");
        User[] users = ResponseArrayUtils.getResponseAsArray(userResponse.getBody(), User[].class);

        for (User user : users) {
            double latitude = Double.parseDouble(user.getAddress().getGeo().getLat());
            double longitude = Double.parseDouble(user.getAddress().getGeo().getLng());

            test.log(Status.INFO, "Verifying coordinates for user: " + user.getName());
            if (Helper.isUserInFancodeCity(latitude, longitude)) {
                String userName = user.getName();
                test.log(Status.INFO, "Fetching todo list for user: " + userName + " with valid coordinates");
                HttpResponse<String> todoResponse = Unirest.get(Endpoints.TODOS).queryString(queryParamKey, user.getId()).asString();
                Assert.assertEquals(todoResponse.getStatus(), StatusCodeEnum.Code_200.getCode(), "Failed to fetch todos");

                Todo[] todos = ResponseArrayUtils.getResponseAsArray(todoResponse.getBody(), Todo[].class);

                int totalTasks = todos.length;
                int completedTasks = 0;

                test.log(Status.INFO, "Counting completed tasks for user: " + userName);
                for (Todo todo : todos) {
                    if (todo.isCompleted()) {
                        completedTasks++;
                    }
                }

                double completionPercentage = Helper.calculateCompletionPercentage(completedTasks, totalTasks);
                test.log(Status.INFO, "Checking if task completion percentage > 50% for user: " + userName);
                Assert.assertTrue(completionPercentage > minimumCompletionPercentage,
                        "User " + user.getId() + " in Fancode city has less than 50% tasks completed");
                String completionPercentageFormatted = String.format("%.2f", completionPercentage);
                test.pass(userName + " has completed " + completionPercentageFormatted + "% of tasks");
            }
        }
    }

    @AfterMethod
    public void captureResult(ITestResult result) {
        switch (result.getStatus()) {
            case ITestResult.FAILURE:
                testLogger.log(Status.FAIL, MarkupHelper.createLabel(result.getName() + " - Test Case Failed", ExtentColor.RED));
                break;
            case ITestResult.SKIP:
                testLogger.log(Status.SKIP, MarkupHelper.createLabel(result.getName() + " - Test Case Skipped", ExtentColor.ORANGE));
                break;
            case ITestResult.SUCCESS:
                testLogger.log(Status.PASS, MarkupHelper.createLabel(result.getName() + " - Test Case Passed", ExtentColor.GREEN));
                break;
            default:
                testLogger.log(Status.INFO, MarkupHelper.createLabel(result.getName() + " - Test Case status not present", ExtentColor.BLUE));
        }
    }

    @AfterTest
    public void tearDownReport() {
        extent.flush();
    }
}