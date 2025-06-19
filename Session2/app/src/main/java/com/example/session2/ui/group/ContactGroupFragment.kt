package com.example.session2.ui.group
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.navigation.fragment.findNavController
import androidx.navigation.fragment.navArgs
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.session2.viewmodel.ContactViewModel
import dagger.hilt.android.AndroidEntryPoint
import com.example.session2.R
import com.example.session2.adapter.ContactAdapter
import com.example.session2.data.contact.Contact
import com.example.session2.databinding.ContactFragmentBinding
import com.google.android.material.floatingactionbutton.FloatingActionButton

@AndroidEntryPoint
class ContactGroupFragment : Fragment() {

    private lateinit var recyclerView: RecyclerView
    private lateinit var contactAdapter: ContactAdapter
    private val args: ContactGroupFragmentArgs by navArgs()
    private val viewModel: ContactViewModel by activityViewModels()
    private lateinit var binding : ContactFragmentBinding
    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = ContactFragmentBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        viewModel.getContactsByGroupId(args.groupId) // Ensure data is loaded when the fragment is created

        recyclerView = view.findViewById(R.id.recyclerViewContacts)
        recyclerView.layoutManager = LinearLayoutManager(requireContext())

        contactAdapter = ContactAdapter(
            mutableListOf(), // Start with an empty list
            onItemClick = {
            },
            onItemLongClick = {
            }
        )
        recyclerView.adapter = contactAdapter
        viewModel.isLoading.observe(viewLifecycleOwner) { isLoading ->
            if (isLoading) {
                // Hiển thị ProgressBar
                binding.progressBar.visibility = View.VISIBLE
                binding.recyclerViewContacts.visibility = View.GONE
            } else {
                // Ẩn ProgressBar
                binding.progressBar.visibility = View.GONE
                binding.recyclerViewContacts.visibility = View.VISIBLE
            }
        }
        viewModel.contacts.observe(viewLifecycleOwner) { contacts ->
            contactAdapter.updateContacts(contacts)
        }
        val addButton = view.findViewById<FloatingActionButton>(R.id.buttonAddGroup)
        addButton.setOnClickListener {
            val action = ContactGroupFragmentDirections
                .actionContactGroupFragmentToAddContactToGroupFragment(args.groupId)
            findNavController().navigate(action)
        }

    }
}
