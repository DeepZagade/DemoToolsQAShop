import java.util.List;
import java.util.concurrent.TimeUnit;

import org.openqa.selenium.By;
import org.openqa.selenium.JavascriptExecutor;
import org.openqa.selenium.Keys;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;

public class GoogleMapsSearch {

	public static void main(String[] args) {
		System.setProperty("webdriver.chrome.driver",
				System.getProperty("user.dir") + "\\src\\main\\resource\\chromedriver.exe");
		WebDriver driver = new ChromeDriver();
		driver.manage().timeouts().implicitlyWait(10, TimeUnit.SECONDS);
		driver.manage().timeouts().getScriptTimeout();
		driver.manage().timeouts().getPageLoadTimeout();
		driver.manage().deleteAllCookies();
		driver.manage().window().maximize();
		// navigate to Google Maps
		driver.get("https://maps.google.com/");

		// search for software companies in Pune
		WebElement searchBox = driver.findElement(By.name("q"));
		searchBox.sendKeys("software companies in Pune" + Keys.ENTER);

		// wait for the search results to load
		try {
			Thread.sleep(3000);
		} catch (InterruptedException e) {
			e.printStackTrace();
		}

		// Wait for the page to finish scrolling

		try {
			Thread.sleep(3000);
		} catch (InterruptedException e) {
			e.printStackTrace();
		}
		// find all the company names and URLs on the searchresults page
		List<WebElement> Companies = driver.findElements(By.xpath("//*[contains(@class,'fontHeadlineSmall ')]"));
		System.out.println("Total Companies= " + Companies.size());

		for (WebElement companyName : Companies) {
			String company = companyName.getText();
			System.out.println(company);
		}

		List<WebElement> websites = driver.findElements(By.xpath("//a[(@data-value='Website')]"));
		System.out.println("Total websites= " + websites.size());
		for (WebElement websiteName : websites) {
			String website = websiteName.getAttribute("href");
			System.out.println(website);
		}

		driver.quit();
	}
}
