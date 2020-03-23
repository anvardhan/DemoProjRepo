package resources;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.Properties;
import java.util.concurrent.TimeUnit;

import org.apache.poi.xssf.usermodel.XSSFCell;
import org.apache.poi.xssf.usermodel.XSSFRow;
import org.apache.poi.xssf.usermodel.XSSFSheet;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.openqa.selenium.Alert;
import org.openqa.selenium.NoAlertPresentException;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.firefox.FirefoxDriver;
import org.openqa.selenium.firefox.FirefoxOptions;
import org.openqa.selenium.firefox.FirefoxProfile;
import org.openqa.selenium.firefox.internal.ProfilesIni;
import org.openqa.selenium.ie.InternetExplorerDriver;
import org.testng.annotations.DataProvider;

public class Base {
	
	public WebDriver driver= null; 
	public Properties prop;
	File f;
	FileInputStream fis;
	
	public static XSSFWorkbook wb;
	public static XSSFSheet ws;
	public static XSSFRow row;
	public static XSSFCell cell;
	public static int rowNum=0;
	public static int colNum=0;
	public static String[][] data;
	
	public WebDriver driverInitialization()
	{
		prop = new Properties();
		//f = new File("./GlobalData.properties"); // if file is present at project level
		f = new File("src/main/java/resources/GlobalData.properties");
		try {
			fis = new FileInputStream(f);
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		}
		try {
			prop.load(fis);
		} catch (IOException e) {
			e.printStackTrace();
		}
		
		if(prop.getProperty("browser").equalsIgnoreCase("firefox"))
		{
			//create firefox profile (firefox -p)
			ProfilesIni init = new ProfilesIni();
			FirefoxProfile profile = init.getProfile("SeleniumFFProfile");
			profile.setPreference("browser.popups.showPopupBlocker",false);			
			FirefoxOptions options = new FirefoxOptions();
			options.setProfile(profile);
			
			System.setProperty("webdriver.gecko.driver", "src/main/java/resources/geckodriver.exe");
			driver=new FirefoxDriver(options);
		} 
		else if(prop.getProperty("browser").equalsIgnoreCase("chrome"))
		{
			System.setProperty("webdriver.chrome.driver", "src/main/java/resources/chromedriver.exe");
			driver=new ChromeDriver();
		}
		else if(prop.getProperty("browser").equalsIgnoreCase("ie"))
		{
			//System.setProperty("webdriver.ie.driver", "src/main/java/resources/.exe");
			driver=new InternetExplorerDriver();
		}
		
		driver.manage().window().maximize();
		driver.manage().timeouts().implicitlyWait(10L,TimeUnit.SECONDS);
		return driver;
		
	}
	
		
	public boolean isAlertPresent() 
	{ 
		try 
	    { 
			driver.switchTo().alert();
	        return true; 
	    }   // try 
		catch (NoAlertPresentException Ex) 
		 { 
	        return false; 
	     }   // catch 
	}
	
	
/* I found catching exception of driver.switchTo().alert(); is so slow in Firefox (FF V20 & selenium-java-2.32.0).`
So I choose another way:

    private static boolean isDialogPresent(WebDriver driver) {
        try {
            driver.getTitle();
            return false;
        } catch (UnhandledAlertException e) {
            // Modal dialog showed
            return true;
        }
    }
And it's a better way when most of your test cases is NO dialog present (throwing exception is expensive).*/
	
	public void alertHandle()
	{
		Alert alert = driver.switchTo().alert();
		alert.accept();	
	}
	public String alertActualText()
	{
		Alert alert = driver.switchTo().alert();
		String actualAlertMsg = alert.getText();
		return actualAlertMsg;
	}
	
	/*
	 * long startTime = System.currentTimeMillis();
	 * long stopTime = System.currentTimeMillis();
					    long elapsedTime = stopTime - startTime;
	 * 
	 * String executionTime = String.format("%d min, %d sec", 
    	    TimeUnit.MILLISECONDS.toMinutes(elapsedTime),
    	    TimeUnit.MILLISECONDS.toSeconds(elapsedTime) - 
    	    TimeUnit.MINUTES.toSeconds(TimeUnit.MILLISECONDS.toMinutes(elapsedTime))
    	);
	*/
	//Handling Data from Data Provider
	@DataProvider
	public Object[][] getData()
	{
		Object [][] data= new Object[3][2]; //3 rowsi.e 3 test with 2 sets of data
		
		data[0][0]="ananduser1";
		data[0][1]="anandPass1";
		
		data[1][0]="ananduser2";
		data[1][1]="anandPass2";
		
		data[2][0]="ananduser3";
		data[2][1]="anandPass3";
		
		return data;
	}
	
	//Handling data from Excel read and write
	
	public static String[][] readExcelData(String f, String sheetname) throws Exception
	{
		try {
			FileInputStream fis = new FileInputStream(f);
			wb = new XSSFWorkbook(fis);
			ws = wb.getSheet(sheetname);			
		} catch (FileNotFoundException e) {
			
			e.printStackTrace();
		}
		
		rowNum = ws.getLastRowNum()+1;
		System.out.println("rowCnt in base class "+rowNum);
		colNum = ws.getRow(0).getLastCellNum();
		System.out.println("cellCnt in base class "+colNum);
		
		//Create array to store data.		
		//String[][] data = new String[rowNum][colNum];
		data = new String[rowNum][colNum];
		
		for (int i=0;i<rowNum;i++)
		{
			row = ws.getRow(i);
			for(int j=0;j<colNum;j++)
			{
				cell = row.getCell(j);
				String value="-";
				if (cell!=null)
				{
					value=cell.getStringCellValue();
				}
				data[i][j]=value;
			}
		}
				
	return data;	
	}
	
	public static void setCellData(String fo, String result,int rowNum, int colNum) throws Exception
	{
		row = ws.getRow(rowNum);
		cell=row.getCell(colNum);
		
		if(cell==null)
		{
			//cell=row.createCell(colNum);
			cell=row.createCell(row.getLastCellNum());
			cell.setCellValue(result);
		}
		else
		{
			cell.setCellValue(result);
		}
		
		//create FileOutputStream to write data back to excel
		
		//Select directory where excel input file is saved or will be saved
		
		//File fo = new File("D:\\Selenium\\TestData\\TestData_Result.xlsx"); --> File is passing from main program
		FileOutputStream fos = new FileOutputStream(fo);		
		wb.write(fos);
				
		fos.flush();
		//close the stream
		fos.close();
		
	}
	
	
}
