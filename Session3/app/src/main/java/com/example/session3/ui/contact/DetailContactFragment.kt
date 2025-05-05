package com.example.session3.ui.contact

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.navigation.fragment.navArgs
import com.example.session3.databinding.DetailContactFragmentBinding
import com.example.session3.viewmodel.ContactViewModel
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
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
        return binding.root
    }
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        viewModel.getContactById(args.contactId)
        viewModel.contact.observe(viewLifecycleOwner) { contact ->
            contact?.let {
                binding.textNameDetail.text = it.name
                binding.textPhoneDetail.text = it.phoneNumber
                binding.textEmailDetail.text = it.email
                binding.textNoteDetail.text = it.note
                binding.textIdDetail.text = it.id.toString()
            }
        }
    }

}