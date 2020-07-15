package resources;

import java.awt.image.BufferedImage;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.Properties;
import java.util.concurrent.TimeUnit;

import javax.imageio.ImageIO;
import javax.mail.Flags;
import javax.mail.Folder;
import javax.mail.Message;
import javax.mail.Session;
import javax.mail.Store;

import org.apache.commons.io.FileUtils;
import org.openqa.selenium.By;
import org.openqa.selenium.OutputType;
import org.openqa.selenium.TakesScreenshot;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.chrome.ChromeOptions;
import org.openqa.selenium.firefox.FirefoxDriver;
import org.openqa.selenium.ie.InternetExplorerDriver;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;


public class Base {
	public static WebDriver driver;
	public Properties prop;
	
	public WebDriver InitializeDriver() throws IOException
	{
	    prop = new Properties();
	    //we shouldnt hard code the path instead we use user.dir to locate the project path
	    //
		FileInputStream file = new FileInputStream(System.getProperty("user.dir")+"\\src\\main\\java\\resources\\data.properties");
		prop.load(file);
		String browser = prop.getProperty("browser");
		System.out.println(browser);

	
	if(browser.equalsIgnoreCase("Chrome"))
	{
		System.setProperty("webdriver.chrome.driver", System.getProperty("user.dir")+"\\src\\main\\java\\resources\\chromedriver.exe");
		
		
		driver = new ChromeDriver();
	}
	
	else if(browser.equalsIgnoreCase("FireFox"))
	{
		System.setProperty("webdriver.gecko.driver", "C:\\Users\\balaji\\Downloads\\geckodriver-v0.26.0-win64\\geckodriver.exe");
	driver = new FirefoxDriver();
	}
	else if(browser.equalsIgnoreCase("IE"))
	{
		 String service = "C:\\Users\\balaji\\Downloads\\IEDriverServer_x64_3.9.0\\IEDriverServer.exe";
		 System.setProperty("webdriver.ie.driver", service);
		 InternetExplorerDriver  driver = new InternetExplorerDriver();
	}
	driver.manage().timeouts().implicitlyWait(10, TimeUnit.SECONDS);
	WebDriverWait wait = new WebDriverWait(driver,30);
	return driver;
	}

	public String getScreenShotPath(String testCaseName, WebDriver driver) throws IOException
	{
		TakesScreenshot ts = (TakesScreenshot) driver;
		File source = ts.getScreenshotAs(OutputType.FILE);
		String destinationFile = System.getProperty("user.dir") + "\\reports\\"+testCaseName+".png";
		FileUtils.copyFile(source,new File(destinationFile));
		return destinationFile;
		
	}
    }

