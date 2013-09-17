<%@taglib uri="http://www.springframework.org/tags/form" prefix="form"%>
<%@taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>

<?xml version="1.0" encoding="ISO-8859-1" ?>

<%@ page language="java" contentType="text/html; charset=ISO-8859-1" pageEncoding="ISO-8859-1"%>

<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">
<html xmlns="http://www.w3.org/1999/xhtml">
<head>
<meta http-equiv="Content-Type" content="text/html; charset=ISO-8859-1" />
<title>Data Manager</title>
</head>
<body>

  <h2>Data Manager</h2>
  
  <p>Uploaded Excel files (*.xlsx) are processed by extracting thier data and put them into JSON files as JSON format.</p>

  <form:form method="POST" commandName="fileUpload" action="${pageContext.request.contextPath}/fileUpload" enctype="multipart/form-data">
 
    Please select a file to upload : <input type="file" name="file" />
    <input type="submit" value="upload" />
    <span> </span>

  </form:form>
  
  
  <div>${message}</div>

  <table border="1px" cellpadding="0" cellspacing="0">
    <thead>
      <tr>
        <th>name</th>
        <th>Download</th>
        <th>Delete</th>
      </tr>
    </thead>
    <tbody>
      <c:forEach var="uploadedFile" items="${uploadedFiles}">
        <tr>
          <td>${uploadedFile.name}</td>
          <td>
            <a href="${pageContext.request.contextPath}/fileUpload/download/${uploadedFile.name}">Download</a>
          </td>
          <td>
            <a href="${pageContext.request.contextPath}/fileUpload/delete/${uploadedFile.name}">Delete</a>
          </td>
        </tr>
      </c:forEach>
    </tbody>
  </table>

</body>
</html>