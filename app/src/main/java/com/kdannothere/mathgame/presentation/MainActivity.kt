package com.kdannothere.mathgame.presentation

import android.media.MediaPlayer
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.lifecycleScope
import androidx.navigation.Navigation
import com.kdannothere.mathgame.R
import com.kdannothere.mathgame.databinding.ActivityMainBinding
import com.kdannothere.mathgame.managers.LangManager
import com.kdannothere.mathgame.managers.SoundManager

class MainActivity : AppCompatActivity() {

    private var _binding: ActivityMainBinding? = null
    private val binding get() = _binding!!

    val musicPlayer: MediaPlayer by lazy {
        MediaPlayer.create(this, SoundManager.musicResId)
    }
    private val soundPlayer: MediaPlayer by lazy {
        MediaPlayer()
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        LangManager.setLanguage(this, lifecycleScope)
        super.onCreate(savedInstanceState)

        _binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        soundPlayer.setOnPreparedListener {
            soundPlayer.start()
        }

        soundPlayer.setOnCompletionListener {
            soundPlayer.reset()
        }
    }

    override fun onPause() {
        super.onPause()
        SoundManager.pauseMusic(musicPlayer, lifecycleScope)
    }

    override fun onResume() {
        super.onResume()
        SoundManager.playMusic(musicPlayer, applicationContext, lifecycleScope)
    }

    override fun onSupportNavigateUp(): Boolean {
        return Navigation.findNavController(this, R.id.nav_host_fragment_container).navigateUp()
    }

    override fun onDestroy() {
        super.onDestroy()
        _binding = null
    }
}