package com.sun.android.ex22.data.repository

import android.content.Context
import com.sun.android.ex22.data.model.Song
import com.sun.android.ex22.data.repository.source.OnResultListener
import com.sun.android.ex22.data.repository.source.SongDataSource

class SongRepository(private val local: SongDataSource.Local) : SongDataSource.Local {
    override fun getSongsLocal(listener: OnResultListener<MutableList<Song>>, context: Context?) {
        local.getSongsLocal(listener, context)
    }

    companion object {
        private var instance: SongRepository? = null

        fun getInstance(local: SongDataSource.Local) = synchronized(this) {
            instance ?: SongRepository(local).also { instance = it }
        }
    }

}
