package API;

import Base.Base;
import io.restassured.response.ExtractableResponse;
import io.restassured.response.Response;

import java.util.Properties;

public class GetTestDemo extends Base {

    static Properties property = getPropertyFromFile("Demo.properties");

    public static ExtractableResponse<Response> getEmployeeDetails(){
        String url = property.getProperty("DemoBaseURI") + property.getProperty("ApiTestURI");
        ExtractableResponse<Response> response = getResponse(RequestType.GET, null, url, null);
        return response;
    }
}
