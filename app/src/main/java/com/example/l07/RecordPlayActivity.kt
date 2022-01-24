package com.example.l07

import android.Manifest
import android.content.Intent
import android.content.pm.PackageManager
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.media.MediaPlayer
import android.media.MediaRecorder
import androidx.appcompat.app.AlertDialog
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import android.os.Environment
import android.util.Log
import androidx.core.content.PackageManagerCompat.LOG_TAG
import java.io.IOException
import android.media.MediaMetadataRetriever
import android.net.Uri
import android.widget.*
import kotlin.concurrent.thread


class RecordPlayActivity : AppCompatActivity() {
    lateinit var mediaRecorder: MediaRecorder
    var mediaPlayer: MediaPlayer? = null
    lateinit var fileName: String
    val REQUEST_AUDIO_PERMISSION_CODE = 11
    lateinit var recordStart: Button
    lateinit var recordStop: Button
    lateinit var playStart: Button
    lateinit var playStop: Button
    lateinit var playPause: Button
    lateinit var progressBar: SeekBar

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
            playPause = findViewById(R.id.buttonAudioPlayPause)
            progressBar = findViewById(R.id.progressBarAudio)

            progressBar.setOnSeekBarChangeListener(object : SeekBar.OnSeekBarChangeListener {
                override fun onProgressChanged(
                    seekBar: SeekBar?,
                    progress: Int,
                    fromUser: Boolean
                ) {
                    val currentPos = seekBar!!.progress
                    val minutes = (currentPos / 60000) % 60
                    val seconds = (currentPos / 1000) % 60
                    val milisec = currentPos % 1000

                    val timeString = String.format("%02d:%02d:%03d", minutes, seconds, milisec)
                    findViewById<TextView>(R.id.textCurrentTime).text = timeString
                }

                override fun onStartTrackingTouch(seekBar: SeekBar?) {
                    pausePlaying()
                }

                override fun onStopTrackingTouch(seekBar: SeekBar?) {
                    if(seekBar != null  && mediaPlayer != null) {
                        mediaPlayer!!.seekTo(seekBar.progress)
                        mediaPlayer!!.start()
                        setupProgressBar()
                    }
                }

            })

            recordStop.isEnabled = false
            playStop.isEnabled = false
            playPause.isEnabled = false

            recordStart.setOnClickListener {
                startRecording()
                recordStart.isEnabled = false
                recordStop.isEnabled = true
                playStart.isEnabled = false
            }
            recordStop.setOnClickListener {
                stopRecording()
                recordStart.isEnabled = true
                recordStop.isEnabled = false
                playStart.isEnabled = true
            }

            playStart.setOnClickListener {
                if (mediaPlayer == null) {
                    startPlaying()
                }
                else {
                    mediaPlayer!!.start()
                }
                setupProgressBar()


                mediaPlayer!!.setOnCompletionListener {
                    playStop.isEnabled = false
                    playStart.isEnabled = true
                    playPause.isEnabled = false
                }
            }

            playPause.setOnClickListener {
                pausePlaying()
                playPause.isEnabled = false
                playStart.isEnabled = true
            }

            playStop.setOnClickListener {
                stopPlaying()
                progressBar.progress = 0
                playStop.isEnabled = false
                playPause.isEnabled = false
                playStart.isEnabled = true
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

    private fun pausePlaying() {
        if(mediaPlayer != null) {
            mediaPlayer!!.pause()
        }
    }

    private fun setupProgressBar() {
        playStart.isEnabled = false
        playPause.isEnabled = true
        playStop.isEnabled = true
        progressBar.max = mediaPlayer!!.duration
        progressBar.progress = 0

        Thread(Runnable {
            var currentPos: Int
            while (mediaPlayer != null && mediaPlayer!!.isPlaying) {
                currentPos =  mediaPlayer!!.currentPosition
                progressBar.progress = currentPos
            }
        }).start()
    }
    private fun stopRecording() {
        mediaRecorder.apply {
            stop()
            release()
        }
    }

    private fun stopPlaying() {
        mediaPlayer!!.stop()
        mediaPlayer!!.reset()
        mediaPlayer!!.release()
        mediaPlayer = null
    }


    fun info(text: String) {
        Toast.makeText(this, text, Toast.LENGTH_LONG).show()
    }
}