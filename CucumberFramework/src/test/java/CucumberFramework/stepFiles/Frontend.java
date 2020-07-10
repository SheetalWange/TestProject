package CucumberFramework.stepFiles;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.TimeUnit;

import org.junit.After;
import org.junit.Assert;
import org.openqa.selenium.By;
import org.openqa.selenium.JavascriptExecutor;
import org.openqa.selenium.Keys;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;

import cucumber.api.java.Before;
import cucumber.api.java.en.And;
import cucumber.api.java.en.Given;
import cucumber.api.java.en.Then;
import cucumber.api.java.en.When;

public class Frontend {
	WebDriver driver;
	WebElement crypto;
	List<WebElement> fullDataList;
	List<WebElement> cryptosAddedToWatchlist = new ArrayList<WebElement>();
	
	
	@Before
	public void setup() {
		System.setProperty("webdriver.chrome.driver", "F:\\TestProject\\CucumberFramework\\src\\test\\java\\CucumberFramework\\resources\\chromedriver.exe");
		this.driver = new ChromeDriver();
		this.driver.manage().window().maximize();
		this.driver.manage().timeouts().pageLoadTimeout(60,TimeUnit.SECONDS);
		this.driver.manage().timeouts().setScriptTimeout(60,TimeUnit.SECONDS);
	}
	
	
	// Background for all scenarios
	@Given("^User navigates to Coinmarketcap website$")
	public void user_navigates_to_Coinmarketcap_website() throws Throwable {
		// Open https://coinmarketcap.com/
		driver.get("https://coinmarketcap.com/");
		
		// Close the cookie banner
		driver.findElement(By.xpath("/html/body/div/div/div[5]/div[2]")).click();
		Thread.sleep(3000);
		
		// Close the chat pop-up
		driver.findElement(By.xpath("/html/body/div/div/div[2]/div[1]/div[2]/div[2]/div[1]/div[2]/button")).click();
	}


	// First scenario - View All
	@Given("^User clicks on View All$")
	public void user_clicks_on_View_All() throws Throwable {
		System.out.println("Starting with the first frontend test.");
		// Click View All
		driver.findElement(By.xpath("/html/body/div/div/div[2]/div[1]/div[2]/div/div[1]/div[1]/div[2]/a[2]")).click();
	}


	@When("^User is navigated to View All page$")
	public void user_is_navigated_to_view_All_page() throws Throwable {
		// Checking user is on View All page
		WebDriverWait wait = new WebDriverWait(driver, 20);
		wait.until(ExpectedConditions.titleIs("All Cryptocurrencies | CoinMarketCap"));
		//String expectedTitle = "All Cryptocurrencies | CoinMarketCap";
		//Assert.assertEquals(expectedTitle, driver.getTitle());
	}
	

	@Then("^Verify that 100 results are displayed$")
	public void verify_that_100_results_are_displayed() throws Throwable {
		// Add cryptos in a list and get the count
		List<WebElement> pageSize = driver.findElements(By.xpath("/html/body/div/div/div[2]/div[1]/div[2]/div/div[2]/div[3]/div/table/tbody/tr"));   

		System.out.println( "Total Cryptos on the page are=" + pageSize.size());
		Assert.assertEquals(100, pageSize.size());
		driver.quit();
	}
	

	// Second scenario - Watchlist
	@Given("^User selects some random cryptocurrencies and adds to watchlist$")
	public void user_selects_some_random_cryptocurrencies_and_adds_to_watchlist() throws Throwable {
		System.out.println("Starting with the second frontend test.");

		// Select a random number between 5 and 10
		int countForWatchlist = getRandomIntegerBetweenRange(5, 10);
		System.out.println("Randomly adding "+countForWatchlist+" cryptos to the Watchlist.");
		
		// Count total cryptos displayed on the current page
		List<WebElement> results = driver.findElements(By.xpath("/html/body/div/div/div[2]/div[1]/div[2]/div/div[2]/div[3]/div/table/tbody/tr"));
		int pageSize = results.size();
		System.out.println("Current page displays "+pageSize+" cryptos.");

		// Select random cryptos and them to the Watchlist
		for (int counter=0; counter < countForWatchlist;)
		{
			int randomCrypto = getRandomIntegerBetweenRange(1, pageSize);
			System.out.println("Randomly selected Crypto number is: "+randomCrypto);
			crypto = driver.findElement(By.xpath("/html/body/div/div/div[2]/div[1]/div[2]/div/div[2]/div[3]/div/table/tbody/tr["+randomCrypto+"]"));

			if(!cryptosAddedToWatchlist.contains(crypto))
			{
				System.out.println("Adding crypto "+crypto.getText()+" to the Watchlist");
				cryptosAddedToWatchlist.add(crypto);
				JavascriptExecutor js = (JavascriptExecutor) driver;
				js.executeScript("window.scrollTo(0,0)");
				driver.findElement(By.xpath("/html/body/div/div/div[2]/div[1]/div[2]/div/div[2]/div[3]/div/table/tbody/tr["+randomCrypto+"]/td[9]/div/div")).click();
				driver.findElement(By.xpath("/html/body/div/div/div[2]/div[1]/div[2]/div/div[2]/div[3]/div/table/tbody/tr["+randomCrypto+"]/td[9]/div/div/div[2]/ul/li[1]")).click();
			    counter++;
			}
		}
	}

	@And("^Opens the watchlist in a different browser tab$")
	public void opens_the_watchlist_in_a_different_browser_tab() throws Throwable {
		// Open the watchlist in a different browser tab
		System.out.println("Opeing Watchlist in a new tab");
		String selectLinkOpeninNewTab = Keys.chord(Keys.CONTROL,Keys.RETURN);
		driver.findElement(By.linkText("Watchlist")).sendKeys(selectLinkOpeninNewTab);
	}

	@When("^Clicks on Watchlist tab$")
	public void clicks_on_Watchlist_tab() throws Throwable {
		// Click on the Watchlist tab
		System.out.println("Switching to Watchlist tab");
		ArrayList<String> tabs = new ArrayList<String> (driver.getWindowHandles());
		driver.switchTo().window(tabs.get(1));
	}

	@Then("^Verify all the options selected are added to watchlist$")
	public void verify_all_the_options_selected_are_added_to_watchlist() throws Throwable {
		//Check values to verify all the options that were selected are added to the watchlist
		List<WebElement> cryptosInWatchlist = driver.findElements(By.xpath("/html/body/div/div/div[2]/div[1]/div[2]/div[1]/div/div[3]/div[3]/div/table/tbody"));
		
		Assert.assertTrue(cryptosAddedToWatchlist.containsAll(cryptosInWatchlist));
		driver.quit();
	}


	// Third scenario - Apply Filters
	@Given("^Displays the dropdown menu on the Cryptocurrencies tab$")
	public void displays_the_dropdown_menu_on_the_Cryptocurrencies_tab() throws Throwable {
		System.out.println("Starting with the third frontend test.");
		
		// Display the dropdown menu on the Cryptocurrencies tab
		driver.findElement(By.xpath("/html/body/div/div/div[2]/div[1]/div[2]/div/div[1]/div[2]/ul[1]/li[1]")).click();
	}

	@And("^Clicks any of the three Full List options on this menu$")
	public void clicks_any_of_the_three_Full_List_options_on_this_menu() throws Throwable {
		// Click on the All Cryptocurrencies Full List options on the menu
		driver.findElement(By.xpath("/html/body/div/div/div[2]/div[1]/div[2]/div/div[1]/div[2]/ul[1]/li[1]/div/div[2]/ul/li[3]")).click();
	}

	@And("^Record the data on the current page$")
	public void record_the_data_on_the_current_page() throws Throwable {
		// Record the data on the current page
		fullDataList = driver.findElements(By.xpath("/html/body/div/div/div[2]/div[1]/div[2]/div/div[2]/div[3]/div/table/tbody"));
	}
	
	@When("^Applies any combination of filters$")
	public void applies_any_combination_of_filters() throws Throwable {
		// Apply Market Cap, Volume (24) filters & Circulating Supply filters
		Thread.sleep(3000);
		driver.findElement(By.xpath("/html/body/div/div/div[2]/div[1]/div[2]/div/div[1]/div[1]/button")).click(); // Click on Filer button
		driver.findElement(By.xpath("/html/body/div/div/div[2]/div[1]/div[2]/div/div[2]/div/div[1]/div/div/div[1]/button")).click();  // Click on Market Cap filter
		driver.findElement(By.xpath("/html/body/div/div/div[2]/div[1]/div[2]/div/div[2]/div/div[1]/div/div/div[2]/div/div[1]/div[1]/div[1]/input")).sendKeys("1000000000"); // Set the MC range
		driver.findElement(By.xpath("/html/body/div/div/div[2]/div[1]/div[2]/div/div[2]/div/div[1]/div/div/div[2]/div/div[2]/div[2]/button[2]")).click();  // Apply the MC filter
		driver.findElement(By.xpath("/html/body/div/div/div[2]/div[1]/div[2]/div/div[2]/div/div[3]/div/div/div[1]/button")).click();  // Click on Volume filter
		driver.findElement(By.xpath("/html/body/div/div/div[2]/div[1]/div[2]/div/div[2]/div/div[3]/div/div/div[2]/div/div[1]/div[1]/div[1]/input")).sendKeys("999999"); // Set the Volume range
		driver.findElement(By.xpath("/html/body/div/div/div[2]/div[1]/div[2]/div/div[2]/div/div[3]/div/div/div[2]/div/div[2]/div[2]/button[2]")).click(); // Apply the Volume filter
		driver.findElement(By.xpath("/html/body/div/div/div[2]/div[1]/div[2]/div/div[2]/div/div[4]/div/div/div[1]/button")).click();   // Click on Circulating Supply filter
		driver.findElement(By.xpath("/html/body/div/div/div[2]/div[1]/div[2]/div/div[2]/div/div[4]/div/div/div[2]/div/div[1]/div[1]/div[1]/input")).sendKeys("1000000000"); // Set the Circulating Supply range
		driver.findElement(By.xpath("/html/body/div/div/div[2]/div[1]/div[2]/div/div[2]/div/div[4]/div/div/div[2]/div/div[2]/div[2]/button[2]")).click(); // Apply the Circulating Supply filter
	}

	@Then("^Verify filtered data against the data recorded above$")
	public void verify_filtered_data_against_the_data_recorded_above() throws Throwable {
		// Check against the data recorded
		List<WebElement> filteredDataList = driver.findElements(By.xpath("/html/body/div/div/div[2]/div[1]/div[2]/div/div[2]/div[3]/div/table/tbody"));
		
		Assert.assertTrue(fullDataList.containsAll(filteredDataList));
		driver.quit();
	}



	@After 
	public void tearDown() {
		driver.quit();
	}

	public static int getRandomIntegerBetweenRange(double min, double max){
	    double x = Math.random()*(max - min + 1) + min;
	    return (int)x;
	}
}