@BackendTest
Feature: Test Coinmarketcap Back-end

@BETest1
Scenario: Retrieve IDs
    Given Retrieve the ID of bitcoin(BTC),usd tether(USDT),and Ethereum(ETH)  
	Then Convert them to Bolivian Boliviano  

@BETest2
Scenario: Retrieve the Ethereum info
	Given Retrieve the Ethereum info
	Then Confirm the logo URL
	And Confirm the technical doc URl
	And Confirm the currency symbol
	And Confirm the date added
	And Confirm the platform is null
	And Confirm the currency is mineable

@BETest3
Scenario: Mineable Cryptocrrrencies in the first 10
	Given Retrieve the info of the first 10 cryptocurrencies
	Then Print the crypto currencies with mineable tag
	And Verify that correct crypto currencies are printed