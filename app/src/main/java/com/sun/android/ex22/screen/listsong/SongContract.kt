package com.sun.android.ex22.screen.listsong

import android.content.Context
import com.sun.android.ex22.data.model.Song
import com.sun.android.ex22.utils.base.BasePresenter

class SongContract {
    interface View {
        fun onGetSongsSuccess(songs: MutableList<Song>)
        fun onError(exception: String?)

        fun getViewContext(): Context?
    }

    interface Presenter : BasePresenter<View> {
        fun loadSongs()

        fun playOrPauseMusic()
        fun setViewController(view: PlaybackControllerFragment?)
        fun startMusic(index: Int?)
        fun pauseMusic()
    }
}

