package csv_checker

import grails.core.GrailsApplication
import grails.gorm.transactions.Transactional

import java.sql.CallableStatement
import java.sql.Connection
import java.sql.DriverManager
import java.sql.ResultSet
import java.sql.SQLException
import java.sql.Statement

/**
 * DatabaseService handles all communication with the database
 */
@Transactional
class DatabaseService
{
    private Connection connection
    GrailsApplication grailsApplication

    /**
     * Get configuration data for accessing the database from the application.yml file and connect to the database
     * @throws SQLException
     */
    void connect() throws SQLException {
        def port = grailsApplication.config.getProperty('environments.development.dataSource.port')
        def database = grailsApplication.config.getProperty('environments.development.dataSource.database')
        def user = grailsApplication.config.getProperty('environments.development.dataSource.user')
        def password = grailsApplication.config.getProperty('environments.development.dataSource.password')
        connection = DriverManager.getConnection("jdbc:sqlserver://localhost:" + port + ";databaseName=" + database, user , password);
    }

    /**
     * Close connection with the database
     * @throws SQLException
     */
    void close() throws SQLException {
        connection.close();
    }

    /**
     * Get all persons from the database
     * @return Person collection
     * @throws SQLException
     */
    Person[] getAllPersons() throws SQLException {
        def persons = [];

        Statement stmt = connection.createStatement();
        ResultSet result = stmt.executeQuery("SELECT * FROM person");

        while(result.next()) {
            Person person = new Person(
                    result.getString("firstName"),
                    result.getString("lastName"), result.getString("zip"),
                    result.getString("city"),
                    result.getString("phone"));
            persons.add(person);
        }

        result.close();
        stmt.close();

        return persons;
    }

    /**
     * Insert person into database using the InsertPersonProcedure
     * @param person object
     * @return returns 0 on success or 1 if a given person already exists in the database
     * @throws SQLException
     */
    int insertPerson(Person person) throws SQLException {
        try {
            CallableStatement cstmt = connection.prepareCall("EXEC InsertPersonProcedure ?, ?, ?, ?, ?");

            cstmt.setString(1, person.getFirstName());
            cstmt.setString(2, person.getLastName());
            cstmt.setString(3, person.getZipCode());
            cstmt.setString(4, person.getCity());
            cstmt.setString(5, person.getPhone());

            cstmt.execute();
            cstmt.close();
            return 0;
        }
        catch(SQLException e) {
            if(e.getMessage().equals("DUPLICATE_RECORD"))
                return 1;
            else
                throw e;
        }
    }

    /**
     * Delete all persons from the database
     * @throws SQLException
     */
    void deleteAllPersons() throws SQLException {
        Statement stmt = connection.createStatement();
        stmt.executeUpdate("DELETE FROM person");
        stmt.close();
    }
}
