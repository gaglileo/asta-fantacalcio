<#-- @ftlvariable name="auction" type="com.gaglileo.models.Auction" -->
<#-- @ftlvariable name="currentUser" type="com.gaglileo.models.UserSession" -->
<!DOCTYPE html>
<html lang="it">
<head>
    <style>
        body {
            font-family: sans-serif;
            text-align: center;
            padding: 20px;
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
        .auction-container {
            max-width: 600px;
            margin: 0 auto;
            padding: 20px;
            border: 1px solid #ddd;
            border-radius: 8px;
            background-color: #f9f9f9;
        }
        .offer-item {
            padding: 15px;
            margin: 10px 0;
            background-color: white;
            border-radius: 4px;
            border-left: 4px solid #007bff;
        }
        .back-btn {
            background-color: #6c757d;
            color: white;
            padding: 8px 16px;
            border: none;
            border-radius: 4px;
            text-decoration: none;
            font-size: 14px;
            margin-top: 20px;
            display: inline-block;
        }
        .back-btn:hover {
            background-color: #5a6268;
        }
    </style>
    <title>Asta - Asta FantaSaracena</title>
</head>
<body>
    <div class="header">
        <h3>Asta FantaSaracena</h3>
        <div class="user-info">
            <p>Benvenuto, <strong>${currentUser.playerName}</strong>!</p>
            <a href="/logout" class="logout-btn">Logout</a>
        </div>
    </div>
    
    <div class="auction-container">
        <h1>Asta per ${auction.footballer}</h1>
        
        <#if auction.isCompleted>
            <#if auction.winner??>
                <h2 style="color: #28a745;">Vincitore: ${auction.winner.player.name} - ${auction.winner.amount}</h2>
            <#else>
                <h2 style="color: #ffc107;">Asta da ripetere (pareggio)</h2>
            </#if>
        <#else>
            <h2 style="color: #007bff;">Asta in corso</h2>
        </#if>
        
        <h3>Offerte ricevute:</h3>
        <#list auction.getOffersList()?reverse as offer>
            <div class="offer-item">
                <h4>
                    ${offer.player.name} - ${offer.amount}
                    <#if offer.player.id == currentUser.playerId>
                        <span style="color: #007bff;">(Tu)</span>
                    </#if>
                </h4>
            </div>
        </#list>
        
        <a href="/" class="back-btn">Torna alla Home</a>
    </div>
</body>
</html>