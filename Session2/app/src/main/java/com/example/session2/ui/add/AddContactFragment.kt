package com.example.session2.ui.add

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import com.example.session2.R
import com.example.session2.databinding.AddContactFragmentBinding
import com.example.session2.model.Contact

class AddContactFragment : Fragment() {
    private lateinit var binding: AddContactFragmentBinding
    private val viewModel: ContactViewModel by activityViewModels()

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = AddContactFragmentBinding.inflate(inflater, container, false)

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

            if (name.isNotEmpty() && phoneNumber.isNotEmpty() && email.isNotEmpty()) {
                val newContact = Contact(0,name, phoneNumber, R.drawable.avt2, email, note)
                viewModel.addContact(newContact)
            }
            // Navigate back to the previous screen
            requireActivity().supportFragmentManager.popBackStack()
        }
        binding.buttonDiscard.setOnClickListener {
            // Discard changes and navigate back
            requireActivity().supportFragmentManager.popBackStack()
        }

        return binding.root
    }
}
