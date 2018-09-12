package csv_checker

import java.nio.file.Path
import java.nio.file.Files;

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
            redirect(controller: "home")
            return;
        }

        File convFile;
        try {
            convFile = new File(f.getOriginalFilename());
            convFile.createNewFile();
            FileOutputStream fos = new FileOutputStream(convFile);
            fos.write(f.getBytes());
            fos.close();

            Path filePath = convFile.toPath();
            String contentType = Files.probeContentType(filePath);

            if(!contentType.equals("text/csv") && !contentType.equals("application/vnd.ms-excel")) {
                flash.message = 'File is not in csv format'
                redirect(controller: "home")
                return;
            }

        } catch (Exception e) {
            flash.message = 'Something went wrong!'
            redirect(controller: "home")
            return;
        }

        convFile.eachLine { line ->
            String[] data = line.split(";");
            if(data.length == 5)
                persons.add(new Person(data[0], data[1], data[2], data[3], data[4]))
        }

        if(persons.size() == 0){
            flash.message = 'No person data can be extracted from the file!'
            redirect(controller: "home")
        }
        else {
            render(view: "index", model: [persons: persons])
        }
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
