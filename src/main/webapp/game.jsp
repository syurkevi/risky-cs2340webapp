<%@ page import="edu.gatech.cs2340.risky.model.Game" %>
<%@ page import="edu.gatech.cs2340.risky.model.Lobby" %>
<%@ page import="java.util.*" %>

<% Lobby lobby = (Lobby) request.getAttribute("lobby"); %>
<% Game game = (Game) request.getAttribute("game"); %>

<html>
<head>
    <title>Game Session</title>
    <link rel="stylesheet" type="text/css" href="css/style.css">
</head>
<body>
<h1>Your game</h1>

<?=lobby.name ?>

</body>
</html>
