package com.example.session3.ui.add

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.navigation.fragment.findNavController
import com.example.session3.R

import com.example.session3.databinding.AddContactFragmentBinding
import com.example.session3.data.Contact
import com.example.session3.sharedPreferences.UserPreferences
import com.example.session3.viewmodel.ContactViewModel
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class AddContactFragment : Fragment() {
    private lateinit var binding: AddContactFragmentBinding
    private val viewModel: ContactViewModel by activityViewModels()

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = AddContactFragmentBinding.inflate(inflater, container, false)
        return binding.root
    }
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        binding.buttonSave.setOnClickListener {
            val name = binding.nameContent.text.toString()
            val phoneNumber = binding.phoneContent.text.toString()
            val email = binding.emailContent.text.toString()
            val note = binding.noteContent.text.toString()

            if (name.isEmpty()) {
                binding.newTextName.error = "Name cannot be empty"
                return@setOnClickListener
            }
            if (phoneNumber.isEmpty()) {
                binding.newTextPhone.error = "Phone number cannot be empty"
                return@setOnClickListener
            }
            if (email.isEmpty()) {
                binding.newTextEmail.error = "Email cannot be empty"
                return@setOnClickListener
            }

            val contact = Contact(0,name, phoneNumber,R.drawable.avt2, email, note)
            viewModel.addContact(contact)
            val action = AddContactFragmentDirections.actionAddContactFragmentToContactFragment()
            findNavController().navigate(action)
        }
        binding.buttonDiscard.setOnClickListener {
            findNavController().navigate(R.id.action_global_toContactFragment)
        }
    }
}
