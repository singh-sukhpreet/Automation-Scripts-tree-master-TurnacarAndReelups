package CrossBrowser;

import java.io.IOException;

import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.firefox.FirefoxDriver;

public class DownloadFile {

	public static void main(String[] args) {
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
		driver.get("http://demo.guru99.com/test/yahoo.html");
		
		// getting the value of href of the link
		String source = driver.findElement(By.id("messenger-download")).getAttribute("href");
		System.out.println(source);
		// setting command which will download the file
		
		String command = "cmd /c start C:\\Wget\\wget.exe -P C:\\Users\\HR\\Downloads --no-check-certificate"+" " + source;
		System.out.println(command);
		// calling wget code
		/*To run this command you need to download the wget.exe from  https://eternallybored.org/misc/wget/ */
		
		try{
			Process exec = Runtime.getRuntime().exec(command);
			int waiting_time = exec.waitFor();
			System.out.println("Time is "+waiting_time);
		} catch (InterruptedException | IOException ex) {
	        System.out.println(ex.toString());
	        }
		driver.close();
				

	}

}
