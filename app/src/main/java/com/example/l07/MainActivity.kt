package com.example.l07

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Button

class MainActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        findViewById<Button>(R.id.buttonRecordPlay).setOnClickListener {
            startActivity(Intent(this, RecordPlayActivity::class.java))
        }

        findViewById<Button>(R.id.buttonRecordVideo).setOnClickListener {
            startActivity(Intent(this, RecordVideoActivity::class.java))
        }

        findViewById<Button>(R.id.buttonPlayVideo).setOnClickListener {
            startActivity(Intent(this, PlayVideoActivity::class.java))
        }
    }
}