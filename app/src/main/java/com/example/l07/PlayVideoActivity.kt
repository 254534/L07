package com.example.l07

import android.media.MediaParser
import android.media.MediaPlayer
import android.net.Uri
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.MediaController
import android.widget.VideoView

class PlayVideoActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_play_video)
        var videoView = findViewById<VideoView>(R.id.videoViewPlay)
        videoView.setVideoURI(Uri.parse("android.resource://" + packageName + "/" + R.raw.sample))

        var mController = MediaController(this)
        videoView.setMediaController(mController)

        videoView.setOnPreparedListener(MediaPlayer.OnPreparedListener {
            videoView.start()
        })
    }
}