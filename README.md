# TestProject
TestProject contains frontend and backend test cases.

#Frontend and Backend-Test Framework

Selenium & Java based frontend and Backend automation suite utilising the BDD methodologies of Cucumber and Gherkin

1. Install JAVA 
2. Install Maven
3. Install Eclipse

Set Path Variables : 

User Variable Path = directory for java Set Maven home in environment Variables

For Windows
Create the following System variables

JAVA_HOME = PAth to java sdk
M2_HOME = Path to maven installation
MAVEN_HOME = Path to maven installation
For Windows Edit Path System variable
Add %M2_HOME%\bin

Add all the dependencies related to cucumber,Selenium, Maven, Junit and rest-assured to pom.xml and update the project.

#Framework Overview 

The Frontend and Backend Test Framework specifies acceptance tests for CoinMarketCap. 

*Feature File :
CucumberFramework.featureFiles package contains "Frontend.feature" and "Backend.feature" features files for respective fronend and backend test cases.

*Step Definition Feature File:
CucumberFramework.stepFiles  package contains "Frontend.java" and "Backend.java" stepfiles for respective Frontend and Backend test cases.

*runners package contains TestRunner junit class.

*CucumberFramework.resouces package contains all the resources required.
Added chromedriver to resources


#Instructions to run project
Run the TestRunner to execute fronend and backend test cases.

Please note : the frontend test1 (@FETest1) will fail in assertions because the view all page displays 200 records but expectations was it to display 100 
records.

