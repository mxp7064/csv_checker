<!doctype html>
<html lang="en" class="no-js">
<head>
    <meta http-equiv="Content-Type" content="text/html; charset=UTF-8"/>
    <meta http-equiv="X-UA-Compatible" content="IE=edge"/>
    <title>
        <g:layoutTitle default="Grails"/>
    </title>
    <meta name="viewport" content="width=device-width, initial-scale=1"/>
    <asset:link rel="icon" href="favicon.ico" type="image/x-ico" />

    <asset:stylesheet src="application.css"/>

    <g:layoutHead/>
</head>
<body>


    <div class="navbar navbar-default navbar-static-top text-center navig">
        <a href="${createLink(controller: 'home')}">
            <asset:image src="csv-logo.png" alt="CSV Logo" width="150"/>
        </a>

        <div class="text-center tbuttons">
            <a class="btn btn-primary" role="button" href="${createLink(controller: 'home')}">
                Upload new file
            </a>

            <a class="btn btn-primary" role="button" href="${createLink(controller: 'viewData')}">
                All Persons
            </a>
        </div>

    </div>


    <g:layoutBody/>

    <div class="footer navbar-fixed-bottom text-center" role="contentinfo">
        Developed by Marko Pancirov
    </div>


    <asset:javascript src="application.js"/>

</body>
</html>
