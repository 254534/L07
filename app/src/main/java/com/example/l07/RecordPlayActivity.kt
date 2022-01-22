package com.example.l07

import android.Manifest
import android.content.Intent
import android.content.pm.PackageManager
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.media.MediaPlayer
import android.media.MediaRecorder
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import android.os.Environment
import android.util.Log
import android.widget.Button
import androidx.core.content.PackageManagerCompat.LOG_TAG
import java.io.IOException


class RecordPlayActivity : AppCompatActivity() {
    lateinit var mediaRecorder: MediaRecorder
    lateinit var mediaPlayer: MediaPlayer
    lateinit var fileName: String
    val REQUEST_AUDIO_PERMISSION_CODE = 11
    lateinit var recordStart: Button
    lateinit var recordStop: Button
    lateinit var playStart: Button
    lateinit var playStop: Button

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_record_play)
        fileName = "${externalCacheDir?.absolutePath}/audiorecordtest.3gp"

        if (ContextCompat.checkSelfPermission(this,
                Manifest.permission.RECORD_AUDIO) == PackageManager.PERMISSION_GRANTED) {
            recordStart = findViewById<Button>(R.id.buttonRecordStart)
            recordStop = findViewById<Button>(R.id.buttonRecordStop)
            playStart = findViewById(R.id.buttonAudioPlayStart)
            playStop = findViewById(R.id.buttonAudioPlayStop)
            recordStop.isEnabled = false
            playStop.isEnabled = false

            findViewById<Button>(R.id.buttonRecordStart).setOnClickListener {
                startRecording()
                recordStart.isEnabled = false
                recordStop.isEnabled = true
                playStart.isEnabled = false
            }
            findViewById<Button>(R.id.buttonRecordStop).setOnClickListener {
                stopRecording()
                recordStart.isEnabled = true
                recordStop.isEnabled = false
                playStart.isEnabled = true
            }

            findViewById<Button>(R.id.buttonAudioPlayStart).setOnClickListener {
                startPlaying()
            }
            findViewById<Button>(R.id.buttonAudioPlayStop).setOnClickListener {
                stopPlaying()
            }

        }

        else {
            ActivityCompat.requestPermissions(this, arrayOf(Manifest.permission.RECORD_AUDIO), REQUEST_AUDIO_PERMISSION_CODE)
        }
    }

    private fun startRecording() {
        mediaRecorder = MediaRecorder().apply {
            setAudioSource(MediaRecorder.AudioSource.MIC)
            setOutputFormat(MediaRecorder.OutputFormat.THREE_GPP)
            setOutputFile(fileName)
            setAudioEncoder(MediaRecorder.AudioEncoder.AMR_NB)
            prepare()
            start()
        }
    }

    private fun startPlaying() {
        mediaPlayer = MediaPlayer().apply {
            try {
                setDataSource(fileName)
                prepare()
                start()
            } catch (e: IOException) {
                Log.e(LOG_TAG, "prepare() failed")
            }
        }
    }

    private fun stopRecording() {
        mediaRecorder.apply {
            stop()
            release()
        }
    }

    private fun stopPlaying() {
        mediaPlayer.release()
    }


    fun info(text: String) {
        Toast.makeText(this, text, Toast.LENGTH_LONG).show()
    }
}