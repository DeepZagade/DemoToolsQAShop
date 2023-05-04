package test.DemoQAShop;

import java.awt.Desktop;
import java.io.File;
import java.io.IOException;
import java.util.concurrent.TimeUnit;

import org.apache.commons.io.FileUtils;
import org.openqa.selenium.By;
import org.openqa.selenium.Keys;
import org.openqa.selenium.OutputType;
import org.openqa.selenium.TakesScreenshot;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.interactions.Actions;
import org.openqa.selenium.support.ui.Select;
import org.testng.ITestResult;
import org.testng.annotations.AfterClass;
import org.testng.annotations.AfterMethod;
import org.testng.annotations.AfterSuite;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.BeforeSuite;
import org.testng.annotations.Test;
import org.testng.asserts.SoftAssert;

import com.aventstack.extentreports.ExtentReports;
import com.aventstack.extentreports.ExtentTest;
import com.aventstack.extentreports.Status;
import com.aventstack.extentreports.reporter.ExtentSparkReporter;

public class SmokeTest {
	//https://github.com/DeepZagade
	
	WebDriver driver;
	File f;
	ExtentReports extent ;
	ExtentTest test;

	@BeforeSuite
	public void beforeSuite() {
		f = new File(System.getProperty("user.dir") + "\\src\\main\\resource\\ExtentReport\\" + "extent.html");
		ExtentSparkReporter spark = new ExtentSparkReporter(f);
		extent = new ExtentReports();
		extent.attachReporter(spark);
	}

	@BeforeClass
	public void beforeClass() {
		System.setProperty("webdriver.chrome.driver",
				System.getProperty("user.dir") + "\\src\\main\\resource\\chromedriver.exe");
		driver = new ChromeDriver();
		driver.manage().window().maximize();
		driver.manage().timeouts().implicitlyWait(10, TimeUnit.SECONDS);
		driver.manage().deleteAllCookies();
		test = extent.createTest(getClass().getSimpleName());
		driver.get("https://shop.demoqa.com");

		
		/*
		 * test = extent.createTest(getClass().getSimpleName());
		 * driver.get(proObj.getPropertyValueFromFile("baseURL"));
		 */

	}

	@Test(priority = 0)
	public void verifysearching() {
		// SoftAssert sAssert = new SoftAssert();
		WebElement searchIcon = driver.findElement(By.xpath("//a[@class='noo-search']"));// click
		searchIcon.click();
		// driver.switchTo().frame();
		WebElement searchBox = driver.findElement(By.xpath("//input[@type='search']"));// or //input[@value='product']
		searchBox.sendKeys("pink drop shoulder oversized t shirt");
		searchBox.sendKeys(Keys.ENTER);
		SoftAssert sAssert = new SoftAssert();
		boolean flag = false;

		try {
			flag = driver.findElement(By.xpath(
					"//h1[@class='product_title entry-title'][contains(text(),'pink drop shoulder oversized t shirt')]"))
					.isDisplayed();
		} catch (Exception e) {

		}
		sAssert.assertTrue(flag, "Searched Product is not displayed");
		sAssert.assertAll();

	}

	@Test(priority = 1)
	public void verifyAddToCart() throws InterruptedException {
		// driver.get("https://shop.demoqa.com/product/pink-drop-shoulder-oversized-t-shirt/");
		// Select se = new
		// Select(driver.findElement(By.xpath("//*[@id='oldSelectMenu']")));
		Select color = new Select(driver.findElement(By.xpath("//*[@id='pa_color']")));
		color.selectByValue("pink");
		Thread.sleep(2000);
		Select size = new Select(driver.findElement(By.xpath("//*[@id='pa_size']")));
		size.selectByValue("36");
		Thread.sleep(2000);
		WebElement addToCartButton = driver.findElement(By.xpath("//button[text()='Add to cart']"));
		addToCartButton.click();// input[@title='Qty']
//		WebElement quantity = driver.findElement(By.xpath("//input[@title='Qty']"));
//		quantity.sendKeys(Keys.CONTROL + "a");
//		quantity.sendKeys("10");
//		quantity.sendKeys(Keys.ENTER);

		SoftAssert sAssert = new SoftAssert();
		boolean flag = false;

		try {
			flag = driver.findElement(By.xpath("//a[contains(text(),'View cart')]")).isDisplayed();
		} catch (Exception e) {

		}
		sAssert.assertTrue(flag, "Product not added to cart");
		sAssert.assertAll();

	}

	@Test(priority = 2)
	public void verifyOrderPlacement() {
		// driver.get("https://shop.demoqa.com/checkout/");
		driver.findElement(By.xpath("//a[contains(text(),'View cart')]")).click();
		driver.findElement(By.xpath("//a[contains(text(),'Proceed to checkout')]")).click();

		// Register
		driver.findElement(By.xpath("//input[@id='billing_first_name']")).sendKeys("Deep");
		driver.findElement(By.xpath("//input[@id='billing_last_name']")).sendKeys("Zagade");

		driver.findElement(By.xpath("//span[@aria-label='Country / Region']")).click();
		driver.findElement(By.xpath("//input[@class='select2-search__field']")).sendKeys("India" + Keys.ENTER);
		driver.findElement(By.xpath("//input[@placeholder='House number and street name']")).sendKeys("SakalNagar");
		driver.findElement(By.xpath("//input[@name='billing_city']")).sendKeys("Pune");

		driver.findElement(By.xpath("//span[@id='select2-billing_state-container']")).click();
		driver.findElement(By.xpath("//input[@class='select2-search__field']")).sendKeys("Maharashtra" + Keys.ENTER);

		driver.findElement(By.xpath("//*[contains(text(),'I have read and agree to the website ')]")).click();
		driver.findElement(By.xpath("//input[@name='billing_postcode']")).sendKeys("411007");
		driver.findElement(By.xpath("//input[@name='billing_phone']")).sendKeys("12345");
		driver.findElement(By.xpath("//input[@name='billing_email']")).sendKeys("abc@gmail.com");

		Actions act = new Actions(driver);
		act.moveToElement(driver.findElement(By.xpath("//*[contains(text(),'privacy policy')]"))).perform();

		// driver.findElement(By.xpath("//*[contains(text(),'I have read and agree to
		// the website ')]")).click();
		driver.findElement(By.xpath("//button[text()='Place order']")).click();

		SoftAssert sAssert = new SoftAssert();
		boolean flag = false;

		try {
			flag = driver.findElement(By.xpath("//p[text()='Thank you. Your order has been received.']")).isDisplayed();
		} catch (Exception e) {

		}
		sAssert.assertTrue(flag, "Order is placed");
		sAssert.assertAll();

	}

	@AfterMethod
	public void afterMethod(ITestResult result) throws IOException {
		if (result.getStatus() == ITestResult.FAILURE) {
			test.log(Status.FAIL, result.getThrowable());
			
			File scrnShot= ((TakesScreenshot)driver).getScreenshotAs(OutputType.FILE);
			String destPathForScrnShot= System.getProperty("user.dir"+"\\src\\main\\resource\\ScreenShot\\demoQAShop.png");
			FileUtils.copyFile(scrnShot, new File(destPathForScrnShot));
			
			// String imagePath = screenObj.getScreenShot(result.getName(),driver);
			// test.addScreenCaptureFromPath(imagePath);

		} else if (result.getStatus() == ITestResult.SUCCESS) {
			test.log(Status.PASS, result.getName() + " is passed");

		} else if (result.getStatus() == ITestResult.SKIP) {
			test.log(Status.SKIP, result.getName() + " is skipped");
		}

	}

	@AfterClass
	public void afterclass() {
		driver.quit();
	}

	@AfterSuite
	public void afterSuiteMethod() {
		extent.flush();
		try {
			Desktop.getDesktop().browse(f.toURI());
			//driver.navigate().to(f.toURI().toString()); by me
		} catch (Exception e) {
			e.getStackTrace();
		}

	}
}
