package com.example.learningenglish.aichat.worker

import android.media.AudioAttributes
import android.media.MediaPlayer
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import timber.log.Timber


sealed interface IAudioPlayerState {
    object Idle : IAudioPlayerState
    object Playing : IAudioPlayerState
    object Paused : IAudioPlayerState
    object Stopped : IAudioPlayerState
    object Error : IAudioPlayerState
    object Loading : IAudioPlayerState
    object Completed : IAudioPlayerState
}


//https://juejin.cn/post/7367659706867335179
class ChatAudioPlayer {

    private val player: MediaPlayer = MediaPlayer()

    private val _state = MutableStateFlow<IAudioPlayerState>(IAudioPlayerState.Idle)
    val state: StateFlow<IAudioPlayerState> = _state
    private val coroutineScope = CoroutineScope(Dispatchers.IO)

    init {
        player.setOnCompletionListener {
            emitState(IAudioPlayerState.Completed)
            emitState(IAudioPlayerState.Idle)
        }
        player.setOnErrorListener { mp, what, extra ->
            Timber.d("onError: $what, $extra")
            emitState(IAudioPlayerState.Error)
            true
        }

        player.setOnInfoListener { mp, what, extra ->
            when (what) {
                MediaPlayer.MEDIA_INFO_BUFFERING_START -> {
                    emitState(IAudioPlayerState.Loading)
                }

                MediaPlayer.MEDIA_INFO_BUFFERING_END -> {
                    emitState(IAudioPlayerState.Playing)
                }
            }
            true
        }

    }

    fun play() {
        stop()
        player.start()
    }

    fun pause() {
        if (player.isPlaying) {
            player.pause()
            emitState(IAudioPlayerState.Paused)
        }
    }

    fun stop() {
        if (player.isPlaying) {
            emitState(IAudioPlayerState.Stopped)
            player.stop()
            player.release()
        }
    }

    fun play(path: String) {
        try {
            stop()
            Timber.d(" ready play : ")
            emitState(IAudioPlayerState.Loading)
            player.reset()
            player.setDataSource(path)
            player.setAudioAttributes(
                AudioAttributes.Builder()
                    .setContentType(AudioAttributes.CONTENT_TYPE_MUSIC)
                    .build()
            );
            player.setOnPreparedListener {
                Timber.d(" prepared : ")
                emitState(IAudioPlayerState.Playing)
                player.start()
            }
            Timber.d(" to prepare : ")
            player.prepareAsync();

        } catch (e: Exception) {
            e.printStackTrace()
            emitState(IAudioPlayerState.Error)
        }
    }

    private fun emitState(state: IAudioPlayerState) {
        coroutineScope.launch {
            _state.emit(state)
        }
    }

}
