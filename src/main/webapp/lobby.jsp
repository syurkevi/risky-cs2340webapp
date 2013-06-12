<%@ page import="edu.gatech.cs2340.todo.model.Todo" %>
<%@ page import="java.util.*" %>

<% TreeMap<Integer, Todo> todos = 
   (TreeMap<Integer, Todo>) request.getAttribute("todos"); %>

<html>
<head>
<title>To-Do Web App</title>
</head>
<body>
<h1>Your to do list</h1>

<table>
<tr>
<th>Title</th><th>Task</th>
</tr>

<% for (Integer id: todos.keySet()) { %>
<% Todo todo = todos.get(id); %>
<tr>
<form action="/todo/update/<%= id %>" method="POST">
  <!-- hidden operation element to simulate HTTP PUT method in server -->
  <input type="hidden" name="operation" value="PUT"/>
  <td><input type="text" name="title" value="<%= todo.getTitle() %>"/></td>
  <td><input type="text" name="task" value="<%= todo.getTask() %>"/></td>
  <td><input type="submit" value="Update"/></td>
</form>
<td valign="bottom">
  <form action="/todo/delete/<%= id %>" method="POST">
  <!-- hidden operation element to simulate HTTP DELETE method in server -->
    <input type="hidden" name="operation" value="DELETE"/>
    <input type="submit" value="Delete"/>
  </form>
 </td>
</tr>
<% } %>
<tr>
<form action="/todo/create" method="POST">
  <td><input type="text" name="title"/></td>
  <td><input type="text" name="task"/></td>
  <td><input type="submit" value="Add"/></td>
</form>
<td></td> <!-- empty cell to align with previous cells -->
</tr>
</table>

</body>
</html>
