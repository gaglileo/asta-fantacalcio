<#-- @ftlvariable name="currentUser" type="com.gaglileo.models.UserSession" -->
<#-- @ftlvariable name="currentAuction" type="com.gaglileo.models.Auction" -->
<#-- @ftlvariable name="auctions" type="kotlin.collections.List<com.gaglileo.models.Auction>" -->
<#-- @ftlvariable name="players" type="kotlin.collections.List<com.gaglileo.models.Player>" -->
<#-- @ftlvariable name="auctionStatus" type="java.lang.String" -->
<!DOCTYPE html>
<html lang="it">
<head>
    <title>Admin Panel - Asta FantaSaracena</title>
    <style>
        body { font-family: sans-serif; margin: 20px; }
        .section { background: #f9f9f9; padding: 20px; margin: 20px 0; border-radius: 8px; }
        .btn { padding: 10px 20px; margin: 5px; border: none; border-radius: 4px; cursor: pointer; }
        .btn-primary { background: #007bff; color: white; }
        .btn-success { background: #28a745; color: white; }
        .btn-warning { background: #ffc107; color: black; }
        .btn-danger { background: #dc3545; color: white; }
        .player-card { background: white; padding: 15px; margin: 10px 0; border-radius: 4px; }
        .auction-card { background: white; padding: 15px; margin: 10px 0; border-radius: 4px; }
    </style>
</head>
<body>
    <h1>🔧 Admin Panel</h1>
    <p>Benvenuto, <strong>${currentUser.playerName}</strong>!</p>
    
    <div class="section">
        <h2>📊 Stato Asta Corrente</h2>
        <p><strong>Status:</strong> ${auctionStatus}</p>
        
        <#if currentAuction??>
            <div class="auction-card">
                <h3>Asta per: ${currentAuction.footballer}</h3>
                <p><strong>ID Asta:</strong> ${currentAuction.id}</p>
                
                <form method="post" action="/admin/assign-winner" style="display: inline;">
                    <button type="submit" class="btn btn-success">🏆 Assegna Vincitore</button>
                </form>
                
                <form method="post" action="/admin/reset-auction/${currentAuction.id}" style="display: inline;">
                    <button type="submit" class="btn btn-warning">🔄 Reset Asta</button>
                </form>
            </div>
        <#else>
            <p>Nessuna asta in corso.</p>
        </#if>
    </div>
    
    <div class="section">
        <h2>🚀 Avvia Nuova Asta</h2>
        <form method="post" action="/admin/start-auction">
            <label for="footballer">Nome Calciatore:</label>
            <input type="text" id="footballer" name="footballer" required style="padding: 8px; margin: 10px;">
            <button type="submit" class="btn btn-primary">Avvia Asta</button>
        </form>
    </div>
    
    <div class="section">
        <h2>👥 Gestione Giocatori</h2>
        <#list players as player>
            <div class="player-card">
                <h4>${player.name}</h4>
                <p><strong>Crediti:</strong> ${player.money}</p>
                <p><strong>ID:</strong> ${player.id}</p>
                
                <form method="post" action="/admin/add-money/${player.id}" style="display: inline;">
                    <input type="number" name="amount" placeholder="+" style="width: 80px; padding: 5px;">
                    <button type="submit" class="btn btn-success">+</button>
                </form>
                
                <form method="post" action="/admin/subtract-money/${player.id}" style="display: inline;">
                    <input type="number" name="amount" placeholder="-" style="width: 80px; padding: 5px;">
                    <button type="submit" class="btn btn-danger">-</button>
                </form>
            </div>
        </#list>
    </div>
    
    <div class="section">
        <h2>📜 Cronologia Aste</h2>
        <#if auctions.isEmpty()>
            <p>Nessuna asta completata.</p>
        <#else>
            <#list auctions as auction>
                <div class="auction-card">
                    <h4>Asta #${auction.id}</h4>
                    <p><strong>Calciatore:</strong> ${auction.footballer}</p>
                    <p><strong>Stato:</strong> 
                        <#if auction.isCompleted>
                            Completata
                        <#elseif auction.isToRepeat>
                            Da ripetere
                        <#else>
                            In corso
                        </#if>
                    </p>
                    
                    <#if !auction.isCompleted && !auction.isToRepeat>
                        <form method="post" action="/admin/force-complete/${auction.id}" style="display: inline;">
                            <button type="submit" class="btn btn-warning">Forza Completamento</button>
                        </form>
                    </#if>
                    
                    <form method="post" action="/admin/reset-auction/${auction.id}" style="display: inline;">
                        <button type="submit" class="btn btn-primary">Reset</button>
                    </form>
                    
                    <form method="post" action="/admin/auction/${auction.id}" style="display: inline;">
                        <button type="submit" class="btn btn-danger" onclick="return confirm('Eliminare questa asta?')">Elimina</button>
                    </form>
                </div>
            </#list>
        </#if>
    </div>
    
    <p><a href="/">🏠 Torna alla Home</a> | <a href="/logout">Logout</a></p>
</body>
</html>
