package com.example.myapplication

import android.content.Context
import android.graphics.Canvas
import android.graphics.Color
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

        surfaceView = findViewById(R.id.surfaceView)
        surfaceHolder = surfaceView.holder

    }

    private fun drawCircle123(canvas: Canvas) {
        paint.color = Color.BLUE
        canvas.drawCircle(circleX, circleY, circleRadius, paint)
    }

    override fun onResume() {
        super.onResume()
        sensorManager.registerListener(this, mySensor, SensorManager.SENSOR_DELAY_UI)
    }

    override fun onDestroy() {
        super.onDestroy()
        sensorManager.unregisterListener(this)
    }

    override fun onSensorChanged(p0: SensorEvent?) {
        var width = surfaceView.width
        var height = surfaceView.height
        if (p0!!.sensor.type == Sensor.TYPE_ACCELEROMETER) {
            // 更新圓點位置
            circleX -= p0.values[0]
            circleY += p0.values[1]

            // 確保圓點不會超出螢幕邊界
            if (circleX < circleRadius) {
                circleX = circleRadius
            } else if (circleX > width - circleRadius) {
                circleX = width - circleRadius
            }

            if (circleY < circleRadius) {
                circleY = circleRadius
            } else if (circleY > height - circleRadius) {
                circleY = height - circleRadius
            }
        }

        // 在 SurfaceView 中更新視圖
        val canvas = surfaceHolder.lockCanvas()
        if (canvas != null) {
            // 清除先前的內容
            canvas.drawColor(Color.BLACK)
            drawCircle123(canvas)

            surfaceHolder.unlockCanvasAndPost(canvas)
        }
    }

    override fun onAccuracyChanged(p0: Sensor?, p1: Int) {
//        TODO("Not yet implemented")
    }

}