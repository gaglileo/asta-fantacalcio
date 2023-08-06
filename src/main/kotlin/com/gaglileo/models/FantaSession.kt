package com.gaglileo.models

class FantaSession
private constructor(
    var currentAuction: Auction?,
    val players: List<Player>,
    val auctions: ArrayList<Auction>
){
    companion object {
        fun newFantaSession() =
            FantaSession(
                null,
                players,
                fakeAuctions()
            )

        fun fakeAuctions() = arrayListOf<Auction>()
    }

    fun addAuction(auction: Auction) {
        currentAuction = auction
    }

    fun assignAuction(auction: Auction) {
        auctions.add(auction)
        players.first { it.id == auction.winner?.player?.id }.apply {
            removeMoney(auction.winner?.amount?:0)
        }
    }

    fun cancelAuction(id: Int) {
        auctions.removeIf { it.id == id }
    }

    fun getAuction(id: Int): Auction {
        return auctions.first { it.id == id }
    }
}

val fantaSession = FantaSession.newFantaSession()