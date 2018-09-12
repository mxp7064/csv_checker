package csv_checker

/**
 * This controller renders a table populated with peron data from the database, it also handles deletion of that data
 */
class ViewDataController {

    def persons;
    ManagePersonsService managePersonsService

    /**
     * Get and render person data from the database
     */
    def index() {
        persons = managePersonsService.getAllPersons();
        render(view: "index", model: [persons: persons])
    }

    /**
     * Delete all persons and redirect to index
     */
    def deleteAllPersons() {
        managePersonsService.deleteAllPersons();
        redirect(actionName: "index")
    }
}
