package com.example.snakes

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.LinearLayout
import android.widget.TextView
import androidx.core.view.isVisible
import androidx.lifecycle.ViewModelProvider


class SnakesFragment : Fragment() {

    lateinit var gridLayout: androidx.gridlayout.widget.GridLayout
    lateinit var buttonStart: Button
    lateinit var buttonRight: Button
    lateinit var buttonLeft: Button
    lateinit var buttonTop: Button
    lateinit var buttonBottom: Button

    lateinit var gameTv: TextView

    lateinit var countTv: TextView

    lateinit var viewModel: SnakesViewModel

     var isStart: Boolean = false


    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.fragment_snakes, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        viewModel = ViewModelProvider(this).get(SnakesViewModel::class.java)

        gridLayout = view.findViewById(R.id.gridLayout)
        buttonStart = view.findViewById(R.id.buttonStart)

        buttonRight = view.findViewById(R.id.buttonRight)
        buttonLeft = view.findViewById(R.id.buttonLeft)

        buttonTop = view.findViewById(R.id.buttonTop)
        buttonBottom = view.findViewById(R.id.buttonBottom)

        gameTv = view.findViewById(R.id.gameTv)

        countTv = view.findViewById(R.id.countTv)



        for (i in 1..150) {
            var tv = TextView(activity)
            var layoutParams = LinearLayout.LayoutParams(
                80, 80
            )
            tv.id = i
            tv.layoutParams = layoutParams
            tv.setBackgroundResource(R.drawable.rectangles)
            gridLayout.addView(tv)
        }



        viewModel.snakeLD.observe(viewLifecycleOwner) {
            try {

               cleanFilds(view)

                val head = it.head
                val idHead: Int = fildsFormul(head.vert, head.hor)
                var tvHead: TextView = view.findViewById(idHead)
                tvHead.setBackgroundResource(R.drawable.ic_launcher_background)

                for (i in it.body) {
                    val idBody: Int = fildsFormul(i.vert, i.hor)
                    var tvBody: TextView = view.findViewById(idBody)
                    tvBody.setBackgroundResource(androidx.constraintlayout.widget.R.drawable.abc_ab_share_pack_mtrl_alpha)
                    
                }

                viewModel.piceLD.observe(viewLifecycleOwner) {
                    val pice = it
                    val idPice: Int = fildsFormul(pice.vert, pice.hor)
                    var tvPice: TextView = view.findViewById(idPice)
                    tvPice.setBackgroundResource(androidx.constraintlayout.widget.R.drawable.abc_ab_share_pack_mtrl_alpha)
                }



            } catch (e: Exception) {

            }
        }


        viewModel.pointsLD.observe(viewLifecycleOwner) {
            countTv.text = it.toString()
        }




        buttonStart.setOnClickListener {
            viewModel.viewState.observe(viewLifecycleOwner) {
                if (it == SnakesState.pause) {
                    buttonStart.text = "pause"
                } else if (it == SnakesState.start) {
                    buttonStart.text = "start"
                } else if (it == SnakesState.gameOver) {
                    buttonStart.text = "reset game"
                }
                }

            viewModel.startOrStop()
        }


        buttonRight.setOnClickListener {
            viewModel.setDirection(Snake.Direction.RIGHT)
        }

        buttonLeft.setOnClickListener {
            viewModel.setDirection(Snake.Direction.LEFT)
        }


        buttonBottom.setOnClickListener {
            viewModel.setDirection(Snake.Direction.BOTTOM)
        }


        buttonTop.setOnClickListener {
            viewModel.setDirection(Snake.Direction.TOP)
        }


    }


    fun fildsFormul(vert: Int, hor: Int): Int {
        return vert * 10 + hor + 1
    }

    private fun cleanFilds(view : View) {
        for (i in 1..150) {
            var tv: TextView = view.findViewById(i)
            tv.setBackgroundResource(R.drawable.rectangles)
        }
    }


}