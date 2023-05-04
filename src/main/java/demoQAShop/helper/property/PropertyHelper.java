package demoQAShop.helper.property;

import java.io.File;
import java.io.FileInputStream;
import java.util.Properties;

public class PropertyHelper {
	FileInputStream fis = null;
	private Properties propObj= null;

	public PropertyHelper(String fileName) {
		propObj = new Properties();
		File file = new File(System.getProperty("user.dir") + "\\src\\main\\resource\\" + fileName);
		try {
			fis = new FileInputStream(file);
			propObj.load(fis);
		} catch (Exception e) {
			//e.printStackTrace();
		}

	}

	public String getPropertyKey(String key) {
		return propObj.getProperty(key);
	}
}
