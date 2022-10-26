package com.okre.stopwatch

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.core.content.ContextCompat
import com.okre.stopwatch.databinding.ActivityMainBinding
import java.util.*
import kotlin.concurrent.timer

class MainActivity : AppCompatActivity() {

    companion object {
        const val START = "start"
        const val STOP = "stop"
        const val CONTINUE = "continue"
        const val RESET = "reset"
        const val RECORD = "record"
        const val RECORD_START = "noRecord"
    }

    private lateinit var bining: ActivityMainBinding
    private var right = START
    private var left = RECORD_START
    private var repeatedTime = 0
    private var timeTask: Timer? = null

    var frame: Int = 0
    var second: Int = 0
    var minute: Int = 0

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        bining = ActivityMainBinding.inflate(layoutInflater).also {
            setContentView(it.root)
        }

        if (savedInstanceState == null) {
            val ft = supportFragmentManager.beginTransaction()
            with(ft) {
                add(R.id.container, RecyclerFragment.newInstance())
                commit()
            }
        }

        with(bining) {
            btnStart.setOnClickListener {
                // 버튼 모양 변경
                buttonDesign(right)

                // 오른쪽: 시작, 중지, 계속
                if (right == START) { // 시작
                    start()
                    right = STOP
                    left = RECORD
                } else if (right == STOP) { // 중지
                    stop()
                    right = CONTINUE
                    left = RESET
                } else { // 계속
                    continueTimer()
                    right = STOP
                    left = RECORD
                }
            }

            btnRecord.setOnClickListener {
                // 버튼 모양 변경
                buttonDesign(left)

                // 왼쪽: 구간기록, 초기화
                if (left == RECORD) { // 구간기록
                } else if (left == RESET) { // 초기화
                    reset()
                    left = RECORD_START
                    right = START
                } else { //

                }
            }
        }
    }

    private fun start() {
        timeTaskStart()
    }

    private fun stop() {
        timeTask?.cancel()
    }

    private fun continueTimer() {
        timeTaskStart()
    }

    private fun buttonDesign(touch: String) {
        when (touch) {
            START, CONTINUE -> {
                // 오른쪽
                bining.btnStart.setBackgroundResource(R.drawable.background_btn_stop)
                bining.btnStart.text = getString(R.string.stopS)

                // 왼쪽
                bining.btnRecord.setTextColor(ContextCompat.getColor(baseContext!!, R.color.black))
                bining.btnRecord.text = getString(R.string.recordS)
            }
            STOP -> {
                // 오른쪽
                bining.btnStart.setBackgroundResource(R.drawable.background_btn_start)
                bining.btnStart.text = getString(R.string.continueS)

                // 왼쪽
                bining.btnRecord.text = getString(R.string.resetS)
            }
            RESET -> {
                // 오른쪽
                bining.btnStart.setBackgroundResource(R.drawable.background_btn_start)
                bining.btnStart.text = getString(R.string.startS)

                // 왼쪽
                bining.btnRecord.setTextColor(ContextCompat.getColor(baseContext!!, R.color.gray_dark))
                bining.btnRecord.text = getString(R.string.recordS)
            }
            else -> {}
        }
    }

    private fun timeTaskStart() {
        timeTask = kotlin.concurrent.timer(period = 10) {
            repeatedTime++
            frame = repeatedTime % 100
            second = repeatedTime / 100
            if (second == 60) {
                repeatedTime = 0
                minute ++
            }

            runOnUiThread {
                timeUi()
            }
        }
    }

    private fun timeUi() {
        with(bining) {
            wholeTextMinute.text = if (minute < 10) { "0$minute" } else { "$minute" }
            wholeTextSecond.text = if (second < 10) { "0$second" } else { "$second" }
            wholeTextFrame.text = if (frame < 10) { "0$frame" } else { "$frame" }
        }
    }

    private fun reset() {
        timeTask?.cancel()
        repeatedTime = 0
        frame = 0
        second = 0
        minute = 0
        runOnUiThread {
            timeUi()
        }
    }
}