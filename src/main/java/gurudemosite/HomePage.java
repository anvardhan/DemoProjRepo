package gurudemosite;

import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.FindBy;
import org.openqa.selenium.support.PageFactory;

public class HomePage {

	public WebDriver driver;
	
	//Identifying Locators of HomePage
	
	@FindBy(xpath="//*[@id=\"navbar-brand-centered\"]/ul/li[5]/a")	
		WebElement linkBankProject;
	
	@FindBy(xpath="//input[@name='uid' and @type='text']")
		WebElement userID;
	
	@FindBy(xpath="//input[@name='password'][@type='password']")
		WebElement password;
	
	@FindBy(xpath="//input[@name='btnLogin'][@type='submit']")
		WebElement loginButton;
	
	
	
	//Required Methods
	
	public HomePage(WebDriver driver) {
		
		this.driver=driver;  //Providing knowledge to HomePage class from any Test class
		PageFactory.initElements(driver,this); //initialize webelements of HomePage class
		
	}
	
	public void loginToGtplBank(String userName, String userPass)
	{
		userID.sendKeys(userName);	//"mngr128266"
		password.sendKeys(userPass);	//"YsEsYnU"
		loginButton.click();
	}
	

	

	
	
	/*public void loginToGtplBank(Hashtable<String,String> data)
	{
		userID.sendKeys(data.get("username"));	//"mngr128266"
		password.sendKeys(data.get("password"));	//"YsEsYnU"
		loginButton.click();
	}*/
	
}
