package com.example.session2.ui.contact

import com.example.session2.adapter.ContactAdapter
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
import com.example.session2.model.Contact
import com.example.session2.viewmodel.ContactViewModel
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class ContactFragment : Fragment() {

    private lateinit var recyclerView: RecyclerView
    private lateinit var contactAdapter: ContactAdapter
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

        // Khởi tạo RecyclerView
        recyclerView = view.findViewById(R.id.recyclerViewContacts)
        recyclerView.layoutManager = LinearLayoutManager(requireContext())

        // Khởi tạo adapter
        contactAdapter = ContactAdapter(
            this,
            viewModel.getContacts().toMutableList(),
            onItemClick = { contact ->
                val action = ContactFragmentDirections
                    .actionContactFragmentToDetailContactFragment(contact.id)
                findNavController().navigate(action)
            },
            onItemLongClick = { contact ->
                showDiaglogDelete(contact)
            }
        )
        recyclerView.adapter = contactAdapter
        viewModel.contacts.observe(viewLifecycleOwner) { contacts ->
            contactAdapter.updateContacts(contacts)
        }
    }
    private fun showDiaglogDelete(contact: Contact) {
        val dialog = AlertDialog.Builder(requireContext())
            .setTitle("Xoá liên hệ")
            .setMessage("Bạn có chắc chắn muốn xoá ${contact.name} không?")
            .setPositiveButton("Có") { _, _ ->
                viewModel.deleteContact(contact)
                contactAdapter.updateContacts(viewModel.getContacts())
                Toast.makeText(requireContext(), "Đã xoá ${contact.name}", Toast.LENGTH_SHORT).show()
            }
            .setNegativeButton("Không", null)
            .create()
        dialog.show()
    }
}
