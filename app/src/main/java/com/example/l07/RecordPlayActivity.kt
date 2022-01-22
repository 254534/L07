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
    var audioDirPath: String? = null
    var audioFilePath: String? = null
    var audioFileName = "audio_filename1" + ".3gp"
    lateinit var fileName: String
    val REQUEST_AUDIO_PERMISSION_CODE = 11

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_record_play)
        fileName = "${externalCacheDir?.absolutePath}/audiorecordtest.3gp"

        if (ContextCompat.checkSelfPermission(this,
                Manifest.permission.RECORD_AUDIO) == PackageManager.PERMISSION_GRANTED) {

            findViewById<Button>(R.id.buttonRecordStart).setOnClickListener {
                mediaRecorder = MediaRecorder().apply {
                    setAudioSource(MediaRecorder.AudioSource.MIC)
                    setOutputFormat(MediaRecorder.OutputFormat.THREE_GPP)
                    setOutputFile(fileName)
                    setAudioEncoder(MediaRecorder.AudioEncoder.AMR_NB)
                    prepare()
                    start()
                }
            }
            findViewById<Button>(R.id.buttonRecordStop).setOnClickListener {
                mediaRecorder.apply {
                    stop()
                    release()
                }
            }

            findViewById<Button>(R.id.buttonAudioPlayStart).setOnClickListener {
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

            findViewById<Button>(R.id.buttonAudioPlayStop).setOnClickListener {
                mediaPlayer.release()
            }

        }

        else {
            ActivityCompat.requestPermissions(this, arrayOf(Manifest.permission.RECORD_AUDIO), REQUEST_AUDIO_PERMISSION_CODE)
        }
    }

    fun info(text: String) {
        Toast.makeText(this, text, Toast.LENGTH_LONG).show()
    }
}