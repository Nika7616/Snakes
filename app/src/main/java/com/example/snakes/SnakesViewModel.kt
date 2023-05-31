package com.example.snakes

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.Job
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch

class SnakesViewModel : ViewModel() {

    lateinit var snake: Snake
    private var _snakeLD = MutableLiveData<Snake>()
    val snakeLD: LiveData<Snake>
        get() = _snakeLD


    lateinit var pice: Position
    private var _piceLD = MutableLiveData<Position>()
    val piceLD: LiveData<Position>
        get() = _piceLD

    var points: Int = 0
    private var _pointsLD = MutableLiveData<Int>()
    val pointsLD: LiveData<Int>
        get() = _pointsLD


    private val _viewState = MutableLiveData<SnakesState>()
    val viewState: LiveData<SnakesState>
        get() = _viewState


    lateinit var job: Job



    var isStart: Boolean = true


     var speedSnake: Long = 400


    companion object {
        const val FILD_WEIGTH = 10
        const val FILD_HIGHT = 15
    }


    init {
        resetGame()


    }

    private fun randomPice(): Position {
        var vert = (0..FILD_HIGHT - 1).random()
        var hor = (0..FILD_WEIGTH - 1).random()
        var pos = Position(vert, hor)
        while (true) {
            for (i in snake.body) {

                if (pos == snake.head || pos == i) {
                    vert = (0..FILD_HIGHT - 1).random()
                    hor = (0..FILD_HIGHT - 1).random()
                    pos = Position(vert, hor)

                } else {
                    return Position(vert, hor)
                }

            }

        }

        return Position(vert, hor)
    }

    fun moveSnake() {

        job = viewModelScope.launch(Dispatchers.Main) {

            while (true) {

                val wait: Job = launch {
                    delay(speedSnake)
                }
                wait.join()


                val wishHead = snake.head.copy()



                when (snake.direction) {
                    Snake.Direction.TOP -> {
                        wishHead.vert--
                    }
                    Snake.Direction.LEFT -> {
                        wishHead.hor--
                    }
                    Snake.Direction.RIGHT -> {
                        wishHead.hor++
                    }
                    Snake.Direction.BOTTOM -> {
                        wishHead.vert++
                    }
                }



                if (varif(wishHead)) {

                    val pice1 = pice
                    if (wishHead == pice1) {
                        val elBody = snake.head.copy()
                        snake.body.add(elBody)

                        pice = randomPice()
                        _piceLD.value = pice
                        points++
                        _pointsLD.value = points

                        if (points % 10 == 0) {
                           speedSnake = speedSnake + 100
                        }

                    } else {


                    }

                    for (i in snake.body) {
                        if (wishHead == i) {
                            _viewState.value = SnakesState.gameOver
                            return@launch
                        }
                    }

                    for (i in snake.body.size - 1 downTo 1) {

                        snake.body[i].vert = snake.body[i - 1].vert
                        snake.body[i].hor = snake.body[i - 1].hor

                    }

                    snake.body[0] = snake.head


                    snake.head = wishHead



                    _snakeLD.value = snake
                } else {
                    _viewState.value = SnakesState.gameOver
                    return@launch
                }

            }

        }

    }

    fun resetGame () {
        val body = mutableListOf<Position>()
        val head = Position(vert = FILD_HIGHT / 2, hor = FILD_WEIGTH / 2)

        for (i in 1..5) {
            val el = Position(
                vert = head.vert - i,
                hor = head.hor
            )
            body.add(el)
        }
        snake = Snake(
            head = head,
            body = body,
            direction = Snake.Direction.BOTTOM
        )


        _snakeLD.value = snake

        points = 0
        _pointsLD.value = points

        isStart = true
        _viewState.value = SnakesState.pause


        pice = randomPice()
        _piceLD.value = pice
    }

    fun startOrStop () {

        if (_viewState.value == SnakesState.gameOver) {
           resetGame()
        }

        if (isStart == true) {
            moveSnake()
            isStart = false
            _viewState.value = SnakesState.pause

        } else if (isStart == false) {
            job.cancel()
            isStart = true
            _viewState.value = SnakesState.start
        }
    }


    private fun varif(head: Position): Boolean {
        if (head.hor >= 0 && head.hor <= FILD_WEIGTH - 1 &&
            head.vert >= 0 && head.vert <= FILD_HIGHT - 1
        ) {
            return true
        } else {
            return false
        }

    }

    fun setDirection(direction: Snake.Direction) {
        if (
            snake.direction == Snake.Direction.LEFT && direction == Snake.Direction.RIGHT ||
            snake.direction == Snake.Direction.RIGHT && direction == Snake.Direction.LEFT ||
            snake.direction == Snake.Direction.TOP && direction == Snake.Direction.BOTTOM ||
            snake.direction == Snake.Direction.BOTTOM && direction == Snake.Direction.TOP
        ) {
            return
        }

        snake.direction = direction

    }


}