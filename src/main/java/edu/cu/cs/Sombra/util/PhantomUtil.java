package edu.cu.cs.Sombra.util;

import java.util.List;

import org.openqa.selenium.By;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.phantomjs.PhantomJSDriver;
import org.openqa.selenium.remote.DesiredCapabilities;

import edu.cu.cs.Sombra.Config;

public class PhantomUtil {
	private static PhantomJSDriver phantom;
	
	public PhantomUtil() {
		if (this.phantom != null)
			return;
		DesiredCapabilities caps = DesiredCapabilities.chrome();
	    caps.setJavascriptEnabled(true);
	   
	    caps.setCapability("phantomjs.binary.path", 
	                Config.PHANTOMJS_PATH);
        
	    PhantomJSDriver phantom = new PhantomJSDriver(caps);
	    this.phantom = phantom;
	}
	
	public PhantomJSDriver getDriver() {
		return this.phantom;
	}
	
	public static List<WebElement> render(String url) {
		PhantomUtil util = new PhantomUtil();
		PhantomJSDriver phantom = util.getDriver();
		if (!url.startsWith("file://")) 
			url = "file://" + url;
		phantom.get(url);
		return phantom.findElements(By.cssSelector("*"));
	}
	
	public static void close() {
		if (PhantomUtil.phantom != null)
			PhantomUtil.phantom.close();
	}
}
