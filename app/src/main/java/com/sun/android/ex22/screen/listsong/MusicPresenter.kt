package com.sun.android.ex22.screen.listsong

import android.content.*
import android.os.Handler
import android.os.IBinder
import android.os.Looper
import android.support.v4.media.session.PlaybackStateCompat
import android.widget.Toast
import com.google.android.exoplayer2.ExoPlayer
import com.google.android.exoplayer2.MediaItem
import com.google.android.exoplayer2.Player
import com.google.android.exoplayer2.ui.PlayerNotificationManager
import com.sun.android.ex22.data.model.Song
import com.sun.android.ex22.data.repository.SongRepository
import com.sun.android.ex22.data.repository.source.OnResultListener
import com.sun.android.ex22.data.repository.source.SongLocalDataSource
import com.sun.android.ex22.service.MusicPlayerService
import com.sun.android.ex22.utils.SongUtils

class MusicPresenter private constructor(private val songRepository: SongRepository?)
    : SongContract.Presenter {

    private var mView: SongContract.View? = null
    private var mViewController: PlaybackControllerFragment? = null
    private var isBound = false
    private var isFirstTime = true
    private var isLooperPaused = true
    private val mMusicReceiver by lazy { MusicReceiver() }
    private var playerService: MusicPlayerService? = null

    private val playerServiceConnection = object : ServiceConnection {
        override fun onServiceConnected(name: ComponentName?, service: IBinder?) {
            val binder: MusicPlayerService.ServiceBinder = service as MusicPlayerService.ServiceBinder
            playerService = binder.getPlayerService()
            val player = playerService?.player
            isBound = true
            loadSongs()
            player?.addListener(object : Player.Listener {

                override fun onMediaItemTransition(mediaItem: MediaItem?, reason: Int) {
                    super.onMediaItemTransition(mediaItem, reason)
                    mViewController?.updateUI(mediaItem?.mediaMetadata, player.currentPosition.toInt(), player.duration.toInt())
                    resumeLooper()

                    if (!isFirstTime)
                        player.let {
                            if (!it.isPlaying) {
                                it.prepare()
                                it.play()
                            }
                        }
                }

                override fun onPlaybackStateChanged(playbackState: Int) {
                    super.onPlaybackStateChanged(playbackState)
                    if (playbackState == ExoPlayer.STATE_READY) {
                        mViewController?.updateUI(player.currentMediaItem?.mediaMetadata, player.currentPosition.toInt(), player.duration.toInt())
                    }
                }
            })
        }

        override fun onServiceDisconnected(name: ComponentName?) {
            TODO("Not yet implemented")
        }
    }

    override fun loadSongs() {
        songRepository?.getSongsLocal(object : OnResultListener<MutableList<Song>> {
            override fun onSuccess(data: MutableList<Song>) {
                mView?.onGetSongsSuccess(data)
                playerService?.setMediaItems(SongUtils.getMediaItems(data))
            }

            override fun onError(exception: String?) {
                mView?.onError(exception)
            }
        }, mView?.getViewContext())
    }

    override fun playOrPauseMusic() {
        if (isFirstTime) {
            resumeLooper()
            isFirstTime = false
        }
        playerService?.playOrPauseMusic(
            onPlay = { mViewController?.showPause() },
            onPause = { mViewController?.showPlay() }
        )
    }

    override fun startMusic(index: Int?) {
        index?.let {
            playerService?.startMusic(it) {
                Toast.makeText(mView?.getViewContext(), "No song in player", Toast.LENGTH_SHORT).show()
            }
            mViewController?.showPause()
        }

    }

    override fun pauseMusic() {
        playerService?.pauseMusic()
        mViewController?.showPlay()
    }

    override fun onStart() {
        TODO("Not yet implemented")
    }

    override fun onStop() {
        isLooperPaused = true
        isFirstTime = true
        doUnBindService()
    }

    override fun setView(view: SongContract.View?) {
        mView = view
        doBindService()
    }

    override fun setViewController(view: PlaybackControllerFragment?) {
        mViewController = view
        mViewController?.activity?.registerReceiver(
            mMusicReceiver,
            IntentFilter().apply {
                    addAction(PlayerNotificationManager.ACTION_PAUSE)
                    addAction(PlayerNotificationManager.ACTION_PLAY)
                }
        )
    }

    private fun doBindService() {
        val playerServiceIntent = Intent(mView?.getViewContext(), MusicPlayerService::class.java)
        mView?.getViewContext()?.bindService(playerServiceIntent, playerServiceConnection, Context.BIND_AUTO_CREATE)
        isBound = true
    }

    private fun doUnBindService() {
        if (isBound) {
            mView?.getViewContext()?.unbindService(playerServiceConnection)
            isBound = false
        }
    }

    fun pauseLooper() {
        isLooperPaused = true
    }

    fun resumeLooper() {
        isLooperPaused = false
        updatePlayerPositionProgress()
    }

    private fun updatePlayerPositionProgress() {
        Handler(Looper.getMainLooper()).postDelayed({
            if (!isLooperPaused) {
                playerService?.let {
                    if (it.getIsPlaying() == true) {
                        it.getCurrentPlayerPosition()?.let { pos -> mViewController?.updateSeekbar(pos.toInt()) }
                    }
                }
                resumeLooper()
            }
        }, 1000)
    }

    fun updatePlayerPositionProgress(progressPercent: Float) {
        playerService?.seekTo(progressPercent)
        resumeLooper()
    }

    fun skipToNextSong() {
        playerService?.skipToNextSong()
    }

    fun skipToPreviousSong() {
        playerService?.skipToPreviousSong()
    }

    inner class MusicReceiver : BroadcastReceiver() {
        override fun onReceive(context: Context?, intent: Intent?) {
            intent?.let {
                playOrPauseMusic()

                when (it.action) {
                    PlayerNotificationManager.ACTION_PAUSE -> {
                        playerService?.setPlayOrPausedState(PlaybackStateCompat.STATE_PAUSED)
                    }
                    PlayerNotificationManager.ACTION_PLAY -> {
                        playerService?.setPlayOrPausedState(PlaybackStateCompat.STATE_PLAYING)
                    }
                    else -> {}
                }
            }
        }
    }

    companion object {
        private var instance: MusicPresenter? = null

        fun getInstance() = synchronized(this) {
            instance ?: MusicPresenter(
                SongRepository.getInstance(
                    SongLocalDataSource.getInstance()
                )
            ).also { instance = it }
        }
    }
}

