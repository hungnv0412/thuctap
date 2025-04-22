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

        return binding.root
    }
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        val contact = viewModel.getContactById(args.contactId)
        binding.textNameDetail.text = contact?.name
        binding.textPhoneDetail.text = contact?.phoneNumber
        binding.textEmailDetail.text = contact?.email
        binding.textNoteDetail.text = contact?.note
    }

}