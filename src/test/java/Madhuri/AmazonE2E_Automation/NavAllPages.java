package Madhuri.AmazonE2E_Automation;

import java.io.IOException;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.interactions.Actions;
import org.openqa.selenium.support.ui.Select;
import org.openqa.selenium.support.ui.WebDriverWait;
import org.testng.annotations.BeforeTest;
import org.testng.annotations.Test;

import resources.Base;

public class NavAllPages extends Base {
	public static WebDriver driver;
	WebDriverWait wait;
	 public static Logger log =LogManager.getLogger(Base.class.getName());
	
	 @BeforeTest
	public void browserInitialize() throws IOException
	{
		
		driver = InitializeDriver();
		log.info("Launched the chrome browser");
		driver.manage().window().maximize();
		
	}
	 @Test
	 public void NavThroughAllPages()
	 {
		 driver.get(prop.getProperty("url"));
		 
		 Actions hoverover = new Actions(driver);
		hoverover.moveToElement(driver.findElement(By.xpath("//span[text()='Account & Lists']"))).build().perform(); 
		driver.findElement(By.xpath("//span[@class='nav-text'][contains(text(),'Baby Registry')]")).click();
		driver.findElement(By.linkText("Get Started")).click();
	
		Select dropdown = new Select(driver.findElement(By.xpath("//span[data-action='a-dropdown-button']")));
		dropdown.selectByVisibleText("08");
		
	 }
	 
	 
	 

}
