package Tests;

import java.io.FileReader;
import java.io.IOException;

import org.testng.Assert;
import org.testng.annotations.Test;

import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;

import Helper.APIHelper;

public class compareJsonTest {
	
	

	 @Test
	    public void testJsonComparison() {
	        try {
	            // Load JSON files
	            JsonElement jsonElement1 = JsonParser.parseReader(new FileReader("API Preview.json"));
	            JsonElement jsonElement2 = JsonParser.parseReader(new FileReader("order.json"));

	            // Convert to JSON Arrays
	            JsonArray jsonArray1 = jsonElement1.getAsJsonArray();
	            JsonArray jsonArray2 = jsonElement2.getAsJsonArray();

	            // Assert JSON arrays have the same size
	            Assert.assertEquals(jsonArray1.size(), jsonArray2.size(), "JSON arrays have different sizes!");

	            // Compare JSON Arrays row by row
	            for (int i = 0; i < jsonArray1.size(); i++) {
	                JsonObject obj1 = jsonArray1.get(i).getAsJsonObject();
	                JsonObject obj2 = jsonArray2.get(i).getAsJsonObject();
	               APIHelper.compareJsonObjects(obj1, obj2, i + 1);
	            }

	        } catch (IOException e) {
	            Assert.fail("Failed to read JSON files: " + e.getMessage());
	        }
	    }

}
