package com.example.session3.adapter

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.example.session3.R
import com.example.session3.data.entity.Group

class GroupAdapter(
    private val groupList: MutableList<Group>,
    private val onItemClick: (Group) -> Unit,
    private val onItemLongClick: (Group) -> Unit,
): RecyclerView.Adapter<GroupAdapter.GroupViewHolder>() {
    class GroupViewHolder(
        private val view: View,
    ): RecyclerView.ViewHolder(view){
        val nameTextView : TextView = view.findViewById(R.id.textNameGroup)
        val noteTextView : TextView = view.findViewById(R.id.textNoteGroup)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): GroupViewHolder {
        val adapterLayout = LayoutInflater.from(parent.context)
            .inflate(R.layout.item_group, parent, false)
        return GroupViewHolder(adapterLayout)
    }

    override fun onBindViewHolder(holder: GroupViewHolder, position: Int) {
        val item = groupList[position]
        holder.nameTextView.text = item.name
        holder.noteTextView.text = item.note
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