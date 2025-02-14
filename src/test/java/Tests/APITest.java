package Tests;

import java.util.List;
import java.util.Map;

import org.testng.Assert;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.Test;

import Base.TestBase;
import Helper.APIHelper;
import io.restassured.RestAssured;
import io.restassured.response.Response;

public class APITest extends TestBase {

	
	
    @BeforeClass
    public void setUp() {
        setup();
    }

    @Test
    public void testGetRequest() {
        // Send GET request
        Response response = RestAssured.given().log().all().when().log().all().get();

        // Print response for debugging
        System.out.println("Response Body:\n" + response.getBody().asPrettyString());
        List<Map<String, Object>> responseData = response.jsonPath().getList("$");
        // Assertions
        Assert.assertEquals(response.getStatusCode(), 200, "Status Code Mismatch");
        Assert.assertNotNull(responseData, "Response Data is Empty");

        APIHelper.saveResponseToFile(responseData);

    }
}
