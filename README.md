# MS3 Coding Challenge

* ms3Challenge-1.0-jar-with-dependencies.jar is in project folder, executing this jar file will begin the application. The User will be prompted to select a .csv file to be parsed.  After the file is parsed the User is prompted to select a directory for the bad-data-<timestamp>.csv to be stored.  A log file will also be created in this directory that contains the number of records parsed, along with counts of the records that contained "good data" and "bad data".
  
# Tools Utilized
* Maven
  - Used for project management and build
  
* ORMLite
  - Database creation, adding and dropping of tables
  - Annotations for persisting Java classes
  - Database Access Object classes for storing objects to database
  
* OpenCSV
  - CSV parsing library
  - Used to read and write csv files
  
# Approach

* Read in each record, then check to see if columns are null, empty, or if column count is the wrong size for each record.
  * If the record passes the checks above: it is stored into a "good data" table.  Otherwise, the record is stored in the "bad data" table.
