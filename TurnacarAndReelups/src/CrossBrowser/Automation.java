package CrossBrowser;
import org.apache.commons.codec.binary.Base64;
import org.apache.commons.csv.CSVFormat;
import org.apache.commons.csv.CSVParser;
import org.apache.commons.csv.CSVRecord;
import org.openqa.selenium.By;
import org.openqa.selenium.Dimension;
import org.openqa.selenium.JavascriptExecutor;
import org.openqa.selenium.NoSuchElementException;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.remote.DesiredCapabilities;
import org.openqa.selenium.remote.RemoteWebDriver;

import java.io.IOException;
import java.io.OutputStreamWriter;
import java.io.Reader;
import java.net.HttpURLConnection;
//import java.net.MalformedURLException;
import java.net.URL;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.List;
import java.text.SimpleDateFormat;  
import java.util.Date; 

public class Automation {
	// declaring global variables
	public static RemoteWebDriver driver = null;
	public static String currenturl = null;
	static String button_link1 =null;
	public static String button_link2=null;
	static String Navigation_Link, Navigation_Link2, descText;
	static String oldButtonWidth, newButtonWidth, oldButtonPostion, newButtonPostion;

	//declaring global variables for input file 
	static String Player_Configuration_Link, Option_name, Option_name2, Option_name3, Option_value, Option_value2, Option_value3;
	//Player_Configuration_Link = Option_name = Option_name2 = Option_name3 = Option_value = Option_value2 = Option_value3 = null;
	
	public static void main(String[] args) throws IOException, InterruptedException {
		
		String SAMPLE_CSV_FILE_PATH = "./doc/testing.csv";
		String username = "support%40klevur.com";
		String authkey = "u9ba8cb6ca275e67";
		Automation cbt = new Automation(username,authkey);
		// setting capabilities from cross-browser API
		DesiredCapabilities caps = new DesiredCapabilities();
		
		/*desktop caps chrome*/
		caps.setCapability("browserName", "chrome");
		caps.setCapability("platform", "Windows 10");
		caps.setCapability("screenResolution", "1366x768");
		
		/*mobile caps*/
		/*caps.setCapability("browserName", "Chrome");
		caps.setCapability("deviceName", "Galaxy S7");
		caps.setCapability("platformVersion", "7.0");
		caps.setCapability("platformName", "Android");
		caps.setCapability("deviceOrientation", "portrait");*/
		
		caps.setCapability("record_video", "true");
		caps.setCapability("record_snapshot", "true");
		String score = null;
		
		// file reading code
		Reader reader = Files.newBufferedReader(Paths.get(SAMPLE_CSV_FILE_PATH));
        CSVParser csvParser = new CSVParser(reader, CSVFormat.DEFAULT
                .withFirstRecordAsHeader()
                .withIgnoreHeaderCase()
                .withTrim());
        
        // date and time when script start outside for loop
		SimpleDateFormat formatter = new SimpleDateFormat("dd/MM/yyyy HH:mm:ss");  
	    Date date = new Date();  
	    System.out.println("At Start current date and time is " +formatter.format(date)); 
	    
        // loop for each case mention in the csv file
	    for (CSVRecord csvRecord : csvParser) {
        	caps.setCapability("name", csvRecord.get("DESCRIPTION"));
			driver = new RemoteWebDriver(new URL("http://" + username + ":" + authkey +"@hub.crossbrowsertesting.com:80/wd/hub"), caps);
			cbt.setSessionId(driver.getSessionId().toString());
			
			if(csvRecord.getRecordNumber()==1 && !csvRecord.get("Player_Configuration_Link").isEmpty()){
				System.out.println("Inside Record One");
				loginonce();
			}
	        
			// Accessing player configuration link
			if(!csvRecord.get("Player_Configuration_Link").isEmpty()){
	        	
				Player_Configuration_Link = csvRecord.get("Player_Configuration_Link");
        		driver.get(Player_Configuration_Link);
        		Thread.sleep(1000);
        		currenturl = driver.getCurrentUrl();
        		// below if code runs if browser redirects to the following urls from the player configuration link
                if(currenturl.equals("http://larengine.redinv.redlinepages.com/") || currenturl.equals("https://dev-engine.redlineinventory.com/") ){
                	System.out.println("Redirect login");
                	loginonce();
                }
            
        	// getting the value mention in option_name column
        	if(!csvRecord.get("option_name").isEmpty()){
        		System.out.println("When option_name column is not empty and calling checkingLoginOnce()");
        		checkingLoginOnce();
        		
        		if(csvRecord.get("Option_name").equals("widget_integration_type") && !csvRecord.get("Option_xpath").isEmpty()) {
        			String optionXpath = csvRecord.get("Option_xpath");
        			String optionName= csvRecord.get("Option_name");
        			String optionValue = csvRecord.get("Option_value");
        			checkingXpath(optionXpath,optionName,optionValue);
        			//System.out.println("inside not empty xpath and xpath is "+csvRecord.get("Option_xpath"));
        		}
        		
        		else{
        			System.out.println("Insdie else part of option_name");
        			Option_name = csvRecord.get("Option_name");
	                WebElement set = driver.findElement(By.name(Option_name));
	                // setting the value mention in option_value column
	                Option_value = csvRecord.get("Option_value");
	                //set.clear();
	                //Thread.sleep(1000);
	                driver.findElement(By.name(Option_name));
	                set.sendKeys(Option_value);
	                //Thread.sleep(1000);
	                System.out.println("Setting "+Option_value+" in "+Option_name);
	                
	                // setting widget integration type to inline
	                if(csvRecord.get("Category").equals("INLINE")){
	                	if(csvRecord.get("DESCRIPTION").equals("Test Show Player Views and Player View Days")){
	                		clickonCheckBox(3);
	                	}
	                	settingInline();
	                }	
                }
        	}
            
         // getting the value mention in option_name2 column
            if(!csvRecord.get("option_name2").isEmpty()){
            	System.out.println("Inside Option_name2 is not emtpy and calling checkingLoginOnce()");
            	// checking Too many Login Attempts error
            	checkingLoginOnce();
            	
            	// the below if code works only for widget button width
            	if(csvRecord.get("option_name2").equals("widget_button_width")){
            		System.out.println("when option_name = to widget_button_width and clicking on Checkbox");
            		clickonCheckBox(1);
            	}
                Option_name2 = csvRecord.get("Option_name2");
                WebElement set2 = driver.findElement(By.name(Option_name2));
                // setting the value mention in option_value2 column
                Option_value2 = csvRecord.get("Option_value2");
                set2.clear();
                //Thread.sleep(2000);
                set2.sendKeys(Option_value2);
                //Thread.sleep(2000);
                // setting widget integration type to inline
                if(csvRecord.get("Category").equals("INLINE")){
                	settingInline();
                }
            }
               
            
         // getting the value mention in option_name3 column
            if(!csvRecord.get("option_name3").isEmpty()){
            	System.out.println("Inside Option_name3 is not emtpy and calling checkingLoginOnce()");
            	// checking Too many Login Attempts error
            	checkingLoginOnce();
            	
            	Option_name3 = csvRecord.get("Option_name3");
                WebElement set3 = driver.findElement(By.name(Option_name3));
                // setting the value mention in option_value2 column
                Option_value3 = csvRecord.get("Option_value3");
                set3.clear();
                //Thread.sleep(2000);
                set3.sendKeys(Option_value3);
                //Thread.sleep(2000);
            }
            
            // submitting the above setting
            submitsetting();
        }
           
            // Navigate to link1 to see the result
            System.out.println("Navigating to Link1");
            //driver.manage().deleteAllCookies();
            Navigation_Link = csvRecord.get("Navigation_Link");
            driver.get(Navigation_Link);
            Thread.sleep(4000);
            
            // if value in category column is inlie
            if(csvRecord.get("Category").equals("INLINE")){
            	//System.out.println("inside inline");
            	// getting values from the columns mention in the csv file
            	String description = csvRecord.get("DESCRIPTION");
            	String option_value = csvRecord.get("Option_value");
            	String option_value2 = csvRecord.get("Option_value2");
            	String output_id=csvRecord.get("Output_id");
            	// calling this method to distinguish the each case
            	gettingDescription(description, option_value, option_value2, output_id);
            	
            	// redirecting to player configuration link to set the default values
            	driver.get(csvRecord.get("Player_Configuration_Link"));
            	
            	/* if the page is redirected to the login page from the configuration 
            	setting page then the loginonce function called to login the sytem*/
            	if(driver.getCurrentUrl().equals("https://dev-engine.redlineinventory.com/")){
            		loginonce();
            	}
                	
                // setting default values in the player configuration after testing the result
            	String option_name2=csvRecord.get("Option_name2");
            	settingDefaultValues(option_name2);
                	
            	// submit the above setting which is in function settingDefaultValues()
                submitsetting();
            } // category inline closes
            
           String category = csvRecord.get("Category");
           String testId = csvRecord.get("Test ID");
           String navigationLink = csvRecord.get("Navigation_Link");
           String outputId = csvRecord.get("Output_id");
           String classNameLink1 = csvRecord.get("ClassName_Link1");
           String PlayerConfigLink = csvRecord.get("Player_Configuration_Link");
           String optionName2 = csvRecord.get("Option_name2");
           String description = csvRecord.get("DESCRIPTION");
           String optionName = csvRecord.get("Option_name");
           gettingCategory(category,testId,navigationLink,classNameLink1,outputId,PlayerConfigLink,optionName2,description,optionName);
           
           Option_value = csvRecord.get("Option_value");
           Option_value2 = csvRecord.get("Option_value2");
           Option_value3 = csvRecord.get("Option_value3");
           
            // setting button_link1 added else if(remove it from here if output is wrong)
            if(!Option_value.isEmpty()){
            	gettingOptionValues(Option_value,Option_value2,Option_value3,outputId);
           }
            
            // Navigate to link2 to see the result
            if(!csvRecord.get("Navigation_Link2").isEmpty()){
            	System.out.println("Navigating to link2");
	            Navigation_Link2 = csvRecord.get("Navigation_Link2");
	            driver.get(Navigation_Link2);
	            Thread.sleep(4000);
            }
            
            // checking button css
            if(csvRecord.get("Category").equals("Button CSS")){
            	String newbuttonPostion = driver.findElement(By.xpath("//*[@class='tac-link-top-container'][1]")).getCssValue("position");
      		  	System.out.println("new button position is "+newbuttonPostion);
	      		if(newbuttonPostion.equals("absolute")){
	      			//System.out.println("Pass");
	      			button_link2="pass";
	      		}
	      		else{
	      			//System.out.println("fail");
	      			button_link2="fail";
	      		}
	      		driver.get(csvRecord.get("Player_Configuration_Link"));
        		//System.out.println("empty the css box");
        		driver.findElement(By.name("widget_css")).clear();
        		driver.findElement(By.className("btn-blue")).click();
        		Thread.sleep(1000);
        		// code added
            	//driver.findElement(By.className("confirm")).click();
            }
         
            
            // checking button width
            else if(csvRecord.get("Category").equals("Button Width")){
            	newButtonWidth = driver.findElement(By.xpath("//*[@id='"+csvRecord.get("Output_id2")+"']/div")).getCssValue("width");
            	//System.out.println(newButtonWidth);
        		if(newButtonWidth.equals("127px") || newButtonWidth.equals("236px") || newButtonWidth.equals("380px")){
        			button_link2="pass";
        			//System.out.println("button 2 is "+ button_link2);
        		}
        		else{
        			button_link2="fail";
        			//System.out.println("button 2 is "+ button_link2);
        		}
        		driver.get(csvRecord.get("Player_Configuration_Link"));
        		System.out.println("unclicking the Checkbox");
        		
        		clickonCheckBox(1);
        		driver.findElement(By.className("btn-blue")).click();
        		Thread.sleep(1000);
        		// code added
            	//driver.findElement(By.className("confirm")).click();
        		
            }  
            
            //checking button click on 2nd navigation_link
            else if(csvRecord.get("Category").equals("Button Click")){
            	driver.manage().window().maximize();
            	String class2=null,id2=null;
            	if(!csvRecord.get("ClassName_Link2").isEmpty()){
            		 class2 = csvRecord.get("ClassName_Link2");
            	}
            	if(!csvRecord.get("Output_id2").isEmpty()){
            		id2 = csvRecord.get("Output_id");
            	}
            	driver.findElement(By.xpath("//*[@class='"+class2+"']/a/div")).click();
            	Thread.sleep(2000);
            	String cssValueOnBtnClick=driver.findElement(By.xpath("//*[@id='"+id2+"']")).getCssValue("display");
            	if(cssValueOnBtnClick.equals("block")){
            		//System.out.println("in IF 2 pass");
        			button_link2="pass";
            	}
            	else{
            		//System.out.println("in else 2fail");
        			button_link2="fail";
            	}
            }
            
            // setting button_link2
            if(!csvRecord.get("Option_value").isEmpty() && !csvRecord.get("Navigation_Link2").isEmpty()){
            	String outputId2 = csvRecord.get("Output_id2");
            	gettingOptionValues2(Option_value,Option_value2,Option_value3,outputId2);
            }
            
           
            if( button_link1.equals("pass") && button_link2.equals("pass") ){
        	score="pass";
            }
            else{
            	score="fail";
            }
	        // setting status of the test
	        cbt.setScore(score);
            System.out.println("Record No. " + csvRecord.getRecordNumber()+" is executed" +" and status is "+score);
            // setting description of the test
            // write description here
            Thread.sleep(3000);
            driver.quit();
            Thread.sleep(5000);
        }
        Thread.sleep(3000);
        // for loop end
        csvParser.close();
        reader.close();
        
     // date and time when script ends
		
		SimpleDateFormat formatter2 = new SimpleDateFormat("dd/MM/yyyy HH:mm:ss");  
	    Date enddate = new Date();  
	    System.out.println("At End current date and time is " +formatter2.format(enddate));  
	}
																
	private static void settingInline() {
		System.out.println("inside settinginline function");
		if (!driver.findElement(By.xpath("//div[@class='panel-body']/form/table/tbody/tr[16]/td[2]/select//option[6]")).isSelected()) {
    		driver.findElement(By.xpath("//div[@class='panel-body']/form/table/tbody/tr[16]/td[2]/select//option[6]"));
    		driver.findElement(By.xpath("//div[@class='panel-body']/form/table/tbody/tr[16]/td[2]/select//option[6]")).click();
            System.out.println("Inside option_name2 and setting widget integration type to inline");
         }
	}

	private static void gettingOptionValues(String Option_value, String Option_value2, String Option_value3,
			String outputId) {
		
		//try fail
    	if(Option_value.equals("Inactive") && !outputId.isEmpty()){
    		tryFail(outputId);
    	}
    	
    	else if(Option_value.equals("Active") && !outputId.isEmpty()){
    		tryPass(outputId);
    	}
    	
    	else if( (Option_value.equals("inline") || Option_value.equals("child:vpopup"))  && Option_value2.equals("chill")){
    		tryPass(outputId);
    	}
    	
    	else if( (Option_value.equals("inline") || Option_value.equals("child:vpopup")) && Option_value3.equals("panel-body")){
    		tryPass(outputId);
    	}
    	
    	// try fail
    	else if( (Option_value.equals("inline") || Option_value.equals("child:vpopup")) && Option_value2.equals("anything")){
    		tryFail(outputId);
    	}
    	
    	else if(Option_value.equals("no setting")){
    		tryPass(outputId);
    	}
    	
    	else if(Option_value.equals("child:vpopup")){
    		tryPass(outputId);
    	}
    	
    	else if(Option_value.equals("append:vpopup")){
    		tryPass(outputId);
    	}
    	
    	else if(Option_value.equals("prepend:vpopup")){
    		tryPass(outputId);
    	}
		
	}

	private static void tryPass(String outputId) {
		try {
  		   driver.findElement(By.id(outputId));
  		   button_link1 = "pass";
  		   System.out.println("after nav1 pass");
  		} catch (NoSuchElementException e) {
  			button_link1 = "fail";
  			System.out.println("after nav1 fail");
  		}
	}

	private static void tryFail(String outputId) {
		try {
  		   driver.findElement(By.id(outputId));
  		   button_link1 = "fail";
  		   System.out.println("after nav1 fail");
  		} catch (NoSuchElementException e) {
  			button_link1 = "pass";
  			System.out.println("after nav1 pass");
  		}
	}
	
	private static void gettingOptionValues2(String option_value4, String option_value22, String option_value32,
			String outputId2) {
		// try fail
    	if(Option_value.equals("Inactive")){
    		tryFail2(outputId2);
    	} 
    	
    	else if(Option_value.equals("Active")){
    		tryPass2(outputId2);
    	}
    	
    	// try fail
    	else if( (Option_value.equals("inline") || Option_value.equals("child:vpopup")) && Option_value2.equals("chill")){
    		tryFail2(outputId2);
    	}
    	
    	
    	else if( (Option_value.equals("inline") || Option_value.equals("child:vpopup")) &&  Option_value3.equals("panel-body")){
    		tryPass2(outputId2);
    	}
    	
    	// try fail
    	else if( (Option_value.equals("inline") || Option_value.equals("child:vpopup")) && Option_value2.equals("anything")){
    		tryFail2(outputId2);
    	}
    	
    	else if(Option_value.equals("no setting")){
    		tryPass2(outputId2);
    	}
    	
    	else if(Option_value.equals("child:vpopup")){
    		tryPass2(outputId2);
    	}
    	
    	else if(Option_value.equals("append:vpopup")){
    		tryPass2(outputId2);
    	}
    	
    	else if(Option_value.equals("prepend:vpopup")){
    		tryPass2(outputId2);
    	}
		
	}

	private static void tryPass2(String outputId2) {
		try {
  		   driver.findElement(By.id(outputId2));
  		   button_link2 = "pass";
  		   System.out.println("after nav2 pass");
  		} catch (NoSuchElementException e) {
  			button_link2 = "fail";
  			System.out.println("after nav2 fail");
  		}
	}

	private static void tryFail2(String outputId2) {
		try {
  		   driver.findElement(By.id(outputId2));
  		   button_link2 = "fail";
  		   System.out.println("after nav2 fail");
  		} catch (NoSuchElementException e) {
  			button_link2 = "pass";
  			System.out.println("after nav2 pass");
  		}
	}

	private static void gettingCategory(String category, String testId, String navigationLink, String classNameLink1,
		String outputId, String playerConfigLink, String optionName2 , String description, String optionName) throws InterruptedException {
		
		// checking youtube functionality
        if(category.equals("Youtube")){
        	String testid=testId;
        	String result = youtube(testid);
        	settingResultPass(result);
        }
        
		// checking maximization on desktop
        else if(category.equals("Maximization")){
        	String testid=testId;
        	String result = maximization(testid);
        	settingResultPass(result);
        }
        
     // checking theme on desktop
        else if(category.equals("Theme")){
        	String result = themeButtons(testId);
        	settingResultPass(result);
        }
        
        // checking 3 button appearance
        else if(category.equals("Button Appearance")){
        	String testid=testId;
        	String result = ButtonAppearance(testid);
        	settingResultPass(result);
        }
        
        // checking close out to full screen 
        else if(category.equals("Close out Fullscreen")){
        	String result = closeOut();
        	settingResultPass(result);
        }
        
        
        // checking no click to text
        else if(category.equals("no click to text")){
        	String testid=testId;
        	String result=noClick(testid);
        	settingResultPass(result);
        }
                    
        // checking auto scale of text
        else if(category.equals("short code auto-scaling")){
        	//System.out.println("in auto scale");
        	String result=shortcodescale();
        	//System.out.println("return from function shortcodescale");
        	settingResultPass(result);
        }
        
        // checking vertical maximization behaviour 
        else if(category.equals("CORE_PLAYER vertical maximization")){
        	String status=verticalBehaviour(navigationLink);
        	passingCondtion(status);
        	
        }
        
        // checking vertical maximization behaviour 
        else if(category.equals("CORE_PLAYER horizontal maximization")){
        	String status=horizontalBehaviour(navigationLink);
        	passingCondtion(status);
        }
        
        // checking Resizing of browser 
        else if(category.equals("CORE_PLAYER resizing browser")){
        	String status=resizingOfBrowser(navigationLink);
        	//System.out.println("inside if condition of resizing browser status is "+status);
        	passingCondtion(status);
        }
        
        
        //checking button css
        else if(category.equals("Button CSS")){
        	newButtonPostion = driver.findElement(By.xpath("//*[@class='tac-link-top-container'][1]")).getCssValue("position");
  		  	//System.out.println("new button position is"+newbuttonPostion);
      		if(newButtonPostion.equals("absolute")){
      			//System.out.println("Pass");
      			button_link1="pass";
      			 
      		}
      		else{
      			//System.out.println("fail");
      			button_link1="fail";
      		}
        }
                    
        
        // checking button width
        else if(category.equals("Button Width")){
        	newButtonWidth = driver.findElement(By.xpath("//a[@id='"+outputId+"']/div")).getCssValue("width");
        	//System.out.println("new Button width is "+ newButtonWidth);
    		if( newButtonWidth.equals("127px") || newButtonWidth.equals("380px") || newButtonWidth.equals("236px") ){
    			button_link1="pass";
    			//System.out.println("button 1 is "+ button_link1);
    		}
    		else{
    			button_link1="fail";
    			//System.out.println("button 1 is "+ button_link1);
    		}
        }
        
        //checking button click
        else if(category.equals("Button Click")){
        	driver.manage().window().maximize();
        	String class1=null,id1=null;
        	if(!classNameLink1.isEmpty()){
        		class1 = classNameLink1;
        	}
        	if(!outputId.isEmpty()){
        		id1 = outputId;
        	}
        	
        	driver.findElement(By.xpath("//*[@class='"+class1+"']/a/div")).click();
        	Thread.sleep(2000);
        	String cssValueOnBtnClick=driver.findElement(By.xpath("//*[@id='"+id1+"']")).getCssValue("display");
        	if(cssValueOnBtnClick.equals("block")){
        		//System.out.println("in IF pass");
    			button_link1="pass";
        	}
        	else{
        		//System.out.println("in else fail");
    			button_link1="fail";
        	}
        }
        
        // checking color scheme of the player
        else if(category.equals("ColorSheme")){
        	driver.switchTo().frame(0);
        	String background_color = driver.findElement(By.id("rlHeaderBar")).getCssValue("background-color");
        	//System.out.println(background_color);
        	if(background_color.equals("rgba(132, 56, 107, 1)")){
    			//System.out.println("in IF pass - color scheme");
    			button_link1="pass";button_link2="pass";
    		}
    		else{
    			//System.out.println("in else fail - color scheme");
    			button_link1="fail";button_link2="fail";
    		}
        	
        	driver.get(playerConfigLink);
        	Option_name2 = optionName2;
            WebElement set2 = driver.findElement(By.name(Option_name2));
            // setting the value mention in option_value2 column
            set2.clear();
            //Thread.sleep(2000); setting the default green color
            set2.sendKeys("#83b840");
            //Thread.sleep(2000);
           
            // setting the default widget integration type i.e no setting
            if (!driver.findElement(By.xpath("//div[@class='panel-body']/form/table/tbody/tr[16]/td[2]/select//option[7]")).isSelected()) {
	            driver.findElement(By.xpath("//div[@class='panel-body']/form/table/tbody/tr[16]/td[2]/select//option[7]")).click();
	            //System.out.println("option value is "+csvRecord.get("Option_value"));
	         }
            
            // submit the above setting
            submitsetting();
        }
        
        // checking TickerText display, not display, font scaling
        else if(category.equals("TickerText")){
    	   	String TickerText = driver.findElement(By.id(outputId)).getText();
        	//System.out.println("Ticker text is "+TickerText);
        	
        	descText = description;
        	//System.out.println("Description is "+descText);
        	// when ticker text is not empty
        	if(!TickerText.isEmpty() && descText.equals("Confirm ticker text appears")){
        		button_link1="pass";button_link2="pass";
        		//System.out.println("first if IN pass ticker text present");
        	}
        	// when ticker text is empty
        	else if(TickerText.isEmpty() && (descText.contains("no ticker text") || descText.contains("does not appear")) ){
        		button_link1="pass";button_link2="pass";
        		//System.out.println("second else if IN pass ticker text not present");
        	}
        	//when ticker text is not empty and desc is equals to does not scale
        	else if(!TickerText.isEmpty() && (descText.equals("Confirm ticker text does not scale") || descText.equals("Test TextUps Short code/ keyword autoscaling"))){
        		//System.out.println("in 3rd else if");
        		String result=tickerTextScale();
        		settingResultPass(result);
        	}
        	else if(!TickerText.isEmpty() && (descText.equals("Confirm TextUps short code") || descText.equals("Confirm textups short code/ keyword message"))){
        		button_link1="pass";button_link2="pass";
        	}
        	else{
        		button_link1="fail";button_link2="fail";
        		System.out.println("IN last else fail statement");
        	}
        }
        
        // checking price display
        else if(category.equals("Price")){
        	
        	System.out.println("inside price category");
        	String priceTag=null;
        	descText = description;
        	System.out.println("desc is "+descText);
        	
        	if(descText.contains("does not display")){
    			System.out.println("inside does not display");
        		priceTag = driver.findElement(By.id(outputId)).getText();
        		if(priceTag.isEmpty()){
        			button_link1="pass";button_link2="pass";
        			System.out.println("inside paass");
        		}
        		else{
        			button_link1="fail";button_link2="fail";
        			System.out.println("inside fail");
        		}
    			
    		}
        	
        	else if(descText.equals("Confirm price label display")){
    			System.out.println("inside confirm price label");
        		
        		priceTag = driver.findElement(By.id(outputId)).getText();
        		if(!priceTag.isEmpty()){
        			button_link1="pass";button_link2="pass";
        			System.out.println("inside paass");
        		}
        		else{
        			button_link1="fail";button_link2="fail";
        			System.out.println("inside fail");
        		}
    		}
        	
        	else if(descText.equals("Test Show Price")){
    			System.out.println("inside show price");
        		driver.findElement(By.id("rl-tac-btn-4S4BSADC8H3406319")).click();
        		System.out.println("after click");
        		driver.switchTo().frame(0);
        		priceTag = driver.findElement(By.id(outputId)).getText();
        		if(!priceTag.isEmpty()){
        			button_link1="pass";button_link2="pass";
        		}
        		else{
        			button_link1="fail";button_link2="fail";
        		}
        		driver.get(playerConfigLink);
        		WebElement set = driver.findElement(By.name(optionName));
                set.sendKeys("No");
                Thread.sleep(1000);
                // code added 
            	//driver.findElement(By.className("confirm")).click();
    		}
        	
        	else if(descText.contains("does not scale")){
        		System.out.println("does not scale");
        		//driver.switchTo().frame("player");
        		String cssfontsize1=driver.findElement(By.className("tag")).getCssValue("font-size");
        		Dimension d1 = new Dimension(800,480);
        		driver.manage().window().setSize(d1);
        		String cssfontsize2=driver.findElement(By.className("tag")).getCssValue("font-size");
        		Thread.sleep(3000);
        		Dimension d2 = new Dimension(650,550);
        		driver.manage().window().setSize(d2);
        		String cssfontsize3=driver.findElement(By.className("tag")).getCssValue("font-size");
        		Thread.sleep(3000);
        		
        		if(cssfontsize1.equals("16px") && cssfontsize2.equals("16px") && cssfontsize3.equals("16px")){
        			button_link1="pass";button_link2="pass";
        		}
        		else{
        			button_link1="fail";button_link2="fail";
        		}
        	}
        } // category price ends
	}// getting category function ends

	private static String themeButtons(String testid) throws InterruptedException {
		String status = null;
		if(testid.equals("RU_THEME_TEST")){
			WebElement xpath = driver.findElement(By.xpath("html/body/div[1]/div/div[2]/div"));
			System.out.println(driver.findElement(By.xpath("html/body/div[1]/div/div[2]/div")).getAttribute("class"));
			List<WebElement> alldivs = xpath.findElements(By.className("row"));
			//System.out.println("Number of div tags = " + alldivs.size());
			String css[] = new String[alldivs.size()];
			for(int i=1;i<=alldivs.size();i++) {
				String setting = driver.findElement(By.xpath("html/body/div[1]/div/div[2]/div/div["+i+"]/div/div/div/h1")).getText();
				driver.findElement(By.xpath("html/body/div[1]/div/div[2]/div/div["+i+"]/div/div/div[2]/div[1]/p/a")).click();
				loginonce();
				if (driver.findElement(By.xpath("//div[@class='panel-body']/form/table/tbody/tr[4]/td[2]")).getText() != setting ) {
		            driver.findElement(By.xpath("//div[@class='panel-body']/form/table/tbody/tr[15]/td[2]")).sendKeys(setting);
		           System.out.println("Setting Theme to "+ setting);
		           submitsetting();
		         }
				driver.get("https://widget-test-site.s3.amazonaws.com/THEMES_TEST/button_theme_tests.html");
				Thread.sleep(3000);
				driver.findElement(By.xpath("html/body/div[1]/div/div[2]/div/div["+i+"]/div/div/div[2]/div[2]/div/a/div")).click();
				css[i] =driver.findElement(By.xpath("//div[@id='ru-lightbox-overlay'][1]")).getAttribute("display");
				System.out.println(css[i]);
			}
		}
		else if(testid.equals("TAC_THEME_TEST")){
			String expectedImage1Src ="https://dev.turnacar-content.redlineinventory.com/player_themes/lib/lexus-00/open_turnacar.png";
			String actualImage1Src=driver.findElementByXPath("//a[@id='rl-tac-btn-1FADP3K21EL180226']/div/img").getAttribute("src");
			// scrolling the page to reach to second link
			JavascriptExecutor js = (JavascriptExecutor) driver;       		
	        WebElement Element = driver.findElement(By.xpath("(//div[@class='panel-body'])[2]"));	
	       	js.executeScript("arguments[0].scrollIntoView();", Element);
	       	// clicking on second link
	       	driver.findElementByXPath("(//div[@class='well'])[2]/p/a").click();
	       	driver.findElementByName("player_theme");
	       	driver.findElementByName("player_theme").sendKeys("no setting");
	       	submitsetting();
	       	driver.get(Navigation_Link);
	       	Thread.sleep(1000);
	       	// scrolling the page to reach to second link
	     	JavascriptExecutor jss = (JavascriptExecutor) driver;       		
	     	WebElement Element1 = driver.findElement(By.xpath("(//div[@class='panel-body'])[2]"));	
	     	jss.executeScript("arguments[0].scrollIntoView();", Element1);
	     	String expectedImage2Src ="https://dev.turnacar.redlineinventory.com/images/icons/TurnACar_Button_001.png";
	     	
			String actualImage2Src=driver.findElementByXPath("//a[@id='rl-tac-btn-1FM5K7D81FGB37159']/div/img").getAttribute("src");
			System.out.println(actualImage1Src + "\n"+expectedImage1Src + "\n" + actualImage2Src +"\n"+expectedImage2Src);
			if(expectedImage1Src.equals(actualImage1Src) && expectedImage2Src.equals(actualImage2Src)){
				status = "pass";
			}
			else {
				status = "fail";
			}
				
		}
		else if(testid.equals("RU_THEME_SELECT_TEST")){
			String actualImsSrc1=driver.findElement(By.xpath("//*[@id='rl-ru-btn-3FA6P0K99HR118812']/div/img")).getAttribute("src");
			String actualImsSrc2=driver.findElement(By.xpath("//*[@id='rl-ru-btn-1FTEW1CG9JFA13928']/div/img")).getAttribute("src");
			
			String expectedImsScr=driver.findElement(By.xpath("//*[@id='rl-ru-btn-1FM5K8GT3JGA02294']/div/img")).getAttribute("src");
			
			if(actualImsSrc1.equals(expectedImsScr) || actualImsSrc2.equals(expectedImsScr)){
				status="pass";
			}
			else{
				status="fail";
			}
		}
		return status;
		
	}

	private static String youtube(String testid) throws InterruptedException {
		driver.manage().window().fullscreen();
		String status=null;
		if(testid.equals("RDT_6_1")){
			String className=driver.findElement(By.xpath("//div[@id='rl-ru-player']/div")).getAttribute("class");
			if(className.contains("youtube")){
				status="pass";
			}
			else{
				status="fail";
			}
		}
		else if(testid.equals("RDT_6_2")){
			driver.switchTo().frame(0);
			String className=driver.findElement(By.xpath("//div[@id='player']/div")).getAttribute("class");
			if(className.contains("html5-video-player unstarted-mode ytp-hide-controls ytp-hide-info-bar ytp-large-width-mode")){
				status="pass";
			}
			else{
				status="fail";
			}
		}
		else if(testid.equals("RDT_6_3") || testid.equals("RDT_6_4")){
			Thread.sleep(4000);
			String className=driver.findElement(By.xpath("//div[@id='rl-ru-player']/div")).getAttribute("class");
			if(!className.contains("youtube")){
				status="pass";
			}
			else{
				status="fail";
			}
		}
		else if(testid.equals("RDT_6_5")){
			String className=driver.findElement(By.xpath("//table/tbody/tr/td/div")).getAttribute("class");
			if(className.contains("notavailable")){
				status="pass";
			}
			else{
				status="fail";
			}
		}
		return status;
	}

	private static void checkingXpath(String optionXpath, String optionName, String optionValue) {
		if (!driver.findElement(By.xpath(optionXpath)).isSelected()) {
			driver.findElement(By.xpath(optionXpath));
			driver.findElement(By.xpath(optionXpath)).click();
            System.out.println("Inside if - when first option is widget_integration_type and Column Option_xpath is not empty");
            System.out.println("setting "+optionName+" to "+optionValue);
         }
	}

	private static void gettingDescription(String description, String option_value, String option_value2, String output_id) throws InterruptedException {
		 // checking widget player status when category is inline
    	if(description.equals("Test setting Player Status to Inactive/Active")){
    		String result = widgetPlayer();
    		settingResultPass(result);
        }
    	 
    	//checking widget status when categroy is inline
    	else if(description.equals("Test setting Widget Status to Inactive/Active when widget integration type is inline")){
    		String player_setting_status=option_value;
    		String result=widgetStatus(player_setting_status);
    		settingResultPass(result);
        }
    	
    	// checking Logo visibility
    	else if(description.equals("Test Show TurnACar Logo")){
    		String result=showLogo();
    		settingResultPass(result);
    	}
    	
    	// checking ticker text when category is inline
    	else if(description.contains("Test Show Info Ticker") || description.contains("Test Show Info Ticker Content")){
    		//System.out.println("inside desc ticketext");
    		String desc=description;
    		String result=tickerText(desc);
    		settingResultPass(result);
        }
    	
    	// checking autospin when category is inline
    	else if(description.contains("Test Autospin and Autospin RPS")){
    		String result = autospin();
    		settingResultPass(result);
    	}
    	
    	//checking widget integration type
    	else if(description.contains("Test Inline Integration Types")){
    		String result = widgetType();
    		settingResultPass(result);
    	}
    	
    	// checking widget Target Id and Class when category is inline
    	else if(description.contains("Test Widget Target Id and Class when category is inline")){
    		String result = widgetIdandClass();
    		settingResultPass(result);
    	}
    	
    	//checking widget target modifier when category is inline
    	else if(description.contains("Test Widget Target Modifier")){
    		String option_val = option_value2;
    		String result = widgetTargetModifier(option_val);
    		settingResultPass(result);
    	}
    	
    	//checking widget CSS(Desktop) modifier when category is inline
    	else if(description.contains("Test Widget CSS (desktop) when widget integration type is inline")){
    		String result = widgetCss();
    		settingResultPass(result);
    	}
    	
    	//checking widget CSS(Desktop) modifier when category is inline
    	else if(description.contains("Test TextUps Integration when widget integration type is inline")){
    		String result = textUps();
    		settingResultPass(result);
    	}
    	
    	// checking price label when category is inline
    	else if(description.contains("Test Show Price")){
    		String priceid=output_id;
    		String result = PriceLabel(priceid);
    		settingResultPass(result);
    	}
    	
    	// checking player views when category is inline
    	else if(description.contains("Test Show Player Views and Player View Days")){
    		
    		String result = playerViews();
    		settingResultPass(result);
    	}
		
	}

	private static void settingDefaultValues(String option_name2) {
		// setting show player views to no
    	if (!driver.findElement(By.xpath("//div[@class='panel-body']/form/table/tbody/tr[6]/td[2]/select//option[2]")).isSelected()) {
            driver.findElement(By.xpath("//div[@class='panel-body']/form/table/tbody/tr[6]/td[2]/select//option[2]")).click();
            System.out.println("Setting player views to no");
            
            // setting player view days to 30
            if(option_name2.equals("player_view_days")){
            	driver.findElement(By.name("player_view_days")).clear();
            	driver.findElement(By.name("player_view_days")).sendKeys("30");
            }
         }
    	
    	// setting show logo to no
    	if (!driver.findElement(By.xpath("//div[@class='panel-body']/form/table/tbody/tr[7]/td[2]/select//option[2]")).isSelected()) {
            driver.findElement(By.xpath("//div[@class='panel-body']/form/table/tbody/tr[7]/td[2]/select//option[2]")).click();
            System.out.println("Setting show logo to no");
         }
    	
    	// setting show info ticker to no
    	if (!driver.findElement(By.xpath("//div[@class='panel-body']/form/table/tbody/tr[9]/td[2]/select//option[2]")).isSelected()) {
            driver.findElement(By.xpath("//div[@class='panel-body']/form/table/tbody/tr[9]/td[2]/select//option[2]")).click();
            System.out.println("Setting show info ticker to no");
         }
    	
    	// setting Info Ticker Content to empty
    	if (driver.findElement(By.xpath("//div[@class='panel-body']/form/table/tbody/tr[10]/td[2]/input")).getText().equals("New Content")) {
            driver.findElement(By.name("player_info_ticker_content")).clear();
            System.out.println("Setting Info Ticker Content to empty");
         }
    	
    	// setting Autospin to no
    	if (!driver.findElement(By.xpath("//div[@class='panel-body']/form/table/tbody/tr[12]/td[2]/select//option[2]")).isSelected()) {
            driver.findElement(By.xpath("//div[@class='panel-body']/form/table/tbody/tr[12]/td[2]/select//option[2]")).click();
            System.out.println("Setting autospin to no");
         }
    	
    	// setting widget-integration-type to no-setting
    	if (!driver.findElement(By.xpath("//div[@class='panel-body']/form/table/tbody/tr[16]/td[2]/select//option[7]")).isSelected()) {
            driver.findElement(By.xpath("//div[@class='panel-body']/form/table/tbody/tr[16]/td[2]/select//option[7]")).click();
           System.out.println("Setting widget-integration-type to no-setting");
         }
    	
    	// setting widget target modifier empty
    	if (!driver.findElement(By.xpath("//div[@class='panel-body']/form/table/tbody/tr[19]/td[2]/input")).getAttribute("value").isEmpty()) {
            driver.findElement(By.name("widget_target_modifier")).clear();
            System.out.println("Setting widget target modifier to empty");
         }
    	
    	// setting Widget CSS (desktop) empty
    	if (!driver.findElement(By.xpath("//div[@class='panel-body']/form/table/tbody/tr[21]/td[2]/input")).getAttribute("value").isEmpty()) {
            driver.findElement(By.name("widget_css")).clear();
            System.out.println("Setting Widget CSS (desktop) to empty");
         }
    	
    	// setting TextUps integration to Inactive
    	if (!driver.findElement(By.xpath("//div[@class='panel-body']/form/table/tbody/tr[25]/td[2]/select//option[2]")).isSelected()) {
            driver.findElement(By.xpath("//div[@class='panel-body']/form/table/tbody/tr[25]/td[2]/select//option[2]")).click();
            System.out.println("Setting TextUps integration to Inactive");
         }
		
	}

	private static void settingResultPass(String result) {
		if(result.equals("pass")){
    		button_link1="pass";button_link2="pass";
    	}
    	else{
    		button_link1="fail";button_link2="fail";
    	}
		
	}

	private static void submitsetting() throws InterruptedException {
		try   
        {    
        	if(driver.findElement(By.className("phpdebugbar-close-btn")).isDisplayed()){
            	driver.findElement(By.className("phpdebugbar-close-btn")).click();
            }                                   
        }      
        catch(Exception e)     
        {       
         /**include the else part here*/ 
        	System.out.println("cross button is not available to click");
        }
        System.out.println("clicking submit button to save the player setting");
    	driver.findElement(By.className("btn-blue"));
    	driver.findElement(By.className("btn-blue")).click();
    	Thread.sleep(1000);
    	// code added after changes
    	//driver.findElement(By.className("confirm")).click();
	}

	private static String maximization(String testid) throws InterruptedException {
		String status = null;
		if(testid.equals("RDT_20_2")){
			driver.findElement(By.id("fullscreen-toggle")).click();
			Thread.sleep(2000);
			Dimension bodyWidth=driver.findElement(By.xpath("html/body")).getSize();
			Dimension playerWidth = driver.findElement(By.id("rl-tac-player")).getSize();
			if(bodyWidth==playerWidth){
				status="pass";
			}
			else {
				status="fail";
				}
		}
		else if(testid.equals("RDT_2_1")){
			driver.manage().window().maximize();
			//driver.switchTo().frame(0);
			Thread.sleep(2000);
			driver.findElement(By.className("rl-fullscreen")).click();
			//driver.switchTo().defaultContent();
			Thread.sleep(2000);
			String className = driver.findElement(By.id("rl-ru-player")).getAttribute("class");
			if(className.equals("is-fullscreen")){
				status="pass";
				driver.findElement(By.className("rl-fullscreen")).click();
			}
			else {
				status="fail";
				}
		}
		
		return status;
	}

	private static String playerViews() throws InterruptedException {
		driver.manage().window().maximize();
		driver.switchTo().frame(0);
		String status=null;
		Thread.sleep(2000);
		String viewLabel = driver.findElement(By.className("views-label")).getText();
		System.out.println(viewLabel);
		if(viewLabel.contains("4 Views in the last 30 days") || viewLabel.contains("4 Views in the last 3 days")){
			status="pass";
		}
		else{
			status="fail";
		}
		return status;
		
	}

	private static String PriceLabel(String priceid) {
		driver.manage().window().maximize();
		driver.switchTo().frame(0);
		System.out.println("inside Pricelabel");
		String status=null;
		String priceTag=driver.findElement(By.id(priceid)).getText();
		 
		if(!priceTag.isEmpty()){
			status="pass";
			System.out.println("inside paass");
		}
		else{
			status="fail";
			System.out.println("inside fail");
		}
		return status;
	}

	private static String ButtonAppearance(String testid) throws InterruptedException {
		driver.manage().window().maximize();
		String status=null;
		System.out.println("inside ButtonAppearance");
		String btn1=null,btn2=null,btn3=null;
		
		
		if(testid.equals("RU_ASYNC_SRP") || testid.equals("TAC_ASYNC_SRP")){
			try{
				// modal box closes
				driver.findElement(By.xpath("//div[@class='modal-footer']/button")).click();
				Thread.sleep(1000);
				
				// getting display property of three buttons
				if(testid.equals("RU_ASYNC_SRP")){
					btn1=driver.findElement(By.xpath("//a[@id='rl-ru-btn-3FA6P0K99HR118812']/div/img")).getCssValue("display");
					 //This will scroll the page till the element is found	
					JavascriptExecutor js = (JavascriptExecutor) driver;       		
			        WebElement Element = driver.findElement(By.xpath("//a[@id='rl-ru-btn-1FM5K8HT4HGD78771']/div/img"));	
			       	js.executeScript("arguments[0].scrollIntoView();", Element);
			        
					btn2=driver.findElement(By.xpath("//a[@id='rl-ru-btn-1FTEW1CG9JFA13928']/div/img")).getCssValue("display");
					btn3=driver.findElement(By.xpath("//a[@id='rl-ru-btn-1FM5K8HT4HGD78771']/div/img")).getCssValue("display");
					// checking display property of buttons
					if(btn1.equals("block") && btn2.equals("block") && btn3.equals("block") ){
						status="pass";
					} 
					else{
						status="fail";
					}
				}
				else if(testid.equals("TAC_ASYNC_SRP")){
					String btnXpath=null, playerXpath=null;
					btn1=driver.findElement(By.xpath("//a[@id='rl-tac-btn-1B7GG22N71S329749']/div/img")).getCssValue("display");
					// function to click button 1
					String cssdisplay1 = btnClick(btnXpath="//a[@id='rl-tac-btn-1B7GG22N71S329749']/div",playerXpath="//div[@id='lightbox-overlay'][1]");
					System.out.println("cssdisplay1 is "+cssdisplay1);
					
					 //This will scroll the page till the element is found	
					JavascriptExecutor js = (JavascriptExecutor) driver;       		
			        WebElement Element = driver.findElement(By.xpath("//a[@id='rl-tac-btn-1C3EL45X86N211991']/div/img"));	
			       	js.executeScript("arguments[0].scrollIntoView();", Element);
			        
			       	btn2=driver.findElement(By.xpath("//a[@id='rl-tac-btn-1C3EL45X86N211991']/div/img")).getCssValue("display");
					// function to click button 2
					String cssdisplay2 = btnClick(btnXpath="//a[@id='rl-tac-btn-1C3EL45X86N211991']/div",playerXpath="//div[@id='lightbox-overlay'][1]");
					System.out.println("cssdisplay2 is "+cssdisplay2);
					
					btn3=driver.findElement(By.xpath("//a[@id='rl-tac-btn-1C3EL65R15N503638']/div/img")).getCssValue("display");
					// function to click button 3
					String cssdisplay3 = btnClick(btnXpath="//a[@id='rl-tac-btn-1C3EL65R15N503638']/div",playerXpath="//div[@id='lightbox-overlay'][1]");
					System.out.println("cssdisplay3 is "+cssdisplay3);
					
					// checking display property of buttons
					if( (btn1.equals("block") && cssdisplay1.equals("block")) && (btn2.equals("block") && cssdisplay2.equals("block")) && (btn3.equals("block") && cssdisplay3.equals("block")) ){
						status="pass";
					} 
					else{
						status="fail";
					}
				}
			}
			catch (NoSuchElementException e) {
				System.out.print("inside catch 1 fail\n");
				 status = "fail";
	  		}
		}
		
		else if( (testid.equals("RU_CONFLICT_TEST")) || (testid.equals("TAC_CONFLICT_TEST")) ){
			try{
				
				String btnXpath=null, playerXpath=null;
				btn1=driver.findElement(By.xpath("//a[@id='rl-ru-btn-JTHBF5C2XD5188034']/div/img")).getCssValue("display");
				// function to click button 1
				String cssdisplay1 = btnClick(btnXpath="//a[@id='rl-ru-btn-JTHBF5C2XD5188034']/div",playerXpath="//div[@id='ru-lightbox-overlay'][1]");
				System.out.println("cssdisplay1 is "+cssdisplay1);
				
				btn2=driver.findElement(By.xpath("//a[@id='rl-tac-btn-JTHBF5C2XD5188034']/div/img")).getCssValue("display");
				// function to click button 2
				String cssdisplay2 = btnClick(btnXpath="//a[@id='rl-tac-btn-JTHBF5C2XD5188034']/div",playerXpath="//div[@id='lightbox-overlay'][1]");
				System.out.println("cssdisplay2 is "+cssdisplay2);
				
				// scroll down code
				JavascriptExecutor js = (JavascriptExecutor) driver;       		
		        WebElement Element = driver.findElement(By.xpath("//a[@id='rl-tac-btn-1FT7W2BT8FEC95330']/div/img"));	
		        //This will scroll the page till the element is found		
		        js.executeScript("arguments[0].scrollIntoView();", Element);
		        
				btn3=driver.findElement(By.xpath("//a[@id='rl-ru-btn-1FT7W2BT8FEC95330']/div/img")).getCssValue("display");
				// function to click button 3
				String cssdisplay3 = btnClick(btnXpath="//a[@id='rl-ru-btn-1FT7W2BT8FEC95330']/div",playerXpath="//div[@id='ru-lightbox-overlay'][1]");
				System.out.println("cssdisplay3 is "+cssdisplay3);
				
				String btn4=driver.findElement(By.xpath("//a[@id='rl-tac-btn-1FT7W2BT8FEC95330']/div/img")).getCssValue("display");
				// function to click button 3
				String cssdisplay4 = btnClick(btnXpath="//a[@id='rl-tac-btn-1FT7W2BT8FEC95330']/div",playerXpath="//div[@id='lightbox-overlay'][1]");
				System.out.println("cssdisplay4 is "+cssdisplay4);
				
				// checking pass condition
				if( (btn1.equals("block") && cssdisplay1.equals("block")) && (btn2.equals("block") && cssdisplay2.equals("block")) && (btn3.equals("block") && cssdisplay3.equals("block")) && (btn4.equals("block") && cssdisplay4.equals("block")) ){
					status="pass";
				}
				else{
					status="fail";
				}
			}
			catch (NoSuchElementException e) {
				System.out.print("inside catch 2 fail\n");
				 status = "fail";
	  			
	  		}
		}
		else if((testid.equals("TAC_ASYNC_VDP_INLINE"))){
			try{
				// checking image is present
				String imagevisible=driver.findElement(By.id("chill")).getCssValue("display");
				System.out.println(imagevisible);
				
				//clicking on the button
				driver.findElement(By.id("initTac")).click();
				Thread.sleep(4000);
				
				// checking it replaces image with the player when click on the above button
				String player=driver.findElement(By.id("rl-tac-integration-1FADP3K21EL180226")).getCssValue("display"); 
				
				// final condition
				if(imagevisible.equals("block") && player.equals("block")){
					status = "pass";
				}
				else {
					status = "fail";
				}
			}
			catch (NoSuchElementException e) {
				System.out.print("inside catch 2 fail\n");
				 status = "fail";
	  		}
		}
		
		
	return status;
	} // button appearance funtion closes

	private static String btnClick(String btnXpath, String playerXpath) throws InterruptedException {
		
		
		driver.findElement(By.xpath(btnXpath)).click();
		Thread.sleep(2000);
		String cssdisplay1 = driver.findElement(By.xpath(playerXpath)).getCssValue("display");
		System.out.println("after click button 1 display is "+cssdisplay1);
		
		// code to set none in display in button 1
		JavascriptExecutor style = (JavascriptExecutor) driver;
		WebElement element = driver.findElement(By.xpath(playerXpath));
		style.executeScript("arguments[0].setAttribute('style', 'display:none')",element);
		Thread.sleep(2000);
		System.out.println("After close player button 1 display is "+driver.findElement(By.xpath(playerXpath)).getCssValue("display"));
		
		
		return cssdisplay1;
	}

	private static String textUps() throws InterruptedException {
		System.out.println("inside textUps");
		
		Thread.sleep(4000);
		driver.switchTo().frame(0);
		try {
			// scroll down code
			JavascriptExecutor js = (JavascriptExecutor) driver;       		
	        WebElement Element = driver.findElement(By.id("spinner-reel"));	
	        //This will scroll the page till the element is found		
	        js.executeScript("arguments[0].scrollIntoView();", Element);
	        Thread.sleep(5000);
	        System.out.println("here1");
			String cssdisplay = driver.findElement(By.className("TickerNews")).getCssValue("display");
			System.out.println("here2");
			if(cssdisplay.equals("block")){
				System.out.println("here11");
				return "pass";
			}
			else {
				System.out.println("here12");
				return "fail";
			}
		}
		catch (NoSuchElementException e) {
			System.out.print("inside catch fail\n");
			return "fail";
  			
  		}
	}

	private static String widgetCss() {
		System.out.println("inside widgetCss");
		
		
		try{
			String cssPosition = driver.findElement(By.id("rl-tac-integration-4S4BSADC8H3406319")).getCssValue("position");
			if(cssPosition.equals("absolute")){
				return "pass";
			}
			else {
				return "fail";
			}
		}
		catch (NoSuchElementException e) {
			System.out.print("inside catch fail\n");
			return "fail";
  			
  		}
	}

	private static String widgetTargetModifier(String option_val) throws InterruptedException {
		System.out.println("inside widgetTargetModifier");
		String status=null;
		if(option_val.equals("parent*1")){
			try{
				driver.findElement(By.xpath("//div[@class='thumbnail']/img")).isDisplayed();
				Thread.sleep(3000);
				driver.switchTo().frame(0);
				System.out.println(driver.findElement(By.xpath("//iframe[@id='rl-tac-iframe-id']")).getAttribute("style"));
				System.out.println("Here4");
				System.out.println("inside try of parent");
				status = "fail";
			}
			catch (NoSuchElementException e) {
	  			status = "pass";
	  			System.out.print("inside catch pass of parent\n");
	  		}
		}
		else if(option_val.equals("child")){
			try{
				driver.findElement(By.xpath("//div[@class='thumbnail']/img")).isDisplayed();
				Thread.sleep(3000);
				driver.switchTo().frame(0);
				System.out.println(driver.findElement(By.xpath("//iframe[@id='rl-tac-iframe-id']")).getAttribute("style"));
				System.out.println("Here4");
				System.out.println("inside try of child");
				status = "pass";
			}
			catch (NoSuchElementException e) {
	  			status = "fail";
	  			System.out.print("inside catch fail of child\n");
	  		}
		}
		return status;
	}

	private static String widgetIdandClass() {
		System.out.println("inside widgetIdandClass");
		String status=null;	
		try{
			driver.findElement(By.id("rl-tac-integration-4S4BSADC8H3406319"));
			System.out.println("inside try");
			status = "pass";
		}
		catch (NoSuchElementException e) {
  			status = "fail";
  			System.out.print("inside catch fail\n");
  		}
		return status;
	}

	private static String widgetType() {
		System.out.println("inside widgetType");
		String status=null;	
		try{
			driver.findElement(By.id("rl-tac-integration-4S4BSADC8H3406319"));
			System.out.println("inside try");
			status = "pass";
		}
		catch (NoSuchElementException e) {
  			status = "fail";
  			System.out.print("inside catch fail\n");
  		}
		return status;
		
	}

	private static String autospin() throws InterruptedException {
		System.out.println("inside autospin");
		String status=null;	
		Thread.sleep(4000);
		driver.switchTo().frame(0);
		try {
			// scroll down code
			JavascriptExecutor js = (JavascriptExecutor) driver;       		
	        WebElement Element = driver.findElement(By.id("spinner-reel"));
	        //This will scroll the page till the element is found		
	        js.executeScript("arguments[0].scrollIntoView();", Element);
	        Thread.sleep(5000);
			String className= driver.findElement(By.id("spinner-reel")).getAttribute("class");
			System.out.println(className);
			if(className.equals("reel-overlay frame-1")){
				return status="fail";
			}
				
			else return status="pass";
	  		} 
		catch (NoSuchElementException e) {
	  			status = "fail";
	  			System.out.print("inside catch fail");
	  		}
		return status;
		
		
	}

	private static String tickerText(String desc) throws InterruptedException {
		
		Thread.sleep(2000);
		driver.switchTo().frame(0);
		// scroll down code
		JavascriptExecutor js = (JavascriptExecutor) driver;       		
        WebElement Element = driver.findElement(By.id("contentDiv"));
        //This will scroll the page till the element is found		
        js.executeScript("arguments[0].scrollIntoView();", Element);
        
		String tickerText=driver.findElement(By.id("tickerText")).getText();
		//System.out.println("ticker text is "+tickerText);
		System.out.println("inside tickerText function");
		
		if(desc.equals("Test Show Info Ticker") && tickerText.contains("2017 Subaru Outback Premium")){
			return "pass";
		}
		else if(desc.equals("Test Show Info Ticker Content") && tickerText.contains("New Content")){
			return "pass";
		}
		else {
			return "fail";
		}
	}

	private static String showLogo() throws InterruptedException {
			
		Thread.sleep(2000);
		driver.switchTo().frame(0);
		// scroll down code
		JavascriptExecutor js = (JavascriptExecutor) driver;       		
        WebElement Element = driver.findElement(By.className("logoDiv"));
        //This will scroll the page till the element is found		
        js.executeScript("arguments[0].scrollIntoView();", Element);
        
        String logocss = driver.findElement(By.className("logoDiv")).getCssValue("display");
        if(logocss.equals("block")){
        	return "pass";
        }
        else return "fail";
        
	}

	private static String widgetStatus(String playerstatus) throws InterruptedException {
		String status=null;
		Thread.sleep(4000);
		//System.out.print("Total iframe is" + driver.findElements(By.xpath("//iframe")).size()+" ");
		if(playerstatus.equals("Inactive")){
			try {
		  		   driver.findElement(By.id("rl-tac-integration-4S4BSADC8H3406319"));
		  		 status = "fail";
		  		} catch (NoSuchElementException e) {
		  			status = "pass";
		  		}
		}
		else if(playerstatus.equals("Active")){
			try {
		  		   driver.findElement(By.id("rl-tac-integration-4S4BSADC8H3406319"));
		  		 status = "pass";
		  		} catch (NoSuchElementException e) {
		  			status = "fail";
		  		}
		}
		return status;
	}

	private static String widgetPlayer() throws InterruptedException {
		Thread.sleep(2000);
		driver.switchTo().frame(0);
		// scroll down code
		JavascriptExecutor js = (JavascriptExecutor) driver;       		
        WebElement Element = driver.findElement(By.id("curtain-display"));
        //This will scroll the page till the element is found		
        js.executeScript("arguments[0].scrollIntoView();", Element);
		        
		String imageSrc = driver.findElement(By.id("curtain-display")).getAttribute("src");
		System.out.println("image src is "+imageSrc);
		if(imageSrc.contains("/images/unavailable.png")){
			//System.out.println("Pass");
			return "pass";
		}
		else if(imageSrc.contains("/images/loading.gif")){
			//System.out.println("Pass");
			return "pass";
		}
		else return "fail";
		
	}

	private static String tickerTextScale() throws InterruptedException {
		System.out.println("Inside tickerTextScale");
		
		String font_size=driver.findElement(By.id("tickerText")).getCssValue("font-size");
		System.out.println("font size is "+font_size);
		
		Dimension d1 = new Dimension(800,480);
		driver.manage().window().setSize(d1);
		String font_size2=driver.findElement(By.id("tickerText")).getCssValue("font-size");
		System.out.println("2nd font size is "+font_size2);
		Thread.sleep(3000);
		
		Dimension d2 = new Dimension(400,400);
		driver.manage().window().setSize(d2);
		String font_size3=driver.findElement(By.id("tickerText")).getCssValue("font-size");
		System.out.println("3rd font size is "+font_size3);
		Thread.sleep(3000);
		if(font_size.equals("16.8px") && font_size2.equals("16.8px") && font_size3.equals("16.8px")){
			return "pass";
		}
		return "fail";
	}

	private static String closeOut() throws InterruptedException {
		
		driver.findElement(By.id("fullscreen-toggle")).click();
		Thread.sleep(2000);
		driver.findElement(By.id("fullscreen-toggle")).click();
		System.out.println("In closeout");
		String data_toggle_state= driver.findElement(By.id("fullscreen-toggle")).getAttribute("data-toggle-state");
		if(data_toggle_state.equals("false")){
			System.out.println("Pass");
			return "pass";
		}
		else return "fail";
	}

	private static String noClick(String testid) {
		String status =null;
		if(testid.equals("RDT_4_3") || testid.equals("RDT_22_3")){
			try{
				driver.findElement(By.xpath("//a[@id='tickerText']")).click();
				String cursor_css= driver.findElement(By.xpath("//a[@id='tickerText']")).getCssValue("cursor");
				//System.out.println("cursor is "+ cursor_css);
				if(cursor_css.equals("default")){
					//System.out.println("Pass");
					status="pass";
				}
				else {
					status="fail";
				}
			}
			catch (NoSuchElementException e) {
				System.out.print("inside catch 2 fail\n");
				 status= "fail";
	  		}
		}
		return status;
	}

	private static String shortcodescale() throws InterruptedException {
		String font_size1= driver.findElement(By.xpath("//a[@id='tickerText']/div")).getCssValue("font-size");
		//System.out.println("font size is "+font_size1);
		
		Dimension d = new Dimension(400,400);
		driver.manage().window().setSize(d);
		Thread.sleep(3000);
		String font_size2= driver.findElement(By.xpath("//a[@id='tickerText']/div")).getCssValue("font-size");
		//System.out.println("font size is "+font_size2);
		
		if(!font_size1.equals(font_size2)){
			return "pass";
		}
		else return "fail";
		
	}

	private static String resizingOfBrowser(String current_url) throws InterruptedException {
		//System.out.println("passing to vertical inside resize of browser"+current_url);
		String verticalStatus = verticalBehaviour(current_url);
		//System.out.println("passing to horizontal inside resize of browser"+current_url);
		String horizontalStatus = horizontalBehaviour(current_url);
		if(verticalStatus.equals("pass") && horizontalStatus.equals("pass"))
		{
			return "pass";
		}
		else return "fail";
	}

	private static void passingCondtion(String status) {
		if (status.equals("pass")){
    		button_link1="pass";
    		button_link2="pass";
    		//System.out.println("setting pass to button_links");
    	}
    	else{
    		button_link1="fail";
    		button_link2="fail";
    		//System.out.println("setting fail to button_links");
    	}
		
	}

	private static String verticalBehaviour(String current_url) throws InterruptedException {
		
		
		// getting window size1
		Dimension windowsize1 = driver.findElement(By.xpath("html/body")).getSize();
		//System.out.println(windowsize1.getHeight());
		// getting player size
		Dimension playersize1 = driver.findElement(By.className("superResponsive")).getSize();
		//System.out.println(playersize1.getHeight());
		
		// re-navigate to navigation_link
		//driver.get(current_url);
		Thread.sleep(1000);
		Dimension d1 = new Dimension(800,480);
		driver.manage().window().setSize(d1);
		Thread.sleep(3000);
		
		// getting window size2
		Dimension windowsize2 = driver.findElement(By.xpath("html/body")).getSize();
		//System.out.println(windowsize2.getHeight());
		// getting player size
		Dimension playersize2 = driver.findElement(By.className("superResponsive")).getSize();
		//System.out.println(playersize2.getHeight());
		
		// re-navigate to navigation_link
		//driver.get(current_url);
		Thread.sleep(1000);
		Dimension d2 = new Dimension(650,550);
		driver.manage().window().setSize(d2);
		Thread.sleep(3000);
		
		// getting window size2
		Dimension windowsize3 = driver.findElement(By.xpath("html/body")).getSize();
		//System.out.println(windowsize3.getHeight());
		// getting player size
		Dimension playersize3 = driver.findElement(By.className("superResponsive")).getSize();
		//System.out.println(playersize3.getHeight());
		
		// setting result 
		if(windowsize1.getHeight()==playersize1.getHeight() && windowsize2.getHeight()==playersize2.getHeight() && windowsize3.getHeight()==playersize3.getHeight()){
			//System.out.println("Inside pass condition of vertical behaviour");
			return "pass";
		}
		else{
			//System.out.println("inside fail condition of vertical behaviour");
			return "fail";
		}
		
	}
	
	private static String horizontalBehaviour(String current_url) throws InterruptedException {
		// setting dimenstion
		Dimension d1 = new Dimension(400,600);
		driver.manage().window().setSize(d1);
		Thread.sleep(3000);
		
		// getting window size
		Dimension windowsize1 = driver.findElement(By.className("superResponsive")).getSize();
		//System.out.println(windowsize1.getWidth());
		// getting player size
		Dimension playersize1 = driver.findElement(By.className("superResponsive")).getSize();
		//System.out.println(playersize1.getWidth());
		
		// re-navigate to navigation_link
		//driver.get(current_url);
		Thread.sleep(1000);
		Dimension d2 = new Dimension(600,800);
		driver.manage().window().setSize(d2);
		Thread.sleep(3000);
		
		// getting window size2
		Dimension windowsize2 = driver.findElement(By.xpath("html/body")).getSize();
		//System.out.println(windowsize2.getHeight());
		// getting player size2
		Dimension playersize2 = driver.findElement(By.className("superResponsive")).getSize();
		//System.out.println(playersize2.getHeight());
		
		// setting result 
		if(windowsize1.getWidth()==playersize1.getWidth() && windowsize2.getWidth()==playersize2.getWidth()){
			//System.out.println("Inside pass condition of horizontal behaviour");
			return "pass";
		}
		else{
			//System.out.println("inside fail condition of horizontal behaviour");
			return "fail";
		}
		
	}

	private static void checkingLoginOnce() throws InterruptedException {
		//System.out.println("Inside checkingloginonce function");
		Thread.sleep(1000);
		currenturl=driver.getCurrentUrl();
		//System.out.println("current url is "+currenturl);
		if(currenturl.equals("http://larengine.redinv.redlinepages.com/") || currenturl.equals("https://dev-engine.redlineinventory.com/")){
			try {
			   String tooManyAttempts = driver.findElement(By.className("alert-error")).getText();
			   if(tooManyAttempts.contains("Too many attempts. Try again in few minutes.")){
				   System.out.println("Too many attempts error waiting for 5 minutes to relogin");
				   Thread.sleep(300000);
			   }
		   		loginonce();
      		} 
			catch (NoSuchElementException e) {
      			loginonce();
      		}
		}
	}

	private static void loginonce() {
		System.out.println("inside loginonce");
		driver.manage().window().maximize();
       //driver.get("http://larengine.redinv.redlinepages.com/");
		driver.get("https://dev-engine.redlineinventory.com/");
		WebElement email = driver.findElement(By.id("email"));
		email.sendKeys("turnacar_tester");
		WebElement password = driver.findElement(By.id("password"));
		password.sendKeys("0cce4767865dd109eb99851a35320fdf");
		WebElement rememberme = driver.findElement(By.className("iCheck-helper"));
		rememberme.click();
		WebElement submit = driver.findElement(By.className("btn-default"));
		submit.click();
	}
	
	private static void clickonCheckBox(int number) {
		// scroll down code
		JavascriptExecutor js = (JavascriptExecutor) driver;       		
        WebElement Element = driver.findElement(By.id("widget-button-width-override"));
        //This will scroll the page till the element is found		
        js.executeScript("arguments[0].scrollIntoView();", Element);
	
        
        List<WebElement> a = driver.findElements(By.xpath("//*[@class='iCheck-helper']"));
       // System.out.println("total no. of checkbox is "+a.size());
        a.get(a.size()-number).click();
		
	}

	private String sessionId,username,authkey;
	private String apiUrl = "crossbrowsertesting.com/api/v3/selenium";
	public Automation(String username, String authkey) {
		// for java URL's must be character incoded. If you use 
		// your email, let's replace that character

		if (username.contains("@")) {
			username = username.replace("@", "%40");
		} 
		this.username = username;
		this.authkey = authkey;
	}
	
	public void setSessionId(String sessionId) {
		this.sessionId = sessionId;
	}
		
	public void takeSnapshot() {
		if (this.sessionId != null) {
			String url = "https://" + apiUrl + "/" + this.sessionId + "/snapshots";
			String payload = "{\"selenium_test_id\": \"" + this.sessionId + "\"}";
			makeRequest("POST",url,payload);
		}
	}
	
	public void setDescription(String desc) {
		String url = "https://" + apiUrl + "/" + this.sessionId;
		String payload = "{\"action\": \"set_description\", \"description\": \"" + desc + "\"}";
		makeRequest("PUT", url,payload);
	}
	
	public void setScore(String score) {
		String url = "https://" + apiUrl + "/" + this.sessionId;
		String payload = "{\"action\": \"set_score\", \"score\": \"" + score + "\"}";
		makeRequest("PUT", url,payload);
		
	}
	private void makeRequest(String requestMethod, String apiUrl, String payload) {
		URL url;
		String auth = "";
		//System.out.println("\n"+ " rq method is "+requestMethod+" "+ " api is "+apiUrl+" payload is "+payload+" unma is " +username+ " aukey is "+ authkey );
        if (username != null && authkey != null) {
            auth = "Basic " + Base64.encodeBase64String((username+":" + authkey).getBytes());
        }
        try {
            url = new URL(apiUrl);
            HttpURLConnection conn = (HttpURLConnection) url.openConnection();
            conn.setRequestMethod(requestMethod);
            conn.setDoOutput(true);
            conn.setRequestProperty("Authorization", auth);
            conn.setRequestProperty("Content-Type", "application/json");
            OutputStreamWriter osw = new OutputStreamWriter(conn.getOutputStream());
            osw.write(payload);
            osw.flush();
            osw.close();
            conn.getResponseMessage();
        } catch (Exception e) {
        	System.out.println(e.getMessage());
        }
	}
}
