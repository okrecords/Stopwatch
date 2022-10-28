package com.okre.stopwatch

import android.os.Bundle
import android.util.Log
import android.view.View
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.ContextCompat
import com.okre.stopwatch.RecyclerFragment.Companion.mAdapter
import com.okre.stopwatch.RecyclerFragment.Companion.myData
import com.okre.stopwatch.databinding.ActivityMainBinding
import java.util.*

class MainActivity : AppCompatActivity() {

    companion object {
        const val START = "start"
        const val STOP = "stop"
        const val CONTINUE = "continue"
        const val RESET = "reset"
        const val RECORD = "record"
        const val RECORD_START = "noRecord"
    }

    private lateinit var binding: ActivityMainBinding
    private var right = START
    private var left = RECORD_START
    private var timeTask: Timer? = null

    private var repeatedTime = 0
    var frame: Int = 0
    var second: Int = 0
    var minute: Int = 0

    var rvRepeatedTime = 0
    var rvFrame: Int = 0
    var rvSecond: Int = 0
    var rvMinute: Int = 0
    var section = 0

    var wholeMinute: String = "00"
    var wholeSecond: String = "00"
    var wholeFrame: String = "00"

    var sectionMinute: String = "00"
    var sectionSecond: String = "00"
    var sectionFrame: String = "00"

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater).also {
            setContentView(it.root)
        }

        // 구간기록 fragment 연결
        if (savedInstanceState == null) {
            val ft = supportFragmentManager.beginTransaction()
            with(ft) {
                add(R.id.container, RecyclerFragment.newInstance())
                commit()
            }
        }

        with(binding) {
            btnStart.setOnClickListener { // 오른쪽 버튼 클릭
                // 버튼 모양 변경
                buttonDesign(right)

                // 오른쪽: 시작, 중지, 계속
                when (right) {
                    START -> { // 시작
                        start()
                        right = STOP
                        left = RECORD
                    }
                    STOP -> {// 중지
                        stop()
                        right = CONTINUE
                        left = RESET
                    }
                    else -> {// 계속
                        continueTimer()
                        right = STOP
                        left = RECORD
                    }
                }
            }

            btnRecord.setOnClickListener { // 왼쪽 버튼 클릭
                // 버튼 모양 변경
                buttonDesign(left)

                // 왼쪽: 구간기록, 초기화
                when (left) {
                    RECORD -> { // 구간기록
                        record()
                        layoutTimeSection.visibility = View.VISIBLE
                        layoutRv.visibility = View.VISIBLE
                    }
                    RESET -> { // 초기화
                        reset()
                        layoutTimeSection.visibility = View.GONE
                        layoutRv.visibility = View.INVISIBLE
                        left = RECORD_START
                        right = START
                    }
                    else -> {} // 구간기록 처음
                }
            }
        }
    }

    // 시작
    private fun start() {
        timeTaskStart()
    }

    // 중지
    private fun stop() {
        timeTask?.cancel()
    }

    //계속
    private fun continueTimer() {
        timeTaskStart()
    }

    // 버튼 모양 변경
    private fun buttonDesign(touch: String) {
        when (touch) {
            START, CONTINUE -> { // 시작, 계속
                // 오른쪽
                binding.btnStart.setBackgroundResource(R.drawable.background_btn_stop)
                binding.btnStart.text = getString(R.string.stopS)

                // 왼쪽
                binding.btnRecord.setTextColor(ContextCompat.getColor(baseContext!!, R.color.black))
                binding.btnRecord.text = getString(R.string.recordS)
            }
            STOP -> { // 중지
                // 오른쪽
                binding.btnStart.setBackgroundResource(R.drawable.background_btn_start)
                binding.btnStart.text = getString(R.string.continueS)

                // 왼쪽
                binding.btnRecord.text = getString(R.string.resetS)
            }
            RESET -> { // 초기화
                // 오른쪽
                binding.btnStart.setBackgroundResource(R.drawable.background_btn_start)
                binding.btnStart.text = getString(R.string.startS)

                // 왼쪽
                binding.btnRecord.setTextColor(ContextCompat.getColor(baseContext!!, R.color.gray_dark))
                binding.btnRecord.text = getString(R.string.recordS)
            }
            else -> {} // 구간기록 버튼 모양 변화 없음
        }
    }

    // 타이머 시작
    private fun timeTaskStart() {
        timeTask = kotlin.concurrent.timer(period = 10) { // 0.01초 마다 작동
            // 메인 타이머
            repeatedTime++
            frame = repeatedTime % 100
            second = repeatedTime / 100
            if (second == 60) { // 60초 되면 분++
                repeatedTime = 0
                minute ++
            }

            // 구간 기록 타이머
            rvRepeatedTime++
            rvFrame = rvRepeatedTime % 100
            rvSecond = rvRepeatedTime / 100
            if (rvSecond == 60) {
                rvRepeatedTime = 0
                rvMinute++
            }

            // ui 변경 thread
            runOnUiThread {
                timeUi()
            }
        }
    }

    // ui 변경 함수
    private fun timeUi() {
        with(binding) {
            // 메인 타이머
            wholeMinute = if (minute < 10) { "0$minute" } else { "$minute" }
            wholeSecond = if (second < 10) { "0$second" } else { "$second" }
            wholeFrame = if (frame < 10) { "0$frame" } else { "$frame" }
            wholeTextMinute.text = wholeMinute
            wholeTextSecond.text = wholeSecond
            wholeTextFrame.text = wholeFrame

            // 구간 기록 타이머
            sectionMinute = if (rvMinute < 10) { "0$rvMinute" } else { "$rvMinute" }
            sectionSecond = if (rvSecond < 10) { "0$rvSecond" } else { "$rvSecond" }
            sectionFrame = if (rvFrame < 10) { "0$rvFrame" } else { "$rvFrame" }
            sectionTextMinute.text = sectionMinute
            sectionTextSecond.text = sectionSecond
            sectionTextFrame.text = sectionFrame
        }
    }

    // 초기화
    private fun reset() {
        timeTask?.cancel()

        // 메인 타이머 초기화
        repeatedTime = 0
        frame = 0
        second = 0
        minute = 0

        // 구간 기록 타이머 초기화
        section = 0
        rvRepeatedTime = 0
        rvFrame = 0
        rvSecond = 0
        rvMinute = 0

        myData.removeAll(myData)
        mAdapter.notifyItemRangeRemoved(0, mAdapter.itemCount)

        // ui 변경 thread
        runOnUiThread {
            timeUi()
        }
    }

    // 구간 기록
    private fun record() {
        section++
        val sectionData = if (section < 10) { "0$section" } else { "$section" }

        val recordData = "$sectionMinute:$sectionSecond.$sectionFrame"
        val wholeTimeData = "$wholeMinute:$wholeSecond.$wholeFrame"

        myData.add(RecordData(sectionData, recordData, wholeTimeData))
        mAdapter.notifyItemInserted(myData.size)

        rvRepeatedTime = 0
        rvFrame = 0
        rvSecond = 0
        rvMinute = 0
    }
}