<%--
  Created by IntelliJ IDEA.
  User: TOI
  Date: 21/07/2023
  Time: 9:52 PM
  To change this template use File | Settings | File Templates.
--%>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<div class="p-5" style="max-width: 300px; margin: 0 auto">
    <form id="addGroupForm" action="<c:url value="/admin/groups"/>" method="post" modelAttribute="group">
        <div class="form-group">
            <label for="groupName">Group Name:</label>
            <input type="text" id="groupName" name="groupName" required>
        </div>
        <button type="submit" class="btn btn-primary">
            Add Group
        </button>
    </form>
</div>
