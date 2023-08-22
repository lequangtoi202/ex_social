<%--
  Created by IntelliJ IDEA.
  User: TOI
  Date: 19/07/2023
  Time: 6:12 PM
  To change this template use File | Settings | File Templates.
--%>
<%@ page contentType="text/html;charset=UTF-8" pageEncoding="UTF-8" language="java" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<div class="container">
    <div class="pt-5 pb-5 table-responsive">
        <table class="mt-2 table" style="background-color: #fff">
            <thead>
            <tr>
                <th scope="col">ID</th>
                <th scope="col">Content</th>
                <th scope="col">Timestamp</th>
                <th scope="col">Creator</th>
                <th scope="col">Num of interactions</th>
                <th scope="col">Num of comments</th>
                <th scope="col">Action</th>
            </tr>
            </thead>
            <tbody>
            <c:forEach items="${posts}" var="p">
                <tr>
                    <td>${p.id}</td>
                    <td class="post-content">${p.content}</td>
                    <td>${p.timestamp}</td>
                    <td>${p.userId}</td>
                    <c:if test="${interactions.get(p.id) != null}">
                        <td>${interactions.get(p.id)}</td>
                    </c:if>
                    <c:if test="${interactions.get(p.id) == null}">
                        <td>0</td>
                    </c:if>
                    <c:if test="${numberOfComments.get(p.id) != null}">
                        <td>${numberOfComments.get(p.id)}</td>
                    </c:if>
                    <c:if test="${numberOfComments.get(p.id) == null}">
                        <td>0</td>
                    </c:if>
                    <td class="action-buttons">
                        <button class="btn btn-danger" onclick="confirmDeletePost(${p.id})">
                            <i class="fas fa-trash-alt"></i>
                        </button>
                    </td>
                </tr>
            </c:forEach>
            </tbody>
        </table>
        <div class="text-center">
            <c:if test="${currentPage > 1}">
                <a href="<c:url value="/admin/posts?page=${currentPage - 1}"/>" class="my-button">Previous</a>
            </c:if>

            <c:forEach begin="1" end="${totalPages}" varStatus="loop">
                <c:choose>
                    <c:when test="${currentPage == loop.index}">
                        <a href="#" class="my-button active">${loop.index}</a>
                    </c:when>
                    <c:otherwise>
                        <a href="<c:url value="/admin/posts?page=${loop.index}"/>" class="my-button">${loop.index}</a>
                    </c:otherwise>
                </c:choose>
            </c:forEach>

            <c:if test="${currentPage < totalPages}">
                <a href="<c:url value="/admin/posts?page=${currentPage + 1}"/>" class="my-button">Next</a>
            </c:if>
        </div>
    </div>
</div>
