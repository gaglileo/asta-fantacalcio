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
    Player.newEntry(0,"Leo", 224),
    Player.newEntry(1,"Trucido", 128),
    Player.newEntry(2,"Fabio", 158),
    Player.newEntry(3,"Mazza", 129),
    Player.newEntry(4,"Magna", 266),
    Player.newEntry(5,"Regina", 207),
    Player.newEntry(6,"Mimi", 131),
    Player.newEntry(7,"Mirco", 138),
)