<html>
<head>
    <meta name="layout" content="main"/>
    <title>Persons</title>

</head>
<body>

<div id="content" role="main">


    <div class="container">

        <g:if test="${persons != -1}">

            <g:if test="${persons.length > 0}">
            <div class="text-center">
                <h3>All Persons</h3>
            </div>

            <div class="text-center" style="margin-top: 15px;">
                <a class="btn btn-primary" role="button" href="${createLink(action: 'deleteAllPersons')}">
                    Delete All
                </a>
            </div>

            <div class="table-responsive ">
                <table class="table table-striped">
                    <thead>
                    <tr>
                        <th>First Name</th>
                        <th>Last Name</th>
                        <th>Zip</th>
                        <th>City</th>
                        <th>Phone</th>
                    </tr>
                    </thead>
                    <tbody>

                    <g:each var="person" in="${persons}">
                        <tr>
                            <td>${person.firstName}</td>
                            <td>${person.lastName}</td>
                            <td>${person.zipCode}</td>
                            <td>${person.city}</td>
                            <td>${person.phone}</td>
                        </tr>
                    </g:each>

                    </tbody>
                </table>
            </div>
            </g:if>
        </g:if>

        <g:if test="${persons.length == 0}">
            <div class="alert alert-info" style="margin: 0px; padding: 0px; padding-left: 5px;">
                <strong>No persons to show</strong>
            </div>
        </g:if>

        <g:if test="${flash.message}">
            <div class="alert alert-info" style="margin: 0px; padding: 0px; padding-left: 5px;">
                <strong>${flash.message}</strong>
            </div>
        </g:if>

        <g:if test="${persons == -1}">
            <div class="alert alert-danger" style="margin: 0px; padding: 0px; padding-left: 5px;">
                <strong>Can't get persons - something went wrong</strong>
            </div>
        </g:if>

        <g:if test="${deleteResult == -1}">
            <div class="alert alert-danger" style="margin: 0px; padding: 0px; padding-left: 5px;">
                <strong>Can't delete persons - something went wrong</strong>
            </div>
        </g:if>

    </div>

</div>
</body>
</html>