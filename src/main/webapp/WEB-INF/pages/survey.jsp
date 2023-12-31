<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<div class="container">
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
        <table class="mt-2 table" style="background-color: #fff">
            <thead>
            <tr>
                <th scope="col">ID</th>
                <th scope="col">Title</th>
                <th scope="col">Description</th>
                <th scope="col">Type</th>
                <th scope="col">Created on</th>
                <th scope="col">Post ID</th>
                <th scope="col">Creator</th>
                <th scope="col">Action</th>
            </tr>
            </thead>
            <tbody>
            <c:forEach items="${surveys}" var="s">
            <tr>
                <td>${s.id}</td>
                <td>${s.title}</td>
                <td>${s.description}</td>
                <td>${s.type}</td>
                <td>${s.createdOn}</td>
                <td>${s.postId}</td>
                <td>${s.userId}</td>
                <td class="action-buttons">
                    <button class="btn btn-danger" onclick="confirmDeleteSurvey(${s.id})">
                        <i class="fas fa-trash-alt"></i>
                    </button>
                </td>
            </tr>
            </c:forEach>
        </table>
        <div class="text-center">
            <c:if test="${currentPage > 1}">
                <a href="<c:url value="/admin/surveys?page=${currentPage - 1}"/>" class="my-button">Previous</a>
            </c:if>

            <c:forEach begin="1" end="${totalPages}" varStatus="loop">
                <c:choose>
                    <c:when test="${currentPage == loop.index}">
                        <a href="#" class="my-button active">${loop.index}</a>
                    </c:when>
                    <c:otherwise>
                        <a href="<c:url value="/admin/surveys?page=${loop.index}"/>" class="my-button">${loop.index}</a>
                    </c:otherwise>
                </c:choose>
            </c:forEach>

            <c:if test="${currentPage < totalPages}">
                <a href="<c:url value="/admin/surveys?page=${currentPage + 1}"/>" class="my-button">Next</a>
            </c:if>
        </div>
    </div>
</div>