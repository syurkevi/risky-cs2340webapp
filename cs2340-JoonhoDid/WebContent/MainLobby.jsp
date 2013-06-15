<%@ page import = "edu.gatech.cs2340.lobby.model.GameLobby" %>
<%@ page import = "edu.gatech.cs2340.player.model.Player" %>
<%@ page import = "java.util.*"%>

<% TreeMap<Integer, GameLobby> gamelobbies = 
	(TreeMap<Integer,GameLobby>) request.getAttribute("gamelobbies");%>

<html>
	<head>
	<title><h3>Main Lobby</h3></title>
	</head>
<body>
	<h1>Main Lobby</h1>
	
	<!-- Creates a new game -->
	<form action="/cs2340-team6">
	<td><input type="submit" value="New Game" ></td>
	
	</form>
	
	<!-- Table list of games -->
	<table>
	<tr>
	<th>Game Lobby</th><th>Players</th>
	</tr>
	<!--  
		<% for(Integer id: gamelobbies.keySet()) { %>
		<% GameLobby gamelobby = gamelobbies.get(id); %>
		<tr>
		<form>
		
		
		</form>
		
		
		</tr>		
		
		<%}%>
	-->
	</table>

</body>
</html>