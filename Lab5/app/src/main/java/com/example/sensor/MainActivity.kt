package com.example.sensor

import android.hardware.Sensor
import android.hardware.SensorEvent
import android.hardware.SensorEventListener
import android.hardware.SensorManager
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.IntOffset
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel

class MainActivity : ComponentActivity(), SensorEventListener {
    private lateinit var sensorManager: SensorManager
    private lateinit var gravitySensor: Sensor

    private var _gravityX = mutableStateOf(0f)
    private var _gravityY = mutableStateOf(0f)

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        sensorManager = getSystemService(SENSOR_SERVICE) as SensorManager
        gravitySensor = sensorManager.getDefaultSensor(Sensor.TYPE_GRAVITY)!!

        setContent {
            MarbleScreen(_gravityX.value, _gravityY.value)
        }
    }

    override fun onResume() {
        super.onResume()
        sensorManager.registerListener(
            this,
            gravitySensor,
            SensorManager.SENSOR_DELAY_GAME
        )
    }

    override fun onPause() {
        super.onPause()
        sensorManager.unregisterListener(this)
    }

    override fun onSensorChanged(event: SensorEvent?) {
        if (event != null && event.sensor.type == Sensor.TYPE_GRAVITY) {
            val smothedValues = lowPassFilter(event.values, floatArrayOf(_gravityX.value, _gravityY.value, 0f))
            _gravityX.value = event.values[0]
            _gravityY.value = event.values[1]
        }
    }

    override fun onAccuracyChanged(sensor: Sensor?, accuracy: Int) {
        // no need for now
    }

    @Composable
    fun MarbleScreen(gravityX: Float, gravityY: Float) {
        // layout container
        BoxWithConstraints(
            modifier = Modifier.fillMaxSize(), // box fills whole screen
            contentAlignment = Alignment.Center
        ) {
            val maxWidthDp = maxWidth
            val maxHeightDp = maxHeight

            var offsetX by remember { mutableStateOf(0f) }
            var offsetY by remember { mutableStateOf(0f) }

            // converts to pixels using local density
            val maxWidthPx = with(LocalDensity.current) {maxWidthDp.toPx() }
            val maxHeightPx = with(LocalDensity.current) {maxHeightDp.toPx() }

            LaunchedEffect(gravityX, gravityY) {
                offsetX = (offsetX + gravityX).coerceIn(0f, maxWidthPx - 100f)
                offsetY = (offsetY - gravityY).coerceIn(0f, maxHeightPx - 100f)
            }
            Box(
                modifier = Modifier
                    .size(100.dp)
                    .offset { IntOffset(offsetX.toInt(), offsetY.toInt()) }
                    .background(Color.Blue, shape = CircleShape)
            )
        }
    }

    fun lowPassFilter(input: FloatArray, output: FloatArray): FloatArray {
        val alpha = 0.8f
        for (i in input.indices) {
            output[i] = alpha * output[i] + (1 - alpha) * input[i]
        }
        return output
    }
}//end of mainActivity class bracket