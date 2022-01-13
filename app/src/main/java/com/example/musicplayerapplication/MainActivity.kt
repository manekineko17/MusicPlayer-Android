package com.example.musicplayerapplication

import android.media.MediaPlayer
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.util.Log
import android.widget.Button
import android.widget.SeekBar

class MainActivity : AppCompatActivity() {

    //change the seekbar position while the song is playing with a runnable object and a handler
    lateinit var runnable: Runnable
    private var handler = Handler(Looper.getMainLooper())
    private var musicPlayer : MediaPlayer? = null
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        val playButton: Button = findViewById(R.id.playButton)
        val stopButton: Button = findViewById(R.id.stopButton)
        val musicSeekbar: SeekBar = findViewById(R.id.musicSeekbar)
        val volumeBar: SeekBar = findViewById(R.id.volumeBar)
        val pauseButton : Button = findViewById(R.id.pauseButton)

        //seekbar starts from 0
        musicSeekbar.progress = 0

        //play button event
        playButton.setOnClickListener {
            //if mediaplayer was not playing, the button will change for "pause"
            if (musicPlayer == null) {
                musicPlayer = MediaPlayer.create(this, R.raw.koto)
                //seekbar max value
                musicSeekbar.max = musicPlayer!!.duration
            }
            musicPlayer?.start()
        }

        //pause button event
        pauseButton.setOnClickListener{
            if (musicPlayer != null){
                musicPlayer?.pause()
            }
        }

        //stop button
         stopButton.setOnClickListener {
            if (musicPlayer != null) {
                musicPlayer?.stop()
                musicPlayer?.seekTo(0)
                musicPlayer?.reset()
                musicPlayer?.release()
                musicPlayer=null
             }
        }

        //seekbar event
        musicSeekbar.setOnSeekBarChangeListener(object : SeekBar.OnSeekBarChangeListener {
            override fun onProgressChanged(seekBar: SeekBar?, pos: Int, fromUser: Boolean) {
                //when we change the position of the seekbar the music will go to that position
                if (fromUser) {
                    musicPlayer?.seekTo(pos)
                    Log.d("TAG", "onProgressChanged: $pos")}
            }
            override fun onStartTrackingTouch(p0: SeekBar?) {
            }
            override fun onStopTrackingTouch(p0: SeekBar?) {
            }
        })

        //runnable music
        runnable = Runnable {
            musicSeekbar.progress = musicPlayer?.currentPosition ?: 0
            handler.postDelayed(runnable, 1000)
        }
        handler.postDelayed(runnable, 1000)

        //back to 0 when the music is finished
        musicPlayer?.setOnCompletionListener {
            playButton.setBackgroundResource(R.drawable.ic_baseline_play_circle_outline_24)
            musicSeekbar.progress = 0
        }

        //volume bar
        volumeBar.setOnSeekBarChangeListener(
            object : SeekBar.OnSeekBarChangeListener {
                override fun onProgressChanged(
                    seekBar: SeekBar?,
                    progress: Int,
                    fromUser: Boolean
                ) {
                    if (fromUser) {
                        val volume = progress / 100.0f
                        musicPlayer?.setVolume(volume, volume)
                    }
                }
                override fun onStartTrackingTouch(p0: SeekBar?) {
                }
                override fun onStopTrackingTouch(p0: SeekBar?) {
                }
            }
        )
    }
}