<%@ page import="com.softserve.itacademy.model.Task" %>
<%@ page import="java.util.List" %>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<html>
<head>
    <title>List of Tasks</title>
    <link href="https://maxcdn.bootstrapcdn.com/bootstrap/4.5.2/css/bootstrap.min.css" rel="stylesheet">

    <style>
        <%@include file="../styles/main.css"%>
    </style>

</head>
<body>
<div class="container">
    <div class="row justify-content-center">
        <div class="col-8">
            <%@include file="header.html" %>
            <h3> List of the tasks</h3>
            <table class="table table-bordered table-hover">
                <thead>
                <tr>
                    <th scope="col">#</th>
                    <th scope="col">Title</th>
                    <th scope="col">Priority</th>
                    <th scope="col" colspan="3">Operations</th>
                </tr>
                </thead>
                <tbody>
                <%
                    for (Task task : (List<Task>) request.getAttribute("tasks")) {

                %>
                <tr>
                    <th scope="row"><%=task.getId()%>
                    </th>
                    <td><%=task.getTitle()%>
                    </td>
                    <td><%=task.getPriority()%>
                    </td>
                    <td class="col-1">
                        <a href="/read-task?id=<%=task.getId()%>" type="button" class="btn btn-primary">Read</a>
                    </td>
                    <td class="col-1">
                        <a href="/edit-task?id=<%=task.getId()%>" type="button" class="btn btn-info">Edit</a>
                    </td>
                    <td class="col-1">
                        <a href="/delete-task?id=<%=task.getId()%>" type="button" class="btn btn-danger">Delete</a>
                    </td>
                </tr>
                <%
                    }
                %>

                </tbody>
            </table>
        </div>
    </div>
</div>

<script src="https://ajax.googleapis.com/ajax/libs/jquery/3.5.1/jquery.min.js"></script>
<script src="https://maxcdn.bootstrapcdn.com/bootstrap/4.5.2/js/bootstrap.min.js"></script>
</body>
</html>
