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
<table class="center">
    <tr>
        <th><button onclick="location.href='/offer/new/0'"  type="button">LEO</button></th>
        <th><button onclick="location.href='/offer/new/1'"  type="button">TRUCIDO</button></th>
    </tr>
    <tr>
        <th><button onclick="location.href='/offer/new/2'"  type="button">FULVIO</button></th>
        <th><button onclick="location.href='/offer/new/3'"  type="button">MAZZA</button></th>
    </tr>
    <tr>
        <th><button onclick="location.href='/offer/new/4'"  type="button">MAGNA</button></th>
        <th><button onclick="location.href='/offer/new/5'"  type="button">REGINA</button></th>
    </tr>
    <tr>
        <th><button onclick="location.href='/offer/new/6'"  type="button">MIMI</button></th>
        <th><button onclick="location.href='/offer/new/7'"  type="button">MIRCO</button></th>
    </tr>
</table>
</body>
</html>