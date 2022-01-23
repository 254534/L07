package com.example.l07

import android.content.Intent
import android.media.MediaParser
import android.media.MediaPlayer
import android.net.Uri
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.provider.MediaStore
import android.widget.MediaController
import android.widget.Toast
import android.widget.VideoView

class PlayVideoActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_play_video)
        var videoView = findViewById<VideoView>(R.id.videoViewPlay)

        val iii: Intent = intent
        val bundle = iii.extras
        var val1:String? = bundle?.getString("title")
        if(val1 == null) {
            videoView.setVideoURI(Uri.parse("android.resource://" + packageName + "/" + R.raw.sample))
        }
        else {
            val mUri: Uri = Uri.withAppendedPath(MediaStore.Video.Media.EXTERNAL_CONTENT_URI, val1)
//            Toast.makeText(this, mUri.toString() + "/" + val1, Toast.LENGTH_LONG).show()
            videoView.setVideoURI(mUri)
        }

        var mController = MediaController(this)
        videoView.setMediaController(mController)


        videoView.setOnPreparedListener(MediaPlayer.OnPreparedListener {
            videoView.start()
        })
    }
}