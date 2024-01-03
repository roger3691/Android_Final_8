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
import android.os.CountDownTimer
import android.os.Handler
import android.view.SurfaceHolder
import android.view.SurfaceView
import android.view.View
import android.widget.TextView
import kotlin.math.pow
import kotlin.math.sqrt
import com.github.mikephil.charting.charts.LineChart
import com.github.mikephil.charting.data.Entry
import com.github.mikephil.charting.data.LineData
import com.github.mikephil.charting.data.LineDataSet
import kotlinx.coroutines.delay

class MainActivity2 : AppCompatActivity(),SensorEventListener {
    private lateinit var sensorManager: SensorManager
    private lateinit var mySensor: Sensor

    private lateinit var surfaceView: SurfaceView
    private lateinit var surfaceHolder: SurfaceHolder
    private var circleX: Float = 500f
    private var circleY: Float = 500f
    private var circleRadius = 50f
    private val paint: Paint = Paint()
    private var bigCircleX: Float = 800f
    private var bigCircleY: Float = 1000f
    private val bigCircleRadius = 100f


    private lateinit var showScore:TextView
    private lateinit var showTime:TextView
    private var score = 0
    private val initialTimeMillis: Long = 30000
    private lateinit var countDownTimer: CountDownTimer
    private var gameRunning = true
    //測試程式階段
    private lateinit var lineChart: LineChart
    private val timeData: MutableList<Entry> = mutableListOf()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main2)

        lineChart = findViewById(R.id.lineChart)

        // 初始化折線圖
        setupLineChart()

        // 模擬分數和時間的變化
        simulateData()



        sensorManager = getSystemService(Context.SENSOR_SERVICE) as SensorManager
        mySensor = sensorManager.getDefaultSensor(Sensor.TYPE_ACCELEROMETER)!!

        surfaceView = findViewById(R.id.surfaceView)
        surfaceHolder = surfaceView.holder

        showScore = findViewById(R.id.score123)
        showTime = findViewById(R.id.time123)

        showScore.text = "分數：0"

        showTime.isEnabled = false
        showTime.setOnClickListener {
            recreate()
        }


        countDownTimer = object : CountDownTimer(initialTimeMillis,1000){
            override fun onTick(p0: Long) {
                val secondsRemaining = p0 / 1000
                showTime.text = "剩餘時間：${secondsRemaining}"
            }

            override fun onFinish() {
                lineChart.setBackgroundColor(Color.GRAY)
                lineChart.visibility = View.VISIBLE
                gameRunning = false
                showTime.isEnabled = true
                showTime.text = "再來一次?"
            }
        }

        startCountdown()
    }

    private fun drawCircle(canvas: Canvas) {
        paint.color = Color.RED
        canvas.drawCircle(bigCircleX,bigCircleY,bigCircleRadius,paint)

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
//        stopCountdown()
    }


    override fun onSensorChanged(p0: SensorEvent?) {
        var width = surfaceView.width
        var height = surfaceView.height
        val canvas = surfaceHolder.lockCanvas()


        if (p0!!.sensor.type == Sensor.TYPE_ACCELEROMETER) {
            // 更新圓點位置
            circleX -= p0.values[0] * 10
            circleY += p0.values[1] * 10

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

            // 檢查碰撞
            if (isCollision(circleX, circleY, circleRadius, bigCircleX, bigCircleY, bigCircleRadius)) {
                // 如果碰撞，改變大紅色圓點位置
                bigCircleX = (Math.random() * (width - 2 * bigCircleRadius) + bigCircleRadius).toFloat()
                bigCircleY = (Math.random() * (height - 2 * bigCircleRadius) + bigCircleRadius).toFloat()

                if(gameRunning){
                    score++
                    showScore.text = "分數：$score"
                }

                // 確保新位置不會超出螢幕邊界
                if (bigCircleX < bigCircleRadius) {
                    bigCircleX = bigCircleRadius
                } else if (bigCircleX > width - bigCircleRadius) {
                    bigCircleX = width - bigCircleRadius
                }

                if (bigCircleY < bigCircleRadius) {
                    bigCircleY = bigCircleRadius
                } else if (bigCircleY > height - bigCircleRadius) {
                    bigCircleY = height - bigCircleRadius
                }
            }
        }

        // 在 SurfaceView 中更新視圖
        if (canvas != null) {
            // 清除先前的內容
            canvas.drawColor(Color.BLACK)
            drawCircle(canvas)

            surfaceHolder.unlockCanvasAndPost(canvas)
        }




    }

    override fun onAccuracyChanged(p0: Sensor?, p1: Int) {
        //TODO("Not yet implemented")
    }
    private fun isCollision(x1: Float, y1: Float, r1: Float, x2: Float, y2: Float, r2: Float): Boolean {
        // 計算兩圓心之間的距離
        val distance = sqrt((x2 - x1).toDouble().pow(2.0) + (y2 - y1).toDouble().pow(2.0))

        // 判斷是否碰撞
        return distance < r1 + r2
    }

    private fun startCountdown(){
        countDownTimer.start()
    }

    private fun stopCountdown(){
        countDownTimer.cancel()
    }
    private fun setupLineChart() {
        lineChart.apply {
            description.isEnabled = false
            setTouchEnabled(false)
            isDragEnabled = false
            setScaleEnabled(false)
            setDrawGridBackground(false)
            axisLeft.setDrawGridLines(false)
            axisRight.setDrawGridLines(false)
            xAxis.setDrawGridLines(false)
        }
    }


    private fun simulateData() {
        // 模擬分數和時間的變化
        val timer = object : CountDownTimer(initialTimeMillis, 1000) {
            override fun onTick(p0: Long) {
                val secondsElapsed = (initialTimeMillis - p0) / 1000f // 獲取已經過的秒數
                // 每秒更新一次時間
                updateChartData(secondsElapsed, score)
                // 每秒更新一次分數

            }


            override fun onFinish() {
                // 計時結束
            }
        }

        timer.start()
    }

    private fun updateChartData(time: Float, score: Int) {
        // 更新時間和分數的折線圖資料
        timeData.add(Entry(time, score.toFloat()))
        val dataSet = LineDataSet(timeData, "Time vs Score")
        dataSet.color = Color.RED
        dataSet.valueTextColor = Color.WHITE

        val lineData = LineData(dataSet)
        lineChart.data = lineData

        // 設置 Y 軸的最小值為 0
        lineChart.axisLeft.axisMinimum = 0f

        // 更新圖表
        lineChart.invalidate()
    }

}