package com.gaglileo.models

import java.util.concurrent.atomic.AtomicInteger

class Auction
private constructor(
    val id: Int,
    val footballer: String,
    val offers: HashMap<Player, Offer>,
    var isCompleted: Boolean,
    var isToRepeat: Boolean,
    var winner: Offer?
    ) {
    companion object {
        private val idCounter = AtomicInteger()
        fun newEntry(footballer: String) = Auction(
            idCounter.getAndIncrement(), footballer, hashMapOf(), false, false, null
        )
    }
    fun addOffer(player: Player, offer: Offer) {
        this.offers[player] = offer
        if(offers.keys.size == 8) {
            isCompleted = true
            checkWinner()
        }
    }

    fun checkWinner() {
        val maxOffer = offers.entries.maxBy { it.value.amount }.value
        offers.entries.filter { it.value.amount == maxOffer.amount }.let {
            if(it.size == 1) {
                winner = maxOffer
                fantaSession.assignAuction(this)
                fantaSession.currentAuction = null
            } else {
                isToRepeat = true
                offers.clear()
            }
        }
    }

    fun getOffersNumber(): Int {
        return offers.size
    }

    fun getOffersPlayerList(): List<Player> {
        return offers.keys.toList()
    }

    fun getOffersList(): List<Offer> {
        return offers.values.toList()
    }
}