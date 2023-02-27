package com.sun.android.adapters

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.sun.android.databinding.ItemContainerContactBinding
import com.sun.android.models.ContactData

class ContactListAdapter : RecyclerView.Adapter<ContactListAdapter.ContactViewHolder>() {
    private var contactList = mutableListOf<ContactData>()

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ContactViewHolder {
        return ContactViewHolder(ItemContainerContactBinding.inflate(
            LayoutInflater.from(parent.context),
            parent,
            false
        ))
    }

    override fun onBindViewHolder(holder: ContactViewHolder, position: Int) {
        holder.setData(contactList[position])
    }

    override fun getItemCount(): Int {
        return contactList.size
    }

    fun setContactList(list : MutableList<ContactData>) {
        contactList.clear()
        contactList.addAll(list)
        notifyDataSetChanged()
    }

    class ContactViewHolder(private val binding : ItemContainerContactBinding) : RecyclerView.ViewHolder(binding.root) {
        fun setData(data : ContactData) {
            binding.textContactName.text = data.displayName
            binding.textContactNumber.text = data.numbers
        }
    }
}
