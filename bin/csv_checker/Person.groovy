package csv_checker

/**
 * Data model class representing a person with a constraint that the zip code must be a 5 digit number
 */
class Person {

    String firstName
    String lastName
    String zipCode
    String city
    String phone
    boolean valid

    Person(firstName, lastName, zipCode, city, phone) {
        this.firstName = firstName
        this.lastName = lastName
        this.zipCode = zipCode
        this.city = city
        this.phone = phone
        if(this.validate()){
            this.valid = true
        } else {
            this.valid = false
        }
    }

    boolean isValid(){
        this.valid
    }

    static constraints = {
        zipCode(nullable:false, blank:false, validator:{zip ->

            if(zip ==~ /^\d{5}$/) {
                return true
            }
            else {
                return "invalidZipFormat"
            }
        })
    }
}
