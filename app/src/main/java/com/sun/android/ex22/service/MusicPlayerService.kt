package com.sun.android.ex22.service

import android.app.Notification
import android.app.PendingIntent
import android.app.Service
import android.content.Intent
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.os.Binder
import android.os.IBinder
import android.provider.MediaStore
import android.support.v4.media.MediaMetadataCompat
import android.support.v4.media.session.MediaSessionCompat
import android.support.v4.media.session.PlaybackStateCompat
import androidx.core.app.NotificationCompat
import androidx.core.app.NotificationManagerCompat.IMPORTANCE_HIGH
import com.google.android.exoplayer2.C
import com.google.android.exoplayer2.ExoPlayer
import com.google.android.exoplayer2.MediaItem
import com.google.android.exoplayer2.Player
import com.google.android.exoplayer2.audio.AudioAttributes
import com.google.android.exoplayer2.ui.PlayerNotificationManager
import com.sun.android.R
import com.sun.android.ex22.screen.MusicPlayerActivity

class MusicPlayerService : Service() {
    private val serviceBinder = ServiceBinder()
    private lateinit var mediaSession : MediaSessionCompat
    var player : ExoPlayer? = null
    private var notificationManager : PlayerNotificationManager? = null

    private val descriptionAdapter = object : PlayerNotificationManager.MediaDescriptionAdapter {
        override fun getCurrentContentTitle(player: Player): CharSequence {
            return player.currentMediaItem?.mediaMetadata?.title ?: ""
        }

        override fun createCurrentContentIntent(player: Player): PendingIntent? {
            val intent = Intent(applicationContext, MusicPlayerActivity::class.java)

            return PendingIntent.getActivity(applicationContext, 0, intent, PendingIntent.FLAG_IMMUTABLE or PendingIntent.FLAG_UPDATE_CURRENT)
        }

        override fun getCurrentContentText(player: Player): CharSequence? {
            return null
        }

        override fun getCurrentLargeIcon(player: Player, callback: PlayerNotificationManager.BitmapCallback): Bitmap? {
            return try {
                MediaStore.Images.Media.getBitmap(contentResolver, player.currentMediaItem?.mediaMetadata?.artworkUri)
            } catch (e: Exception) {
                BitmapFactory.decodeResource(resources, R.mipmap.ic_launcher_round)
            }
        }
    }

    private val notificationListener = object : PlayerNotificationManager.NotificationListener {
        override fun onNotificationCancelled(notificationId: Int, dismissedByUser: Boolean) {
            super.onNotificationCancelled(notificationId, dismissedByUser)
            stopForeground(true)
            player?.let {
                if (it.isPlaying) {
                    it.pause()
                }
            }
        }

        override fun onNotificationPosted(notificationId: Int, notification: Notification, ongoing: Boolean) {
            super.onNotificationPosted(notificationId, notification, ongoing)
            mediaSession.setMetadata(getMetadata())

            setPlayOrPausedState(mediaSession.controller.playbackState.state)
            startForeground(notificationId, notification)
        }
    }

    override fun onBind(intent: Intent): IBinder {
        return serviceBinder
    }

    override fun onCreate() {
        super.onCreate()
        setupMediaSession()

        player = ExoPlayer.Builder(applicationContext).build()

        val audioAttributes = AudioAttributes.Builder()
            .setUsage(C.USAGE_MEDIA)
            .setContentType(C.AUDIO_CONTENT_TYPE_MUSIC)
            .build()

        player?.setAudioAttributes(audioAttributes, true)

        setupNotification()
    }

    private fun setupNotification() {
        notificationManager = PlayerNotificationManager.Builder(this, MUSIC_NOTIFICATION_ID, MUSIC_CHANNEL_ID)
            .setNotificationListener(notificationListener)
            .setMediaDescriptionAdapter(descriptionAdapter)
            .setChannelImportance(IMPORTANCE_HIGH)
            .setSmallIconResourceId(R.drawable.ic_favorite)
            .setChannelDescriptionResourceId(R.string.app_name)
            .setNextActionIconResourceId(R.drawable.ic_next)
            .setPreviousActionIconResourceId(R.drawable.ic_previous)
            .setPauseActionIconResourceId(R.drawable.ic_pause)
            .setPlayActionIconResourceId(R.drawable.ic_play)
            .setChannelNameResourceId(R.string.app_name)
            .build()

        notificationManager?.also {
            it.setPlayer(player)
            it.setPriority(NotificationCompat.PRIORITY_MAX)
            it.setUseRewindAction(false)
            it.setUseFastForwardAction(false)
            it.setMediaSessionToken(mediaSession.sessionToken)
        }
    }

    private fun setupMediaSession() {
        mediaSession = MediaSessionCompat(applicationContext, MUSIC_SESSION_TAG)
        mediaSession.isActive = true

        mediaSession.setMetadata(getMetadata())

        val playbackStateBuilder = PlaybackStateCompat.Builder()
            .setState(PlaybackStateCompat.STATE_PLAYING, 0, 1f)
            .setActions(
                PlaybackStateCompat.ACTION_PLAY or
                    PlaybackStateCompat.ACTION_PAUSE or
                    PlaybackStateCompat.ACTION_SKIP_TO_PREVIOUS or
                    PlaybackStateCompat.ACTION_SKIP_TO_NEXT or
                    PlaybackStateCompat.ACTION_SEEK_TO
            )

        mediaSession.setPlaybackState(playbackStateBuilder.build())

        mediaSession.setCallback(object: MediaSessionCompat.Callback(){
            override fun onMediaButtonEvent(mediaButtonEvent: Intent?): Boolean {
                return super.onMediaButtonEvent(mediaButtonEvent)
            }
            override fun onSeekTo(pos: Long) {
                super.onSeekTo(pos)
                player?.seekTo(pos)
                setPlayOrPausedState(mediaSession.controller.playbackState.state)
            }
        })
    }

    private fun getMetadata() : MediaMetadataCompat? {
        return player?.let {
            MediaMetadataCompat.Builder()
                .putString(MediaMetadataCompat.METADATA_KEY_TITLE, it.currentMediaItem?.mediaMetadata?.title.toString())
                .putLong(MediaMetadataCompat.METADATA_KEY_DURATION, it.duration)
                .build()
        }
    }

    fun startMusic(index: Int, onError: () -> Unit) {
        player?.let {
            if (it.mediaItemCount < 1) {
                onError()
            } else {
                var indexInPlayer = 0
                index.let { idx ->
                    indexInPlayer = when {
                        idx >= it.mediaItemCount -> { it.mediaItemCount - 1 }
                        idx < 0 -> { 0 }
                        else -> { idx }
                    }
                }
                it.seekTo(indexInPlayer, 0)
                it.prepare()
                it.play()
                setPlayOrPausedState(PlaybackStateCompat.STATE_PLAYING)
            }
        }
    }

    fun setMediaItems(items: MutableList<MediaItem>) {
        player?.setMediaItems(items)
    }

    fun seekTo(progressPercent: Float) {
        player?.let {
            it.seekTo((it.duration * progressPercent).toLong())
        }
    }

    fun getIsPlaying(): Boolean? {
        return player?.isPlaying
    }

    fun getPlayerDuration(): Long? {
        return player?.duration
    }

    fun getCurrentPlayerPosition() : Long? {
        return player?.currentPosition
    }

    fun playOrPauseMusic(onPlay: () -> Unit, onPause: () -> Unit) {
        player?.let {
            if (!it.isPlaying) {
                it.prepare()
                it.play()
                onPlay()
                setPlayOrPausedState(PlaybackStateCompat.STATE_PLAYING)
            } else {
                pauseMusic()
                onPause()
                setPlayOrPausedState(PlaybackStateCompat.STATE_PAUSED)
            }
        }
    }

    fun pauseMusic() {
        player?.pause()
    }

    fun skipToNextSong() {
        player?.let {
            if (it.hasNextMediaItem()) it.seekToNext()
        }
    }

    fun skipToPreviousSong() {
        player?.let {
            if (it.hasPreviousMediaItem()) it.seekToPrevious()
        }
    }

    fun setPlayOrPausedState(state: Int) {
        val playbackState = mediaSession.controller.playbackState
        val stateBuilder = PlaybackStateCompat.Builder(playbackState)
        player?.currentPosition?.let { stateBuilder.setState(state, it, 1f) }

        mediaSession.setPlaybackState(stateBuilder.build())
    }

    override fun onDestroy() {
        player?.let {
            if (it.isPlaying) it.stop()
        }
        notificationManager?.setPlayer(null)
        player?.release()
        player = null
        stopForeground(true)
        stopSelf()
        super.onDestroy()
    }

    inner class ServiceBinder : Binder() {
        fun getPlayerService(): MusicPlayerService {
            return this@MusicPlayerService
        }
    }

    companion object {
        const val MUSIC_CHANNEL_ID = "Music Channel In Dev"
        const val MUSIC_NOTIFICATION_ID = 11111
        const val MUSIC_SESSION_TAG = "11111"
    }
}
