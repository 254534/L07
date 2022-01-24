package com.example.l07

import android.Manifest
import android.content.Intent
import android.content.pm.PackageManager
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.provider.MediaStore
import android.widget.Button
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat

class MainActivity : AppCompatActivity() {
    val REQUEST_VIDEO_CAPTURE = 1

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

        if (ContextCompat.checkSelfPermission(this,
                Manifest.permission.WRITE_EXTERNAL_STORAGE) == PackageManager.PERMISSION_GRANTED) {
            findViewById<Button>(R.id.buttonStartRecording).setOnClickListener {
                var intent = Intent(MediaStore.ACTION_VIDEO_CAPTURE)
                intent.resolveActivity(packageManager)
                startActivityForResult(intent, REQUEST_VIDEO_CAPTURE)
            }
        }
        else {
            ActivityCompat.requestPermissions(this, arrayOf(Manifest.permission.WRITE_EXTERNAL_STORAGE), REQUEST_VIDEO_CAPTURE)
        }

    }
}