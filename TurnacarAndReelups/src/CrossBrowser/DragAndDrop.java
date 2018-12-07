package CrossBrowser;

import org.openqa.selenium.By;
import org.openqa.selenium.JavascriptExecutor;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.firefox.FirefoxDriver;
import org.openqa.selenium.interactions.Actions;

public class DragAndDrop {

	public static void main(String[] args) throws InterruptedException {
		// TODO Auto-generated method stub
		// for firefox
		System.setProperty("webdriver.gecko.driver","D:\\Learning\\geckodriver.exe");
		
		//code to clean the console and move the log messages to the given file
		System.setProperty(FirefoxDriver.SystemProperty.DRIVER_USE_MARIONETTE,"true");
		System.setProperty(FirefoxDriver.SystemProperty.BROWSER_LOGFILE,"C:\\temp\\logs.txt");
		System.out.println("Refer to C:\\temp\\logs.txt file to see the log messages");
		WebDriver driver = new FirefoxDriver();

		//for chrome
		/*System.setProperty("webdriver.chrome.driver","D:\\Learning\\chromedriver.exe");
		WebDriver driver = new ChromeDriver();*/
		
		driver.manage().window().maximize();
		driver.get("http://demo.guru99.com/test/drag_drop.html");
		
		// scroll the page by 100 pixel so that elements are visible
		JavascriptExecutor js = (JavascriptExecutor) driver;
		js.executeScript("window.scrollBy(0,100)");
		Thread.sleep(3000);
		
		// Element to drag
		WebElement from = driver.findElement(By.xpath("//*[@id='credit2']/a"));
		
		// Element on which the above is drop
		WebElement to = driver.findElement(By.xpath("//*[@id='bank']/li"));
		
		// creating object of actions class
		Actions act = new Actions(driver);
		act.dragAndDrop(from, to).build().perform();
		 
	}

}
