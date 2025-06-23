package com.example.session2.ui.Group

import com.example.session2.adapter.GroupAdapter
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.session2.R
import com.example.session2.databinding.GroupFragmentBinding
import com.example.session2.data.group.Group
import com.example.session2.viewmodel.GroupViewModel
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class GroupFragment : Fragment() {

    private lateinit var recyclerView: RecyclerView
    private lateinit var groupAdapter: GroupAdapter
    private lateinit var binding: GroupFragmentBinding
    private val viewModel: GroupViewModel by activityViewModels()

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = GroupFragmentBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        // Khởi tạo RecyclerView
        recyclerView = view.findViewById(R.id.recyclerViewGroups)
        recyclerView.layoutManager = LinearLayoutManager(requireContext())

        // Khởi tạo adapter
        groupAdapter = GroupAdapter(
            emptyList(),
            onItemClick = {
                    group ->
                val action = GroupFragmentDirections
                    .actionGroupFragmentToContactGroupFragment(group.id)
                findNavController().navigate(action)
            },
            onItemLongClick = {group ->
                showDiaglogDelete(group)
            }
        )
        viewModel.isLoading.observe(viewLifecycleOwner) { isLoading ->
            if (isLoading) {
                // Hiển thị ProgressBar
                binding.progressBar.visibility = View.VISIBLE
                binding.recyclerViewGroups.visibility = View.GONE
            } else {
                // Ẩn ProgressBar
                binding.progressBar.visibility = View.GONE
                binding.recyclerViewGroups.visibility = View.VISIBLE
            }
        }
        recyclerView.adapter = groupAdapter
        viewModel.groups.observe(viewLifecycleOwner) { groups ->
            groupAdapter.updateGroups(groups)
        }
    }
    private fun showDiaglogDelete(group: Group) {
        val dialog = AlertDialog.Builder(requireContext())
            .setTitle("Xoá liên hệ")
            .setMessage("Bạn có chắc chắn muốn xoá ${group.name} không?")
            .setPositiveButton("Có") { _, _ ->
                viewModel.deleteGroup(group.id)
                Toast.makeText(requireContext(), "Đã xoá ${group.name}", Toast.LENGTH_SHORT).show()
            }
            .setNegativeButton("Không", null)
            .create()
        dialog.show()
    }
}
