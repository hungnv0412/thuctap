package com.example.session3.ui.group

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.activityViewModels
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.session3.adapter.GroupAdapter
import com.example.session3.viewmodel.GroupViewmodel
import dagger.hilt.android.AndroidEntryPoint
import com.example.session3.R

@AndroidEntryPoint
class GroupFragment : androidx.fragment.app.Fragment() {
    private lateinit var recyclerView: RecyclerView
    private lateinit var adapter: GroupAdapter
    private val viewModel: GroupViewmodel by activityViewModels()

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.group_fragment,container,false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        viewModel.loadGroups()
        recyclerView=view.findViewById(R.id.recyclerViewGroups)
        recyclerView.layoutManager= LinearLayoutManager(requireContext())

        adapter= GroupAdapter(mutableListOf(), onItemClick = {
            val action = GroupFragmentDirections.actionGroupFragmentToContactGroupFragment(it.id)
            findNavController().navigate(action)
        }, onItemLongClick = {})

        recyclerView.adapter = adapter
        val addButton = view.findViewById<com.google.android.material.floatingactionbutton.FloatingActionButton>(R.id.buttonAddGroup)
        addButton.setOnClickListener {
            val action = GroupFragmentDirections.actionGroupFragmentToAddGroupFragment()
            findNavController().navigate(action)
        }
        viewModel.groups.observe (viewLifecycleOwner){groups ->
            adapter.updateGroup(groups)
        }
    }
}