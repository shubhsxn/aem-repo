// Trying to write a logic to create allure reports(Environment section) based on config.properties
//Files to be used : config.properties & environment.properties under allure-results
//Not being used below for now
package utilities.com;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.Properties;

import org.apache.log4j.Logger;

public class WritePropertiesFile {
	public static void WritePropertiesFile() {
		try {
			Properties properties = new Properties();
			properties.setProperty("Environment", "Prod");
			properties.setProperty("Browser", "Chrome");
			properties.setProperty("Device", "Laptop");

			File file = new File("environment.properties");
			FileOutputStream fileOut = new FileOutputStream(file);
			properties.store(fileOut, "Favorite Things");
			fileOut.close();
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}

	}
}
