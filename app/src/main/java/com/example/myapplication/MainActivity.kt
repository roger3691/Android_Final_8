package com.example.myapplication

import android.content.Context
import android.content.Intent
import android.hardware.Sensor
import android.hardware.SensorEvent
import android.hardware.SensorEventListener
import android.hardware.SensorManager
import android.os.Bundle
import android.view.ViewGroup
import android.widget.Button
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity


class MainActivity : AppCompatActivity(),SensorEventListener{
    private lateinit var btn:Button
    private lateinit var btnStart:Button
    private lateinit var text:TextView
    private lateinit var sensorManager:SensorManager
    private lateinit var mySensor:Sensor
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        btn = findViewById(R.id.btn1)
        btnStart = findViewById(R.id.btn2)
        text = findViewById(R.id.textView)

        sensorManager = getSystemService(Context.SENSOR_SERVICE) as SensorManager
        mySensor = sensorManager.getDefaultSensor(Sensor.TYPE_ACCELEROMETER)!!

        btn.text = "重置小按鈕"
        btnStart.text = "開始遊戲"
        text.text = "期末專題第八組\n\n限時30秒\n利用加速度感測器操控藍點\n碰觸紅點來讓分數++"

        btn.setOnClickListener {
            val params = btnStart.layoutParams as ViewGroup.MarginLayoutParams
            params.topMargin = 500
            params.leftMargin = 500
        }

        btnStart.setOnClickListener {
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

    override fun onSensorChanged(p0: SensorEvent?) {

        if(p0!!.sensor.type == Sensor.TYPE_ACCELEROMETER){
            val params = btnStart.layoutParams as ViewGroup.MarginLayoutParams
            val newLeftMargin = params.leftMargin - p0.values[0].toInt() * 10
            val newTopMargin = params.topMargin + p0.values[1].toInt() * 10
            if(newLeftMargin in 0..835)
                params.leftMargin =  newLeftMargin

            if(newTopMargin in 0..1500)
                params.topMargin = newTopMargin

            //text.text = "${p0.values[0]}  + ${p0.values[1]} + ${p0.values[2]}"
            btnStart.layoutParams = params
            btnStart.requestLayout()
            }
    }

    override fun onAccuracyChanged(p0: Sensor?, p1: Int) {
        //TODO("Not yet implemented")
    }
}