package com.gaglileo.models

import java.util.concurrent.atomic.AtomicInteger

class Win
private constructor(val id: Int, val player: Player, val footballer: String, val amount: Int){
    companion object {
        private val idCounter = AtomicInteger()

        fun newWin(player: Player, footballer: String, amount: Int) = Win(
            idCounter.getAndIncrement(), player, footballer, amount
        )
    }
}