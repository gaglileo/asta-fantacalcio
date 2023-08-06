package com.gaglileo.models

class Player
private constructor(val id: Int, val name: String, var money: Int) {
    companion object {
        fun newEntry(id: Int, name: String, money: Int) = Player(
            id,
            name,
            money
        )
    }

    fun addMoney(toAdd: Int) {
        money+=toAdd
    }

    fun removeMoney(toRemove: Int) {
        money-=toRemove
    }
}

val players = listOf(
    Player.newEntry(0,"Leo", 179),
    Player.newEntry(1,"Trucido", 171),
    Player.newEntry(2,"Fulvio", 134),
    Player.newEntry(3,"Mazza", 114),
    Player.newEntry(4,"Magna", 144),
    Player.newEntry(5,"Regina", 117),
    Player.newEntry(6,"Mimi", 180),
    Player.newEntry(7,"Mirco", 111),
)