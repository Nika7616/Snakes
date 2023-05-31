package com.example.snakes

data class Snake (
    var direction: Direction,
    var body: MutableList<Position>,
    var head: Position,

        ) {
    enum class Direction {
       LEFT,
       RIGHT,
       TOP,
       BOTTOM

    }
}