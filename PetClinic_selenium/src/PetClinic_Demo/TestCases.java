package PetClinic_Demo;		

import org.openqa.selenium.By;
import org.openqa.selenium.Keys;
import org.openqa.selenium.WebDriver;		
import org.openqa.selenium.chrome.*;
import org.openqa.selenium.htmlunit.HtmlUnitDriver;
import org.testng.Assert;
import org.testng.annotations.AfterMethod;
import org.testng.annotations.BeforeMethod;
	
import org.testng.annotations.Test;

public class TestCases {
	public WebDriver driver;

	
	@BeforeMethod
	public void setup()
	{			

		driver = new HtmlUnitDriver();
		driver.get("http://10.127.127.74:8080/petclinic/");	
	}
		
	
// Test to pass as to verify listeners .		
@Test		
public void VerifyHomePage()				
{		
   
   String welcometext= driver.findElement(By.xpath("/html/body/div/div/h2")).getText();
   Assert.assertEquals(welcometext, "Welcome", "Text not found");		
}	

@Test
public void VerifyFindOwnersPage() throws Exception {	
	driver.findElement(By.xpath(".//a[@href='/petclinic/owners/find.html']")).click();
	String lastNameText = driver.findElement(By.id("lastName")).getText();
	 Assert.assertEquals(lastNameText, "Last name", "Text not found");		
}

@Test
public void AddOwner() throws Exception {	
	driver.findElement(By.xpath(".//a[@href='/petclinic/owners/find.html']")).click();
	driver.findElement(By.linkText("Add Owner")).click();
	driver.findElement(By.id("firstName")).sendKeys("Demo1");
	driver.findElement(By.id("lastName")).sendKeys("Test2");
	driver.findElement(By.id("address")).sendKeys("Sector 21");
	driver.findElement(By.id("city")).sendKeys("Gurgaon");
	driver.findElement(By.id("telephone")).sendKeys("1234567890");
	driver.findElement(By.xpath(".//*[text()='Add Owner']")).submit();
	Thread.sleep(1000);
	String OwnerInfoText = driver.findElement(By.xpath("/html/body/div/div/h2[1]")).getText();
	Assert.assertEquals(OwnerInfoText, "Owner Information", "Text not found");
	
}

@Test(dependsOnMethods="AddOwner")
public void FindOwner() throws Exception {
	driver.findElement(By.xpath(".//a[@href='/petclinic/owners/find.html']")).click();
	Thread.sleep(1000);
	driver.findElement(By.xpath(".//input[@id='lastName']")).sendKeys("Test2");
	driver.findElement(By.xpath(".//*[text()='Find Owner']")).submit();
	String OwnerInfoText = driver.findElement(By.xpath("/html/body/div/div/h2[1]")).getText();
	if(OwnerInfoText.equals("Owner Information"))
	{
	Assert.assertEquals(OwnerInfoText, "Owner Information", "Text not found");
	}else
	{
		Assert.assertEquals(OwnerInfoText, "Owners", "Text not found");
	}
}



// Forcefully failed this test as to verify listener.		
@AfterMethod		
public void AfterTest()				
{		
	driver.close();
}		
}