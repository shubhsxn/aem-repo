# Allure


About Framework:
Tools and reports:
How to run this framework:
Enhancements to existing framework:


Project issues:



# MouseOver issue - Resolved
pp.us - Running on both ie & chrome 
pp.com - Running on both ie & chrome 



TBD:
1. Create WBBrokenLinks class - To find broken links on any given page. To be extended to entire website
1. Add parameters(test Data) to 'NavigationTests' & 'SearchResultsTest' in testng.xml 
1. Add submit the forms - 	https://www.corteva.ca/fr/contactez-nous.html 
							https://www.corteva.ca/fr/contactez-nous.html
							https://engage.corteva.com/US_contact-us
1. Add components check on header and footer links (If not covered in global header & Footer class)
2. Update package name per below link
https://www.oracle.com/technetwork/java/codeconventions-135099.html
3. Mobile
4. Work on duplicated named components
5. Check whether script is capable to identify 
 	- Missing Images.
 	- No.of Pages displaying on Website.
 	- Count of total links in each page. 	
6. Add Broken link test
7. Check all links(Ex: Check email with subject : Question About Production Push Thursday)
8. Work on @test(dependsOnGroups,dependsOnMethods ,groups,singleThreaded , dataProvider& priority)
9. Use @Flaky whenever required 
 	 	https://www.tutorialspoint.com/testng/testng_dependency_test.htm
10. Tune Allure reports

 

 Notes: 1. If you see failure on mobile script - Check around wait first
 		2. 	Check UiAutomator2 
 		3. Mobile issues on - getAttrute() & wait.until(ExpectedConditions.elementToBeClickable(elementLocation)); 
