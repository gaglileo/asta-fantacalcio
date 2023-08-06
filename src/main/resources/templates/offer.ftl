<#-- @ftlvariable name="currentAuction" type="com.gaglileo.models.Auction" -->
<#-- @ftlvariable name="playerId" type="int" -->
<!DOCTYPE html>
<html lang="it">
<head>
    <title>Offerta</title>
</head>
<body style="text-align: center; font-family: sans-serif">
<div>
    <h1>Offerta per ${currentAuction.footballer}</h1>
    <form action="/offer/${playerId}" method="post">
        <p>
            <label title="offerta">
                <input type="number" name="amount">
            </label>
        </p>
        <p>
            <input type="submit">
        </p>
    </form>
</div>
</body>
</html>