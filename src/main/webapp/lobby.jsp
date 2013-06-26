<html>
<head>
    <link rel="stylesheet" type="text/css" href="/risky/css/bootstrap.css">
    <link rel="stylesheet" type="text/css" href="/risky/css/style.css">
    <title>Risky Web App</title>
</head>

<body>
    <h1><%=lobby.name %></h1>

    <hr></hr>

    <h3>Players</h3>
    <p><% for (int i=0 ; i < lobby.players.size() ; i++) { %><div>
        <% Player player = lobby.players.get(i); %>
        <form action="/risky/lobby/player/" method="post">
            <input type="text" name="player_name" value="<%=player.name %>" />
            <input type="hidden" name="operation" value="update">
            <input type="hidden" name="player_id" value="<%=i %>">
            <submit><i class="icon-edit"></i></submit>
        </form>
        <form action="/risky/lobby/player/" method="post">
            <input type="hidden" name="operation" value="delete">
            <input type="hidden" name="player_id" value="<%=i %>">
            <submit><i class="icon-remove"></i></submit>
        </form>
    </div><% } %></p>
    <div>
        <form action="/risky/lobby/player/add" method="post">
            <div class="input-append">
                <input type="text" name="playerName" />
                <input type="submit" class="btn btn-success" value="add player" />
            </div>
        </form>
    </div>

    <hr></hr>

    <h3>Get ready to rumble!</h3>
    <div><% if (!lobby.hasEnoughPlayers()) { %>Not yet though, <span class="badge badge-important">3</span> player minimum<% }
    else if (lobby.hasTooManyPlayer()) { %>Woah there, <span class="badge badge-important">6</span> player maximum<% }
    else { %>
        <a class="btn btn-primary" href="/risky/game/">Start Match</a>
        <small><% if (lobby.players.size() == lobby.MAX_PLAYERS) { %>No more players<% }
        else { %>Up to <span class="badge badge-info"><%=lobby.MAX_PLAYERS - lobby.players.size() %></span> more players</small><% } %>
    </div><% } %>
</body>
</html>
