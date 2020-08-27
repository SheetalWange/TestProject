package CucumberFramework.stepFiles;

import java.util.ArrayList;

import java.util.Arrays;
import java.util.List;

import org.junit.Assert;

import cucumber.api.java.en.Given;
import cucumber.api.java.en.Then;
import cucumber.api.java.en.And;

import io.restassured.RestAssured;
import io.restassured.path.json.JsonPath;
import io.restassured.response.Response;
import io.restassured.specification.RequestSpecification;


public class Backend {
	JsonPath mapResponse, infoResponse;
	List<String> actualMineableCryptos = new ArrayList<String>();
	String baseURI ="https://pro-api.coinmarketcap.com";
	String APIKey = "*************************************";
	
	//Retrieve the ID of bitcoin (BTC), usd tether (USDT), and Ethereum (ETH), using the /cryptocurrency/map call
	@Given("^Retrieve the ID of bitcoin\\(BTC\\),usd tether\\(USDT\\),and Ethereum\\(ETH\\)$")
	public void retrieve_the_ID_of_bitcoin_BTC_usd_tether_USDT_and_Ethereum_ETH() throws Throwable {
		System.out.println("Starting with the first backend test.");

		RestAssured.baseURI = baseURI;
		RequestSpecification httpRequest = RestAssured.given();
		Response response = httpRequest.queryParam("start", "1")
				                       .queryParam("limit", "10")
				                       .queryParam("symbol", "BTC,USDT,ETH")
				                       .queryParam("CMC_PRO_API_KEY", APIKey)
				                       .get("/v1/cryptocurrency/map");
		
		int statusCode = response.getStatusCode();
		Assert.assertEquals(200, statusCode);
	
		//String responseBody = response.getBody().asString();
		//System.out.println("My Response is" +responseBody);
		 
		mapResponse = response.jsonPath();
		
		for(int i=0; i<3; i++)
		{
			int cryptoID = mapResponse.get("data["+i+"].id");
			String cryptoName = mapResponse.get("data["+i+"].symbol");
			System.out.println("ID of " +cryptoName+ " is: " +cryptoID);
		}
	}

	// Use the IDs of these currencies and convert them to Bolivian Boliviano using the /tools/price-conversion call
	@Then("^Convert them to Bolivian Boliviano$")
	public void convert_them_to_Bolivian_Boliviano() throws Throwable {
		RestAssured.baseURI = baseURI;

		for(int i=0; i<3; i++)
		{
			int cryptoID = mapResponse.get("data["+i+"].id");
			String cryptoName = mapResponse.get("data["+i+"].symbol");
			
			System.out.println("Retrieving price of "+cryptoName+"-" +cryptoID+ " in Bolivian Boliviano");

			RequestSpecification httpRequest = RestAssured.given();
			Response response = httpRequest.queryParam("id", cryptoID)
											.queryParam("amount", 1)
											.queryParam("convert", "BOB")
											.queryParam("CMC_PRO_API_KEY", APIKey)
											.get("/v1/tools/price-conversion");

			int statusCode = response.getStatusCode();
			Assert.assertEquals(200, statusCode);
			
			//String responseBody = response.getBody().asString();
			//System.out.println("My Response is" +responseBody);

			JsonPath pcResponse = response.jsonPath();
			String crypto = pcResponse.get("data.symbol");
			Double BOBPrice = pcResponse.getDouble("data.quote.BOB.price");
			System.out.println("Bolivian Boliviano price of " +crypto+ " is: " +BOBPrice);
		}
	}
	
	
	//Retrieve the Ethereum (ID 1027) technical documentation website from the cryptocurrency/info call.
	@Given("^Retrieve the Ethereum info$")
	public void retrieve_the_Ethereum_info() throws Throwable {
		System.out.println("Starting with the second backend test.");

		RestAssured.baseURI = baseURI;
		RequestSpecification httpRequest = RestAssured.given();
		Response response = httpRequest.queryParam("id", "1027")
				                       .queryParam("CMC_PRO_API_KEY", APIKey)
				                       .get("/v1/cryptocurrency/info");
		
		int statusCode = response.getStatusCode();
		Assert.assertEquals(200, statusCode);
	
		//String responseBody = response.getBody().asString();
		//System.out.println("My Response is" +responseBody);
		 
		infoResponse = response.jsonPath();
	}
	
	//Confirm the logo URL is present
	@Then("^Confirm the logo URL$")
	public void confirm_the_logo_URL() throws Throwable {
		String expectedLogoUrl = "https://s2.coinmarketcap.com/static/img/coins/64x64/1027.png";
		String actualLogoUrl = infoResponse.getString("data.1027.logo");
		Assert.assertEquals(expectedLogoUrl, actualLogoUrl);
	}
	
	//Confirm the technical_doc URl is present
	@And("^Confirm the technical doc URl$")
	public void confirm_the_technical_doc_URl() throws Throwable {
		String expectedTDocURI = "https://github.com/ethereum/wiki/wiki/White-Paper";
		List<String> actualTDocURIs = infoResponse.getList("data.1027.urls.technical_doc");
		Assert.assertTrue(actualTDocURIs.contains(expectedTDocURI));
	}

	//Confirm the currency symbol
	@And("^Confirm the currency symbol$")
	public void confirm_the_currency_symbol() throws Throwable {
		String expectedCurrencySymbol = "ETH";
		String actualCurrencySymbol = infoResponse.getString("data.1027.symbol");
		Assert.assertEquals(expectedCurrencySymbol, actualCurrencySymbol);
	}

	//Confirm the date added
	@And("^Confirm the date added$")
	public void confirm_the_date_added() throws Throwable {
		String expectedDateAdded = "2015-08-07T00:00:00.000Z";
		String actualDateAdded = infoResponse.getString("data.1027.date_added");
		Assert.assertEquals(expectedDateAdded, actualDateAdded);
	}

	//Confirm the platform is null
	@And("^Confirm the platform is null$")
	public void confirm_the_platform_is_null() throws Throwable {
		String actualplatform = infoResponse.getString("data.1027.platform");
		Assert.assertNull(actualplatform);
	}

	//Confirm that the currency has the mineable tag associated with it
	@And("^Confirm the currency is mineable$")
	public void confirm_the_currency_is_mineable() throws Throwable {
		String expectedTags = "mineable";
		List<String> actualTags = infoResponse.getList("data.1027.tags");
		Assert.assertTrue(actualTags.contains(expectedTags));
	}
	
	
	//Retrieve the first 10 currencies from the cryptocurrency/info call (ID 1, 2, 3 … 10)
	@Given("^Retrieve the info of the first (\\d+) cryptocurrencies$")
	public void retrieve_the_info_of_the_first_cryptocurrencies(int arg1) throws Throwable {
		System.out.println("Starting with the third backend test.");

		RestAssured.baseURI = baseURI;
		RequestSpecification httpRequest = RestAssured.given();
		Response response = httpRequest.queryParam("id", "1,2,3,4,5,6,7,8,9,10")
				                       .queryParam("CMC_PRO_API_KEY", APIKey)
				                       .get("/v1/cryptocurrency/info");
		
		int statusCode = response.getStatusCode();
		Assert.assertEquals(200, statusCode);
	
		//String responseBody = response.getBody().asString();
		//System.out.println("My Response is" +responseBody);
		 
		infoResponse = response.jsonPath();
	}

	//Check which currencies have the mineable tag associated with them
	@Then("^Print the crypto currencies with mineable tag$")
	public void print_the_crypto_currencies_with_mineable_tag() throws Throwable {
		String tag = "mineable";
		for(int i=1; i<=10; i++)
		{
			List<String> actualTags = infoResponse.getList("data."+i+".tags");
			String cryptoName = infoResponse.getString("data."+i+".name");
			if(actualTags.contains(tag))
			{
				System.out.println("Crypto"+ cryptoName +"is mineable.");
				actualMineableCryptos.add(cryptoName);
			};
		}
	}

	//Verify that the correct crypto currencies have been printed out.
	@Then("^Verify that correct crypto currencies are printed$")
	public void verify_that_correct_crypto_currencies_are_printed() throws Throwable {
		List<String> expectedMineableCryptos = Arrays.asList("Bitcoin","Litecoin","Namecoin","Terracoin","Peercoin","Novacoin","Devcoin","Feathercoin","Mincoin","Freicoin");
		Assert.assertTrue(actualMineableCryptos.containsAll(expectedMineableCryptos));
	}
}
