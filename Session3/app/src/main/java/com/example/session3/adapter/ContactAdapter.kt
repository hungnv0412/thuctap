package com.example.session3.adapter

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.example.session3.R
import com.example.session3.data.Contact
import com.example.session3.ui.contact.ContactFragment

class ContactAdapter(
    private val context: ContactFragment,
    private val contactList: MutableList<Contact>,
    private val onItemClick: (Contact) -> Unit,
    private val onItemLongClick: (Contact) -> Unit,
): RecyclerView.Adapter<ContactAdapter.ContactViewHolder>() {
    class ContactViewHolder(
        private val view: View,
    ): RecyclerView.ViewHolder(view){
        val idTextView: TextView = view.findViewById(R.id.textID)
        val nameTextView : TextView = view.findViewById(R.id.textName)
        val phoneTextView : TextView = view.findViewById(R.id.textPhone)
        val avtImageView : ImageView = view.findViewById(R.id.imageAvatar)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ContactViewHolder {
        val adapterLayout = LayoutInflater.from(parent.context)
            .inflate(R.layout.item_contact, parent, false)
        return ContactViewHolder(adapterLayout)
    }

    override fun onBindViewHolder(holder: ContactViewHolder, position: Int) {
        val item = contactList[position]
        holder.idTextView.text = item.id.toString()
        holder.nameTextView.text = item.name
        holder.phoneTextView.text = item.phoneNumber
        holder.avtImageView.setImageResource(item.avatar)
        holder.itemView.setOnClickListener {
            onItemClick(item)
        }
        holder.itemView.setOnLongClickListener {
            onItemLongClick(item)
            true
        }

    }
    override fun getItemCount(): Int {
        return contactList.size
    }
    fun updateContact(newContactList: List<Contact>) {
        contactList.clear()
        contactList.addAll(newContactList)
        notifyDataSetChanged()
    }
}