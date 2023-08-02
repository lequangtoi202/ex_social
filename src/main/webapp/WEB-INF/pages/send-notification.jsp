<%--
  Created by IntelliJ IDEA.
  User: TOI
  Date: 21/07/2023
  Time: 9:52 PM
  To change this template use File | Settings | File Templates.
--%>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>

<div class="p-5" style="max-width: 500px; margin: 0 auto">
    <c:if test="${groupId != null}">
        <c:set var="groupId" value="${groupId}" />
        <input type="text" id="groupId" value="${groupId}" hidden >
    </c:if>
    <form id="sendMailForm">
        <label for="subject">Subject:</label>
        <input type="text" id="subject" name="subject" required><br><br>

        <label for="body">Body:</label>
        <textarea id="body" name="body" required></textarea><br><br>

        <label for="from">From:</label>
        <input type="text" id="from" name="from" required><br><br>

        <input type="submit" class="btn btn-primary" value="Send Mail">
    </form>
</div>
