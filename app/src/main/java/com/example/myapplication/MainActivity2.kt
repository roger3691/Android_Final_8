package com.example.myapplication

import android.content.Context
import android.graphics.Paint
import android.hardware.Sensor
import android.hardware.SensorEvent
import android.hardware.SensorEventListener
import android.hardware.SensorManager
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.SurfaceHolder
import android.view.SurfaceView
import android.view.ViewGroup

class MainActivity2 : AppCompatActivity(),SensorEventListener {
    private lateinit var sensorManager: SensorManager
    private lateinit var mySensor: Sensor

    private lateinit var surfaceView: SurfaceView
    private lateinit var surfaceHolder: SurfaceHolder
    private var circleX: Float = 100f
    private var circleY: Float = 100f
    private val circleRadius = 50f
    private val paint: Paint = Paint()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main2)

        sensorManager = getSystemService(Context.SENSOR_SERVICE) as SensorManager
        mySensor = sensorManager.getDefaultSensor(Sensor.TYPE_ACCELEROMETER)!!
    }

    override fun onResume() {
        super.onResume()
        sensorManager.registerListener(this,mySensor,SensorManager.SENSOR_DELAY_UI)
    }

    override fun onDestroy() {
        super.onDestroy()
        sensorManager.unregisterListener(this)
    }

    override fun onSensorChanged(p0: SensorEvent?) {
        if(p0!!.sensor.type == Sensor.TYPE_ACCELEROMETER){

        }
    }

    override fun onAccuracyChanged(p0: Sensor?, p1: Int) {
//        TODO("Not yet implemented")
    }
}