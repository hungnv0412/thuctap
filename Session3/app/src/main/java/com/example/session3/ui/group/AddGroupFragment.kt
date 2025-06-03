package com.example.session3.ui.add

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.navigation.fragment.findNavController
import com.example.session3.R

import com.example.session3.data.Entity.Group
import com.example.session3.databinding.AddGroupFragmentBinding
import com.example.session3.viewmodel.GroupViewmodel
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class AddGroupFragment : Fragment() {
    private lateinit var binding: AddGroupFragmentBinding
    private val viewModel: GroupViewmodel by activityViewModels()

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = AddGroupFragmentBinding.inflate(inflater, container, false)
        return binding.root
    }
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        binding.buttonSave.setOnClickListener {
            val name = binding.nameContent.text.toString()


            if (name.isEmpty()) {
                binding.newGroupName.error = "Name cannot be empty"
                return@setOnClickListener
            }


            val group = Group(0,name)
            viewModel.addGroup(group)
            val action = AddGroupFragmentDirections.actionAddGroupFragmentToGroupFragment()
            findNavController().navigate(action)
        }
        binding.buttonDiscard.setOnClickListener {
            findNavController().navigate(R.id.action_addGroupFragment_to_groupFragment)
        }
    }
}
