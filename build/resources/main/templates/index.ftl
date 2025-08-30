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
    </style>
    <title>Asta FantaSaracena</title>
    <meta http-equiv="refresh" content="5">
</head>
<body style="text-align: center; font-family: sans-serif">
    <div class="header">
        <h3>Asta FantaSaracena</h3>
        <div class="user-info">
            <p>Benvenuto, <strong>${currentUser.playerName}</strong>!</p>
            <a href="/logout" class="logout-btn">Logout</a>
        </div>
    </div>
    
    <hr>
    
    <#list players?reverse as player>
        <div>
            <h5>
                ${player.name} - ${player.money}
                <#if player.id == currentUser.playerId>
                    <span style="color: #007bff;">(Tu)</span>
                </#if>
            </h5>
        </div>
    </#list>
    
    <hr>
    
    <#list auctions?reverse as auction>
        <div>
            <a href="/auctions/${(auction.id)}">
                <h3>
                ${auction.id} - ${auction.footballer} - ${auction.winner.player.name} - ${auction.winner.amount}
            </h3>
            </a>
        </div>
    </#list>
    
    <hr>
    
    <div>
        <#if currentAuction??>
            <h1>${(currentAuction.footballer)}</h1>
            <h3>Offerte: ${currentAuction.getOffersNumber()}</h3>
            
            <#if !currentAuction.isCompleted && currentAuction.getOffersPlayerList()?seq_contains(players?filter(p -> p.id == currentUser.playerId)[0])>
                <p style="color: #28a745;">Hai già fatto un'offerta!</p>
            <#elseif !currentAuction.isCompleted>
                <a href="/offer/new/${currentUser.playerId}" class="bid-btn">Fai un'offerta</a>
            </#if>
            
            <#list (currentAuction.getOffersPlayerList()) as player>
                <div>
                    <h4>
                        ${player.name} ha offerto
                        <#if player.id == currentUser.playerId>
                            <span style="color: #007bff;">(Tu)</span>
                        </#if>
                    </h4>
                </div>
            </#list>
        <#else>
            <h1>In attesa di chiamata</h1>
        </#if>
    </div>

</body>
</html>