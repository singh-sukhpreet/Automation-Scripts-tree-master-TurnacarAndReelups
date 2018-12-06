package CrossBrowser;

import java.io.FileInputStream;
import java.io.IOException;
import java.util.Properties;

import org.openqa.selenium.By;
import org.openqa.selenium.JavascriptExecutor;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.firefox.FirefoxDriver;

public class FileUpload {

	public static void main(String[] args) throws InterruptedException, IOException {
		// TODO Auto-generated method stub
			
			System.setProperty("webdriver.gecko.driver","D:\\Learning\\geckodriver.exe");

			// code to clean the console and move the log messages to the given file
			System.setProperty(FirefoxDriver.SystemProperty.DRIVER_USE_MARIONETTE,"true");
			System.setProperty(FirefoxDriver.SystemProperty.BROWSER_LOGFILE,"C:\\temp\\logs.txt");
			System.out.println("Refer to C:\\temp\\logs.txt file to see the log messages");
				 
			WebDriver driver = new FirefoxDriver();
			Properties p = new Properties();
			FileInputStream fis = new FileInputStream(System.getProperty("user.dir") + "\\locators.properties");
			p.load(fis);
			// opening file containing the code - <input type="file" id="fileupload" name="Text">
			driver.get(p.getProperty("Navigation_link"));
			
			//driver.findElement(By.id("fileupload")).click();
			// if the above code is not wokring in the firefox the use the following code
			JavascriptExecutor js = (JavascriptExecutor)driver;
			System.out.println("hi1");
			//123456
			js.executeAsyncScript("window.scrollBy(0,600)");
		
			driver.quit();
			System.out.println("hi2");
			/*executor.executeScript("arguments[0].click();", driver.findElement(By.id("fileupload")));
			Thread.sleep(2000);
			
			// code to execute the script written in AutoIt Editor and located in the mention path
			 auto id script code
				ControlFocus("File Upload","","Edit1")
				ControlSetText("File Upload","","Edit1","C:\Users\Public\Pictures\Sample Pictures\Screenshot_1543404751.png")
				ControlClick("File Upload","","Button1")
			Runtime.getRuntime().exec("D:\\Learning\\AutoIt\\FileUpload.exe");
			Thread.sleep(2000);*/
			
		
	}

}

