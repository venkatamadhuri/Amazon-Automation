package Madhuri.AmazonE2E_Automation;

import static org.testng.Assert.assertTrue;

import java.io.File;
import java.io.IOException;
import java.util.Arrays;
import java.util.Hashtable;
import java.util.List;
import java.util.Properties;
import java.util.stream.Stream;
import java.util.stream.StreamSupport;

import javax.swing.JOptionPane;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.openqa.selenium.By;
import org.openqa.selenium.JavascriptExecutor;
import org.openqa.selenium.Keys;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.interactions.Actions;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.Select;
import org.openqa.selenium.support.ui.WebDriverWait;
import org.testng.Assert;
import org.testng.Reporter;
import org.testng.annotations.AfterTest;
import org.testng.annotations.BeforeTest;
import org.testng.annotations.Test;

import com.twilio.Twilio;
import com.twilio.base.ResourceSet;
import com.twilio.rest.api.v2010.account.Message;

import resources.Base;


public class AddingItemsToCart extends Base{
	public static WebDriver driver;
	String ActualItem;
	String SelectedItem;
	WebDriverWait wait;
	public static final String Acc_SID= "AC36134d35e8b313395a61a064263e40a6";
	public static final String Auth_Token="4586c309f5bcb10a4a8bf6cd9872086f";
	
	 public static Logger log =LogManager.getLogger(Base.class.getName());

	@BeforeTest
	public void browserInitialize() throws IOException
	{
		
		driver = InitializeDriver();
		Assert.assertNotNull(driver); 
		log.info("Launched the chrome browser");
		driver.manage().window().maximize();
		
	}
	@Test(enabled=false)
	public void pageTitle()
	{
		String expectedUrl = "https://www.amazon.ca/";
		String actualUrl = driver.getCurrentUrl();
		Assert.assertEquals(actualUrl, expectedUrl);
		log.info("Successfully navigated the homepage");
		String Actual = driver.getTitle();
		String expected = "Amazon.ca: Low Prices – Fast Shipping – Millions of Items";
		Assert.assertEquals(Actual, expected, "Title doesnt match with expected");
	}
	 @Test(enabled=false)
	 public void getSearch() throws InterruptedException
	 {
		 		driver.get(prop.getProperty("url"));
			    wait = new WebDriverWait(driver, 30);
			    WebElement searchlist = wait.until(ExpectedConditions.visibilityOfElementLocated(By.xpath("//input[@id='twotabsearchtextbox']")));
			    searchlist.sendKeys("watches");
			    searchlist.sendKeys(Keys.RETURN); 
			    log.info("baby dresses text is entered in the search bar and clicked enter");
		List<WebElement> elements =  driver.findElements(By.cssSelector("h2.a-size-mini.a-spacing-none.a-color-base.s-line-clamp-2"));
		Assert.assertTrue(elements.contains("watch"), "Search test is passed");
	 }
	@Test(enabled=false)
	public void createAmazonAcc() throws InterruptedException
	{
		driver.get(prop.getProperty("url"));
		log.info("Successfully navigated the homepage");
		Actions hoverover = new Actions(driver);
		hoverover.moveToElement(driver.findElement(By.xpath("//span[text()='Hello, Sign in')]"))).click().build().perform();
		log.info("Clicked on Account SignIn");
		driver.findElement(By.linkText("Create your Amazon account")).click();
		log.info("Clicked on create account");
		Thread.sleep(2000);
		driver.findElement(By.xpath("//input[@id='ap_customer_name']")).sendKeys(prop.getProperty("name"));
		log.info("Entered name");
		driver.findElement(By.xpath("//input[@type='email']")).sendKeys(prop.getProperty("email-id"));
		log.info("Entered email");
		driver.findElement(By.name("password")).sendKeys(prop.getProperty("password"));
		log.info("password entered");
		driver.findElement(By.name("passwordCheck")).sendKeys(prop.getProperty("password"));
		log.info("password re-entered");
		driver.findElement(By.xpath("//input[@id='continue']")).click();
		log.info("Account is created");
	}
	public void logIn() throws InterruptedException
	{
		driver.get(prop.getProperty("url"));
		Actions hoverover = new Actions(driver);
		hoverover.moveToElement(driver.findElement(By.xpath("//span[contains(text(),'Hello, Sign in')]"))).click().build().perform();
				
		driver.findElement(By.xpath("//input[@id='ap_email']")).sendKeys(prop.getProperty("email-id"));
		log.info("Entered phone number");
		
		driver.findElement(By.id("continue")).click();
		driver.findElement(By.name("password")).sendKeys(prop.getProperty("password"));
		log.info("password entered");		
	
		driver.findElement(By.xpath("//input[@id='signInSubmit']")).click();
		log.info("Successfully logged in by clicking on the submit button");
		
		driver.findElement(By.id("continue")).click();

		Twilio.init(Acc_SID, Auth_Token);
		String smsbody = getMessage();
		String OTP= smsbody.replaceAll("[^-?0-9]", " ");
		System.out.println(OTP);
		
		driver.findElement(By.xpath("//input[@type='text']")).sendKeys(OTP);
		driver.findElement(By.xpath("//input[@type='submit']")).click();

	}
	public static String getMessage() {
		return getMessages().filter(m -> m.getDirection().compareTo(Message.Direction.INBOUND) == 0)
				.filter(m -> m.getTo().equals("+13343784755")).map(Message::getBody).findFirst()
				.orElseThrow(IllegalStateException::new);
	}
	private static Stream<Message> getMessages() {
		ResourceSet<Message> messages = Message.reader(Acc_SID).read();
		return StreamSupport.stream(messages.spliterator(), false);
	}
	@Test
	public void getItemsToCart() throws InterruptedException
	{
		driver.get(prop.getProperty("url"));
		log.info("Successfully navigated to the homepage");
		Thread.sleep(10000);
		
		WebElement text = driver.findElement(By.xpath("//input[@id='twotabsearchtextbox']"));
		Assert.assertTrue(text.isDisplayed());
		text.sendKeys("baby dresses");
		log.info("baby dresses text is entered in the search bar");
		Thread.sleep(2000);
		
		driver.findElement(By.className("nav-input")).click();
		log.info("cliked on search button");
				
		String[] productNames = {"Amazon Essentials", "Mapletop"};
		List<WebElement>  items = driver.findElements(By.cssSelector("h2.a-size-mini.a-spacing-none.a-color-base.s-line-clamp-2"));
		log.info("Getting all the items in the webpage");
		for(int i =0 ; i<items.size();i++)
		{
			ActualItem = items.get(i).getText();
			log.info("Getting the text of the Actual item");
			List<String> itemNeeded = Arrays.asList(productNames);
		if(itemNeeded.contains(ActualItem))
					{
			
			items.get(i).click();
			log.info("Clicking the Actual item");
					break;
					}
		}		
	
		Select ddl = new Select(driver.findElement(By.className("a-native-dropdown")));
		log.info("DDL is selected with the help of select class");	
		ddl.selectByIndex(2);
		log.info("Preemie in DDL is selected");	
		
		Thread.sleep(2000);
		WebElement addToCart = driver.findElement(By.cssSelector("#add-to-cart-button"));
		addToCart.click();
		log.info("Clicked on Add to cart option");	
		
		driver.findElement(By.xpath("//span[@id ='nav-cart-count']")).click();
		log.info("Clicking on Add to Cart icon");		
		List<WebElement> selectedItems = driver.findElements(By.cssSelector("a.a-link-normal.sc-product-link"));
		log.info("Getting all the items in the webpage");
		
		for(int i =0 ; i<selectedItems.size();i++)
		{
			SelectedItem = selectedItems.get(i).getText();
			log.info("Getting the text of the selected item from the cart");
			List<String> itemNeeded = Arrays.asList(productNames);
		if(itemNeeded.contains(SelectedItem))
					{
				
					break;
					}
		}
		Assert.assertEquals(ActualItem + " Girls' Baby 3-Pack Dress, Dots, Preemie" , SelectedItem);
		log.info("Comapring the the items added in the cart with actualItem");	
	}
	@Test(enabled=false)
	public void babyRegistry() throws InterruptedException {
		logIn();
		log.info("Successfully Logged In");
		wait = new WebDriverWait(driver, 30);
		log.info("Explicit wait variable is instanciated");
		
		Actions act = new Actions(driver);
		WebElement menuOptions = wait.until(ExpectedConditions.visibilityOfElementLocated(By.xpath("//span[text()='Account & Lists']")));
		act.moveToElement(menuOptions).perform();
		log.info("Hover over to menu options is successfully done");
		
		WebElement babyreg = driver.findElement(By.xpath("//span[@class='nav-text'][contains(text(),'Baby Registry')]"));
		Assert.assertTrue(babyreg.isDisplayed());
		babyreg.click();		
		log.info("Clicked on Baby Registry");
	
		WebElement srt = driver.findElement(By.linkText("Get Started"));
		Assert.assertTrue(srt.isDisplayed());
		srt.click();		
		log.info("Get Started button is clicked");
		
		WebElement day = wait.until(ExpectedConditions.visibilityOfElementLocated(By.xpath("//select[@id='arrivalDate-d-native']")));
		Select d = new Select(day);
		d.getFirstSelectedOption();
		log.info("Day is selected in drop down list");
		
		WebElement month = driver.findElement(By.xpath("//select[@id='arrivalDate-m-native']"));
		Select m = new Select(month);
		m.selectByVisibleText("July");
		log.info("Month is selected in drop down list");
		
		WebElement year = driver.findElement(By.xpath("//select[@id='arrivalDate-y-native']"));
		Select y = new Select(year);
		y.selectByVisibleText("2020");
		log.info("Year is selected in drop down list");
		
		driver.findElement(By.xpath("//span[text()='Select an address']")).click();
		driver.findElement(By.linkText("Add new address")).click();
		driver.findElement(By.xpath("//input[@id='address-ui-widgets-enterAddressFullName']")).sendKeys(prop.getProperty("name"));
		
		driver.findElement(By.xpath("//input[@id='address-ui-widgets-enterAddressLine1']")).sendKeys(prop.getProperty("address"));
		driver.findElement(By.xpath("//input[@id='address-ui-widgets-enterAddressCity']")).sendKeys(prop.getProperty("city"));
		
		WebElement province = wait.until(ExpectedConditions.visibilityOfElementLocated(By.id("address-ui-widgets-enterAddressStateOrRegion-dropdown-nativeId")));		
		Select pro = new Select(province);
		pro.selectByValue("Ontario");
		log.info("Ontario is selected in drop down list");
		Thread.sleep(2000);
		
		driver.findElement(By.xpath("//input[@id='address-ui-widgets-enterAddressPostalCode']")).sendKeys(prop.getProperty("pin"));
		log.info("Pin is entered");
		
		driver.findElement(By.xpath("//input[@id='address-ui-widgets-enterAddressPhoneNumber']")).sendKeys(prop.getProperty("ph-no"));
		log.info("Ph number is entered");
		
		act.moveToElement(driver.findElement(By.xpath("//input[@id='address-ui-widgets-form-submit-button']"))).click();
		log.info("Clied on Add address");
	
		WebElement radio = driver.findElement(By.xpath("//span[text()='Only people with the link']"));
		Assert.assertTrue(radio.isEnabled(), "People with link radio is selected ");
		Thread.sleep(5000);
		driver.findElement(By.xpath("//button[@id='a-autoid-0-announce']")).click();
		log.info("Successfully created baby registry");
	}
	@AfterTest
	public void closeBrowser()
	{
		//driver.close();
	}

}

		
		
	
