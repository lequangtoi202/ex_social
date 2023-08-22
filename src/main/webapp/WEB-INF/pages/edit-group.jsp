<%--
  Created by IntelliJ IDEA.
  User: TOI
  Date: 21/07/2023
  Time: 9:52 PM
  To change this template use File | Settings | File Templates.
--%>
<%@ page contentType="text/html;charset=UTF-8" pageEncoding="UTF-8" language="java" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<div class="p-5">
    <form id="editGroupForm" action="<c:url value="/admin/groups/${groupId}"/>" method="post" modelAttribute="group" accept-charset="UTF-8">
        <div class="form-group">
            <label for="groupName">Group Name:</label>
            <input type="text" id="groupName" name="groupName" value="${group.groupName}" required>
        </div>
        <button type="submit btn btn-primary">
            Edit group
        </button>
    </form>
</div>
