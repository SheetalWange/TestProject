@FrontendTest
Feature: Test Coinmarketcap

		
Background: Open Coinmarketcap website
	Given User navigates to Coinmarketcap website

@FETest1
Scenario: View All 
	Given User clicks on View All
	When User is navigated to View All page
	Then Verify that 100 results are displayed

@FETest2
Scenario: Add to Watchlist
	Given User selects some random cryptocurrencies and adds to watchlist
	And Opens the watchlist in a different browser tab
	When Clicks on Watchlist tab  
	Then Verify all the options selected are added to watchlist 

@FETest3
Scenario: Filtering the Full List
	Given Displays the dropdown menu on the Cryptocurrencies tab
	And Clicks any of the three Full List options on this menu
	And Record the data on the current page
	When Applies any combination of filters
	Then Verify filtered data against the data recorded above