package org.ofs.source;

import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.Properties;

public class ConfigProperties {
	static Properties prop = new Properties();
	public ConfigProperties() {
		
		InputStream input = null;

		try {

			input = new FileInputStream("source\\config.properties");

			// load a properties file
			prop.load(input);

		} catch (IOException io) {
			io.printStackTrace();
		} finally {
			if (input != null) {
				try {
					input.close();
				} catch (IOException e) {
					e.printStackTrace();
				}
			}
		}
	}

	public static String get(String string) {
		return prop.getProperty(string);
	}
}
