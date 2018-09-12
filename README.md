How to run:
 
1) Clone the repository
2) Run person_table_script.sql and insertPersonProcedure.sql scripts in the SQL Server database (found in the /scripts/ folder)
2) Edit csv_checker/grails-app/conf/application.yml file and put your database credentials under environments -> development -> dataSource
3) Change directory in the command line to csv_checker/ root project folder
4) Run the project with the Grails command: run-app
5) Go to http://localhost:8080/ in your browser


Note: My source code is composed of:
 - 3 controllers in the controllers folder and their corresponding views in the views folder
 - 2 services in the services folder
 - Person model class
 - main layout file in the views/layouts folder