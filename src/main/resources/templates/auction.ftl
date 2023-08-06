<#-- @ftlvariable name="auction" type="com.gaglileo.models.Auction" -->
<!DOCTYPE html>
<html lang="it">
<head>
    <title>Asta</title>
</head>
<body style="text-align: center; font-family: sans-serif">
<div>
    <h1>Asta per ${auction.footballer}</h1>
    <#list auction.getOffersList()?reverse as offer>
        <div>
            <h3>
                ${offer.player.name} - ${offer.amount}
            </h3>
        </div>
    </#list>
</div>
</body>
</html>