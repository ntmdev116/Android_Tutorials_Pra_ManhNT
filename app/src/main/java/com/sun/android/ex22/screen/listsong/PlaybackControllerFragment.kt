package com.sun.android.ex22.screen.listsong

import android.view.View
import android.widget.SeekBar
import com.google.android.exoplayer2.MediaMetadata
import com.sun.android.R
import com.sun.android.databinding.FragmentPlaybackControllerBinding
import com.sun.android.ex22.utils.base.BaseFragment

class PlaybackControllerFragment : BaseFragment() {
    private val binding by lazy { FragmentPlaybackControllerBinding.inflate(layoutInflater) }
    private val mPresenter by lazy { MusicPresenter.getInstance() }

    override fun getViewLayout(): View {
        return binding.root
    }

    override fun initView(view: View) {
        binding.let {
            it.btnPlayPause.setOnClickListener { mPresenter.playOrPauseMusic() }
            it.btnPrev.setOnClickListener { mPresenter.skipToPreviousSong() }
            it.btnNext.setOnClickListener { mPresenter.skipToNextSong() }
            it.playbackSeekBar.setOnSeekBarChangeListener(object : SeekBar.OnSeekBarChangeListener {
                override fun onProgressChanged(seekBar: SeekBar?, progress: Int, fromUser: Boolean) {

                }

                override fun onStartTrackingTouch(seekBar: SeekBar?) {
                    mPresenter.pauseLooper()
                }

                override fun onStopTrackingTouch(seekBar: SeekBar?) {
                    val progress = seekBar?.progress?.toFloat()
                    if (progress != null) {
                        mPresenter.updatePlayerPositionProgress(progress/seekBar.max)
                    }
                }
            })
        }
    }

    override fun initData() {
        mPresenter.setViewController(this)
    }

    fun showPause() {
        binding.btnPlayPause.setImageResource(R.drawable.ic_pause)
    }

    fun showPlay() {
        binding.btnPlayPause.setImageResource(R.drawable.ic_play)
    }

    fun updateUI(item: MediaMetadata?, currentProgress: Int?, duration: Int?) {
        binding.currentPlayingSongName.text = item?.title ?: "No song name"
        item?.artworkUri?.let {
            binding.currentPlayingSongArtwork.setImageURI(it)
        }
        if (binding.currentPlayingSongArtwork.drawable == null) {
            binding.currentPlayingSongArtwork.setImageResource(R.mipmap.ic_launcher)
        }

        binding.playbackSeekBar.max = duration ?: 0
        binding.playbackSeekBar.progress = currentProgress ?: 0
    }

    fun updateSeekbar(progress: Int) {
        binding.playbackSeekBar.progress = progress
    }

    companion object {
        fun newInstance() = PlaybackControllerFragment()
    }
}
