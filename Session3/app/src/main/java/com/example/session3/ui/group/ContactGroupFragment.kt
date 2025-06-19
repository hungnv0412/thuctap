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
import com.example.session3.data.entity.Contact
import com.example.session3.viewmodel.ContactViewModel
import dagger.hilt.android.AndroidEntryPoint
import com.example.session3.R
import com.google.android.material.floatingactionbutton.FloatingActionButton

@AndroidEntryPoint
class ContactGroupFragment : Fragment() {

    private lateinit var recyclerView: RecyclerView
    private lateinit var contactAdapter: ContactAdapter
    private val args: ContactGroupFragmentArgs by navArgs()
    private val viewModel: ContactViewModel by activityViewModels()

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        return inflater.inflate(R.layout.contact_fragment, container, false)
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
            val action = ContactGroupFragmentDirections
                .actionContactGroupFragmentToAddContactToGroupFragment(args.groupId)
            findNavController().navigate(action)
        }

    }

    private fun showDiaglogDelete(contact: Contact) {
        val dialog = AlertDialog.Builder(requireContext())
            .setTitle("Xoá liên hệ")
            .setMessage("Bạn có chắc chắn muốn xoá ${contact.name} không?")
            .setPositiveButton("Có") { _, _ ->
                viewModel.deleteContactFromGroup(contact.id, args.groupId)
                Toast.makeText(requireContext(), "Đã xoá ${contact.name}", Toast.LENGTH_SHORT).show()
            }
            .setNegativeButton("Không", null)
            .create()
        dialog.show()
    }
}
