<#-- @ftlvariable name="currentAuction" type="com.gaglileo.models.Auction" -->
<#-- @ftlvariable name="playerId" type="int" -->
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
        .form-container {
            max-width: 400px;
            margin: 0 auto;
            padding: 20px;
            border: 1px solid #ddd;
            border-radius: 8px;
            background-color: #f9f9f9;
        }
        .form-group {
            margin-bottom: 20px;
        }
        label {
            display: block;
            margin-bottom: 5px;
            font-weight: bold;
        }
        input[type="number"] {
            width: 100%;
            padding: 10px;
            border: 1px solid #ddd;
            border-radius: 4px;
            font-size: 16px;
        }
        .submit-btn {
            background-color: #28a745;
            color: white;
            padding: 12px 24px;
            border: none;
            border-radius: 4px;
            cursor: pointer;
            font-size: 16px;
            width: 100%;
        }
        .submit-btn:hover {
            background-color: #218838;
        }
        .back-btn {
            background-color: #6c757d;
            color: white;
            padding: 8px 16px;
            border: none;
            border-radius: 4px;
            text-decoration: none;
            font-size: 14px;
            margin-top: 10px;
            display: inline-block;
        }
        .back-btn:hover {
            background-color: #5a6268;
        }
    </style>
    <title>Offerta - Asta FantaSaracena</title>
</head>
<body>
    <div class="header">
        <h3>Asta FantaSaracena</h3>
        <div class="user-info">
            <p>Benvenuto, <strong>${currentUser.playerName}</strong>!</p>
            <a href="/logout" class="logout-btn">Logout</a>
        </div>
    </div>
    
    <div class="form-container">
        <h1>Offerta per ${currentAuction.footballer}</h1>
        <p>Giocatore: <strong>${currentUser.playerName}</strong></p>
        
        <form action="/offer/${playerId}" method="post">
            <div class="form-group">
                <label for="amount">Inserisci la tua offerta:</label>
                <input type="number" id="amount" name="amount" min="1" required>
            </div>
            <button type="submit" class="submit-btn">Invia Offerta</button>
        </form>
        
        <a href="/" class="back-btn">Torna alla Home</a>
    </div>
</body>
</html>