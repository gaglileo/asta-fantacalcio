package com.gaglileo.plugins

import com.gaglileo.models.Auction
import com.gaglileo.models.Offer
import com.gaglileo.models.fantaSession
import com.gaglileo.models.players
import io.ktor.http.*
import io.ktor.server.routing.*
import io.ktor.server.application.*
import io.ktor.server.freemarker.*
import io.ktor.server.http.content.*
import io.ktor.server.request.*
import io.ktor.server.response.*
import io.ktor.server.util.*

fun Application.configureRouting() {

    routing {
        static("/static") {
            resources("files")
        }
        get("/") {
            call.respondRedirect("auctions")
        }
        route("auctions"){
            get {
                call.respond(FreeMarkerContent("index.ftl",
                    mapOf(
                        "auctions" to fantaSession.auctions,
                        "players" to fantaSession.players,
                        "currentAuction" to fantaSession.currentAuction
                    )
                ))
            }
            get("{id}") {
                val auctionId = call.parameters["id"]?.toInt()!!
                val auction = fantaSession.getAuction(auctionId)
                call.respond(FreeMarkerContent("auction.ftl",
                    mapOf(
                        "auction" to auction
                    )
                )
                )
            }
            post {
                val footballer = call.receive<String>()
                fantaSession.addAuction(Auction.newEntry(footballer))
                call.respondText("Auction started", status = HttpStatusCode.Created)
            }
            post("assign") {
                fantaSession.currentAuction?.checkWinner()
                call.respondText("Auction assigned", status = HttpStatusCode.Created)
            }
            delete("{id}") {
                val auctionId = call.parameters["id"]?.toInt()!!
                fantaSession.cancelAuction(auctionId)
                call.respondText("Auction canceled", status = HttpStatusCode.Accepted)
            }
        }
        route("offer") {
            get("new/{playerId}") {
                val playerId = call.parameters["playerId"]
                call.respond(FreeMarkerContent("offer.ftl", model =
                mapOf(
                    "currentAuction" to fantaSession.currentAuction,
                    "playerId" to playerId
                )))
            }
            post("{playerId}") {
                val playerId = call.parameters["playerId"]?.toInt()!!
                val formParameters = call.receiveParameters()
                val amount = formParameters.getOrFail<Int>("amount")
                val newOffer = Offer.newEntry(amount, players[playerId])
                fantaSession.currentAuction?.addOffer(newOffer.player, newOffer)
                call.respondRedirect("/")
            }
        }
        route("player") {
            post("add/{playerId}/{amount}") {
                val playerId = call.parameters["playerId"]?.toInt()!!
                val amount = call.parameters["playerId"]?.toInt()!!
                players.first { it.id == playerId }.apply {
                    money+=amount
                }
            }
            post("sub/{playerId}/{amount}") {
                val playerId = call.parameters["playerId"]?.toInt()!!
                val amount = call.parameters["playerId"]?.toInt()!!
                players.first { it.id == playerId }.apply {
                    money-=amount
                }
            }
        }
    }
}
