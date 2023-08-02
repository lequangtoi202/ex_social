<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<div class="pt-5 table-responsive">
    <div class="d-flex">
        <select id="userRoleFilter" style="width: 10%" onchange="filterUsers()">
            <option value="all">All</option>
            <option value="alumni">Alumni</option>
            <option value="lecturer">Lecturer</option>
        </select>
        <div style="margin-left: 5px">
            <input type="text" placeholder="Search..." onchange="getAllUserByFullName(this)"/>
        </div>
    </div>
    <table class="mt-2 table table-striped" style="background-color: #6CB0EC">
        <thead>
        <tr>
            <th scope="col">ID</th>
            <th scope="col">Username</th>
            <th scope="col">FullName</th>
            <th scope="col">Email</th>
            <th scope="col">Phone</th>
            <th scope="col">Avatar</th>
            <th scope="col">Action</th>
        </tr>
        </thead>
        <tbody>
        <c:forEach items="${users}" var="u">
        <tr>
            <td>${u.id}</td>
            <td>${u.username}</td>
            <td>${u.fullName}</td>
            <td>${u.email}</td>
            <td>${u.phone}</td>
            <td class="user-avatar">
                <img src="${u.avatarLink}"/></td>
            <td class="action-buttons">
                <button class="btn btn-warning" onclick="viewUserDetail(${u.id})">
                    <i class="fas fa-eye"></i>
                </button>
                <button class="btn btn-danger" onclick="confirmDeleteUser(${u.id})">
                    <i class="fas fa-trash-alt"></i>
                </button>
            </td>
        </tr>
        </c:forEach>

    </table>
</div>
