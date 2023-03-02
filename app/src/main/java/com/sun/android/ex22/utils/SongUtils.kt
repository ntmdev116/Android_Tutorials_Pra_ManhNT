package com.sun.android.ex22.utils

import com.google.android.exoplayer2.MediaItem
import com.google.android.exoplayer2.MediaMetadata
import com.sun.android.ex22.data.model.Song

object SongUtils {
    fun getMediaItems(songs: MutableList<Song>): MutableList<MediaItem> {
        val mediaItems = mutableListOf<MediaItem>()

        songs.forEach {
            mediaItems.add(
                MediaItem.Builder()
                .setUri(it.path)
                .setMediaMetadata(getMediaMetaData(it))
                .build())
        }

        return mediaItems
    }

    private fun getMediaMetaData(song: Song): MediaMetadata {
        return MediaMetadata.Builder()
            .setTitle(song.title)
            .setArtworkUri(song.artworkUri)
            .build()
    }
}
