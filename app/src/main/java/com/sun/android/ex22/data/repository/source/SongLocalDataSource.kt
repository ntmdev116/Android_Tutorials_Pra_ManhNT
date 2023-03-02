package com.sun.android.ex22.data.repository.source

import android.content.Context
import android.net.Uri
import android.os.Handler
import android.os.Looper
import android.provider.MediaStore
import com.sun.android.ex22.data.model.Song
import java.util.concurrent.Executors

class SongLocalDataSource : SongDataSource.Local {
    private val executor = Executors.newSingleThreadExecutor()

    override fun getSongsLocal(listener: OnResultListener<MutableList<Song>>, context: Context?) {

        if (context == null) {
            listener.onError(NullPointerException().message)
        } else {
            val mRunnable = Runnable { handleGetLocalSongs(context, listener) }
            executor.execute(mRunnable)
        }
    }

    private fun handleGetLocalSongs(context: Context, listener: OnResultListener<MutableList<Song>>) {
        val songs = mutableListOf<Song>()
        val projection = arrayOf(
            MediaStore.Audio.Media._ID,
            MediaStore.Audio.Media.TITLE,
            MediaStore.Audio.Media.ARTIST,
            MediaStore.Audio.Media.DURATION,
            MediaStore.Audio.Media.DATA,
            MediaStore.Audio.Media.ALBUM_ID,
        )

        val selection = MediaStore.Audio.Media.IS_MUSIC + " != 0"
        val sortOrder = MediaStore.Audio.Media.TITLE + " ASC"

        val cursor = context.contentResolver.query(
            MediaStore.Audio.Media.EXTERNAL_CONTENT_URI,
            projection,
            selection,
            null,
            sortOrder
        )

        cursor?.use { c ->
            val idColumn = c.getColumnIndexOrThrow(MediaStore.Audio.Media._ID)
            val titleColumn = c.getColumnIndexOrThrow(MediaStore.Audio.Media.TITLE)
            val artistColumn = c.getColumnIndexOrThrow(MediaStore.Audio.Media.ARTIST)
            val durationColumn = c.getColumnIndexOrThrow(MediaStore.Audio.Media.DURATION)
            val dataColumn = c.getColumnIndexOrThrow(MediaStore.Audio.Media.DATA)
            val albumIdColumn = c.getColumnIndex(MediaStore.Audio.Media.ALBUM_ID)

            while (c.moveToNext()) {
                val id = c.getInt(idColumn)
                val title = c.getString(titleColumn)
                val artist = c.getString(artistColumn)
                val duration = c.getLong(durationColumn)
                val path = c.getString(dataColumn)
                val albumId = c.getLong(albumIdColumn)

                val albumArtUri = Uri.parse(artworkContentUri)
                    .buildUpon()
                    .appendPath(albumId.toString())
                    .build()

                if (duration > 5)
                    songs.add(Song(id, title, artist, duration, path, albumArtUri))
            }
        }

        executor.execute {
            Handler(Looper.getMainLooper()).post {
                if (songs.size > 0) listener.onSuccess(songs)
                else listener.onError("No song available")
            }
        }
    }

    companion object {
        private var instance: SongLocalDataSource? = null
        private val artworkContentUri = "content://media/external/audio/albumart"

        fun getInstance() = synchronized(this) {
            instance ?: SongLocalDataSource().also { instance = it }
        }
    }
}
