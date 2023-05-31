package com.example.snakes

sealed class SnakesState {
    object beforeStart: SnakesState()

    object start: SnakesState()

    object pause: SnakesState()

    object gameOver: SnakesState()
}