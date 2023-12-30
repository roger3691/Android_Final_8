package com.example.myapplication

import android.content.Context
import android.content.Intent
import android.graphics.Paint
import android.hardware.Sensor
import android.hardware.SensorEvent
import android.hardware.SensorEventListener
import android.hardware.SensorManager
import android.os.Bundle
import android.util.Log
import android.view.SurfaceHolder
import android.view.ViewGroup
import android.widget.Button
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity


class MainActivity : AppCompatActivity(),SensorEventListener{
    private lateinit var btnStart:Button
    private lateinit var btn:Button
    private lateinit var text:TextView
    private lateinit var sensorManager:SensorManager
    private lateinit var mySensor:Sensor
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        btnStart = findViewById(R.id.btnStart)
        btn = findViewById(R.id.button2)
        text = findViewById(R.id.textView)

        sensorManager = getSystemService(Context.SENSOR_SERVICE) as SensorManager
        mySensor = sensorManager.getDefaultSensor(Sensor.TYPE_ACCELEROMETER)!!

        btnStart.setOnClickListener {
            val params = btn.layoutParams as ViewGroup.MarginLayoutParams
            params.topMargin = 500
            params.leftMargin = 500
        }

        btn.setOnClickListener {
            Intent(this,MainActivity2::class.java).apply {
                startActivity(this)
            }
        }
    }

    override fun onResume() {
        super.onResume()
        sensorManager.registerListener(this,mySensor,SensorManager.SENSOR_DELAY_UI)
    }

    override fun onDestroy() {
        super.onDestroy()
        sensorManager.unregisterListener(this)
    }

    fun getScreenWidth(){

    }

    override fun onSensorChanged(p0: SensorEvent?) {

        if(p0!!.sensor.type == Sensor.TYPE_ACCELEROMETER){
            val params = btn.layoutParams as ViewGroup.MarginLayoutParams
            val newLeftMargin = params.leftMargin - p0.values[0].toInt() * 1000 / 50
            val newTopMargin = params.topMargin + p0.values[1].toInt() * 1000 / 50
            if(newLeftMargin in 0..835)
                params.leftMargin =  newLeftMargin

            if(newTopMargin in 0..1800)
                params.topMargin = newTopMargin

            text.text = "${p0.values[0]}  + ${p0.values[1]} + ${p0.values[2]}"
            btn.layoutParams = params
            btn.requestLayout()
            }
    }

    override fun onAccuracyChanged(p0: Sensor?, p1: Int) {
//        TODO("Not yet implemented")
    }
}