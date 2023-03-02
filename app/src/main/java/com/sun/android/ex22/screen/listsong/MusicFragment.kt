package com.sun.android.ex22.screen.listsong

import android.content.Context
import android.view.View
import android.widget.Toast
import com.sun.android.databinding.FragmentMusicBinding
import com.sun.android.ex22.data.model.Song
import com.sun.android.ex22.screen.adapter.SongListAdapter
import com.sun.android.ex22.utils.OnItemRecyclerViewClickListener
import com.sun.android.ex22.utils.base.BaseFragment

class MusicFragment : BaseFragment(), SongContract.View, OnItemRecyclerViewClickListener<Int> {
    private val binding by lazy { FragmentMusicBinding.inflate(layoutInflater) }
    private val mMusicPresenter by lazy { MusicPresenter.getInstance() }
    private val mSongListAdapter: SongListAdapter by lazy { SongListAdapter() }

    override fun getViewLayout(): View {
        return binding.root
    }

    override fun initView(view: View) {

        mSongListAdapter.registerItemRecyclerViewClickListener(this)

        binding.recyclerViewMusic.apply {
            adapter = mSongListAdapter
        }
    }

    override fun initData() {
        mMusicPresenter.setView(this)
        mMusicPresenter.loadSongs()

    }

    override fun onGetSongsSuccess(songs: MutableList<Song>) {
        mSongListAdapter.updateSongList(songs)

    }

    override fun onError(exception: String?) {
        Toast.makeText(context, exception, Toast.LENGTH_SHORT).show()
    }

    override fun getViewContext(): Context? {
        return context
    }

    override fun onItemClick(index: Int?) {
        mMusicPresenter.startMusic(index)
    }

    override fun onDestroy() {
        mMusicPresenter.onStop()
        super.onDestroy()
    }

    companion object {
        fun newInstance() = MusicFragment()
    }
}
