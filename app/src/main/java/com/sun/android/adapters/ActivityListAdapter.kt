package com.sun.android.adapters

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.sun.android.databinding.ItemContainerActivityBinding
import com.sun.android.models.NavigationActivityData

class ActivityListAdapter :
    RecyclerView.Adapter<ActivityListAdapter.ButtonViewHolder>() {

    private val items = mutableListOf<NavigationActivityData>()
    var onItemClickListener: ((NavigationActivityData) -> Unit)? = null

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ButtonViewHolder {
        return ButtonViewHolder(ItemContainerActivityBinding.inflate(
            LayoutInflater.from(parent.context),
            parent,
            false
        ))
    }

    override fun onBindViewHolder(holder: ButtonViewHolder, position: Int) {
        val item = items[position]
        holder.setData(item)
    }

    override fun getItemCount() = items.size

    fun updateActivityList(listActivity: MutableList<NavigationActivityData>) {
        items.clear()
        items.addAll(listActivity)
    }

    inner class ButtonViewHolder(private val binding : ItemContainerActivityBinding) : RecyclerView.ViewHolder(binding.root) {
        fun setData(data : NavigationActivityData) {
            binding.buttonActivity.apply {
                text = data.name
                setOnClickListener { onItemClickListener?.invoke(data) }
            }
        }
    }
}
