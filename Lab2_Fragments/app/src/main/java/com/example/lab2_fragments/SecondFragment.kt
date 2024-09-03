package com.example.lab2_fragments

import android.graphics.Bitmap
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.MotionEvent
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment

class SecondFragment : Fragment() {
    private lateinit var drawingView: DrawingView

    companion object {
        var savedBitmap: Bitmap? = null
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val view = inflater.inflate(R.layout.fragment_second, container, false)
        drawingView = view.findViewById(R.id.drawingView)

        savedBitmap?.let {
            drawingView.setBitmap(it)
        }

        drawingView.setOnTouchListener { _, event ->
            if (event.action == MotionEvent.ACTION_MOVE || event.action == MotionEvent.ACTION_DOWN) {
                drawingView.addTouch(event.x, event.y)
                true
            } else {
                false
            }
        }
        return view
    }

    override fun onPause() {
        super.onPause()
        savedBitmap = drawingView.getBitMap()
    }



    override fun onDestroyView() {
        super.onDestroyView()
        Log.d("SecondFragment", "Fragment view destroyed")
    }
}// end of second fragment class implementation