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
    private lateinit var runnableBackZero: Runnable
    private lateinit var runnablePlus: Runnable
    private lateinit var runnableMinus: Runnable
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
            handler.removeCallbacks(runnableBackZero)
            if (event.action == MotionEvent.ACTION_MOVE) {
                var y = event.y
                when {
                    y < rowY -> number += 1
                    y > rowY -> number -= 1
                }
                _binding.tvNumberShow.text = number.toString()
            }
            rowY = event.y
            handler.postDelayed(runnableBackZero, 2000)

            true
        }

        _binding.btnPlus.setOnClickListener {
            handler.removeCallbacks(runnableBackZero)
            number++
            _binding.tvNumberShow.text = number.toString()
            handler.postDelayed(runnableBackZero, 2000)
        }

        _binding.btnMinus.setOnClickListener {
            handler.removeCallbacks(runnableBackZero)
            number--
            _binding.tvNumberShow.text = number.toString()
            handler.postDelayed(runnableBackZero, 2000)
        }

        _binding.btnPlus.setOnLongClickListener {
            handler.post(runnablePlus)
            true
        }

        _binding.btnMinus.setOnLongClickListener {
            handler.post(runnableMinus)
            true
        }


        runnableBackZero = Runnable {
            when {
                number > 0 -> {
                    number--
                    this.runOnUiThread {
                        _binding.tvNumberShow.text = number.toString()
                    }
                    handler.postDelayed(runnableBackZero, 50)
                }

                number < 0 -> {
                    number++
                    this.runOnUiThread {
                        _binding.tvNumberShow.text = number.toString()
                    }
                    handler.postDelayed(runnableBackZero, 50)
                }
                else -> {
                    handler.removeCallbacks(runnableBackZero)
                }
            }
        }
        runnablePlus = Runnable {
            handler.removeCallbacks(runnableBackZero)
            number++
            if (_binding.btnPlus.isPressed) {
                handler.postDelayed(runnablePlus, 50)
                this.runOnUiThread {
                    _binding.tvNumberShow.text = number.toString()
                }
            } else {
                handler.removeCallbacks(runnablePlus)
                handler.postDelayed(runnableBackZero, 2000)
            }
        }

        runnableMinus = Runnable {
            handler.removeCallbacks(runnableBackZero)
            number--
            if (_binding.btnMinus.isPressed) {
                handler.postDelayed(runnableMinus, 50)
                this.runOnUiThread {
                    _binding.tvNumberShow.text = number.toString()
                }
            } else {
                handler.removeCallbacks(runnableMinus)
                handler.postDelayed(runnableBackZero, 2000)
            }
        }
    }
}