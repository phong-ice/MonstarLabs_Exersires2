package com.example.monstarlabsexercise2

import android.annotation.SuppressLint
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.os.Handler
import android.os.HandlerThread
import android.os.Looper
import android.util.Log
import android.view.MotionEvent
import android.view.View
import android.widget.Toast
import androidx.annotation.UiThread
import com.example.monstarlabsexercise2.databinding.ActivityMainBinding

class MainActivity : AppCompatActivity() {

    private lateinit var _binding: ActivityMainBinding
    private lateinit var handler: Handler
    private lateinit var runnable: Runnable
    private val handlerThread: HandlerThread = HandlerThread("MyThread")
    var number: Int = 0
    var rowY: Float = 0f

    @SuppressLint("ClickableViewAccessibility")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        _binding = ActivityMainBinding.inflate(layoutInflater)
        val view = _binding.root
        setContentView(view)
        handlerThread.start()
        handler = Handler(handlerThread.looper)
        _binding.layoutTouchEvent.setOnTouchListener { _, event ->
            handler.removeCallbacks(runnable)
            if (event.action == MotionEvent.ACTION_MOVE) {
                var y = event.y
                when {
                    y < rowY -> number += 1
                    y > rowY -> number -= 1
                }
                _binding.tvNumberShow.text = number.toString()
            }
            rowY = event.y
            handler.postDelayed(runnable, 2000)

            true
        }

        _binding.btnPlus.setOnClickListener {
            number += 1
            _binding.tvNumberShow.text = number.toString()
        }

        _binding.btnMinus.setOnClickListener {
            number -= 1
            _binding.tvNumberShow.text = number.toString()
        }

        runnable = Runnable {
            when {
                number > 0 -> {
                    number--
                    this.runOnUiThread {
                        _binding.tvNumberShow.text = number.toString()
                    }
                    handler.postDelayed(runnable, 50)
                }

                number < 0 -> {
                    number++
                    this.runOnUiThread {
                        _binding.tvNumberShow.text = number.toString()
                    }
                    handler.postDelayed(runnable, 50)
                }
                else -> {
                    handler.removeCallbacks(runnable)
                }
            }
        }
    }
}