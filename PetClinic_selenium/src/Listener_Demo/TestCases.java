package Listener_Demo;		

import org.openqa.selenium.By;
import org.openqa.selenium.Keys;
import org.openqa.selenium.WebDriver;		
import org.openqa.selenium.chrome.*;		
import org.testng.Assert;
import org.testng.annotations.AfterMethod;
import org.testng.annotations.BeforeMethod;
	
import org.testng.annotations.Test;

public class TestCases {
	public WebDriver driver;

	
	@BeforeMethod
	public void setup()
	{			
		System.setProperty("webdriver.chrome.driver", "chromedriver.exe");
		ChromeOptions options = new ChromeOptions();
        options.addArguments("--start-maximized");
		driver = new ChromeDriver(options);
	}
		
	
// Test to pass as to verify listeners .		
@Test		
public void Login()				
{		
    driver.get("http://10.127.127.74:8080/demoportal/");	
    driver.findElement(By.partialLinkText("Login")).click();
    driver.findElement(By.name("username")).sendKeys("mngr34926");							
    driver.findElement(By.name("password")).sendKeys("amUpenu");							
    driver.findElement(By.name("submit")).sendKeys(Keys.ENTER);					
}	

@Test
public void CountOfBlueColorTiles() throws Exception {
	   driver.get("http://10.127.127.74:8080/demoportal/");	
	   int itemcount= driver.findElements(By.className("color-blue")).size();
	   Assert.assertEquals(itemcount, 3);
}

@Test
public void CountOfGreenColorTiles() throws Exception {
	driver.get("http://10.127.127.74:8080/demoportal/");	
	   int itemcount= driver.findElements(By.className("color-green")).size();
	   Assert.assertEquals(itemcount, 3);
}

@Test
public void CountOfRedColorTiles() throws Exception {
	driver.get("http://10.127.127.74:8080/demoportal/");	
	   int itemcount= driver.findElements(By.className("color-red")).size();
	   Assert.assertEquals(itemcount, 3);
}



// Forcefully failed this test as to verify listener.		
@AfterMethod		
public void AfterTest()				
{		
	driver.close();
}		
}