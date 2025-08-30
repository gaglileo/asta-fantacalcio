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
    }
}
