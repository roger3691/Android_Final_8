package com.example.myapplication

import android.content.Context
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
        mySensor = sensorManager.getDefaultSensor(Sensor.TYPE_GYROSCOPE)!!

        btnStart.setOnClickListener {
//            btn.layout(167,1061,811,1187)

            val params = btn.layoutParams as ViewGroup.MarginLayoutParams
//            params.leftMargin = 0
//            params.topMargin = 0

            btn.layoutParams = params
            btn.requestLayout()

            text.text = "${params.rightMargin}"

            // 設定移動的動畫
//            val animation: Animation = TranslateAnimation(0f, 200f, 0f, 0f) // 偏移量為 200 像素
//            animation.duration = 1000 // 持續時間為 1000 毫秒 (1 秒)
//            animation.fillAfter = true // 動畫結束後保持最終的位置
//
//            animation.setAnimationListener(object:Animation.AnimationListener{
//                override fun onAnimationStart(p0: Animation?) {
//                    btn.isEnabled = false
//                }
//
//                override fun onAnimationEnd(p0: Animation?) {
//                    btn.isEnabled = true
//                    btn.layout(210,1082,454,1208)
//                }
//
//                override fun onAnimationRepeat(p0: Animation?) {
//                    //TODO("Not yet implemented")
//                }
//
//            })
//
//            btn.startAnimation(animation)

        }

        btn.setOnClickListener {
            text.text = "l：${btn.left}t：${btn.top} r：${btn.right} b：${btn.bottom}"
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
            if(p0!!.sensor.type == Sensor.TYPE_GYROSCOPE){
                val params = btn.layoutParams as ViewGroup.MarginLayoutParams
                val newLeftMargin = params.leftMargin + p0.values[1].toInt() * 10
                if(newLeftMargin in 0..835)
                    params.leftMargin = newLeftMargin

                btn.layoutParams = params
                btn.requestLayout()
            }
    }

    override fun onAccuracyChanged(p0: Sensor?, p1: Int) {
//        TODO("Not yet implemented")
    }
}