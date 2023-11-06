package com.kdannothere.mathgame.managers

import android.content.Context
import android.media.MediaPlayer
import com.kdannothere.mathgame.R
import com.kdannothere.mathgame.presentation.MathApp
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.async
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

object SoundManager {

    val musicResId = R.raw.music_melody
    private val soundClickResId = R.raw.sound_click
    private val soundCorrectResId = R.raw.sound_correct
    private val soundWrongResId = R.raw.sound_wrong

    fun playMusic(mediaPlayer: MediaPlayer, context: Context, scope: CoroutineScope) {
        scope.launch(MathApp.dispatcherIO) {
            val isMusicOn = async { DataManager.loadMusicSetting(context) }
            if (!isMusicOn.await() || mediaPlayer.isPlaying) return@launch
            withContext(MathApp.dispatcherMain) {
                mediaPlayer.isLooping = true
                mediaPlayer.start()
            }
        }
    }

    fun pauseMusic(mediaPlayer: MediaPlayer, scope: CoroutineScope) {
        if (mediaPlayer.isPlaying) {
            scope.launch(MathApp.dispatcherMain) {
                mediaPlayer.pause()
            }
        }
    }

    fun playSoundClick(
        mediaPlayer: MediaPlayer,
        context: Context,
        scope: CoroutineScope,
        isSoundOn: Boolean,
    ) = playSound(mediaPlayer, context, scope, isSoundOn, soundClickResId)

    fun playSoundCorrect(
        mediaPlayer: MediaPlayer,
        context: Context,
        scope: CoroutineScope,
        isSoundOn: Boolean,
    ) = playSound(mediaPlayer, context, scope, isSoundOn, soundCorrectResId)

    fun playSoundWrong(
        mediaPlayer: MediaPlayer,
        context: Context,
        scope: CoroutineScope,
        isSoundOn: Boolean,
    ) = playSound(mediaPlayer, context, scope, isSoundOn, soundWrongResId)

    private fun playSound(
        mediaPlayer: MediaPlayer,
        context: Context,
        scope: CoroutineScope,
        isSoundOn: Boolean,
        rawResId: Int,
    ) {
        if (!isSoundOn) return
        scope.launch(MathApp.dispatcherIO) {
            withContext(MathApp.dispatcherMain) {
                val assetFileDescriptor =
                    context.resources.openRawResourceFd(rawResId) ?: return@withContext
                mediaPlayer.reset()
                mediaPlayer.setDataSource(
                    assetFileDescriptor.fileDescriptor,
                    assetFileDescriptor.startOffset,
                    assetFileDescriptor.length
                )
                assetFileDescriptor.close()
                mediaPlayer.prepareAsync()
            }
        }
    }
}