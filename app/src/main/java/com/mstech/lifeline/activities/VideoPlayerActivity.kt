package com.mstech.lifeline.activities

import android.media.MediaPlayer
import android.net.Uri
import android.os.Bundle
import android.widget.MediaController
import android.widget.VideoView
import androidx.appcompat.app.AppCompatActivity
import com.blankj.utilcode.util.ToastUtils
import com.mstech.lifeline.R


class VideoPlayerActivity : AppCompatActivity() {
    var simpleVideoView: VideoView? = null
    var mediaControls: MediaController? = null
    var path = "";
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_video_player)
        supportActionBar?.hide()
        simpleVideoView = findViewById(R.id.simpleVideoView);

        if (intent.extras!=null){
            path = intent.getStringExtra("path").toString()
        }
        if (mediaControls == null) {
            // create an object of media controller class
            mediaControls =  MediaController(this);
            mediaControls!!.setAnchorView(simpleVideoView);
            simpleVideoView!!.setMediaController(mediaControls);
        }
        // set the media controller for video view

        // set the uri for the video view
        simpleVideoView!!.setVideoURI(Uri.parse(path));
        // start a video
        simpleVideoView!!.start();

        simpleVideoView!!.setOnCompletionListener(MediaPlayer.OnCompletionListener {
            ToastUtils.showShort("Thank You!!")

        })

        simpleVideoView!!.setOnErrorListener(MediaPlayer.OnErrorListener { mediaPlayer, i, i1 ->
            ToastUtils.showShort("Oops An Error Occur While Playing Video...!!!")
            finish()
            true
        })



    }
}