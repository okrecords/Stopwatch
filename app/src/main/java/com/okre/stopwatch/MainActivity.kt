package com.okre.stopwatch

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.okre.stopwatch.databinding.ActivityMainBinding

class MainActivity : AppCompatActivity() {

    private lateinit var bining: ActivityMainBinding

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


    }
}