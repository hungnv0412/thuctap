package com.example.session2.ui.contact

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.navigation.fragment.navArgs
import com.example.session2.R
import com.example.session2.databinding.DetailContactFragmentBinding

class DetailContactFragment : Fragment() {
    val viewModel: ContactViewModel by activityViewModels()
    private val args: DetailContactFragmentArgs by navArgs()
    private lateinit var binding: DetailContactFragmentBinding
    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        binding = DetailContactFragmentBinding.inflate(inflater, container, false)
        val contactId = args.contactId
        val contact = viewModel.getContactById(contactId)
        contact?.let {
            binding.textNameDetail.text = it.name
            binding.textPhoneDetail.text = it.phoneNumber
            binding.textEmailDetail.text = it.email
            binding.textNoteDetail.text = it.note
        } ?: run {
            // Handle case where contact is not found
            binding.textNameDetail.text = "Contact not found"
        }
        return binding.root
    }
}