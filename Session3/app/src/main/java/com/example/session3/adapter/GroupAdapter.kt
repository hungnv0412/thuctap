package com.example.session3.adapter

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.example.session3.R
import com.example.session3.data.Entity.Group

class GroupAdapter(
    private val groupList: MutableList<com.example.session3.data.Entity.Group>,
    private val onItemClick: (com.example.session3.data.Entity.Group) -> Unit,
    private val onItemLongClick: (com.example.session3.data.Entity.Group) -> Unit,
): RecyclerView.Adapter<GroupAdapter.GroupViewHolder>() {
    class GroupViewHolder(
        private val view: View,
    ): RecyclerView.ViewHolder(view){
        val nameTextView : TextView = view.findViewById(R.id.textNameGroup)

    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): GroupViewHolder {
        val adapterLayout = LayoutInflater.from(parent.context)
            .inflate(R.layout.item_contact, parent, false)
        return GroupViewHolder(adapterLayout)
    }

    override fun onBindViewHolder(holder: GroupViewHolder, position: Int) {
        val item = groupList[position]
        holder.nameTextView.text = item.name
        holder.itemView.setOnClickListener {
            onItemClick(item)
        }
        holder.itemView.setOnLongClickListener {
            onItemLongClick(item)
            true
        }

    }
    override fun getItemCount(): Int {
        return groupList.size
    }
    fun updateGroup(newGroupList: List<Group>) {
        groupList.clear()
        groupList.addAll(newGroupList)
        notifyDataSetChanged()
    }
}