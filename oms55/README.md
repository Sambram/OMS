#DOB - Data On Boarding


##Build:
mvn clean install

##Update License Information

open the licensing template file:  
	*`vi "license_15_Mac.lic"`*  
update the year value:  
	*`:%s/2015/change to current year value/g`*  
run Hybris custom Maven command to change the licensing information:    
	*`mvn hybris:licenseUpdater -Dlicense.file=license_15_Mac.lic`* 
	
Note: 
On windows machines use "license_15_Win.lic"
