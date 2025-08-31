package com.gaglileo.plugins

import com.gaglileo.models.Auction
import com.gaglileo.models.Offer
import com.gaglileo.models.UserSession
import com.gaglileo.models.fantaSession
import com.gaglileo.models.players
import io.ktor.http.*
import io.ktor.server.routing.*
import io.ktor.server.application.*
import io.ktor.server.freemarker.*
import io.ktor.server.http.content.*
import io.ktor.server.request.*
import io.ktor.server.response.*
import io.ktor.server.sessions.*
import io.ktor.server.util.*

fun Application.configureRouting() {

    routing {
        static("/static") {
            resources("files")
        }
        
        // Login routes (public)
        get("/login") {
            call.respond(
                FreeMarkerContent(
                    "login.ftl",
                    mapOf("players" to players)
                )
            )
        }
        
        post("/login") {
            val formParameters = call.receiveParameters()
            val playerId = formParameters.getOrFail<Int>("playerId")
            val player = players.firstOrNull { it.id == playerId }
            
            if (player != null) {
                call.sessions.set(UserSession(player.id, player.name))
                call.respondRedirect("/")
            } else {
                call.respondRedirect("/login")
            }
        }
        
        get("/logout") {
            call.sessions.clear<UserSession>()
            call.respondRedirect("/login")
        }
        
        // Protected routes - require authentication
        get("/") {
            val session = call.sessions.get<UserSession>()
            if (session == null) {
                call.respondRedirect("/login")
                return@get
            }
            call.respondRedirect("/auctions")
        }
        
        get("/test") {
            val session = call.sessions.get<UserSession>()
            if (session == null) {
                call.respondRedirect("/login")
                return@get
            }
            
            // Test with minimal HTML
            call.respondText("<h1>Test Page</h1><p>User: ${session.playerName}</p>", ContentType.Text.Html)
        }
        
        route("auctions") {
            get {
                val session = call.sessions.get<UserSession>()
                if (session == null) {
                    call.respondRedirect("/login")
                    return@get
                }
                
                call.respond(
                    FreeMarkerContent(
                        "index.ftl",
                        mapOf(
                            "auctions" to fantaSession.auctions,
                            "players" to fantaSession.players,
                            "currentAuction" to fantaSession.currentAuction,
                            "currentUser" to session
                        )
                    )
                )
            }
            
            get("{id}") {
                val session = call.sessions.get<UserSession>()
                if (session == null) {
                    call.respondRedirect("/login")
                    return@get
                }
                
                val auctionId = call.parameters["id"]?.toInt()!!
                val auction = fantaSession.getAuction(auctionId)
                call.respond(
                    FreeMarkerContent(
                        "auction.ftl",
                        mapOf(
                            "auction" to auction,
                            "currentUser" to session
                        )
                    )
                )
            }
            
            post {
                val session = call.sessions.get<UserSession>()
                if (session == null) {
                    call.respond(HttpStatusCode.Unauthorized, "Not authenticated")
                    return@post
                }
                
                val footballer = call.receive<String>()
                fantaSession.addAuction(Auction.newEntry(footballer))
                call.respondText("Auction started", status = HttpStatusCode.Created)
            }
            
            post("assign") {
                val session = call.sessions.get<UserSession>()
                if (session == null) {
                    call.respond(HttpStatusCode.Unauthorized, "Not authenticated")
                    return@post
                }
                
                fantaSession.currentAuction?.checkWinner()
                call.respondText("Auction assigned", status = HttpStatusCode.Created)
            }
            
            delete("{id}") {
                val session = call.sessions.get<UserSession>()
                if (session == null) {
                    call.respond(HttpStatusCode.Unauthorized, "Not authenticated")
                    return@delete
                }
                
                val auctionId = call.parameters["id"]?.toInt()!!
                fantaSession.cancelAuction(auctionId)
                call.respondText("Auction canceled", status = HttpStatusCode.Accepted)
            }
        }
        
        route("offer") {
            get("new/{playerId}") {
                val session = call.sessions.get<UserSession>()
                if (session == null) {
                    call.respondRedirect("/login")
                    return@get
                }
                
                val playerId = call.parameters["playerId"]?.toInt()!!
                
                // Security check: user can only bid for themselves
                if (session.playerId != playerId) {
                    call.respond(HttpStatusCode.Forbidden, "You can only bid for yourself")
                    return@get
                }
                
                call.respond(
                    FreeMarkerContent(
                        "offer.ftl", 
                        mapOf(
                            "currentAuction" to fantaSession.currentAuction,
                            "playerId" to playerId,
                            "currentUser" to session
                        )
                    )
                )
            }
            
            post("{playerId}") {
                val session = call.sessions.get<UserSession>()
                if (session == null) {
                    call.respond(HttpStatusCode.Unauthorized, "Not authenticated")
                    return@post
                }
                
                val playerId = call.parameters["playerId"]?.toInt()!!
                
                // Security check: user can only bid for themselves
                if (session.playerId != playerId) {
                    call.respond(HttpStatusCode.Forbidden, "You can only bid for yourself")
                    return@post
                }
                
                val formParameters = call.receiveParameters()
                val amount = formParameters.getOrFail<Int>("amount")
                val newOffer = Offer.newEntry(amount, players[playerId])
                fantaSession.currentAuction?.addOffer(newOffer.player, newOffer)
                call.respondRedirect("/")
            }
        }
        
        route("player") {
            post("add/{playerId}/{amount}") {
                val session = call.sessions.get<UserSession>()
                if (session == null) {
                    call.respond(HttpStatusCode.Unauthorized, "Not authenticated")
                    return@post
                }
                
                val playerId = call.parameters["playerId"]?.toInt()!!
                val amount = call.parameters["amount"]?.toInt()!!
                
                // Security check: user can only modify their own money
                if (session.playerId != playerId) {
                    call.respond(HttpStatusCode.Forbidden, "You can only modify your own account")
                    return@post
                }
                
                players.first { it.id == playerId }.apply {
                    money += amount
                }
                call.respondText("Money added", status = HttpStatusCode.OK)
            }
            
            post("sub/{playerId}/{amount}") {
                val session = call.sessions.get<UserSession>()
                if (session == null) {
                    call.respond(HttpStatusCode.Unauthorized, "Not authenticated")
                    return@post
                }
                
                val playerId = call.parameters["playerId"]?.toInt()!!
                val amount = call.parameters["amount"]?.toInt()!!
                
                // Security check: user can only modify their own money
                if (session.playerId != playerId) {
                    call.respond(HttpStatusCode.Forbidden, "You can only modify your own account")
                    return@post
                }
                
                players.first { it.id == playerId }.apply {
                    money -= amount
                }
                call.respondText("Money subtracted", status = HttpStatusCode.OK)
            }
        }
        
        // Admin routes - only accessible by Leo (player ID 0)
        route("admin") {
            get {
                val session = call.sessions.get<UserSession>()
                if (session == null) {
                    call.respondRedirect("/login")
                    return@get
                }
                
                if (!fantaSession.isAdmin(session.playerId)) {
                    call.respond(HttpStatusCode.Forbidden, "Access denied. Admin only.")
                    return@get
                }
                
                val currentAuction = fantaSession.currentAuction
                val auctionStatus = fantaSession.getCurrentAuctionStatus()
                val players = fantaSession.players
                val auctions = fantaSession.auctions
                
                val html = """
                    <!DOCTYPE html>
                    <html lang="it">
                    <head>
                        <title>Admin Panel - Asta FantaSaracena</title>
                        <style>
                            body { font-family: sans-serif; margin: 20px; background: #f5f5f5; }
                            .container { max-width: 1200px; margin: 0 auto; }
                            .section { background: white; padding: 20px; margin: 20px 0; border-radius: 8px; box-shadow: 0 2px 4px rgba(0,0,0,0.1); }
                            .header { background: #343a40; color: white; padding: 20px; border-radius: 8px; margin-bottom: 20px; }
                            .btn { padding: 10px 20px; margin: 5px; border: none; border-radius: 4px; cursor: pointer; text-decoration: none; display: inline-block; }
                            .btn-primary { background: #007bff; color: white; }
                            .btn-success { background: #28a745; color: white; }
                            .btn-warning { background: #ffc107; color: black; }
                            .btn-danger { background: #dc3545; color: white; }
                            .player-card { background: #f8f9fa; padding: 15px; margin: 10px 0; border-radius: 4px; border-left: 4px solid #007bff; }
                            .auction-card { background: #f8f9fa; padding: 15px; margin: 10px 0; border-radius: 4px; border-left: 4px solid #28a745; }
                            .grid { display: grid; grid-template-columns: repeat(auto-fit, minmax(300px, 1fr)); gap: 15px; margin-top: 15px; }
                            input[type="text"], input[type="number"] { padding: 8px; margin: 5px; border: 1px solid #ddd; border-radius: 4px; }
                        </style>
                    </head>
                    <body>
                        <div class="container">
                            <div class="header">
                                <h1>🔧 Admin Panel</h1>
                                <p>Benvenuto, <strong>${session.playerName}</strong>!</p>
                            </div>
                            
                            <div class="section">
                                <h2>📊 Stato Asta Corrente</h2>
                                <p><strong>Status:</strong> $auctionStatus</p>
                                
                                ${if (currentAuction != null) """
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
                                """ else "<p>Nessuna asta in corso.</p>"}
                            </div>
                            
                            <div class="section">
                                <h2>🚀 Avvia Nuova Asta</h2>
                                <form method="post" action="/admin/start-auction">
                                    <label for="footballer">Nome Calciatore:</label>
                                    <input type="text" id="footballer" name="footballer" required>
                                    <button type="submit" class="btn btn-primary">Avvia Asta</button>
                                </form>
                            </div>
                            
                            <div class="section">
                                <h2>👥 Gestione Giocatori</h2>
                                <div class="grid">
                                    ${players.joinToString("") { player ->
                                        """
                                        <div class="player-card">
                                            <h4>${player.name}</h4>
                                            <p><strong>Crediti:</strong> ${player.money}</p>
                                            <p><strong>ID:</strong> ${player.id}</p>
                                            
                                            <form method="post" action="/admin/add-money/${player.id}" style="display: inline;">
                                                <input type="number" name="amount" placeholder="+" style="width: 80px;">
                                                <button type="submit" class="btn btn-success">+</button>
                                            </form>
                                            
                                            <form method="post" action="/admin/subtract-money/${player.id}" style="display: inline;">
                                                <input type="number" name="amount" placeholder="-" style="width: 80px;">
                                                <button type="submit" class="btn btn-danger">-</button>
                                            </form>
                                        </div>
                                        """
                                    }}
                                </div>
                            </div>
                            
                            <div class="section">
                                <h2>📜 Cronologia Aste</h2>
                                ${if (auctions.isEmpty()) "<p>Nessuna asta completata.</p>" else auctions.joinToString("") { auction ->
                                    """
                                    <div class="auction-card">
                                        <h4>Asta #${auction.id}</h4>
                                        <p><strong>Calciatore:</strong> ${auction.footballer}</p>
                                        <p><strong>Stato:</strong> ${
                                            when {
                                                auction.isCompleted -> "Completata"
                                                auction.isToRepeat -> "Da ripetere"
                                                else -> "In corso"
                                            }
                                        }</p>
                                        
                                        ${if (!auction.isCompleted && !auction.isToRepeat) """
                                            <form method="post" action="/admin/force-complete/${auction.id}" style="display: inline;">
                                                <button type="submit" class="btn btn-warning">Forza Completamento</button>
                                            </form>
                                        """ else ""}
                                        
                                        <form method="post" action="/admin/reset-auction/${auction.id}" style="display: inline;">
                                            <button type="submit" class="btn btn-primary">Reset</button>
                                        </form>
                                        
                                        <form method="post" action="/admin/auction/${auction.id}" style="display: inline;">
                                            <button type="submit" class="btn btn-danger" onclick="return confirm('Eliminare questa asta?')">Elimina</button>
                                        </form>
                                    </div>
                                    """
                                }}
                            </div>
                            
                            <p style="text-align: center; margin-top: 30px;">
                                <a href="/" class="btn btn-primary">🏠 Torna alla Home</a>
                                <a href="/logout" class="btn btn-danger">Logout</a>
                            </p>
                        </div>
                    </body>
                    </html>
                """.trimIndent()
                
                call.respondText(html, ContentType.Text.Html)
            }
            
            post("start-auction") {
                val session = call.sessions.get<UserSession>()
                if (session == null) {
                    call.respond(HttpStatusCode.Unauthorized, "Not authenticated")
                    return@post
                }
                
                if (!fantaSession.isAdmin(session.playerId)) {
                    call.respond(HttpStatusCode.Forbidden, "Access denied. Admin only.")
                    return@post
                }
                
                val footballer = call.receive<String>()
                fantaSession.addAuction(Auction.newEntry(footballer))
                call.respondRedirect("/admin")
            }
            
            post("assign-winner") {
                val session = call.sessions.get<UserSession>()
                if (session == null) {
                    call.respond(HttpStatusCode.Unauthorized, "Not authenticated")
                    return@post
                }
                
                if (!fantaSession.isAdmin(session.playerId)) {
                    call.respond(HttpStatusCode.Forbidden, "Access denied. Admin only.")
                    return@post
                }
                
                fantaSession.currentAuction?.checkWinner()
                call.respondRedirect("/admin")
            }
            
            post("force-complete/{auctionId}") {
                val session = call.sessions.get<UserSession>()
                if (session == null) {
                    call.respond(HttpStatusCode.Unauthorized, "Not authenticated")
                    return@post
                }
                
                if (!fantaSession.isAdmin(session.playerId)) {
                    call.respond(HttpStatusCode.Forbidden, "Access denied. Admin only.")
                    return@post
                }
                
                val auctionId = call.parameters["auctionId"]?.toInt()!!
                fantaSession.forceCompleteAuction(auctionId)
                call.respondRedirect("/admin")
            }
            
            post("reset-auction/{auctionId}") {
                val session = call.sessions.get<UserSession>()
                if (session == null) {
                    call.respond(HttpStatusCode.Unauthorized, "Not authenticated")
                    return@post
                }
                
                if (!fantaSession.isAdmin(session.playerId)) {
                    call.respond(HttpStatusCode.Forbidden, "Access denied. Admin only.")
                    return@post
                }
                
                val auctionId = call.parameters["auctionId"]?.toInt()!!
                fantaSession.resetAuction(auctionId)
                call.respondRedirect("/admin")
            }
            
            delete("auction/{auctionId}") {
                val session = call.sessions.get<UserSession>()
                if (session == null) {
                    call.respond(HttpStatusCode.Unauthorized, "Not authenticated")
                    return@delete
                }
                
                if (!fantaSession.isAdmin(session.playerId)) {
                    call.respond(HttpStatusCode.Forbidden, "Access denied. Admin only.")
                    return@delete
                }
                
                val auctionId = call.parameters["auctionId"]?.toInt()!!
                fantaSession.cancelAuction(auctionId)
                call.respondRedirect("/admin")
            }
            
            post("add-money/{playerId}/{amount}") {
                val session = call.sessions.get<UserSession>()
                if (session == null) {
                    call.respond(HttpStatusCode.Unauthorized, "Not authenticated")
                    return@post
                }
                
                if (!fantaSession.isAdmin(session.playerId)) {
                    call.respond(HttpStatusCode.Forbidden, "Access denied. Admin only.")
                    return@post
                }
                
                val playerId = call.parameters["playerId"]?.toInt()!!
                val amount = call.parameters["amount"]?.toInt()!!
                
                players.first { it.id == playerId }.apply {
                    money += amount
                }
                call.respondRedirect("/admin")
            }
            
            post("subtract-money/{playerId}/{amount}") {
                val session = call.sessions.get<UserSession>()
                if (session == null) {
                    call.respond(HttpStatusCode.Unauthorized, "Not authenticated")
                    return@post
                }
                
                if (!fantaSession.isAdmin(session.playerId)) {
                    call.respond(HttpStatusCode.Forbidden, "Access denied. Admin only.")
                    return@post
                }
                    
                val playerId = call.parameters["playerId"]?.toInt()!!
                val amount = call.parameters["amount"]?.toInt()!!
                
                if (players.first { it.id == playerId }.money < amount) {
                    call.respond(HttpStatusCode.BadRequest, "Insufficient funds")
                    return@post
                }
                
                players.first { it.id == playerId }.apply {
                    money -= amount
                }
                call.respondRedirect("/admin")
            }
        }
    }
}
