package csv_checker

/**
 * This controller handles the file upload, renders the table from the file input and saves the data in the database
 */
class CheckTableController {

    def persons = [];
    ManagePersonsService managePersonsService

    /**
     * Handle file upload and render data from the file in a table
     */
    def index() {
        persons = [];
        def f = request.getFile('filecsv')
        if (f.empty) {
            flash.message = 'File cannot be empty'
            redirect controller: 'home'
            return
        }

        File convFile = new File(f.getOriginalFilename());
        convFile.createNewFile();
        FileOutputStream fos = new FileOutputStream(convFile);
        fos.write(f.getBytes());
        fos.close();

        convFile.eachLine { line ->
            String[] data = line.split(";");
            persons.add(new Person(data[0], data[1], data[2], data[3], data[4]))
        }

        render(view: "index", model: [persons: persons])
    }

    /**
     * Handle saving of persons data in the database and redirect to the ViewData controller
     */
    def save() {
        int result = managePersonsService.savePersonsInDatabase(persons);
        if(result == -1) {
            flash.message = 'Something went wrong'
        }
        if(result == 0) {
            flash.message = 'Data successfully saved'
        }
        if (result > 0) {
            flash.message = 'Data successfully saved but there were ' + result + ' duplicates which were not saved!'
        }

        redirect(controller: "viewData")
    }
}
