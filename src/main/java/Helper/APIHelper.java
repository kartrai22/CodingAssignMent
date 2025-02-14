package Helper;

import java.io.FileWriter;
import java.io.IOException;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.testng.Assert;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;

import Exception.APIExceptions;
import io.restassured.response.Response;
import utils.ConfigReader;

public class APIHelper {
	private static final Logger logger = LogManager.getLogger(APIHelper.class);

	public static void validateResponse(Response response) {
		logger.info("Validating API response. Status Code: " + response.getStatusCode());

		if (response.getStatusCode() != 200) {
			logger.error("Unexpected API response: " + response.getStatusCode());
			throw new APIExceptions("Unexpected API response: " + response.getStatusCode());
		}
	}
	
	public static void saveResponseToFile(List<Map<String, Object>> dataList) {
	    String fileName = ConfigReader.getProperty("response.file.name");

	    // Create a JSON array
	    JsonArray jsonArray = new JsonArray();

	    for (Map<String, Object> data : dataList) {
	        JsonObject jsonObject = new JsonObject();
	        
	        // Convert values to String safely
	        jsonObject.addProperty("id", String.valueOf(data.get("id")));
	        jsonObject.addProperty("OrdersCount", String.valueOf(data.get("OrdersCount")));
	        jsonObject.addProperty("Email", data.get("Email").toString());
	        jsonObject.addProperty("ProducID", data.get("ProducID").toString());
	        jsonObject.addProperty("Name", data.get("Name").toString());

	        jsonArray.add(jsonObject);
	    }

	    // Save JSON to file
	    try (FileWriter file = new FileWriter(fileName)) {
	        Gson gson = new GsonBuilder().setPrettyPrinting().create();
	        gson.toJson(jsonArray, file);
	        System.out.println("✅ Successfully stored response in " + fileName);
	    } catch (IOException e) {
	        e.printStackTrace();
	    }
    }
	 public static void compareJsonObjects(JsonObject obj1, JsonObject obj2, int row) {
	        Set<Map.Entry<String, JsonElement>> entries1 = obj1.entrySet();

	        for (Map.Entry<String, JsonElement> entry : entries1) {
	            String key = entry.getKey();
	            JsonElement value1 = entry.getValue();
	            JsonElement value2 = obj2.get(key);

	            // Assert values are equal
	            Assert.assertNotNull(value2, "Key '" + key + "' is missing in second JSON at row " + row);
	            Assert.assertEquals(value1, value2, "Mismatch found in Row " + row + ", Column '" + key + "'");
	        }
	    }
	 }
