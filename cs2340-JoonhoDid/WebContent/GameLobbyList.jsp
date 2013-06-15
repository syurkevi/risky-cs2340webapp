<%@ page import = "edu.gatech.cs2340.lobby.model.GameLobby" %>
<%@ page import = "edu.gatech.cs2340.player.model.Player" %>
<%@ page import = "java.util.*"%>

<% GameLobby gameLobby = (GameLobby) request.getAttribute("gameLobby"); %>
<html>
	<head>
	<title>GameLobby</title>
	</head>

	<body>
	<div><h2>Game Lobby</h2></div>
	
	<!-- Creating Player -->
	<div>
		<%if(gameLobby.getNumPlayers()<6) { %>
		<form action="/cs2340-team6/GameLobbyList/create" method="POST">
			<input type="text" name="newPlayer"/>
			<input type="submit" value="Add Player"/>  
		</form>
		<% } else {%>
		Max Players!
		<% } %>
		
		<%if(gameLobby.canStart()) {%>
		<form action="/cs2340-team6/Game" method="POST">
			<input type="submit" name="start" value="Start"/>
		</form>
		<% } %>
	</div>
	<table>	

	<tr>
	<th>Players</th>
	</tr>

	<% for (Integer id: gameLobby.getPlayers().keySet()) { %>
	<% Player player = gameLobby.getPlayer(id.intValue()); %>
	<tr>
		<td><%=player.getPlayerName()%></td>
		<td>
		
		<!-- Removing Player -->
		<form action="/cs2340-team6/GameLobbyList/delete/" method="POST">
			<input type="hidden" name="operation" value= "DELETE"/>
			<input type="hidden" name="removed" value= "<%=player.getPlayerId()%>"/>
			<input type="submit" name="<%=player.getPlayerId()%>" value="Remove"/>
		</form>	
		</td>
	</tr>
	<% } %>
	</table>

	</body>
</html>