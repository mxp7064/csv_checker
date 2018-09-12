package csv_checker

import grails.gorm.transactions.Transactional

import java.sql.SQLException

/**
 * ManagePersonsService handles the business logic and uses the DatabaseService abstraction to manipulate with data
 */
@Transactional
class ManagePersonsService {

    DatabaseService databaseService

    /**
     * Save persons in database
     * @param persons
     * @return returns -1 on failure or duplicate count on success
     */
    int savePersonsInDatabase(persons){
        int duplicateCount = 0;
        try {
            databaseService.connect();
            persons.each { person ->
                if(person.isValid()) {
                    duplicateCount += savePersonInDatabase(person);
                }
            }
            databaseService.close();
            return duplicateCount;
        }
        catch(SQLException e){
            e.printStackTrace();
            return -1;
        }
    }

    /**
     * Save single person in the database
     * @param person
     * @return returns 0 on success or 1 if a given person already exists in the database
     * @throws SQLException
     */
    int savePersonInDatabase(Person person) throws SQLException {
        return databaseService.insertPerson(person);
    }

    /**
     * Get all persons from the database
     * @return returns persons collection or -1 on failure
     */
    def getAllPersons(){
        try {
            databaseService.connect();
            Person[] persons = databaseService.getAllPersons();
            databaseService.close();
            return persons;
        }
        catch(SQLException e){
            e.printStackTrace();
            return -1;
        }
    }

    /**
     * Delete all persons from the database
     * @return returns -1 on failure
     */
    def deleteAllPersons() {
        try {
            databaseService.connect();
            databaseService.deleteAllPersons();
            databaseService.close();
        }
        catch (SQLException e) {
            e.printStackTrace();
            return -1;
        }
    }
}

