package com.example.session3.ui.contact
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
import com.example.session3.adapter.ContactAdapter
import com.example.session3.data.Entity.Contact
import com.example.session3.viewmodel.ContactViewModel
import dagger.hilt.android.AndroidEntryPoint
import com.example.session3.R
import com.google.android.material.floatingactionbutton.FloatingActionButton

@AndroidEntryPoint
class AddContactToGroupFragment : Fragment() {

    private lateinit var recyclerView: RecyclerView
    private lateinit var contactAdapter: ContactAdapter
    private val viewModel: ContactViewModel by activityViewModels()
    private val args : AddContactToGroupFragmentArgs by navArgs()

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        return inflater.inflate(R.layout.contact_fragment, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        viewModel.loadContacts() // Ensure data is loaded when the fragment is created

        recyclerView = view.findViewById(R.id.recyclerViewContacts)
        recyclerView.layoutManager = LinearLayoutManager(requireContext())

        contactAdapter = ContactAdapter(
            mutableListOf(), // Start with an empty list
            onItemClick = { contact ->
                viewModel.addContactToGroup(contact.id, args.groupId)

                findNavController().popBackStack()
            },
            onItemLongClick = { contact ->
                showDiaglogDelete(contact)
            }
        )
        recyclerView.adapter = contactAdapter

        viewModel.contacts.observe(viewLifecycleOwner) { contacts ->
            contactAdapter.updateContact(contacts) // Update adapter when data changes
        }
        val addButton = view.findViewById<FloatingActionButton>(R.id.buttonAddContact)
        addButton.setOnClickListener {
            findNavController().navigate(R.id.action_contactFragment_to_addContactFragment)
        }

    }

    private fun showDiaglogDelete(contact: Contact) {
        val dialog = AlertDialog.Builder(requireContext())
            .setTitle("Xoá liên hệ")
            .setMessage("Bạn có chắc chắn muốn xoá ${contact.name} không?")
            .setPositiveButton("Có") { _, _ ->
                viewModel.deleteContact(contact)
                Toast.makeText(requireContext(), "Đã xoá ${contact.name}", Toast.LENGTH_SHORT).show()
            }
            .setNegativeButton("Không", null)
            .create()
        dialog.show()
    }
}
