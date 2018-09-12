<html>
<head>
    <meta name="layout" content="main"/>
    <title>Check Table</title>

</head>
<body>

<div id="content" role="main">


    <div class="container">

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
                    <tr class="${!person.isValid() ? 'danger':'success'}">
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

        <div class="alert alert-info" style="margin: 0px; padding: 0px; padding-left: 5px;">
            <strong>Info!</strong> Rows in red have invalid zip code and won't be saved
        </div>

        <div class="text-center tbuttons">
            <a class="btn btn-primary" role="button" href="${createLink(controller: 'home')}">
                Upload new file
            </a>

            <a class="btn btn-primary" role="button" href="${createLink(action: 'save')}">
                Save
            </a>
        </div>

    </div>

</div>
</body>
</html>