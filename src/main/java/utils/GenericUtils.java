package utils;

import org.openqa.selenium.By;

public class GenericUtils {
	
	
	  public static By customXpath(String baseXPath, Object value) {
	        String finalXpath = baseXPath.replace("{}", value.toString());
	        return By.xpath(finalXpath);
	    }

}
