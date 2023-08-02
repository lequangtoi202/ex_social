<%--
  Created by IntelliJ IDEA.
  User: TOI
  Date: 21/07/2023
  Time: 9:52 PM
  To change this template use File | Settings | File Templates.
--%>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<div class="pt-5">
    <form id="editGroupForm" action="<c:url value="/admin/surveys/${surveyId}"/>" method="post" modelAttribute="survey">
        <div class="form-group">
            <label for="id1">ID:</label>
            <input type="text" id="id1" name="id" value="${survey.id}" disabled>
        </div>
        <div class="form-group">
            <label for="title">Title:</label>
            <input type="text" id="title" name="title" value="${survey.title}" required>
        </div>
        <div class="form-group">
            <label for="description">Description:</label>
            <textarea id="description" name="description" value="${survey.description}"></textarea>
        </div>
        <div class="form-group">
            <label>Type:</label>
            <select name="type">
                <c:forEach items="${surveyType}" var="type">
                    <option value="${type}" ${type == survey.type ? 'selected' : ''}>
                            ${type}
                    </option>
                </c:forEach>
            </select>
        </div>
        <button type="submit" class="btn btn-primary">
            Edit survey
        </button>
    </form>
</div>
