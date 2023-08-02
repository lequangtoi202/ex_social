<%--
  Created by IntelliJ IDEA.
  User: TOI
  Date: 19/07/2023
  Time: 12:57 PM
  To change this template use File | Settings | File Templates.
--%>
<%@page contentType="text/html" pageEncoding="UTF-8" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="sec" uri="http://www.springframework.org/security/tags" %>
<div>
    <nav class="navbar navbar-expand-sm bg-dark navbar-dark px-4">
        <ul class="navbar-nav">
            <li class="nav-item active">
                <a class="nav-link" href="<c:url value="/admin"/>">Trang chủ</a>
            </li>
            <li class="nav-item dropdown">
                <a class="nav-link dropdown-toggle" href="#" id="navbarDropdown" role="button" data-toggle="dropdown" aria-haspopup="true" aria-expanded="false">
                    Danh mục
                </a>
                <div class="dropdown-menu" aria-labelledby="navbarDropdown">
                    <a class="dropdown-item" href="<c:url value="/admin/users"/>">User</a>
                    <a class="dropdown-item" href="<c:url value="/admin/groups"/>">Group</a>
                    <a class="dropdown-item" href="<c:url value="/admin/posts"/>">Post</a>
                    <a class="dropdown-item" href="<c:url value="/admin/surveys"/>">Survey</a>
                    <a class="dropdown-item" href="<c:url value="/admin/send-notification"/>">Send Notification</a>
                </div>
            </li>
        </ul>

        <ul class="navbar-nav" style="margin-left: auto">
            <sec:authorize access="isAuthenticated()">
                <sec:authentication property="principal.username" var="loggedInUsername" />
                <li class="nav-item">
                    <a class="nav-link" href="#">${loggedInUsername}</a>
                </li>
                <li class="nav-item">
                    <a class="nav-link" href="<c:url value="/admin/login?logout"/>">Logout</a>
                </li>
            </sec:authorize>
            <sec:authorize access="!isAuthenticated()">
                <li class="nav-item">
                    <a class="nav-link" href="<c:url value="/admin/login"/>">Login</a>
                </li>
            </sec:authorize>
        </ul>
    </nav>
</div>
