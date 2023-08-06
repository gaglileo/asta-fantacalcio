package com.gaglileo.models

class Offer
private constructor(val amount: Int, val player: Player){
    companion object {
        fun newEntry(amount: Int, player: Player) = Offer(
            amount, player
        )
    }
}