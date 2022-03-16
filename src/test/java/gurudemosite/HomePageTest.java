package gurudemosite;

import static org.testng.Assert.assertEquals;

import org.openqa.selenium.WebDriver;
import org.testng.annotations.*;
import resources.Base;

public class HomePageTest extends Base {
	WebDriver driver;
	static String[][] data; //used for excel
	
	@BeforeTest
	public void setUp()
	{
		System.out.println("Starting Test");
		driver = driverInitialization();
		//driver.get("http://demo.guru99.com/V1/index.php");
		driver.get(prop.getProperty("url"));
	}
	
	@Test (priority=6,enabled=false,description="Validation of Home Page Title")
	public void homePageTitle_Duplicate()
	{
		String expectedTitle = "Guru99 Bank Manager HomePage";
		String pageTitle = driver.getTitle();
		System.out.println("pageTitle "+pageTitle);
		assertEquals(pageTitle,expectedTitle);
	}
	
	@Test (priority=6,enabled=false,description="Validation of Home Page Title")
	public void homePageTitle()
	{
		String expectedTitle = "Guru99 Bank Manager HomePage";
		String pageTitle = driver.getTitle();
		System.out.println("pageTitle "+pageTitle);
		assertEquals(pageTitle,expectedTitle);
	}
	
	@Test (priority=1,enabled=false)
	public void loginValidInput()
	{
		HomePage hp = new HomePage(driver); // we need to pass driver instance to give knowledge to HomePage class
		hp.linkBankProject.click();
		hp.loginToGtplBank("mngr128266","YsEsYnU");
		
	}
	
	//dataProvider Example
	@Test(priority=2,dataProvider="getData",enabled=false,description="Combine userId and Pswd test with multipile Combination and validation")
	public void loginInValidInput(String userName, String userPass)
	{
		driver.navigate().to(prop.getProperty("url"));
		HomePage hp = new HomePage(driver); // we need to pass driver instance to give knowledge to HomePage class		
		hp.loginToGtplBank(userName, userPass);
		try {
			Thread.sleep(2000);
		} catch (InterruptedException e) {
			e.printStackTrace();
		}
		
		//Explicit wait--ExpectedConditions.alertIsPresent() gives us exactly the same thing, but in a nicer way and in just one line
		
		assertEquals(isAlertPresent(),true);
		System.out.println("not Logged in as expected");
		
		if(isAlertPresent())
		{
			alertHandle();
		}	
	}
	
	@Test(priority=7,enabled=true,description="Login validation with multiple sets of data including valid and invalid")
	public void loginTest() throws Exception
	{
		driver.navigate().to(prop.getProperty("url"));
		String userName = null;
		String userPass = null;		
				
		String actualPageTitle = null;
		String expectedPageTitle = null;
		String expectedPopupMsg = null;
	    String actualPopupMsg = null;
	    
		HomePage hp = new HomePage(driver); // we need to pass driver instance to give knowledge to HomePage class		
		
		String excelInput = new String("D:\\Selenium\\DemoProject_Guru\\GtplBankGuru99\\src\\main\\java\\resources\\TestData.xlsx");
		String excelOutput = new String("D:\\Selenium\\DemoProject_Guru\\GtplBankGuru99\\src\\main\\java\\resources\\TestDataResult.xlsx");
		//String excelInput = new String("/src/main/java/resources/TestData.xlsx");
		//String excelOutput = new String("D:\\Selenium\\TestData\\TestData_Result.xlsx");
		System.out.println("Reading file started");
		data=readExcelData(excelInput,"Sheet1"); //reading excel data
		System.out.println("Reading file competed");
		
		
		for (int i=1;i<rowNum;i++)
		{
			userName = data[i][0];
			userPass = data[i][1];
			
			System.out.println("username--> "+userName+ " AND password--> "+userPass);
			
			//calling login method
		
		   hp.loginToGtplBank(userName, userPass);
		    try {
			Thread.sleep(5000);
			} catch (InterruptedException e) {
			e.printStackTrace();
			}
		
		
		//Explicit wait--ExpectedConditions.alertIsPresent() gives us exactly the same thing, but in a nicer way and in just one line
		
		//assertEquals(isAlertPresent(),true);
		//System.out.println("Logged in successful");
		
		    //Verify result
		    
		   
		   		    
		    if(isAlertPresent())
		    {
		    	expectedPopupMsg = "User or Password is not valid"; 
		    	actualPopupMsg = alertActualText();
		    	alertHandle();
		    	if(expectedPopupMsg.equalsIgnoreCase(actualPopupMsg))
		    	{
		    		System.out.println("Popup msg is as expected - Actual- "+actualPopupMsg+" and Expected- "+expectedPopupMsg);
		    		//set excel with PASS
		    		//data[i][3]="PASS";
		    		setCellData(excelOutput,"PASS",i,3);		    		
		    		setCellData(excelOutput,actualPopupMsg,i,4);
		    		
		    	}
		    	else 
		    	{
		    		System.out.println("Popup msg is not as expected - Actual- "+actualPopupMsg+" and Expected- "+expectedPopupMsg);
		    		//set excel with FAIL
		    		//data[i][3]="FAIL";
		    		setCellData(excelOutput,"FAIL",i,3);
		    		
		    	}
		    	
		    	    	
		    } else {
		    		 actualPageTitle = "Guru99 Bank Manager HomePage";
		    	     expectedPageTitle=driver.getTitle();
				    if(actualPageTitle.equalsIgnoreCase(expectedPageTitle))
				    {
				    	System.out.println("Login successful and Title of page is as expected");
				    	//set excel to PASS
				    	setCellData(excelOutput,"PASS",i,3);
				    	setCellData(excelOutput,actualPageTitle,i,4);
				    }else {
				    	System.out.println("Login successful and but Title of page is not as expected");
				    	//set excel to FAIL
				    	setCellData(excelOutput,"FAIL",i,3);
				    	
				    }
		    }
		    
		    
		    driver.navigate().to(prop.getProperty("url"));
		}
		assertEquals(actualPopupMsg, expectedPopupMsg);
		assertEquals(actualPageTitle, expectedPageTitle);

	}

	
	
	
	//use of HashTable
	
		@Test (priority=3,enabled=false,description="Validate that user id field is mandatory field and it gives error as 'User-ID must not be blank' if we dont provide any value")
	public void blankUserId()
	{
		System.out.println("code for usrID");
	}	
	
	@Test (priority=4,enabled=false,description="Validate that password field is mandatory field and it gives error as 'Password must not be blank' if we dont provide any value")
	public void blankPassword()
	{
		System.out.println("code for password");
	}	
	
	@Test (priority=5,enabled=false,description="Validation of reset functionality")
	public void resetButton()
	{
		System.out.println("code for reset functionality");
	}
	
	
	
	@AfterTest
	public void tearDown()
	{
		System.out.println("All Test Completed");
		driver.close();
	}

		
}
