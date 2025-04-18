package com.example.session2.ui.contact

import ContactAdapter
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.activityViewModels
import androidx.fragment.app.viewModels
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.session2.R

class ContactFragment : androidx.fragment.app.Fragment() {

    private lateinit var recyclerView: RecyclerView
    private lateinit var contactAdapter: ContactAdapter
    private val viewmodel: ContactViewModel by activityViewModels()

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val view = inflater.inflate(R.layout.contact_fragment, container, false)

        // Initialize RecyclerView
        recyclerView = view.findViewById(R.id.recyclerViewContacts)
        recyclerView.layoutManager = LinearLayoutManager(requireContext())

        // Initialize Adapter with ViewModel data
        contactAdapter = ContactAdapter(this, viewmodel.getContacts(),
            onItemClick = { contact ->
                // Handle item click
            },
            onItemLongClick = { contact ->
                // Handle item long click
            }
        )
        recyclerView.adapter = contactAdapter

        viewmodel.contacts.observe(viewLifecycleOwner){contact ->
            contactAdapter.notifyItemInserted(contact.size - 1)
        }

        return view
    }
}
