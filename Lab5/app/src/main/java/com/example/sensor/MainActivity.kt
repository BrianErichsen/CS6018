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

/*Author: Brian Erichsen Fagundes
* October 03 - 2024
* Lab5 - CS6018
* Professor: Nabil Makarem*/

// I am using sensorEventListener to listen for sensor events (SEL)
class MainActivity : ComponentActivity(), SensorEventListener {
    private lateinit var sensorManager: SensorManager // register and unregisters the SEL
    private lateinit var gravitySensor: Sensor  // gravity sensor - detects phone orientation by gravity

    private var _gravityX = mutableStateOf(0f) // stores current sensor values for x and y
    private var _gravityY = mutableStateOf(0f)

// on create called when the activity is created
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        sensorManager = getSystemService(SENSOR_SERVICE) as SensorManager // sys call
        gravitySensor = sensorManager.getDefaultSensor(Sensor.TYPE_GRAVITY)!! // assumed to be not null

    // replaces setContentView ...
    // invokes marble - composable fun and passes current values of x and y
        setContent {
            MarbleScreen(_gravityX.value, _gravityY.value)
        }
    }
    // for
    override fun onResume() {
        super.onResume()
        // sets manager to register gravity sensor updates
        sensorManager.registerListener(
            this,
            gravitySensor, // sensor
            SensorManager.SENSOR_DELAY_GAME
        )
    }

    // when activity goes into background ...
    // unregister sensor listeners
    override fun onPause() {
        super.onPause()
        sensorManager.unregisterListener(this)
    }

    // called whenever sensor values are changed
    override fun onSensorChanged(event: SensorEvent?) {
        // checks if it is gravity type and decreases jitter in sensor data
        if (event != null && event.sensor.type == Sensor.TYPE_GRAVITY) {
            val smothedValues = lowPassFilter(event.values, floatArrayOf(_gravityX.value, _gravityY.value, 0f))
            _gravityX.value = smothedValues[0] // updates value x and y
            _gravityY.value = smothedValues[1]
        }
    }

    // required by interface but not needed
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

            // stores current position on the screen
            // remember retains state across recompositions
            var offsetX by remember { mutableStateOf(0f) }
            var offsetY by remember { mutableStateOf(0f) }

            // converts to pixels using local density
            val maxWidthPx = with(LocalDensity.current) {maxWidthDp.toPx() }
            val maxHeightPx = with(LocalDensity.current) {maxHeightDp.toPx() }

            // triggers effect when x and y values change
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

    // the idea is to reduce jitter in the sensor data
    // event.values = array
    fun lowPassFilter(input: FloatArray, output: FloatArray): FloatArray {
        val alpha = 0.8f
        for (i in input.indices) {
            output[i] = alpha * output[i] + (1 - alpha) * input[i]
        }
        return output
    }
}//end of mainActivity class bracket