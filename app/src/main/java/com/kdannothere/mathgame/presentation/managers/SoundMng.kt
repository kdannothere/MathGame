package com.kdannothere.mathgame.presentation.managers

import androidx.lifecycle.lifecycleScope
import com.kdannothere.mathgame.R
import com.kdannothere.mathgame.presentation.MainActivity
import com.kdannothere.mathgame.presentation.MathApp
import kotlinx.coroutines.async
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

object SoundMng {

    val musicResId = R.raw.music_melody
    private val soundClickResId = R.raw.sound_click
    private val soundCorrectResId = R.raw.sound_correct
    private val soundWrongResId = R.raw.sound_wrong

    fun playMusic(activity: MainActivity) {
        activity.lifecycleScope.launch(MathApp.dispatcherIO) {
            val isMusicOn = async { DataMng.loadMusicSetting(activity) }
            if (!isMusicOn.await() || activity.musicPlayer.isPlaying) return@launch
            withContext(MathApp.dispatcherMain) {
                activity.musicPlayer.isLooping = true
                activity.musicPlayer.start()
            }
        }
    }

    fun pauseMusic(activity: MainActivity) {
        if (activity.musicPlayer.isPlaying) {
            activity.lifecycleScope.launch(MathApp.dispatcherMain) {
                activity.musicPlayer.pause()
            }
        }
    }

    fun playSoundClick(
        activity: MainActivity,
        isSoundOn: Boolean,
    ) = playSound(activity, isSoundOn, soundClickResId)

    fun playSoundCorrect(
        activity: MainActivity,
        isSoundOn: Boolean,
    ) = playSound(activity, isSoundOn, soundCorrectResId)

    fun playSoundWrong(
        activity: MainActivity,
        isSoundOn: Boolean,
    ) = playSound(activity, isSoundOn, soundWrongResId)

    private fun playSound(
        activity: MainActivity,
        isSoundOn: Boolean,
        rawResId: Int,
    ) {
        if (!isSoundOn) return
        activity.lifecycleScope.launch(MathApp.dispatcherIO) {
            withContext(MathApp.dispatcherMain) {
                val assetFileDescriptor =
                    activity.resources.openRawResourceFd(rawResId) ?: return@withContext
                activity.soundPlayer.reset()
                activity.soundPlayer.setDataSource(
                    assetFileDescriptor.fileDescriptor,
                    assetFileDescriptor.startOffset,
                    assetFileDescriptor.length
                )
                assetFileDescriptor.close()
                activity.soundPlayer.prepareAsync()
            }
        }
    }
}