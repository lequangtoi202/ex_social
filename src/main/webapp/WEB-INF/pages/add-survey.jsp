<%--
  Created by IntelliJ IDEA.
  User: TOI
  Date: 21/07/2023
  Time: 9:52 PM
  To change this template use File | Settings | File Templates.
--%>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<div class="p-5">
    <form id="editGroupForm" action="<c:url value="/admin/surveys"/>" method="post" modelAttribute="survey">
        <div class="form-group">
            <label for="title">Title:</label>
            <input type="text" id="title" name="title" required>
        </div>
        <div class="form-group">
            <label for="description">Description:</label>
            <textarea id="description" name="description"></textarea>
        </div>
        <div class="form-group">
            <label>Type:</label>
            <select name="type" id="type">
                <c:forEach items="${surveyType}" var="type">
                    <option value="${type}">
                            ${type}
                    </option>
                </c:forEach>
            </select>
        </div>
        <button type="submit" class="btn btn-primary">
            Add survey
        </button>
    </form>
</div>
