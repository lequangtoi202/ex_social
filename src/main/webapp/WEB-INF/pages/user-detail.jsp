<%--
  Created by IntelliJ IDEA.
  User: TOI
  Date: 20/07/2023
  Time: 9:41 AM
  To change this template use File | Settings | File Templates.
--%>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>

<div class="user-profile">

    <div class="avatar">
        <img src="${user.avatarLink}" alt="User Avatar">
    </div>
    <div class="user-info">
        <h3>User information</h3>
        <p>Username: ${user.username}</p>
        <p>Full Name:  ${user.fullName}</p>
        <p>Email:  ${user.email}</p>
        <p>Phone:  ${user.phone}</p>
    </div>
</div>