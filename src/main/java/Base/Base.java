package Base;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.util.Date;
import java.util.Map;
import java.util.Properties;

import io.restassured.response.ExtractableResponse;
import io.restassured.response.Response;
import org.testng.Assert;
import org.testng.Reporter;

import static io.restassured.RestAssured.given;


public class Base {

    public static void log(final String message){
        Date date = new Date();
        Reporter.log("-----> [" + date + "] " + message, true);
    }

    public enum RequestType{
        GET;
    }

    protected static ExtractableResponse<Response> getResponse(final RequestType requestType, final Map<String, String> headerMap,
                                                               final String requestURL, final String requestBody){

        ExtractableResponse<Response> response = null;

        if(requestBody != null){
            Reporter.log("Request Body : " + requestBody);
        }

        try{
            switch(requestType){

                case GET:
                    if(headerMap!=null){
                        response = given().log().all().headers(headerMap).when().get(requestURL).then().log().all()
                                .extract();
                    }
                    else{
                        response = given().log().all().when().get(requestURL).then().log().all().extract();
                    }
                    break;
            }
        } catch (Exception e){
            Base.log("Exception occurred : " + e.toString());
        }

        return response;
    }

    public static Properties getPropertyFromFile(final String fileName){
        InputStream inputStream;
        Properties properties = new Properties();

        try{
            inputStream = new FileInputStream(fileName);
            properties.load(inputStream);
            inputStream.close();
        } catch (FileNotFoundException e) {
            Base.log("Exception : " + e);
        } catch (IOException e) {
            Base.log("Exception : " + e);
        }
        return properties;
    }

    public static void validateResultTrue(final String actualValue, final String expectedValue, String message){
        Base.log("Actual : " + actualValue);
        Base.log("Expected : " + expectedValue);
        Assert.assertTrue(actualValue.equalsIgnoreCase(expectedValue), message);
    }

    public static void validateResultFalse(final String actualValue, final String expectedValue, String message){
        Base.log("Actual : " + actualValue);
        Base.log("Expected : " + expectedValue);
        Assert.assertFalse(!actualValue.equalsIgnoreCase(expectedValue), message);
    }

    public static void validateResult(final int actualValue, final int expectedValue, String message){
        Base.log("Actual : " + actualValue);
        Base.log("Expected : " + expectedValue);
        Assert.assertEquals(actualValue, expectedValue, message);
    }

    public static void validateResult(final String actualValue, final String expectedValue, String message){
        Base.log("Actual : " + actualValue);
        Base.log("Expected : " + expectedValue);
        Assert.assertEquals(actualValue, expectedValue, message);
    }

    public static void validateResult(final Long actualValue, final Long expectedValue, String message){
        Base.log("Actual : " + actualValue);
        Base.log("Expected : " + expectedValue);
        Assert.assertEquals(actualValue, expectedValue, message);
    }

    public static long getCurrentTime(){
        return System.currentTimeMillis();
    }
}
