package com.sun.android.ex22.data.repository.source

import android.content.Context
import com.sun.android.ex22.data.model.Song

interface SongDataSource {
    interface Local {
        fun getSongsLocal(listener: OnResultListener<MutableList<Song>>, context: Context?)
    }
}
