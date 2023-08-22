<%--
  Created by IntelliJ IDEA.
  User: TOI
  Date: 19/07/2023
  Time: 6:12 PM
  To change this template use File | Settings | File Templates.
--%>
<%@ page contentType="text/html;charset=UTF-8" pageEncoding="UTF-8" language="java" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<div class="pt-5 pb-5 table-responsive">
    <div>
        <div class="messageDiv">
            <c:if test="${success}">
                <div class="alert alert-success">
                        ${success}
                </div>
            </c:if>
            <c:if test="${error}">
                <div class="alert alert-danger">
                        ${error}
                </div>
            </c:if>
        </div>
    </div>
    <div class="add-button-container d-flex">
        <button class="btn btn-primary" onclick="addNewGroup()">
            <i class="fas fa-plus"></i> Add
        </button>
        <div style="margin-left: 5px">
            <input type="text" placeholder="Search..." onchange="getGroupByName(this)"/>
        </div>
    </div>
    <table class="mt-2 table" style="background-color: #fff">
        <thead>
        <tr>
            <th scope="col">ID</th>
            <th scope="col">Group Name</th>
            <th scope="col">Creator ID</th>
            <th scope="col">Num of Members</th>
            <th scope="col">Action</th>
        </tr>
        </thead>
        <tbody>
        <c:forEach items="${groups}" var="group">
            <tr>
                <td>${group.id}</td>
                <td>${group.groupName}</td>
                <td>${group.creatorId}</td>
                <c:if test="${numOfUsers.get(group.id) != null}">
                    <td>${numOfUsers.get(group.id)}</td>
                </c:if>
                <c:if test="${numOfUsers.get(group.id) == null}">
                    <td>0</td>
                </c:if>
                <td class="action-buttons">
                    <button class="btn btn-warning" onclick="viewUsersGroup(${group.id})">
                        <i class="fas fa-eye"></i>
                    </button>
                    <button class="btn btn-warning" onclick="editGroup(${group.id})">
                        <i class="fas fa-edit"></i>
                    </button>
                    <button class="btn btn-danger" onclick="confirmDeleteGroup(${group.id})">
                        <i class="fas fa-trash-alt"></i>
                    </button>
                    <button class="btn btn-primary" onclick="sendToAllMemberInGroup(${group.id})">
                        <i class="fas fa-bell"></i>
                    </button>
                </td>
            </tr>
        </c:forEach>
        </tbody>
    </table>
    <div class="text-center">
        <c:if test="${currentPage > 1}">
            <a href="<c:url value="/admin/groups?page=${currentPage - 1}"/>" class="my-button">Previous</a>
        </c:if>

        <c:forEach begin="1" end="${totalPages}" varStatus="loop">
            <c:choose>
                <c:when test="${currentPage == loop.index}">
                    <a href="#" class="my-button active">${loop.index}</a>
                </c:when>
                <c:otherwise>
                    <a href="<c:url value="/admin/groups?page=${loop.index}"/>" class="my-button">${loop.index}</a>
                </c:otherwise>
            </c:choose>
        </c:forEach>

        <c:if test="${currentPage < totalPages}">
            <a href="<c:url value="/admin/groups?page=${currentPage + 1}"/>" class="my-button">Next</a>
        </c:if>
    </div>
</div>


