<%--
  Created by IntelliJ IDEA.
  User: TOI
  Date: 19/07/2023
  Time: 6:12 PM
  To change this template use File | Settings | File Templates.
--%>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<div class="container">
    <div class="pt-5 table-responsive">
        <table class="mt-2 table table-striped" style="background-color: #6CB0EC">
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
                    <c:if test="${comments.get(p.id) != null}">
                        <td>${comments.get(p.id)}</td>
                    </c:if>
                    <c:if test="${comments.get(p.id) == null}">
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
    </div>
</div>
