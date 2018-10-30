package com.egco428.ex8_accelerometer

import android.content.Context
import android.graphics.Color
import android.hardware.Sensor
import android.hardware.SensorEvent
import android.hardware.SensorEventListener
import android.hardware.SensorManager
import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import android.widget.Toast
import kotlinx.android.synthetic.main.activity_main.*

class MainActivity : AppCompatActivity(), SensorEventListener {

    private var sensorManager: SensorManager? = null
    private var lastUpdate: Long = 0
    private var color = false

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        sensorManager = getSystemService(Context.SENSOR_SERVICE) as SensorManager
        textView.setBackgroundColor(Color.GREEN)
        lastUpdate = System.currentTimeMillis()

    }

    override fun onAccuracyChanged(sensor: Sensor?, accuracy: Int) {

    }

    override fun onSensorChanged(event: SensorEvent?) {
        if(event!!.sensor.type == Sensor.TYPE_ACCELEROMETER) {
            getAccelerometer(event)
        }
    }

    private fun getAccelerometer(event: SensorEvent?){
        val value = event!!.values
        val x = value[0]
        val y = value[1]
        val z = value[2]

        val accel = (x*x+y*y+z*z)/(SensorManager.GRAVITY_EARTH*SensorManager.GRAVITY_EARTH)
        val actualTime = System.currentTimeMillis()
        if(accel>=2){
            if(actualTime-lastUpdate < 200) {
                return
            }
            lastUpdate = actualTime
            Toast.makeText(this,"Device was shuffled",Toast.LENGTH_SHORT).show()
            if(color){
                textView.setBackgroundColor(Color.GREEN)
            } else {
                textView.setBackgroundColor(Color.RED)
            }
            color = !color
        }
    }

    override fun onPause() {
        super.onPause()
        sensorManager!!.unregisterListener(this)
    }

    override fun onResume() {
        super.onResume()
        sensorManager!!.registerListener(this, sensorManager!!.getDefaultSensor(Sensor.TYPE_ACCELEROMETER),SensorManager.SENSOR_DELAY_NORMAL)
    }
}
