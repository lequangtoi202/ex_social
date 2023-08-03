<%--
  Created by IntelliJ IDEA.
  User: TOI
  Date: 22/07/2023
  Time: 7:43 PM
  To change this template use File | Settings | File Templates.
--%>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<div class="pt-5 pb-5 table-responsive">
    <table class="table" style="background-color: #fff">
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
                <img src="${u.avatarLink}"/>
            </td>
            <td class="action-buttons">
                <button class="btn btn-warning" onclick="viewUserDetail(${u.id})">
                    <i class="fas fa-eye"></i>
                </button>
                <button class="btn btn-danger" onclick="confirmDeleteUserFromGroup(${groupId}, ${u.id})">
                    <i class="fas fa-trash-alt"></i>
                </button>
            </td>
        </tr>
        </c:forEach>
    </table>
    <div class="text-center">
        <c:if test="${currentPage > 1}">
            <a href="<c:url value="/admin/groups/${groupId}/members?page=${currentPage - 1}"/>" class="my-button">Previous</a>
        </c:if>

        <c:forEach begin="1" end="${totalPages}" varStatus="loop">
            <c:choose>
                <c:when test="${currentPage == loop.index}">
                    <a href="#" class="my-button active">${loop.index}</a>
                </c:when>
                <c:otherwise>
                    <a href="<c:url value="/admin/groups/${groupId}/members?page=${loop.index}"/>"
                       class="my-button">${loop.index}</a>
                </c:otherwise>
            </c:choose>
        </c:forEach>

        <c:if test="${currentPage < totalPages}">
            <a href="<c:url value="/admin/groups/${groupId}/members?page=${currentPage + 1}"/>"
               class="my-button">Next</a>
        </c:if>
    </div>
</div>

