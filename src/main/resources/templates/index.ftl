<#-- @ftlvariable name="auctions" type="kotlin.collections.List<com.gaglileo.models.Auction>" -->
<#-- @ftlvariable name="players" type="kotlin.collections.List<com.gaglileo.models.Player>" -->
<#-- @ftlvariable name="currentAuction" type="com.gaglileo.models.Auction" -->
<#-- @ftlvariable name="currentUser" type="com.gaglileo.models.UserSession" -->
<!DOCTYPE html>
<html lang="it">
<head>
    <style>
        th, td {
            padding: 30px;
        }
        table.center {
            margin-left: auto;
            margin-right: auto;
        }
        .header {
            display: flex;
            justify-content: space-between;
            align-items: center;
            padding: 20px;
            background-color: #f8f9fa;
            margin-bottom: 20px;
        }
        .user-info {
            text-align: right;
        }
        .logout-btn {
            background-color: #dc3545;
            color: white;
            padding: 8px 16px;
            border: none;
            border-radius: 4px;
            text-decoration: none;
            font-size: 14px;
        }
        .logout-btn:hover {
            background-color: #c82333;
        }
        .bid-btn {
            background-color: #28a745;
            color: white;
            padding: 10px 20px;
            border: none;
            border-radius: 4px;
            text-decoration: none;
            font-size: 16px;
            margin: 10px;
        }
        .bid-btn:hover {
            background-color: #218838;
        }
        .btn {
            background-color: #007bff;
            color: white;
            padding: 8px 16px;
            border: none;
            border-radius: 4px;
            text-decoration: none;
            font-size: 14px;
        }
        .btn-primary {
            background-color: #007bff;
        }
        .btn:hover {
            opacity: 0.8;
        }
        .players-row {
            display: flex;
            flex-wrap: wrap;
            justify-content: center;
            gap: 15px;
            margin: 20px 0;
        }
        .player-card {
            background: #f8f9fa;
            border: 1px solid #dee2e6;
            border-radius: 8px;
            padding: 10px 15px;
            min-width: 120px;
            text-align: center;
            box-shadow: 0 2px 4px rgba(0,0,0,0.1);
        }
        .player-card.current-user {
            background: #e3f2fd;
            border-color: #2196f3;
        }
        .player-name {
            font-weight: bold;
            margin-bottom: 5px;
            color: #333;
        }
        .player-money {
            font-size: 14px;
            color: #666;
        }
        .current-user-badge {
            background: #2196f3;
            color: white;
            padding: 2px 6px;
            border-radius: 10px;
            font-size: 10px;
            margin-top: 5px;
            display: inline-block;
        }
        .auctions-row {
            display: flex;
            flex-wrap: wrap;
            justify-content: center;
            gap: 15px;
            margin: 20px 0;
        }
        .auction-card {
            background: #fff3cd;
            border: 1px solid #ffeaa7;
            border-radius: 8px;
            padding: 15px 20px;
            min-width: 150px;
            text-align: center;
            box-shadow: 0 2px 4px rgba(0,0,0,0.1);
            text-decoration: none;
            color: #333;
            transition: transform 0.2s, box-shadow 0.2s;
        }
        .auction-card:hover {
            transform: translateY(-2px);
            box-shadow: 0 4px 8px rgba(0,0,0,0.15);
            text-decoration: none;
            color: #333;
        }
        .auction-id {
            font-size: 12px;
            color: #666;
            margin-bottom: 5px;
        }
        .auction-footballer {
            font-weight: bold;
            font-size: 16px;
            color: #333;
        }
    </style>
    <title>Asta FantaSaracena</title>
    <meta http-equiv="refresh" content="5">
</head>
<body style="text-align: center; font-family: sans-serif">
    <div class="header">
        <h3>Asta FantaSaracena</h3>
        <div class="user-info">
            <p>Benvenuto, <strong>${currentUser.playerName}</strong>!</p>
            <#if currentUser.playerId == 0>
                <a href="/admin" class="btn btn-primary" style="margin-right: 10px;">🔧 Admin Panel</a>
            </#if>
            <a href="/logout" class="logout-btn">Logout</a>
        </div>
    </div>
    
    <hr>
    
    <div class="players-row">
        <#list players?reverse as player>
            <div class="player-card <#if player.id == currentUser.playerId>current-user</#if>">
                <div class="player-name">${player.name}</div>
                <div class="player-money">${player.money}€</div>
                <#if player.id == currentUser.playerId>
                    <div class="current-user-badge">Tu</div>
                </#if>
            </div>
        </#list>
    </div>
    
    <hr>
    
    <h2>Lista Aste</h2>
    <#if auctions?? && auctions?size gt 0>
        <div class="auctions-row">
            <#list auctions as auction>
                <a href="/auctions/${auction.id}" class="auction-card">
                    <div class="auction-id">ID: ${auction.id}</div>
                    <div class="auction-footballer">${auction.footballer}</div>
                </a>
            </#list>
        </div>
    <#else>
        <p>Nessuna asta disponibile</p>
    </#if>
    
    <hr>
    
    <h2>Asta Corrente</h2>
    <#if currentAuction??>
        <h1>${currentAuction.footballer}</h1>
        <p>ID: ${currentAuction.id}</p>
        <p>Completata: ${currentAuction.isNotCompleted()?string("No", "Si")}</p>
        
        <#if currentAuction.isNotCompleted()>
            <a href="/offer/new/${currentUser.playerId}" class="bid-btn">Fai un'offerta</a>
        </#if>
    <#else>
        <h1>In attesa di chiamata</h1>
    </#if>

</body>
</html>