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
    
    fun isAdmin(playerId: Int): Boolean {
        return playerId == 0 // Leo is player ID 0
    }
    
    fun forceCompleteAuction(auctionId: Int) {
        val auction = auctions.firstOrNull { it.id == auctionId }
        auction?.let {
            if (it.offers.isNotEmpty()) {
                it.checkWinner()
            }
        }
    }
    
    fun resetAuction(auctionId: Int) {
        val auction = auctions.firstOrNull { it.id == auctionId }
        auction?.let {
            it.offers.clear()
            it.isCompleted = false
            it.isToRepeat = false
            it.winner = null
        }
    }
    
    fun getCurrentAuctionStatus(): String {
        val auction = currentAuction
        return when {
            auction == null -> "No auction in progress"
            auction.isCompleted -> "Auction completed"
            auction.isToRepeat -> "Auction needs to be repeated"
            else -> "Auction in progress (${auction.getOffersNumber()}/8 offers)"
        }
    }
}

val fantaSession = FantaSession.newFantaSession()