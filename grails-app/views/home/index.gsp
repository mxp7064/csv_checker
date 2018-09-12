<html>
<head>
    <meta name="layout" content="main"/>
    <title>CSV LOADER</title>
</head>
<body>

<div id="content" role="main" class="text-center">

    <p class="tip">Upload your csv file</p>

    <g:form controller="checkTable" enctype="multipart/form-data" useToken="true" class="uploadForm">
        <input type="file" name="filecsv" style="display:inline"/>
        <input type="submit" class="btn btn-primary" value="UPLOAD"/>
    </g:form>

    <g:if test="${flash.message}">
        <div class="message" role="status">${flash.message}</div>
    </g:if>

</div>
</body>
</html>