package com.sun.android.ex22.screen.adapter

import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.sun.android.databinding.ItemContainerSongBinding
import com.sun.android.ex22.data.model.Song
import com.sun.android.ex22.utils.OnItemRecyclerViewClickListener

class SongListAdapter : RecyclerView.Adapter<SongListAdapter.SongViewHolder>() {
    private var songList = mutableListOf<Song>()
    private var onSongClickListener: OnItemRecyclerViewClickListener<Int>? = null

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): SongViewHolder {
        return SongViewHolder(ItemContainerSongBinding.inflate(
            LayoutInflater.from(parent.context),
            parent,
            false
        ), onSongClickListener)
    }

    override fun onBindViewHolder(holder: SongViewHolder, position: Int) {
        holder.bindViewData(songList[position])
    }

    override fun getItemCount(): Int {
        return songList.size
    }

    fun registerItemRecyclerViewClickListener(onItemRecyclerViewClickListener: OnItemRecyclerViewClickListener<Int>?) {
        onSongClickListener = onItemRecyclerViewClickListener
    }

    fun updateSongList(songs : MutableList<Song>?) {
        songs?.let {
            songList.clear()
            songList.addAll(it)
            notifyDataSetChanged()
        }
    }

    inner class SongViewHolder(
        private val binding : ItemContainerSongBinding,
        private val listener: OnItemRecyclerViewClickListener<Int>?
    ) : RecyclerView.ViewHolder(binding.root), View.OnClickListener {
        init {
            itemView.setOnClickListener(this)
        }

        private var songData: Song? = null

        fun bindViewData(song: Song) {
            song.let {
                binding.songName.text = it.title
                binding.artistName.text = it.artist
                binding.songImage.setImageURI(song.artworkUri)
                songData = it
            }
        }

        override fun onClick(v: View?) {
            listener?.onItemClick(songList.indexOf(songData))
        }
    }
}
