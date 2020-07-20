package testCases;

import API.GetTestDemo;
import Base.Base;
import Pojo.DemoResponsePojo;
import Pojo.EmployeeData;
import com.google.gson.Gson;
import io.restassured.http.Header;
import io.restassured.http.Headers;
import io.restassured.response.ExtractableResponse;
import io.restassured.response.Response;
import org.testng.Assert;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.BeforeTest;
import org.testng.annotations.Test;

import java.util.List;
import java.util.Optional;


public class DemoTests extends Base {

    public static Headers responseHeader;
    public static DemoResponsePojo responseBody;
    static Gson gson = new Gson();

    @BeforeTest(description = "get employee details api response")
    public static void getResponse(){
        Base.log("fetching employee details");
        ExtractableResponse<Response> response =  GetTestDemo.getEmployeeDetails();
        Base.log("successfully fetched employee details");

        //store response headers in Headers object
        responseHeader = response.headers();

        //store response body in string and convert to json object
        String respBody = response.asString();
        responseBody = gson.fromJson(respBody, DemoResponsePojo.class);
    }

    @Test(description = "verify status code of get employee details api", priority = 1)
    public static void testStatusCode(){
        Base.log(gson.toJson(responseBody));
        String statusCode = responseBody.getStatus();
        validateResult(statusCode, "200", "failed due to incorrect status code!");
        Base.log("Status code is validated");
    }

    @Test(description = "verify response headers", priority = 2)
    public static void testResponseHeaders(){
        Base.log(responseHeader.toString());
        String contentType = responseHeader.get("Content-Type").getValue();
        validateResult(contentType, "application/json; charset=UTF-8", "failed due to response not in JSON format!");
        Base.log("response is in JSON format");
    }

    @Test(description = "validate response body", priority = 3)
    public static void testResponseBody(){
        Base.log(gson.toJson(responseBody));
        //verify status, age, role, dob and message
        validateResult(responseBody.getStatus(), "200", "failed due to incorrect status!");
        Base.log("status value is verified");

        List<EmployeeData> empDataList = responseBody.getEmployeeData();
        Optional<EmployeeData> empData = empDataList.stream().filter(responseList->responseList.getId().equalsIgnoreCase("101209986")).findAny();

        int age = empData.map(EmployeeData::getAge).get();
        validateResult(age, 25, "failed due to incorrect age!");
        Base.log("age value is verified");

        String role = empData.map(EmployeeData::getRole).get();
        validateResult(role, "QA Automation Developer", "failed due to incorrect role!");
        Base.log("role value is verified");

        String dob = empData.map(EmployeeData::getDob).get();
        validateResult(dob, "25-02-1994", "failed due to incorrect dob!");
        Base.log("DOB value is verified");

        validateResult(responseBody.getMessage(), "data retrieved successful", "failed due to incorrect message!");
        Base.log("message value is verified");
    }

    @Test(description = "validate response body", priority = 4)
    public static void testResponseBodyCompany(){
        Base.log(gson.toJson(responseBody));
        List<EmployeeData> empDataList = responseBody.getEmployeeData();
        Optional<EmployeeData> empData = empDataList.stream().filter(x->x.getId().equalsIgnoreCase("101209986")).findAny();

        String company = empData.map(EmployeeData::getCompany).get();
        validateResultTrue(company, "ABC Infotech" , "failed due to incorrect company value!");
        Base.log("company value is verified");
    }
}
