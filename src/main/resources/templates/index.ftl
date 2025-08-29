<#-- @ftlvariable name="auctions" type="kotlin.collections.List<com.gaglileo.models.Auction>" -->
<#-- @ftlvariable name="players" type="kotlin.collections.List<com.gaglileo.models.Player>" -->
<#-- @ftlvariable name="currentAuction" type="com.gaglileo.models.Auction" -->
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
    </style>
    <title>Asta FantaSaracena</title>
    <meta http-equiv="refresh" content="5">
</head>
<body style="text-align: center; font-family: sans-serif">
<h3>Asta FantaSaracena</h3>
<hr>
<#list players?reverse as player>
    <div>
        <h5>
            ${player.name} - ${player.money}
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
        <#list (currentAuction.getOffersPlayerList()) as player>
            <div>
                <h4>
                    ${player.name} ha offerto
                </h4>
            </div>
        </#list>
    <#else>
        <h1>In attesa di chiamata</h1>
    </#if>
</div>

</body>
</html>