How to run:

1) Install latest version of Grails from http://grails.org/index.html
2) Clone the repository
3) Run person_table_script.sql and insertPersonProcedure.sql scripts (found in the /scripts/ folder) in the SQL Server database 
4) Edit csv_checker/grails-app/conf/application.yml file and put your database credentials under environments -> development -> dataSource
5) Change directory in the command line to csv_checker root project folder
6) Run the project with the Grails command: run-app
7) Go to http://localhost:8080/ in your browser


Note: My source code is composed of:
 - 3 controllers in the controllers folder and their corresponding views in the views folder
 - 2 services in the services folder
 - Person model class
 - main layout file in the views/layouts folder

 Note: For the visual representation I used Bootstrap