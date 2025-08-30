<!DOCTYPE html>
<html lang="it">
<head>
    <style>
        body {
            font-family: sans-serif;
            display: flex;
            justify-content: center;
            align-items: center;
            height: 100vh;
            margin: 0;
            background-color: #f5f5f5;
        }
        .login-container {
            background: white;
            padding: 40px;
            border-radius: 8px;
            box-shadow: 0 2px 10px rgba(0,0,0,0.1);
            text-align: center;
        }
        .form-group {
            margin-bottom: 20px;
        }
        label {
            display: block;
            margin-bottom: 5px;
            font-weight: bold;
        }
        select {
            width: 100%;
            padding: 10px;
            border: 1px solid #ddd;
            border-radius: 4px;
            font-size: 16px;
        }
        button {
            background-color: #007bff;
            color: white;
            padding: 12px 24px;
            border: none;
            border-radius: 4px;
            cursor: pointer;
            font-size: 16px;
        }
        button:hover {
            background-color: #0056b3;
        }
        .logo {
            max-width: 200px;
            margin-bottom: 20px;
        }
    </style>
    <title>Login - Asta FantaSaracena</title>
</head>
<body>
    <div class="login-container">
        <img src="/static/saracena_logo.png" alt="Logo" class="logo">
        <h2>Accedi all'Asta</h2>
        <form method="post" action="/login">
            <div class="form-group">
                <label for="playerId">Seleziona il tuo nome:</label>
                <select name="playerId" id="playerId" required>
                    <option value="">-- Scegli un giocatore --</option>
                    <#list players as player>
                        <option value="${player.id}">${player.name} (${player.money} crediti)</option>
                    </#list>
                </select>
            </div>
            <button type="submit">Accedi</button>
        </form>
    </div>
</body>
</html>
